package com.localpack.bypass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * What the mod remembers between sessions: which pack files it holds, which servers it has already
 * dealt with, and what every server sent.
 *
 * <p>This is what stops the mod asking the same question twice. The resource pack folder can only
 * be searched by hashing its contents, which is slow and — while a pack is being used by the game —
 * not always possible; an entry here says "the pack with this SHA-1 is this file, of this size"
 * and is confirmed by a single {@code stat} instead. The hash is the server's, so a server that
 * genuinely changes its pack produces a hash that is not in here and the pack is fetched again.
 *
 * <p>Everything is guarded by one lock: the pack flow runs on the client thread and the downloads
 * on an IO pool, and both read and write this. Writes are batched — a batch of packs would
 * otherwise write the file once per pack — and happen off the client thread.
 */
public final class PackRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Object LOCK = new Object();

    private static final ScheduledExecutorService WRITER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "localpack-history");
        t.setDaemon(true);
        return t;
    });

    /** One downloaded pack file. */
    public static final class Pack {
        public String hash;
        /** File name inside the pack folder, not a full path: the folder can be moved. */
        public String file;
        public long bytes;
        /** The server whose pack this is, as it appeared in the connection. */
        public String host;
        public String url;
        public long firstSeenMs;
        public long lastSeenMs;

        public Pack() {}

        Pack(String hash, String file, long bytes, String host, String url, long now) {
            this.hash = hash;
            this.file = file;
            this.bytes = bytes;
            this.host = host;
            this.url = url;
            this.firstSeenMs = now;
            this.lastSeenMs = now;
        }
    }

    /** One server the player has been asked about. */
    public static final class Server {
        public String host;
        /** When the player last agreed to a download here; 0 means they never have. */
        public long lastAcceptedMs;
        public long lastSeenMs;
        public int accepted;
        public int declined;
        public String lastHash;

        public Server() {}

        Server(String host, long now) {
            this.host = host;
            this.lastSeenMs = now;
        }
    }

    /** One pack push, as it arrived. */
    public static final class Event {
        public long timeMs;
        public String host;
        public String url;
        public String hash;
        /** Translation key suffix describing the outcome: found, downloaded, declined, failed, ignored. */
        public String result;
        public String detail;
        public long bytes;
        public long durationMs;

        public Event() {}

        public Event(long timeMs, String host, String url, String hash, String result, String detail,
                     long bytes, long durationMs) {
            this.timeMs = timeMs;
            this.host = host;
            this.url = url;
            this.hash = hash;
            this.result = result;
            this.detail = detail;
            this.bytes = bytes;
            this.durationMs = durationMs;
        }
    }

    private static final class Data {
        int version = 1;
        List<Pack> packs = new ArrayList<>();
        List<Server> servers = new ArrayList<>();
        List<Event> events = new ArrayList<>();
    }

    private static Data data;
    private static boolean dirty;
    private static boolean saveScheduled;

    private PackRegistry() {}

    // ------------------------------------------------------------------ load/save

    private static Data data() {
        synchronized (LOCK) {
            if (data == null) data = load();
            return data;
        }
    }

    private static Data load() {
        Path file = GamePaths.historyFile();
        if (!Files.isRegularFile(file)) return new Data();
        try {
            Data loaded = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), Data.class);
            if (loaded == null) return new Data();
            if (loaded.packs == null) loaded.packs = new ArrayList<>();
            if (loaded.servers == null) loaded.servers = new ArrayList<>();
            if (loaded.events == null) loaded.events = new ArrayList<>();
            // Entries written by an older version, or damaged by hand, are dropped rather than
            // trusted: everything here is a cache, and a missing entry only costs a hash pass.
            loaded.packs.removeIf(p -> p == null || p.hash == null || p.file == null);
            loaded.servers.removeIf(s -> s == null || s.host == null);
            loaded.events.removeIf(e -> e == null);
            return loaded;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not read {}: {}. Starting empty.", file, e.toString());
            return new Data();
        }
    }

    /** Marks the file for writing. The write itself is coalesced and never blocks the caller. */
    private static void touch() {
        synchronized (LOCK) {
            dirty = true;
            if (saveScheduled) return;
            saveScheduled = true;
        }
        WRITER.schedule(PackRegistry::flush, 2, TimeUnit.SECONDS);
    }

    /** Writes now, if anything changed. Called at shutdown as well as by the scheduled write. */
    public static void flush() {
        String json;
        synchronized (LOCK) {
            saveScheduled = false;
            if (!dirty || data == null) return;
            dirty = false;
            json = GSON.toJson(data);
        }
        Path file = GamePaths.historyFile();
        try {
            Files.createDirectories(GamePaths.stateDir());
            Path part = file.resolveSibling(file.getFileName() + ".part");
            Files.writeString(part, json, StandardCharsets.UTF_8);
            Files.move(part, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not save {}: {}", file, e.toString());
        }
    }

    // ------------------------------------------------------------------ packs

    private static String key(String hash) {
        return hash == null ? "" : hash.toLowerCase(Locale.ROOT);
    }

    /**
     * The remembered pack for this SHA-1, if its file is still where it was recorded and still the
     * same size. The size check is the point: it costs one {@code stat} and catches a file that was
     * replaced or truncated, without hashing anything.
     */
    public static Optional<Pack> find(String hash) {
        String wanted = key(hash);
        if (wanted.isEmpty()) return Optional.empty();
        Path dir = Config.packDir();
        synchronized (LOCK) {
            for (Pack pack : data().packs) {
                if (!key(pack.hash).equals(wanted)) continue;
                Path file = dir.resolve(pack.file);
                try {
                    if (Files.isRegularFile(file) && (pack.bytes <= 0 || Files.size(file) == pack.bytes)) {
                        pack.lastSeenMs = System.currentTimeMillis();
                        touch();
                        return Optional.of(pack);
                    }
                } catch (IOException | RuntimeException e) {
                    return Optional.empty();
                }
                // Recorded, but the file is gone or has changed: forget it and let the hash pass
                // over the folder decide.
                removeLocked(pack);
                touch();
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static void remember(String hash, String file, long bytes, String host, String url) {
        String wanted = key(hash);
        if (wanted.isEmpty() || file == null) return;
        long now = System.currentTimeMillis();
        synchronized (LOCK) {
            Data current = data();
            for (Pack pack : current.packs) {
                if (!key(pack.hash).equals(wanted)) continue;
                pack.file = file;
                pack.bytes = bytes;
                pack.host = host;
                pack.url = url;
                pack.lastSeenMs = now;
                touch();
                return;
            }
            current.packs.add(new Pack(wanted, file, bytes, host, url, now));
            touch();
        }
    }

    public static void forget(String hash) {
        String wanted = key(hash);
        synchronized (LOCK) {
            for (Pack pack : new ArrayList<>(data().packs)) {
                if (key(pack.hash).equals(wanted)) removeLocked(pack);
            }
            touch();
        }
    }

    private static void removeLocked(Pack pack) {
        data().packs.remove(pack);
    }

    public static List<Pack> packs() {
        synchronized (LOCK) {
            return new ArrayList<>(data().packs);
        }
    }

    /** Every pack grouped by the server it came from, servers ordered by the most recent pack. */
    public static Map<String, List<Pack>> packsByServer() {
        Map<String, List<Pack>> grouped = new LinkedHashMap<>();
        synchronized (LOCK) {
            List<Pack> sorted = new ArrayList<>(data().packs);
            sorted.sort((a, b) -> Long.compare(b.lastSeenMs, a.lastSeenMs));
            for (Pack pack : sorted) {
                grouped.computeIfAbsent(pack.host == null ? "" : pack.host, h -> new ArrayList<>()).add(pack);
            }
        }
        return grouped;
    }

    /**
     * The server a remembered pack came from, without touching the disk. Used to tell "this pack is
     * the one this server gave me before" from "this pack is somebody else's", which decides which
     * packs a new one replaces.
     */
    public static Optional<String> hostOfHash(String hash) {
        String wanted = key(hash);
        if (wanted.isEmpty()) return Optional.empty();
        synchronized (LOCK) {
            for (Pack pack : data().packs) {
                if (key(pack.hash).equals(wanted)) return Optional.of(pack.host == null ? "" : pack.host);
            }
        }
        return Optional.empty();
    }

    // ---------------------------------------------------------------- servers

    private static Server serverLocked(String host) {
        String name = host == null ? "" : host;
        for (Server server : data().servers) {
            if (name.equals(server.host)) return server;
        }
        Server created = new Server(name, System.currentTimeMillis());
        data().servers.add(created);
        return created;
    }

    /** True when the player has already agreed to download from this server before. */
    public static boolean isTrusted(String host) {
        if (host == null || host.isEmpty()) return false;
        synchronized (LOCK) {
            for (Server server : data().servers) {
                if (host.equals(server.host)) return server.lastAcceptedMs > 0;
            }
        }
        return false;
    }

    public static void accepted(String host, String hash) {
        long now = System.currentTimeMillis();
        synchronized (LOCK) {
            Server server = serverLocked(host);
            server.lastAcceptedMs = now;
            server.lastSeenMs = now;
            server.accepted++;
            server.lastHash = hash;
            touch();
        }
    }

    public static void declined(String host, String hash) {
        long now = System.currentTimeMillis();
        synchronized (LOCK) {
            Server server = serverLocked(host);
            server.lastSeenMs = now;
            server.declined++;
            server.lastHash = hash;
            touch();
        }
    }

    /** Forgets the consent given to a server, so the mod asks again next time. */
    public static void forgetTrust(String host) {
        synchronized (LOCK) {
            for (Server server : data().servers) {
                if (host != null && host.equals(server.host)) {
                    server.lastAcceptedMs = 0;
                    server.accepted = 0;
                }
            }
            touch();
        }
    }

    public static List<Server> servers() {
        synchronized (LOCK) {
            return new ArrayList<>(data().servers);
        }
    }

    public static Optional<Server> server(String host) {
        synchronized (LOCK) {
            for (Server server : data().servers) {
                if (host != null && host.equals(server.host)) return Optional.of(server);
            }
        }
        return Optional.empty();
    }

    // ---------------------------------------------------------------- history

    /** Records one pack push. Silently does nothing when history is switched off. */
    public static void event(String host, String url, String hash, String result, String detail,
                             long bytes, long durationMs) {
        if (!Config.get().historyEnabled()) return;
        Event entry = new Event(System.currentTimeMillis(), host, url, hash, result, detail, bytes, durationMs);
        int limit = Config.get().historyLimit();
        synchronized (LOCK) {
            List<Event> events = data().events;
            events.add(entry);
            while (events.size() > limit) events.remove(0);
            touch();
        }
    }

    /** Newest first, which is the order the history screen shows them in. */
    public static List<Event> events(int max) {
        List<Event> copy;
        synchronized (LOCK) {
            copy = new ArrayList<>(data().events);
        }
        copy.sort((a, b) -> Long.compare(b.timeMs, a.timeMs));
        return copy.size() > max ? new ArrayList<>(copy.subList(0, max)) : copy;
    }

    public static void clearHistory() {
        synchronized (LOCK) {
            data().events.clear();
            touch();
        }
    }

    /** Forgets everything: packs, servers and the log. */
    public static void clearAll() {
        synchronized (LOCK) {
            Data current = data();
            current.packs.clear();
            current.servers.clear();
            current.events.clear();
            touch();
        }
    }
}
