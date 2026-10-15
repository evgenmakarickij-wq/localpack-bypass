package com.localpack.bypass.client;

import com.localpack.bypass.Config;
import com.localpack.bypass.PackRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * What servers sent, newest first: every resource pack push, what happened to it, and where it came
 * from. This is the record a player needs to answer "why did this server put something on my
 * screen?" — which pack, from which address, at what time, and whether it was downloaded, found
 * already, or refused.
 */
public final class HistoryScreen extends OptionsSubScreen {

    /** Enough to see a pattern without the file growing for ever; the setting decides what is kept. */
    private static final int SHOWN = 200;

    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("MM-dd HH:mm:ss", Locale.ROOT).withZone(ZoneId.systemDefault());

    public HistoryScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("localpack-bypass.history.title"));
    }

    @Override
    protected void addOptions() {
        list.addSmall(
                Screens.small("localpack-bypass.history.clear", this::confirmClear),
                Screens.small("localpack-bypass.history.refresh", this::rebuildWidgets));

        if (!Config.get().historyEnabled()) {
            list.addHeader(Component.translatable("localpack-bypass.history.off"));
            return;
        }

        List<PackRegistry.Event> events = PackRegistry.events(SHOWN);
        if (events.isEmpty()) {
            list.addHeader(Component.translatable("localpack-bypass.history.empty"));
            return;
        }
        list.addHeader(Component.translatable("localpack-bypass.history.count", events.size()));
        for (PackRegistry.Event event : events) {
            StringWidget row = new StringWidget(0, 0, Screens.BIG, 18, rowText(event), font);
            row.setTooltip(Tooltip.create(rowDetails(event)));
            list.addBig(row);
        }
    }

    private static Component rowText(PackRegistry.Event event) {
        return Component.translatable("localpack-bypass.history.row",
                Component.literal(TIME.format(Instant.ofEpochMilli(event.timeMs))),
                Screens.serverName(event.host),
                Component.translatable("localpack-bypass.result." + event.result));
    }

    private static Component rowDetails(PackRegistry.Event event) {
        Component details = Component.translatable("localpack-bypass.history.details",
                Component.literal(event.hash == null ? "?" : event.hash),
                Screens.size(event.bytes),
                Component.literal(event.durationMs > 0 ? event.durationMs + " ms" : "-"));
        if (event.url != null && !event.url.isEmpty()) {
            details = details.copy().append(Component.literal("\n")).append(Component.literal(event.url));
        }
        if (event.detail != null && !event.detail.isEmpty()) {
            details = details.copy().append(Component.literal("\n")).append(Component.literal(event.detail));
        }
        return details;
    }

    private void confirmClear() {
        minecraft.setScreen(new ConfirmScreen(
                yes -> {
                    minecraft.setScreen(this);
                    if (yes) {
                        PackRegistry.clearHistory();
                        PackRegistry.flush();
                        rebuildWidgets();
                    }
                },
                Component.translatable("localpack-bypass.history.clear.title"),
                Component.translatable("localpack-bypass.history.clear.message"),
                Component.translatable("localpack-bypass.history.clear.yes"),
                Component.translatable("localpack-bypass.history.clear.no")));
    }
}
