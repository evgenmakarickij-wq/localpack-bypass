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

    // Всё ниже трогается только из потока клиента.
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

    /** Ждём 300 мс, чтобы собрать в одну пачку все паки, которые сервер шлёт подряд. */
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
                            LOGGER.warn("Не удалось проверить локальные ресурспаки", error);
                            result = new Scan(List.of(), batch);
                        }
                        onScanned(client, dir, result);
                    } catch (RuntimeException e) {
                        LOGGER.error("Ошибка обработки ресурспаков", e);
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
            LOGGER.info("Найден локальный ресурспак: {}", a.fileName());
            a.incoming().send(Action.ACCEPTED);
        }
        if (scan.missing().isEmpty()) {
            apply(client, scan.found());
        } else {
            ask(client, dir, scan);
        }
    }

    /** Одно окно на всю пачку: одна кнопка «Скачать все ресурспаки». */
    private static void ask(Minecraft client, Path dir, Scan scan) {
        Screen previous = client.gui.screen();
        List<Incoming> missing = scan.missing();

        boolean required = false;
        for (Incoming i : missing) {
            if (i.packet().required()) required = true;
        }

        Component message = Component.literal(
                "Сервер отправил новые или изменённые ресурспаки: " + missing.size()
                        + hostSuffix(missing.get(0).packet().url())
                        + ".\nНажмите кнопку, чтобы скачать всё нужное сразу. "
                        + "Паки сохранятся в папку resourcepacks и включатся автоматически.");
        if (required) {
            message = message.copy().append(Component.literal(
                    "\n\nСервер требует эти паки: при отказе он может отключить вас."));
        }
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
                        apply(client, scan.found());
                    }
                },
                Component.literal("Ресурспаки сервера"),
                message,
                Component.literal("Скачать все ресурспаки"),
                Component.literal("Не скачивать")));
    }

    private static void downloadAll(Minecraft client, Path dir, Scan scan) {
        for (Incoming i : scan.missing()) {
            i.send(Action.ACCEPTED);
        }
        LOGGER.info("Скачиваю ресурспаки сервера: {}", scan.missing().size());

        // Один и тот же пак (по хешу) качаем один раз.
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
                                LOGGER.error("Не удалось скачать ресурспак", o.error());
                                i.send(o.error() instanceof IllegalArgumentException
                                        ? Action.INVALID_URL : Action.FAILED_DOWNLOAD);
                            }
                        }
                        apply(client, all);
                    } catch (RuntimeException e) {
                        LOGGER.error("Ошибка обработки ресурспаков", e);
                        finish(client);
                    }
                }));
    }

    /** Включает все паки пачки разом, сохраняет выбор и один раз перезагружает ресурсы. */
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

        // Старые паки мода снимаем, но не те, что пришли в этом же заходе на сервер.
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
            // Всё уже включено, перезагрузка не нужна.
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
                LOGGER.error("Пак {} не удалось включить (не найден или несовместим)", a.fileName());
                a.incoming().send(Action.FAILED_RELOAD);
            }
        }
        client.options.updateResourcePacks(repository); // сохраняет в options.txt

        client.reloadResourcePacks().whenComplete((ignored, error) -> client.execute(() -> {
            if (error != null) {
                LOGGER.error("Ошибка перезагрузки ресурсов", error);
            }
            for (Applied a : good) {
                a.incoming().send(error == null ? Action.SUCCESSFULLY_LOADED : Action.FAILED_RELOAD);
            }
            finish(client);
        }));
    }

    private static void finish(Minecraft client) {
        busy = false;
        schedule(client); // если за это время пришли ещё паки
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
