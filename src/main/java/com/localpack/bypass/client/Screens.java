package com.localpack.bypass.client;

import com.localpack.bypass.Config;
import com.localpack.bypass.PackStorage;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
     * A heading row.
     *
     * <p>An options list in this version of the game knows two things: one option, through addBig,
     * and a pair of widgets, through addSmall. A heading is neither, so its row is built by hand —
     * which is all the addHeader of the newer version did.
     */
    static void header(OptionsList list, Screen screen, Font font, Component text) {
        Rows.add(list, screen, new StringWidget(0, 0, BIG, 18, text, font));
    }

    /**
     * A full-width row holding a widget the mod owns rather than an option: a text field, a button,
     * a line of history. Same reason as above — addBig takes an option and nothing else.
     */
    static void row(OptionsList list, Screen screen, AbstractWidget widget) {
        Rows.add(list, screen, widget);
    }

    /**
     * The one way into a row that is not an option.
     *
     * <p>The list's Entry type is protected, so nothing outside the class — or a subclass of it —
     * can build one; the only public doors are addBig and addSmall, and neither fits a heading or a
     * single widget. Extending the list is what makes Entry reachable. This class exists for that
     * access alone and is never instantiated, which is why its constructor is private.
     */
    private static final class Rows extends OptionsList {

        private Rows(Minecraft minecraft, int width, OptionsSubScreen screen) {
            super(minecraft, width, screen);
        }

        static void add(OptionsList list, Screen screen, AbstractWidget widget) {
            list.addEntry(Entry.big(List.of(widget), screen));
        }
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
