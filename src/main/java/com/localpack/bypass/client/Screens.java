package com.localpack.bypass.client;

import com.localpack.bypass.Config;
import com.localpack.bypass.PackStorage;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/** Bits shared by the mod's screens. */
final class Screens {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    /** The width vanilla gives a full-row widget, and the width of half of one. */
    static final int BIG = 310;
    static final int SMALL = 150;

    private Screens() {}

    static Button big(String key, Runnable action) {
        return Button.builder(Component.translatable(key), b -> action.run()).width(BIG).build();
    }

    static Button small(String key, Runnable action) {
        return Button.builder(Component.translatable(key), b -> action.run()).width(SMALL).build();
    }

    /**
     * A heading row: a line of text, with nothing to click.
     */
    static void header(ModList list, Font font, Component text) {
        list.add(new StringWidget(0, 0, BIG, 18, text, font));
    }

    /**
     * A full-width row holding a widget the mod owns rather than a setting: a text field, a button,
     * a line of history.
     */
    static void row(ModList list, AbstractWidget widget) {
        list.add(widget);
    }

    /** True when packs are kept somewhere other than the folder the game itself loads from. */
    static boolean separateFolder() {
        return !Config.packDir().toAbsolutePath().normalize()
                .equals(PackStorage.gameDir().toAbsolutePath().normalize());
    }

    /** Opens a folder in whatever file manager the system has, creating it if it is not there yet. */
    static void open(Path dir) {
        try {
            Files.createDirectories(dir);
            Util.getPlatform().openPath(dir);
        } catch (Exception e) {
            LOGGER.warn("Could not open {}: {}", dir, e.toString());
        }
    }

    /** A size a player can read, in units the language files can translate. */
    static Component size(long bytes) {
        if (bytes <= 0) return Component.translatable("localpack-bypass.size.unknown");
        if (bytes < 1024) return Component.translatable("localpack-bypass.size.bytes", bytes);
        if (bytes < 1024 * 1024) {
            return Component.translatable("localpack-bypass.size.kb", decimal(bytes / 1024.0));
        }
        return Component.translatable("localpack-bypass.size.mb", decimal(bytes / (1024.0 * 1024.0)));
    }

    private static String decimal(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    /** "example.org" to "example.org", and "" to a phrase rather than an empty button. */
    static Component serverName(String host) {
        return host == null || host.isEmpty()
                ? Component.translatable("localpack-bypass.packs.unknownServer")
                : Component.literal(host);
    }
}
