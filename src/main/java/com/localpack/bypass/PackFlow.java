package com.localpack.bypass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket.Action;
import net.minecraft.server.packs.repository.PackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class PackFlow {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    private static final String FILE_ID_PREFIX = "file/";
    private static final String AUTO_ID_PREFIX = FILE_ID_PREFIX + LocalPackBypassHelper.AUTO_FILE_PREFIX;
    private static final long SAME_SESSION_MS = 20_000L;

    private static final ExecutorService IO = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "localpack-io");
        t.setDaemon(true);
        return t;
    });

    private record Incoming(ClientCommonPacketListenerImpl listener, ClientboundResourcePackPushPacket packet) {
        void send(Action action) {
            listener.send(new ServerboundResourcePackPacket(packet.id(), action));
        }
    }

    private record Applied(Incoming incoming, String fileName) {}

    private record Scan(List<Applied> found, List<Incoming> missing) {}

    private record Outcome(String fileName, Throwable error) {}

    // Everything below is touched only from the client thread.
    private static final List<Incoming> QUEUE = new ArrayList<>();
    private static boolean timerScheduled = false;
    private static boolean busy = false;
    private static Set<String> recentIds = new LinkedHashSet<>();
    private static long lastApplyMs = 0L;

    private PackFlow() {}

    public static void handle(ClientCommonPacketListenerImpl listener, ClientboundResourcePackPushPacket packet) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            QUEUE.add(new Incoming(listener, packet));
            schedule(client);
        });
    }

    /** Wait 300 ms to collect all packs the server sends back-to-back into one batch. */
    private static void schedule(Minecraft client) {
        if (busy || timerScheduled || QUEUE.isEmpty()) return;
        timerScheduled = true;
        CompletableFuture.delayedExecutor(300, TimeUnit.MILLISECONDS)
                .execute(() -> client.execute(() -> {
                    timerScheduled = false;
                    startBatch(client);
                }));
    }

    private static void startBatch(Minecraft client) {
        if (busy || QUEUE.isEmpty()) return;
        busy = true;
        List<Incoming> batch = new ArrayList<>(QUEUE);
        QUEUE.clear();
        Path dir = client.getResourcePackDirectory();

        CompletableFuture
                .supplyAsync(() -> scan(dir, batch), IO)
                .whenComplete((scan, error) -> client.execute(() -> {
                    try {
                        Scan result = scan;
                        if (error != null) {
                            LOGGER.warn("Could not check local resource packs", error);
                            result = new Scan(List.of(), batch);
                        }
                        onScanned(client, dir, result);
                    } catch (RuntimeException e) {
                        LOGGER.error("Error while processing resource packs", e);
                        finish(client);
                    }
                }));
    }

    private static Scan scan(Path dir, List<Incoming> batch) {
        List<Applied> found = new ArrayList<>();
        List<Incoming> missing = new ArrayList<>();
        for (Incoming in : batch) {
            Path local = LocalPackBypassHelper.findLocalPackByHash(dir, in.packet().hash());
            if (local != null) {
                found.add(new Applied(in, local.getFileName().toString()));
            } else {
                missing.add(in);
            }
        }
        return new Scan(found, missing);
    }

    private static void onScanned(Minecraft client, Path dir, Scan scan) {
        for (Applied a : scan.found()) {
            LOGGER.info("Found local resource pack: {}", a.fileName());
            a.incoming().send(Action.ACCEPTED);
        }
        if (scan.missing().isEmpty()) {
            apply(client, scan.found());
        } else {
            ask(client, dir, scan);
        }
    }

    /** One screen for the whole batch: "Download all" or "Decline and leave". */
    private static void ask(Minecraft client, Path dir, Scan scan) {
        Screen previous = client.gui.screen();
        List<Incoming> missing = scan.missing();

        Component message = Component.translatable(
                "localpack-bypass.screen.message",
                missing.size(),
                hostSuffix(missing.get(0).packet().url()));
        for (Incoming i : missing) {
            if (i.packet().prompt().isPresent()) {
                message = message.copy().append(Component.literal("\n\n")).append(i.packet().prompt().get());
                break;
            }
        }

        client.gui.setScreen(new ConfirmScreen(
                accepted -> {
                    client.gui.setScreen(previous);
                    if (accepted) {
                        downloadAll(client, dir, scan);
                    } else {
                        for (Incoming i : missing) {
                            i.send(Action.DECLINED);
                        }
                        leaveServer(client, missing.get(0));
                    }
                },
                Component.translatable("localpack-bypass.screen.title"),
                message,
                Component.translatable("localpack-bypass.screen.download"),
                Component.translatable("localpack-bypass.screen.decline")));
    }

    /** Like vanilla on decline: disconnect from the server. */
    private static void leaveServer(Minecraft client, Incoming any) {
        QUEUE.clear();
        finish(client);
        // CHANGED: goes through the Disconnector interface implemented by the mixin
        ((Disconnector) any.listener()).localpackBypass$disconnect(
                Component.translatable("localpack-bypass.disconnect"));
    }

    private static void downloadAll(Minecraft client, Path dir, Scan scan) {
        for (Incoming i : scan.missing()) {
            i.send(Action.ACCEPTED);
        }
        LOGGER.info("Downloading server resource packs: {}", scan.missing().size());

        // The same pack (by hash) is downloaded only once.
        Map<String, CompletableFuture<Outcome>> byHash = new LinkedHashMap<>();
        for (Incoming i : scan.missing()) {
            String hash = i.packet().hash().toLowerCase(Locale.ROOT);
            String url = i.packet().url();
            byHash.computeIfAbsent(hash, h -> CompletableFuture.supplyAsync(() -> {
                try {
                    return new Outcome(LocalPackBypassHelper.download(url, h, dir), null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new Outcome(null, e);
                } catch (Throwable t) {
                    return new Outcome(null, t);
                }
            }, IO));
        }

        CompletableFuture.allOf(byHash.values().toArray(new CompletableFuture[0]))
                .whenComplete((ignored, error) -> client.execute(() -> {
                    try {
                        List<Applied> all = new ArrayList<>(scan.found());
                        for (Incoming i : scan.missing()) {
                            Outcome o = byHash.get(i.packet().hash().toLowerCase(Locale.ROOT)).join();
                            if (o.error() == null) {
                                all.add(new Applied(i, o.fileName()));
                            } else {
                                LOGGER.error("Could not download resource pack", o.error());
                                i.send(o.error() instanceof IllegalArgumentException
                                        ? Action.INVALID_URL : Action.FAILED_DOWNLOAD);
                            }
                        }
                        apply(client, all);
                    } catch (RuntimeException e) {
                        LOGGER.error("Error while processing resource packs", e);
                        finish(client);
                    }
                }));
    }

    /** Enables all packs of the batch at once, saves the selection and reloads resources once. */
    private static void apply(Minecraft client, List<Applied> applied) {
        if (applied.isEmpty()) {
            finish(client);
            return;
        }
        for (Applied a : applied) {
            a.incoming().send(Action.DOWNLOADED);
        }

        PackRepository repository = client.getResourcePackRepository();
        repository.reload();

        Set<String> ids = new LinkedHashSet<>();
        for (Applied a : applied) {
            ids.add(FILE_ID_PREFIX + a.fileName());
        }

        List<String> current = new ArrayList<>(repository.getSelectedIds());

        // Remove old packs downloaded by the mod, except those received in this same server join.
        long now = System.currentTimeMillis();
        Set<String> keep = new LinkedHashSet<>();
        if (now - lastApplyMs < SAME_SESSION_MS) {
            keep.addAll(recentIds);
        }
        keep.addAll(ids);
        recentIds = keep;
        lastApplyMs = now;

        Set<String> selected = new LinkedHashSet<>(current);
        selected.removeIf(s -> s.startsWith(AUTO_ID_PREFIX) && !keep.contains(s));
        selected.addAll(ids);

        if (new ArrayList<>(selected).equals(current)) {
            // Everything is already enabled, no reload needed.
            for (Applied a : applied) {
                a.incoming().send(Action.SUCCESSFULLY_LOADED);
            }
            finish(client);
            return;
        }

        repository.setSelected(selected);
        Set<String> active = new LinkedHashSet<>(repository.getSelectedIds());
        List<Applied> good = new ArrayList<>();
        for (Applied a : applied) {
            if (active.contains(FILE_ID_PREFIX + a.fileName())) {
                good.add(a);
            } else {
                LOGGER.error("Pack {} could not be enabled (not found or incompatible)", a.fileName());
                a.incoming().send(Action.FAILED_RELOAD);
            }
        }
        client.options.updateResourcePacks(repository); // saves to options.txt

        client.reloadResourcePacks().whenComplete((ignored, error) -> client.execute(() -> {
            if (error != null) {
                LOGGER.error("Error while reloading resources", error);
            }
            for (Applied a : good) {
                a.incoming().send(error == null ? Action.SUCCESSFULLY_LOADED : Action.FAILED_RELOAD);
            }
            finish(client);
        }));
    }

    private static void finish(Minecraft client) {
        busy = false;
        schedule(client); // in case more packs arrived meanwhile
    }

    private static String hostSuffix(String url) {
        try {
            String host = URI.create(url).getHost();
            return host == null ? "" : " (" + host + ")";
        } catch (RuntimeException e) {
            return "";
        }
    }
}
