package com.localpack.bypass;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Keeps accepted archives active after disconnect while respecting a later manual disable. */
public final class PersistentPacks {

    private static final String FILE_PREFIX = "file/";
    private static final Set<String> PENDING = ConcurrentHashMap.newKeySet();
    private static final Object TRANSFER_LOCK = new Object();
    private static long generation;
    private static int activeTransfers;
    private static boolean deleting;
    private static final Set<Path> ACTIVE_TARGETS = new LinkedHashSet<>();
    private static final Set<Thread> ACTIVE_THREADS = new LinkedHashSet<>();
    private static final ExecutorService IO = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "localpack-cleanup");
        thread.setDaemon(true);
        return thread;
    });

    // Client thread only. Waiting a few ticks lets vanilla unload its session pack first.
    private static int activationCountdown = -1;

    private PersistentPacks() {}

    public static void initialize() {
        for (PackRegistry.Pack pack : PackRegistry.packs()) {
            if (pack.pendingEnable) PENDING.add(pack.hash);
        }
        if (!PENDING.isEmpty()) activationCountdown = 5;
        ClientTickEvents.END_CLIENT_TICK.register(PersistentPacks::tick);
    }

    public static void queue(String hash) {
        if (hash == null || hash.isBlank()) return;
        PENDING.add(hash.toLowerCase(java.util.Locale.ROOT));
        Minecraft.getInstance().execute(() -> activationCountdown = Math.max(activationCountdown, 1));
    }

    /** True when the verified archive is already selected as a normal persistent resource pack. */
    public static boolean isActive(String hash) {
        Minecraft client = Minecraft.getInstance();
        return PackRegistry.pack(hash).filter(pack -> !pack.userDisabled).filter(pack -> {
            Path file = PackStorage.gameDir().resolve(pack.file);
            try {
                return Files.isRegularFile(file) && (pack.bytes <= 0 || Files.size(file) == pack.bytes)
                        && client.getResourcePackRepository().getSelectedIds().contains(FILE_PREFIX + pack.file);
            } catch (IOException | RuntimeException e) {
                return false;
            }
        }).isPresent();
    }

    public static long beginTransfer(Set<Path> targets) {
        synchronized (TRANSFER_LOCK) {
            if (deleting) return -1L;
            activeTransfers++;
            targets.stream().map(path -> path.toAbsolutePath().normalize()).forEach(ACTIVE_TARGETS::add);
            ACTIVE_THREADS.add(Thread.currentThread());
            return generation;
        }
    }

    public static void endTransfer(Set<Path> targets) {
        synchronized (TRANSFER_LOCK) {
            activeTransfers = Math.max(0, activeTransfers - 1);
            targets.stream().map(path -> path.toAbsolutePath().normalize()).forEach(ACTIVE_TARGETS::remove);
            ACTIVE_THREADS.remove(Thread.currentThread());
            TRANSFER_LOCK.notifyAll();
        }
    }

    public static void runIfCurrent(long expectedGeneration, Runnable action) {
        synchronized (TRANSFER_LOCK) {
            if (!deleting && generation == expectedGeneration) action.run();
        }
    }

    public static void afterDisconnect() {
        Minecraft.getInstance().execute(() -> {
            if (!PENDING.isEmpty()) activationCountdown = Math.max(activationCountdown, 3);
        });
    }

    private static void tick(Minecraft client) {
        if (activationCountdown < 0) return;
        if (activationCountdown-- > 0) return;
        activationCountdown = -1;
        PackRepository repository = client.getResourcePackRepository();
        repository.reload();
        activatePending(client, repository);
    }

    private static void activatePending(Minecraft client, PackRepository repository) {
        Set<String> available = new LinkedHashSet<>(repository.getAvailableIds());
        Set<String> selected = new LinkedHashSet<>(repository.getSelectedIds());

        // A mod-owned selection that disappeared was manually disabled in the vanilla pack screen.
        for (PackRegistry.Pack pack : PackRegistry.packs()) {
            String id = FILE_PREFIX + pack.file;
            if (pack.autoEnabled && available.contains(id) && !selected.contains(id)) {
                PackRegistry.markUserDisabled(pack.hash);
            } else if (pack.userDisabled && selected.contains(id)) {
                PackRegistry.markUserEnabled(pack.hash);
            }
        }

        boolean changed = false;
        for (PackRegistry.Pack pack : PackRegistry.packs()) {
            if (!pack.pendingEnable && !PENDING.contains(pack.hash)) continue;
            String id = FILE_PREFIX + pack.file;
            if (pack.userDisabled) {
                PackRegistry.clearPending(pack.hash);
                PENDING.remove(pack.hash);
                continue;
            }
            if (!available.contains(id)) continue;
            changed |= selected.add(id);
            PackRegistry.markAutoEnabled(pack.hash);
            PENDING.remove(pack.hash);
        }

        if (changed) {
            repository.setSelected(selected);
            client.options.updateResourcePacks(repository);
        }
        PackRegistry.flush();
    }

    /** Deselects, forgets and deletes every downloaded archive without restarting the game. */
    public static void deleteAll(Minecraft client) {
        long deletionGeneration;
        Set<Path> activeTargets;
        synchronized (TRANSFER_LOCK) {
            if (deleting) return;
            deleting = true;
            deletionGeneration = ++generation;
            activeTargets = new LinkedHashSet<>(ACTIVE_TARGETS);
            ACTIVE_THREADS.forEach(Thread::interrupt);
        }
        List<PackRegistry.Pack> packs = PackRegistry.packs();
        client.getDownloadedPackSource().popAll();

        PackRepository repository = client.getResourcePackRepository();
        repository.reload();
        Set<String> selected = new LinkedHashSet<>(repository.getSelectedIds());
        Set<String> downloadedIds = new LinkedHashSet<>();
        packs.forEach(pack -> downloadedIds.add(FILE_PREFIX + pack.file));
        boolean changed = selected.removeAll(downloadedIds);
        if (changed) {
            repository.setSelected(selected);
            client.options.updateResourcePacks(repository);
        }

        PENDING.clear();
        activationCountdown = -1;
        PackRegistry.clearPacks();
        PackRegistry.flush();
        CompletableFuture.runAsync(() -> deleteWithRetries(packs, activeTargets, deletionGeneration), IO);
    }

    private static void deleteWithRetries(List<PackRegistry.Pack> packs, Set<Path> transferTargets,
                                          long deletionGeneration) {
        Set<Path> files = new LinkedHashSet<>();
        try {
            for (int attempt = 0; attempt < 120; attempt++) {
                synchronized (TRANSFER_LOCK) {
                    if (!deleting || generation != deletionGeneration) return;
                }
                collectOwnedFiles(packs, files);
                files.addAll(transferTargets);
                files.removeIf(PersistentPacks::deleteOwnedPath);
                synchronized (TRANSFER_LOCK) {
                    if (activeTransfers == 0 && files.isEmpty()) return;
                }
                TimeUnit.MILLISECONDS.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            synchronized (TRANSFER_LOCK) {
                if (generation == deletionGeneration) {
                    deleting = false;
                    generation++;
                }
                TRANSFER_LOCK.notifyAll();
            }
        }
        if (!files.isEmpty()) {
            LocalPackBypassClient.LOGGER.warn("Could not delete {} resource pack file(s): {}", files.size(), files);
        }
    }

    private static void collectOwnedFiles(List<PackRegistry.Pack> packs, Set<Path> files) {
        for (PackRegistry.Pack pack : packs) {
            if (pack.file == null) continue;
            ownedFile(PackStorage.gameDir(), pack.file).ifPresent(files::add);
            ownedFile(PackStorage.downloadDir(), pack.file).ifPresent(files::add);
        }
        addVanillaCacheFiles(packs, files);
    }

    private static java.util.Optional<Path> ownedFile(Path root, String name) {
        try {
            Path normalizedRoot = root.toAbsolutePath().normalize();
            Path file = Path.of(name);
            if (file.isAbsolute() || file.getNameCount() != 1) return java.util.Optional.empty();
            Path candidate = normalizedRoot.resolve(file).normalize();
            return candidate.getParent() != null && candidate.getParent().equals(normalizedRoot)
                    ? java.util.Optional.of(candidate) : java.util.Optional.empty();
        } catch (RuntimeException e) {
            return java.util.Optional.empty();
        }
    }

    private static void addVanillaCacheFiles(List<PackRegistry.Pack> packs, Set<Path> files) {
        Path root = GamePaths.gameDir().resolve("downloads").toAbsolutePath().normalize();
        if (!Files.isDirectory(root)) return;
        try (var children = Files.list(root)) {
            List<Path> idDirectories = children.filter(Files::isDirectory).toList();
            for (PackRegistry.Pack pack : packs) {
                if (!LocalPackBypassHelper.isValidSha1(pack.hash)) continue;
                for (Path idDirectory : idDirectories) {
                    Path candidate = idDirectory.resolve(pack.hash.toLowerCase(java.util.Locale.ROOT)).normalize();
                    if (candidate.startsWith(root) && candidate.getParent().equals(idDirectory)) {
                        files.add(candidate);
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            LocalPackBypassClient.LOGGER.warn("Could not list Minecraft's resource-pack cache: {}", e.toString());
        }
    }

    private static boolean deleteQuietly(Path file) {
        try {
            Files.deleteIfExists(file);
            return !Files.exists(file);
        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    private static boolean deleteOwnedPath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (var children = Files.walk(path)) {
                    for (Path child : children.sorted(java.util.Comparator.reverseOrder()).toList()) {
                        Files.deleteIfExists(child);
                    }
                }
            } else {
                Files.deleteIfExists(path);
            }
            return !Files.exists(path);
        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

}
