package com.localpack.bypass.client;

import com.localpack.bypass.PackRegistry;
import com.localpack.bypass.PackStorage;
import com.localpack.bypass.PersistentPacks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Every pack the mod has downloaded, grouped by the server it came from, with a button per pack that
 * turns it on or off.
 *
 * <p>Turning a pack on or off here is the same thing as doing it on the vanilla resource pack
 * screen: the same selection, written to the same place, with the same reload. What this screen adds
 * is the one thing the vanilla screen cannot show — which server each pack arrived from.
 */
public final class PackManagerScreen extends OptionsSubScreen {

    private static final String FILE_ID_PREFIX = "file/";

    public PackManagerScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("localpack-bypass.packs.title"));
    }

    @Override
    protected void addOptions() {
        PackRepository repository = minecraft.getResourcePackRepository();
        repository.reload();
        Set<String> available = new LinkedHashSet<>(repository.getAvailableIds());
        Set<String> selected = new LinkedHashSet<>(repository.getSelectedIds());

        list.addSmall(
                Screens.small("localpack-bypass.config.openStorage", () -> Screens.open(PackStorage.downloadDir())),
                Screens.small("localpack-bypass.config.openGame", () -> Screens.open(PackStorage.gameDir())));
        list.addSmall(
                Screens.small("localpack-bypass.packs.forgetMissing", this::forgetMissing),
                Screens.small("localpack-bypass.packs.forgetAll", this::deleteAll));

        Map<String, List<PackRegistry.Pack>> byServer = PackRegistry.packsByServer();
        if (byServer.isEmpty()) {
            list.addHeader(Component.translatable("localpack-bypass.packs.empty"));
            return;
        }
        for (Map.Entry<String, List<PackRegistry.Pack>> server : byServer.entrySet()) {
            list.addHeader(Component.translatable("localpack-bypass.packs.server",
                    Screens.serverName(server.getKey()), server.getValue().size()));
            for (PackRegistry.Pack pack : server.getValue()) {
                list.addBig(packButton(pack, available, selected));
            }
        }
    }

    private Button packButton(PackRegistry.Pack pack, Set<String> available, Set<String> selected) {
        String id = FILE_ID_PREFIX + pack.file;
        boolean present = available.contains(id);
        boolean on = present && selected.contains(id);
        ChatFormatting color = !present ? ChatFormatting.RED : on ? ChatFormatting.GREEN : ChatFormatting.GRAY;
        Component state = Component.translatable(stateKey(present, on)).withStyle(color);

        return Button.builder(Component.empty().append(state).append(" ")
                        .append(Component.literal(pack.file == null ? "?" : pack.file)),
                        button -> press(pack, present, on))
                .width(Screens.BIG)
                .tooltip(Tooltip.create(details(pack, present)))
                .build();
    }

    private static String stateKey(boolean present, boolean on) {
        if (!present) return "localpack-bypass.packs.missing";
        return on ? "localpack-bypass.packs.on" : "localpack-bypass.packs.off";
    }

    private static Component details(PackRegistry.Pack pack, boolean present) {
        boolean details = com.localpack.bypass.Config.get().verboseLog();
        return Component.translatable("localpack-bypass.packs.details",
                Component.translatable(present ? "localpack-bypass.packs.details.action"
                        : "localpack-bypass.packs.details.gone"),
                Screens.serverName(pack.host),
                details ? Screens.size(pack.bytes) : Component.literal("-"),
                Component.literal(details && pack.url != null ? pack.url : "-"),
                Component.literal(details && pack.hash != null ? pack.hash : "-"));
    }

    /**
     * A pack that is still there is switched on or off; a pack whose file has gone is forgotten, so
     * the list stops offering something that cannot be switched on.
     */
    private void press(PackRegistry.Pack pack, boolean present, boolean on) {
        if (!present) {
            PackRegistry.forget(pack.hash);
            PackRegistry.flush();
            rebuildWidgets();
            return;
        }
        PackRepository repository = minecraft.getResourcePackRepository();
        repository.reload();
        Set<String> selection = new LinkedHashSet<>(repository.getSelectedIds());
        if (on) {
            selection.remove(FILE_ID_PREFIX + pack.file);
            PackRegistry.markUserDisabled(pack.hash);
        } else {
            selection.add(FILE_ID_PREFIX + pack.file);
            PackRegistry.markUserEnabled(pack.hash);
        }
        if (!selection.equals(new LinkedHashSet<>(repository.getSelectedIds()))) {
            repository.setSelected(selection);
            minecraft.options.updateResourcePacks(repository); // saves on() and reloads the pack stack
        }
        rebuildWidgets();
    }

    /** Drops the entries whose files are no longer in the folder. Touches no files. */
    private void forgetMissing() {
        Path dir = PackStorage.gameDir();
        for (PackRegistry.Pack pack : PackRegistry.packs()) {
            try {
                if (pack.file == null || !Files.isRegularFile(dir.resolve(pack.file))) {
                    PackRegistry.forget(pack.hash);
                }
            } catch (RuntimeException e) {
                PackRegistry.forget(pack.hash);
            }
        }
        PackRegistry.flush();
        rebuildWidgets();
    }

    private void deleteAll() {
        PersistentPacks.deleteAll(minecraft);
        rebuildWidgets();
    }
}
