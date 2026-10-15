package com.localpack.bypass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asks the player about a new release and drives the install.
 *
 * <p>The prompt only ever appears on the title screen, so a session in progress is never
 * interrupted. The download shows a screen with no buttons; when it finishes, the result replaces
 * that screen. If the player has moved on by then (they pressed escape and joined a world), the
 * outcome is only written to the log instead of being pushed in front of them.
 *
 * <p>Set {@code -Dlocalpack.bypass.updateCheck=false} to switch the whole thing off.
 */
@Environment(EnvType.CLIENT)
public final class UpdateFlow {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    private static final String DISABLE_PROPERTY = "localpack.bypass.updateCheck";

    // Client thread only.
    private static boolean started;
    private static Updater.UpdateInfo pending;
    private static boolean prompted;
    private static Screen progressScreen;
    /** Where the player was before the update screens took over: the title screen. */
    private static Screen returnScreen;

    private UpdateFlow() {}

    public static void init() {
        if (started) return;
        started = true;

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.info("Development environment: not checking for updates");
            return;
        }
        if (!Boolean.parseBoolean(System.getProperty(DISABLE_PROPERTY, "true"))) {
            LOGGER.info("Update checks are disabled by {}", DISABLE_PROPERTY);
            return;
        }

        ClientTickEvents.END_CLIENT_TICK.register(UpdateFlow::tick);

        Updater.check(false).thenAccept(info -> {
            if (info == null) return;
            Minecraft.getInstance().execute(() -> pending = info);
        });
    }

    private static void tick(Minecraft client) {
        if (pending == null || prompted) return;
        if (!(client.screen instanceof TitleScreen)) return;

        prompted = true;
        Updater.UpdateInfo info = pending;
        Screen previous = client.screen;
        client.setScreen(new ConfirmScreen(
                accepted -> {
                    client.setScreen(previous);
                    if (accepted) {
                        download(client, info);
                    } else {
                        LOGGER.info("The player skipped {}", info.version());
                        Updater.skipVersion(info.version());
                    }
                },
                Component.translatable("localpack-bypass.update.title"),
                Component.translatable("localpack-bypass.update.message", info.version(), Updater.currentVersion()),
                Component.translatable("localpack-bypass.update.download"),
                Component.translatable("localpack-bypass.update.skip")));
    }

    private static void download(Minecraft client, Updater.UpdateInfo info) {
        showProgress(client, Component.translatable("localpack-bypass.update.downloading", info.version()));

        Updater.install(info).thenAccept(result -> client.execute(() -> {
            if (client.screen != progressScreen) {
                // The player left the title screen while the update was downloading: do not interrupt.
                LOGGER.info("Update finished ({}) while the player was elsewhere", result.kind());
                progressScreen = null;
                return;
            }
            progressScreen = null;
            showOutcome(client, result);
        }));
    }

    /** ConfirmScreen with both buttons missing: a plain message screen for the duration of the download. */
    private static void showProgress(Minecraft client, Component message) {
        returnScreen = client.screen;
        progressScreen = new ConfirmScreen(
                accepted -> client.setScreen(returnScreen),
                Component.translatable("localpack-bypass.update.title"),
                message,
                null,
                null);
        client.setScreen(progressScreen);
    }

    private static void showOutcome(Minecraft client, Updater.InstallResult result) {
        Component message = switch (result.kind()) {
            case INSTALLED -> Component.translatable("localpack-bypass.update.installed", result.version());
            case QUEUED -> result.detail().isEmpty()
                    ? Component.translatable("localpack-bypass.update.queued", result.version())
                    : Component.translatable("localpack-bypass.update.queuedManual", result.version(), result.detail());
            case DEV -> Component.translatable("localpack-bypass.update.dev");
            case FAILED -> Component.translatable("localpack-bypass.update.failed", result.detail());
        };
        client.setScreen(new ConfirmScreen(
                accepted -> client.setScreen(returnScreen),
                Component.translatable("localpack-bypass.update.title"),
                message,
                Component.translatable("localpack-bypass.update.ok"),
                null));
    }
}
