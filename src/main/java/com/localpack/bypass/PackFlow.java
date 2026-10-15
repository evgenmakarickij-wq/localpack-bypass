package com.localpack.bypass;

import com.localpack.bypass.client.InfoScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket.Action;
import net.minecraft.server.packs.repository.PackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Everything that happens between a server offering a resource pack and the pack being on screen.
 *
 * <p>A pack can arrive at any moment — during the configuration phase of a connection, or in the
 * middle of play — and the same server often sends several, one after another. The flow is therefore
 * built around three rules:
 *
 * <ul>
 *   <li><b>Ask once per pack.</b> The same pack offered again is not a new question. The answers
 *       given on a connection are kept, so a server that repeats itself is answered from memory, and
 *       a server the player has dealt with before is answered without a screen at all (see
 *       {@code Config.rememberServers}).
 *   <li><b>One screen at a time.</b> While a question is on screen nothing else is asked or started;
 *       the packs that arrive meanwhile wait in a queue and are handled with the same answer.
 *   <li><b>Never fail silently.</b> A download that does not work says so, on screen if there is
 *       room for it and in the log either way. A click that appears to do nothing is the failure
 *       mode this class exists to avoid.
 * </ul>
 *
 * <p>State below is touched only from the client thread, except the downloads, which run on
 * {@link #IO} and report back through {@code client.execute}.
 */
public final class PackFlow {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    private static final String FILE_ID_PREFIX = "file/";
    private static final String AUTO_ID_PREFIX = FILE_ID_PREFIX + LocalPackBypassHelper.AUTO_FILE_PREFIX;
    private static final String ZIP_SUFFIX = ".zip";

    /**
     * After the player agrees, further packs of the same connection are downloaded without asking,
     * and packs received on that connection within this window are kept enabled together.
     */
    private static final long APPROVAL_WINDOW_MS = 120_000L;

    private static final ExecutorService IO = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "localpack-io");
        t.setDaemon(true);
        return t;
    });

    /** One id, so a second problem replaces the first notice instead of stacking behind it. */
    private static final SystemToast.SystemToastId PROBLEM_TOAST = new SystemToast.SystemToastId();

    private record Incoming(ClientCommonPacketListenerImpl listener, ClientboundResourcePackPushPacket packet) {
        void send(Action action) {
            listener.send(new ServerboundResourcePackPacket(packet.id(), action));
        }

        String hash() {
            return packet.hash().toLowerCase(Locale.ROOT);
        }

        String url() {
            return packet.url();
        }

        /** The server this pack came from, or an empty string when the URL does not name one. */
        String host() {
            return hostOf(packet.url());
        }

        /** Identity of the connection, shared by its configuration and play phases. */
        Object connection() {
            return ((Disconnector) listener).localpackBypass$connectionKey();
        }

        /** The saved-server entry, which is where the game keeps "Server Resource Packs" per server. */
        ServerData serverData() {
            return ((Disconnector) listener).localpackBypass$serverData();
        }
    }

    private record Applied(Incoming incoming, String fileName) {}

    private record Scan(List<Applied> found, List<Incoming> missing) {}

    private record Outcome(String fileName, Throwable error) {}

    // Everything below is touched only from the client thread.
    private static final List<Incoming> QUEUE = new ArrayList<>();
    private static boolean timerScheduled = false;
    private static boolean busy = false;
    /** True while this mod is showing a question: nothing else is asked and no batch is started. */
    private static boolean asking = false;
    /** The question on screen, to notice if something else replaced it. */
    private static Screen prompt;
    private static Set<String> recentIds = new LinkedHashSet<>();
    private static long lastApplyMs = 0L;
    private static WeakReference<Object> lastApplyConnection = new WeakReference<>(null);
    private static WeakReference<Object> approvedConnection = new WeakReference<>(null);
    private static long approvedAtMs = 0L;
    /** Per connection: what the player already answered about each pack, by hash. */
    private static final Map<Object, Map<String, Boolean>> ANSWERS = new WeakHashMap<>();

    private PackFlow() {}

    public static void handle(ClientCommonPacketListenerImpl listener, ClientboundResourcePackPushPacket packet) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            Incoming incoming = new Incoming(listener, packet);
            Boolean answered = answerOf(incoming);
            if (Boolean.FALSE.equals(answered)) {
                // Already turned down on this connection: saying no again is the whole handling.
                LOGGER.info("Pack {} was declined on this connection already", incoming.hash());
                PackRegistry.event(incoming.host(), incoming.url(), incoming.hash(), "declined", null, 0L, 0L);
                incoming.send(Action.DECLINED);
                return;
            }
            if (Boolean.TRUE.equals(answered)) {
                LOGGER.info("Pack {} was accepted on this connection already", incoming.hash());
            }
            QUEUE.add(incoming);
            schedule(client);
        });
    }

    /** Waits for the rest of a batch — servers send their packs back to back — then handles them together. */
    private static void schedule(Minecraft client) {
        // A question that is no longer on screen was not answered: something else replaced it.
        if (asking && client.screen != prompt) {
            asking = false;
            prompt = null;
        }
        if (busy || timerScheduled || asking || QUEUE.isEmpty()) return;
        timerScheduled = true;
        int wait = Config.get().batchWaitMs();
        CompletableFuture.delayedExecutor(wait, TimeUnit.MILLISECONDS)
                .execute(() -> client.execute(() -> {
                    timerScheduled = false;
                    startBatch(client);
                }));
    }

    private static void startBatch(Minecraft client) {
        if (busy || asking || QUEUE.isEmpty()) return;
        busy = true;
        List<Incoming> batch = new ArrayList<>(QUEUE);
        QUEUE.clear();
        Path dir = PackStorage.downloadDir();

        // Reading the repository has to happen here, on the client thread; the rest — remembering,
        // hashing, copying — is file work and belongs off it.
        Set<String> available = new LinkedHashSet<>(client.getResourcePackRepository().getAvailableIds());

        CompletableFuture
                .supplyAsync(() -> scan(dir, available, batch), IO)
                .whenComplete((scan, error) -> client.execute(() -> {
                    try {
                        Scan result = scan;
                        if (error != null) {
                            LOGGER.warn("Could not check local resource packs", error);
                            result = new Scan(List.of(), batch);
                        }
                        onScanned(client, result);
                    } catch (RuntimeException e) {
                        LOGGER.error("Error while processing resource packs", e);
                        finish(client);
                    }
                }));
    }

    /**
     * Sorts a batch into packs that are already on disk and packs that have to be fetched.
     *
     * <p>The game's own pack list is looked at first: a pack the repository already knows is one
     * file name, no hashing and no folder scan. Only when that misses is what this mod remembers
     * consulted — one {@code stat} — and only when that misses too is the folder hashed. A server
     * that genuinely changed its pack sends a hash that is in none of those places, so it is
     * fetched again, which is the point.
     */
    private static Scan scan(Path dir, Set<String> available, List<Incoming> batch) {
        List<Applied> found = new ArrayList<>();
        List<Incoming> missing = new ArrayList<>();
        for (Incoming in : batch) {
            String hash = in.hash();
            String fileName = LocalPackBypassHelper.fileNameFor(hash);

            if (available.contains(FILE_ID_PREFIX + fileName)) {
                LOGGER.info("The game already lists {} for this connection", fileName);
                found.add(new Applied(in, fileName));
                continue;
            }

            Path local = locate(dir, hash);
            if (local != null) {
                try {
                    // The pack may live outside the game's own folder; the game can only load it
                    // from there, so a copy is put in place when one is needed.
                    PackStorage.ensureLoadable(local);
                    found.add(new Applied(in, fileName));
                    continue;
                } catch (IOException e) {
                    LOGGER.warn("Could not make {} usable by the game: {}", local, e.toString());
                }
            }

            missing.add(in);
            LOGGER.info("No local pack with SHA-1 {} (url {}). Zips in {}: {}",
                    hash, in.url(), dir, LocalPackBypassHelper.describeZips(dir));
        }
        return new Scan(found, missing);
    }

    /** The file holding this pack: what the mod remembers, or a hash pass over the folder. */
    private static Path locate(Path dir, String hash) {
        Optional<PackRegistry.Pack> remembered = PackRegistry.find(hash);
        if (remembered.isPresent()) {
            Path file = dir.resolve(remembered.get().file);
            if (Files.isRegularFile(file)) {
                LOGGER.info("Found pack {} for SHA-1 {} from memory", file, hash);
                return file;
            }
        }
        return LocalPackBypassHelper.findLocalPackByHash(dir, hash);
    }

    private static void onScanned(Minecraft client, Scan scan) {
        for (Applied a : scan.found()) {
            LOGGER.info("Found local resource pack: {}", a.fileName());
            PackRegistry.event(a.incoming().host(), a.incoming().url(), a.incoming().hash(),
                    "found", a.fileName(), 0L, 0L);
            a.incoming().send(Action.ACCEPTED);
        }
        if (scan.missing().isEmpty()) {
            apply(client, scan.found(), List.of());
        } else {
            ask(client, scan);
        }
    }

    private static boolean isApproved(Incoming in) {
        Object key = in.connection();
        return key != null
                && approvedConnection.get() == key
                && System.currentTimeMillis() - approvedAtMs < APPROVAL_WINDOW_MS;
    }

    private static void approve(Incoming in) {
        approvedConnection = new WeakReference<>(in.connection());
        approvedAtMs = System.currentTimeMillis();
    }

    private static Boolean answerOf(Incoming in) {
        Object key = in.connection();
        if (key == null) return null;
        Map<String, Boolean> answers = ANSWERS.get(key);
        return answers == null ? null : answers.get(in.hash());
    }

    /** The per-server "Server Resource Packs" setting, or Prompt when there is no saved server. */
    private static ServerData.ServerPackStatus packStatus(Incoming in) {
        try {
            ServerData data = in.serverData();
            return data == null ? ServerData.ServerPackStatus.PROMPT : data.getResourcePackStatus();
        } catch (RuntimeException e) {
            return ServerData.ServerPackStatus.PROMPT;
        }
    }

    /**
     * Keeps the answer, so the same question is not asked again — in this mod's own memory, which is
     * what makes a server without a saved entry behave the same way, and in the game's per-server
     * setting, which is the switch the player can see and change in the server list.
     */
    private static void rememberConsent(Incoming in) {
        String host = in.host();
        PackRegistry.accepted(host, in.hash());
        ServerData data = in.serverData();
        if (data == null) return;
        try {
            data.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
            ServerList.saveSingleServer(data);
        } catch (RuntimeException e) {
            LOGGER.warn("Could not keep the resource pack choice for this server: {}", e.toString());
        }
    }

    private static void answer(Incoming in, boolean accepted) {
        Object key = in.connection();
        if (key == null) return;
        ANSWERS.computeIfAbsent(key, k -> new HashMap<>()).put(in.hash(), accepted);
    }

    /**
     * Asks the player about everything that is not on disk yet, or answers it from what is already
     * known: the game's own per-server "Server Resource Packs" setting, a pack they agreed to on this
     * connection, a server they have agreed to before, or a question they already answered.
     */
    private static void ask(Minecraft client, Scan scan) {
        Incoming first = scan.missing().get(0);
        String host = first.host();

        // The game keeps this setting per server — "Enabled", "Prompt", "Disabled" — and vanilla
        // never asks again when it is Enabled, so neither does this. Cancelling the pack push means
        // vanilla's own download never runs, which is the whole point of this mod, but the choice
        // the player made is still theirs and is honoured here.
        ServerData.ServerPackStatus status = packStatus(first);
        if (status == ServerData.ServerPackStatus.DISABLED) {
            LOGGER.info("Resource packs are switched off for {}; declining", host.isEmpty() ? "this server" : host);
            for (Incoming i : scan.missing()) {
                PackRegistry.event(i.host(), i.url(), i.hash(), "declined", "server packs disabled", 0L, 0L);
            }
            declineAll(client, scan);
            return;
        }

        Boolean remembered = answerOf(first);
        boolean trusted = status == ServerData.ServerPackStatus.ENABLED
                || (Config.get().rememberServers() && PackRegistry.isTrusted(host));
        if (Boolean.TRUE.equals(remembered) || isApproved(first) || trusted) {
            LOGGER.info("Downloading {} more pack(s) from {} without asking (already agreed)",
                    scan.missing().size(), host.isEmpty() ? "this server" : host);
            approve(first);
            downloadAll(client, scan);
            return;
        }
        if (Boolean.FALSE.equals(remembered)) {
            declineAll(client, scan);
            return;
        }

        Screen previous = client.screen;
        Component message = Component.translatable(
                "localpack-bypass.screen.message",
                scan.missing().size(),
                hostSuffix(first.url()));
        for (Incoming i : scan.missing()) {
            if (i.packet().prompt().isPresent()) {
                message = message.copy().append(Component.literal("\n\n")).append(i.packet().prompt().get());
                break;
            }
        }

        // The click that started the join is often still in flight when this screen appears, and the
        // button that started it sits roughly where these two do, so a click landing in the first
        // moment is that one rather than an answer.
        //
        // A timestamp is used for this rather than ConfirmScreen.setDelay, which looks like the
        // obvious tool but is not: it switches both buttons off and leaves them off until tick() has
        // run ten times, and it dereferences buttons that only exist once the screen has been shown.
        // A question the player cannot answer is worse than one they answer a little too early, so
        // the screen stays fully usable and a too-early click is simply ignored.
        final long ignoreClicksUntil = System.currentTimeMillis() + 300;

        ConfirmScreen question = new ConfirmScreen(
                accepted -> {
                    if (System.currentTimeMillis() < ignoreClicksUntil) return;
                    asking = false;
                    prompt = null;
                    client.setScreen(previous);
                    answer(first, accepted);
                    if (accepted) {
                        approve(first);
                        rememberConsent(first);
                        downloadAll(client, scan);
                    } else {
                        PackRegistry.declined(host, first.hash());
                        for (Incoming i : scan.missing()) {
                            PackRegistry.event(i.host(), i.url(), i.hash(), "declined", null, 0L, 0L);
                        }
                        declineAll(client, scan);
                    }
                },
                Component.translatable("localpack-bypass.screen.title"),
                message,
                Component.translatable("localpack-bypass.screen.download"),
                Component.translatable("localpack-bypass.screen.decline"));

        asking = true;
        prompt = question;
        client.setScreen(question);
    }

    /** Like vanilla on decline: say no, then leave the server. */
    private static void declineAll(Minecraft client, Scan scan) {
        for (Incoming i : scan.missing()) {
            i.send(Action.DECLINED);
        }
        if (scan.missing().isEmpty()) {
            finish(client);
            return;
        }
        QUEUE.clear();
        finish(client);
        ((Disconnector) scan.missing().get(0).listener())
                .localpackBypass$disconnect(Component.translatable("localpack-bypass.disconnect"));
    }

    private static void downloadAll(Minecraft client, Scan scan) {
        Path dir = PackStorage.downloadDir();
        for (Incoming i : scan.missing()) {
            i.send(Action.ACCEPTED);
        }
        LOGGER.info("Downloading {} server resource pack(s) into {}", scan.missing().size(), dir);

        Duration timeout = Duration.ofSeconds(Config.get().downloadTimeoutSeconds());
        int attempts = Config.get().downloadAttempts();

        // The same pack (by hash) is downloaded only once, however many times it was pushed.
        Map<String, CompletableFuture<Outcome>> byHash = new LinkedHashMap<>();
        for (Incoming i : scan.missing()) {
            String hash = i.hash();
            String url = i.url();
            String host = i.host();
            byHash.computeIfAbsent(hash, h -> CompletableFuture.supplyAsync(() -> {
                long started = System.currentTimeMillis();
                try {
                    LocalPackBypassHelper.Downloaded downloaded =
                            LocalPackBypassHelper.download(url, h, dir, timeout, attempts);
                    PackStorage.ensureLoadable(dir.resolve(downloaded.fileName()));
                    long took = System.currentTimeMillis() - started;
                    PackRegistry.remember(h, downloaded.fileName(), downloaded.bytes(), host, url);
                    PackRegistry.event(host, url, h, "downloaded", null, downloaded.bytes(), took);
                    LOGGER.info("Downloaded {} ({} bytes) in {} ms", downloaded.fileName(), downloaded.bytes(), took);
                    return new Outcome(downloaded.fileName(), null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new Outcome(null, e);
                } catch (Throwable t) {
                    PackRegistry.event(host, url, h, "failed", detailOf(t), 0L,
                            System.currentTimeMillis() - started);
                    return new Outcome(null, t);
                }
            }, IO));
        }

        CompletableFuture.allOf(byHash.values().toArray(new CompletableFuture[0]))
                .whenComplete((ignored, error) -> client.execute(() -> {
                    try {
                        List<Applied> all = new ArrayList<>(scan.found());
                        List<Component> failures = new ArrayList<>();
                        for (Incoming i : scan.missing()) {
                            Outcome o = byHash.get(i.hash()).join();
                            if (o.error() == null) {
                                all.add(new Applied(i, o.fileName()));
                                continue;
                            }
                            LOGGER.error("Could not download resource pack {} from {}", i.hash(), i.url(), o.error());
                            i.send(isBadUrl(o.error()) ? Action.INVALID_URL : Action.FAILED_DOWNLOAD);
                            failures.add(Component.translatable("localpack-bypass.screen.failed",
                                    hostSuffix(i.url()), reason(o.error())));
                        }
                        apply(client, all, failures);
                    } catch (RuntimeException e) {
                        LOGGER.error("Error while processing resource packs", e);
                        finish(client);
                    }
                }));
    }

    /** Enables every pack of the batch at once, saves the selection and reloads resources once. */
    private static void apply(Minecraft client, List<Applied> applied, List<Component> failures) {
        if (applied.isEmpty()) {
            finish(client);
            show(client, failures);
            return;
        }
        for (Applied a : applied) {
            a.incoming().send(Action.DOWNLOADED);
        }

        PackRepository repository = client.getResourcePackRepository();
        repository.reload();

        Set<String> ids = new LinkedHashSet<>();
        Set<String> hosts = new LinkedHashSet<>();
        for (Applied a : applied) {
            ids.add(FILE_ID_PREFIX + a.fileName());
            hosts.add(a.incoming().host());
        }

        // Packs this mod enabled earlier on the same connection stay on, so a batch that arrived in
        // two halves is not half switched off by the second half.
        Object connKey = applied.get(0).incoming().connection();
        long now = System.currentTimeMillis();
        Set<String> keep = new LinkedHashSet<>();
        if (connKey != null && lastApplyConnection.get() == connKey && now - lastApplyMs < APPROVAL_WINDOW_MS) {
            keep.addAll(recentIds);
        }
        keep.addAll(ids);
        recentIds = keep;
        lastApplyMs = now;
        lastApplyConnection = new WeakReference<>(connKey);

        boolean keepEarlier = Config.get().keepEarlierPacks();
        List<String> current = new ArrayList<>(repository.getSelectedIds());
        Set<String> selected = new LinkedHashSet<>(current);
        selected.removeIf(id -> {
            if (!id.startsWith(AUTO_ID_PREFIX) || ids.contains(id) || keep.contains(id)) return false;
            // The pack this server gave us last time is replaced by the one it is sending now. Packs
            // from other servers are none of this batch's business, so they are left as they were
            // unless the player asked for a clean slate.
            String owner = PackRegistry.hostOfHash(hashOfId(id)).orElse(null);
            if (owner != null && hosts.contains(owner)) return true;
            return !keepEarlier;
        });
        selected.addAll(ids);

        if (!new ArrayList<>(selected).equals(current)) {
            repository.setSelected(selected);
            // Writes the selection to options.txt, and reloads the pack stack if it changed. Vanilla
            // does both here, which is why there is no separate reload call: asking for one as well
            // is the double reload of MC-252082.
            client.options.updateResourcePacks(repository);
        }

        List<Applied> good = new ArrayList<>();
        List<Component> problems = new ArrayList<>(failures);
        Set<String> active = new LinkedHashSet<>(repository.getSelectedIds());
        for (Applied a : applied) {
            if (active.contains(FILE_ID_PREFIX + a.fileName())) {
                good.add(a);
            } else {
                LOGGER.error("Pack {} could not be enabled (not found or incompatible)", a.fileName());
                a.incoming().send(Action.FAILED_RELOAD);
                problems.add(Component.translatable("localpack-bypass.screen.reloadFailed", a.fileName()));
            }
        }

        // Answered here and now rather than from a reload callback. The server keeps a connection in
        // its configuration phase until it hears one of these, and Minecraft defers a reload that is
        // asked for while a loading overlay is up — returning a future that completes much later, or
        // not at all if the join is abandoned. Waiting on it means a join that sits on "Joining
        // world" forever; the packs are selected and the reload is on its way either way.
        for (Applied a : good) {
            a.incoming().send(Action.SUCCESSFULLY_LOADED);
        }

        finish(client);
        show(client, problems);
    }

    /** The hash inside a {@code file/localpack-<hash>.zip} id. */
    private static String hashOfId(String id) {
        int end = id.endsWith(ZIP_SUFFIX) ? id.length() - ZIP_SUFFIX.length() : id.length();
        return end > AUTO_ID_PREFIX.length() ? id.substring(AUTO_ID_PREFIX.length(), end) : "";
    }

    private static void finish(Minecraft client) {
        busy = false;
        if (approvedConnection.get() != null) {
            approvedAtMs = System.currentTimeMillis(); // keep the approval alive while packs keep coming
        }
        schedule(client); // in case more packs arrived meanwhile
    }

    /**
     * Says what did not work. Leaving the player with a click that appears to have done nothing is
     * the bug this exists to prevent, so anything that fails ends up here: on screen when the player
     * is in a world or looking at one of this mod's screens, and as a toast when a join is in
     * progress and the screen belongs to that join.
     */
    private static void show(Minecraft client, List<Component> problems) {
        if (problems.isEmpty()) return;

        Component message = Component.empty();
        int shown = Math.min(problems.size(), 4);
        for (int i = 0; i < shown; i++) {
            if (i > 0) message = message.copy().append(Component.literal("\n"));
            message = message.copy().append(problems.get(i));
        }
        if (problems.size() > shown) {
            message = message.copy().append(Component.literal("\n"))
                    .append(Component.translatable("localpack-bypass.screen.moreFailures", problems.size() - shown));
        }

        LOGGER.warn("Resource pack problem: {}", message.getString());
        Component heading = Component.translatable("localpack-bypass.screen.failedTitle");

        Screen current = client.screen;
        if (current == null || current instanceof InfoScreen) {
            client.setScreen(new InfoScreen(heading, message));
            return;
        }
        SystemToast.addOrUpdate(client.getToastManager(), PROBLEM_TOAST, heading, message);
    }

    private static String hostOf(String url) {
        try {
            String host = URI.create(url).getHost();
            return host == null ? "" : host.toLowerCase(Locale.ROOT);
        } catch (RuntimeException e) {
            return "";
        }
    }

    private static String hostSuffix(String url) {
        String host = hostOf(url);
        return host.isEmpty() ? "" : " (" + host + ")";
    }

    /**
     * What went wrong, in the player's language, as a line short enough to fit in a screen.
     *
     * <p>The message the exception carries is about sockets and digests and is written in whatever
     * language the code uses; it goes to the log and to the record, and the player gets one of these
     * instead.
     */
    private static Component reason(Throwable error) {
        if (error instanceof LocalPackBypassHelper.DownloadFailedException failed) {
            return switch (failed.kind()) {
                case BAD_URL -> Component.translatable("localpack-bypass.error.url");
                case HTTP_STATUS -> Component.translatable("localpack-bypass.error.status", httpCode(failed));
                case TIMEOUT -> Component.translatable("localpack-bypass.error.timeout");
                case CHECKSUM -> Component.translatable("localpack-bypass.error.checksum");
                case DISK -> Component.translatable("localpack-bypass.error.disk");
                case CONNECTION -> Component.translatable("localpack-bypass.error.connection");
            };
        }
        if (error instanceof InterruptedException) {
            return Component.translatable("localpack-bypass.error.stopped");
        }
        String text = error == null ? null : error.getMessage();
        if (text == null || text.isBlank()) {
            text = error == null ? "?" : error.getClass().getSimpleName();
        }
        return Component.translatable("localpack-bypass.error.other", text);
    }

    /** True when there is no point trying this address again: the server gave one that cannot be used. */
    private static boolean isBadUrl(Throwable error) {
        return error instanceof LocalPackBypassHelper.DownloadFailedException failed
                && failed.kind() == LocalPackBypassHelper.Failure.BAD_URL;
    }

    /**
     * The line the record keeps about a failure: what kind it was and the one detail that goes with
     * it. Kept separate from what the player is shown, because this is what a server's pack has to be
     * judged by and it has to read the same in every language.
     */
    private static String detailOf(Throwable error) {
        if (error instanceof LocalPackBypassHelper.DownloadFailedException failed) {
            return failed.detail();
        }
        return error == null ? "" : error.toString();
    }

    /** The number inside a "http 404" detail, or a dash when it cannot be read. */
    private static String httpCode(LocalPackBypassHelper.DownloadFailedException failed) {
        String detail = failed.detail();
        return detail.startsWith("http ") ? detail.substring(5) : detail;
    }
}
