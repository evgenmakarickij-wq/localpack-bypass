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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class PackFlow {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    private static final String FILE_ID_PREFIX = "file/";
    private static final String AUTO_ID_PREFIX = FILE_ID_PREFIX + LocalPackBypassHelper.AUTO_FILE_PREFIX;

    private PackFlow() {}

    public static void handle(ClientCommonPacketListenerImpl listener, ClientboundResourcePackPushPacket packet) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> start(listener, packet, client));
    }

    private static void start(ClientCommonPacketListenerImpl listener,
                              ClientboundResourcePackPushPacket packet,
                              Minecraft client) {
        UUID id = packet.id();
        String hash = packet.hash();
        Path dir = client.getResourcePackDirectory();

        CompletableFuture
                .supplyAsync(() -> LocalPackBypassHelper.findLocalPackByHash(dir, hash))
                .exceptionally(t -> {
                    LOGGER.warn("Не вдалося перевірити локальні паки", t);
                    return null;
                })
                .thenAccept(found -> client.execute(() -> {
                    if (found != null) {
                        LOGGER.info("Знайдено локальний ресурспак з відповідним хешем: {}", found.getFileName());
                        listener.send(new ServerboundResourcePackPacket(id, Action.ACCEPTED));
                        activate(listener, id, client, found.getFileName().toString());
                    } else {
                        ask(listener, packet, client, dir);
                    }
                }));
    }

    private static void ask(ClientCommonPacketListenerImpl listener,
                            ClientboundResourcePackPushPacket packet,
                            Minecraft client,
                            Path dir) {
        UUID id = packet.id();
        Screen previous = client.gui.screen();

        Component message = Component.literal(
                "Сервер надіслав новий або змінений ресурспак"
                        + hostSuffix(packet.url())
                        + ".\nЗавантажити його в теку resourcepacks і ввімкнути?");
        if (packet.required()) {
            message = message.copy().append(Component.literal(
                    "\n\nСервер вимагає цей пак: після відмови він може вас відключити."));
        }
        if (packet.prompt().isPresent()) {
            message = message.copy().append(Component.literal("\n\n")).append(packet.prompt().get());
        }

        client.gui.setScreen(new ConfirmScreen(
                accepted -> {
                    client.gui.setScreen(previous);
                    if (accepted) {
                        download(listener, packet, client, dir);
                    } else {
                        listener.send(new ServerboundResourcePackPacket(id, Action.DECLINED));
                    }
                },
                Component.literal("Ресурспак сервера"),
                message));
    }

    private static void download(ClientCommonPacketListenerImpl listener,
                                 ClientboundResourcePackPushPacket packet,
                                 Minecraft client,
                                 Path dir) {
        UUID id = packet.id();
        listener.send(new ServerboundResourcePackPacket(id, Action.ACCEPTED));
        LOGGER.info("Завантажую ресурспак сервера...");

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return LocalPackBypassHelper.download(packet.url(), packet.hash(), dir);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new CompletionException(e);
                    } catch (java.io.IOException e) {
                        throw new CompletionException(e);
                    }
                })
                .whenComplete((fileName, error) -> client.execute(() -> {
                    if (error == null) {
                        activate(listener, id, client, fileName);
                        return;
                    }
                    Throwable cause = error instanceof CompletionException && error.getCause() != null
                            ? error.getCause() : error;
                    LOGGER.error("Не вдалося завантажити ресурспак", cause);
                    Action action = cause instanceof IllegalArgumentException
                            ? Action.INVALID_URL : Action.FAILED_DOWNLOAD;
                    listener.send(new ServerboundResourcePackPacket(id, action));
                }));
    }

    private static void activate(ClientCommonPacketListenerImpl listener,
                                 UUID id,
                                 Minecraft client,
                                 String fileName) {
        listener.send(new ServerboundResourcePackPacket(id, Action.DOWNLOADED));

        PackRepository repository = client.getResourcePackRepository();
        repository.reload();
        String packId = FILE_ID_PREFIX + fileName;

        List<String> current = new ArrayList<>(repository.getSelectedIds());
        Set<String> selected = new LinkedHashSet<>(current);
        selected.removeIf(s -> s.startsWith(AUTO_ID_PREFIX) && !s.equals(packId));
        selected.add(packId);

        if (new ArrayList<>(selected).equals(current)) {
            listener.send(new ServerboundResourcePackPacket(id, Action.SUCCESSFULLY_LOADED));
            return;
        }

        repository.setSelected(selected);
        if (!repository.getSelectedIds().contains(packId)) {
            LOGGER.error("Пак {} не вдалося увімкнути (не знайдений або несумісний)", fileName);
            listener.send(new ServerboundResourcePackPacket(id, Action.FAILED_RELOAD));
            return;
        }
        client.options.updateResourcePacks(repository);

        client.reloadResourcePacks().whenComplete((ignored, error) -> client.execute(() -> {
            if (error == null) {
                listener.send(new ServerboundResourcePackPacket(id, Action.SUCCESSFULLY_LOADED));
            } else {
                LOGGER.error("Помилка перезавантаження ресурсів", error);
                listener.send(new ServerboundResourcePackPacket(id, Action.FAILED_RELOAD));
            }
        }));
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
