package com.localpack.bypass.client;

import com.localpack.bypass.Config;
import com.localpack.bypass.PackStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * The mod's settings, shown by Mod Menu.
 *
 * <p>Built on the vanilla options screen so that everything the player already knows applies: the
 * same sliders, the same toggles, the same scrolling and the same "Done" button. There are no
 * hotkeys anywhere in this mod — every setting below is reachable only from here, which is where a
 * player looks for a setting.
 */
public final class SettingsScreen extends OptionsSubScreen {

    /** Kept outside the widget so a window resize does not throw away what was typed. */
    private String folderText;

    public SettingsScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("localpack-bypass.config.title"));
        this.folderText = Config.get().resourcePackDir();
    }

    @Override
    protected void addOptions() {
        Config config = Config.get();

        Screens.header(list, this, font, Component.translatable("localpack-bypass.config.header.storage"));

        EditBox folder = new EditBox(font, 0, 0, Screens.BIG, 20,
                Component.translatable("localpack-bypass.config.folder"));
        folder.setMaxLength(400);
        folder.setValue(folderText);
        folder.setHint(Component.translatable("localpack-bypass.config.folder.hint"));
        folder.setResponder(typed -> folderText = typed);
        Screens.row(list, this, folder);

        list.addSmall(
                Screens.small("localpack-bypass.config.openStorage", () -> Screens.open(PackStorage.downloadDir())),
                Screens.small("localpack-bypass.config.openGame", () -> Screens.open(PackStorage.gameDir())));

        Screens.header(list, this, font, Component.translatable("localpack-bypass.config.header.asking"));
        list.addBig(toggle("localpack-bypass.config.rememberServers",
                config.rememberServers(), config::rememberServers));
        list.addBig(toggle("localpack-bypass.config.keepEarlier",
                config.keepEarlierPacks(), config::keepEarlierPacks));

        Screens.header(list, this, font, Component.translatable("localpack-bypass.config.header.download"));
        list.addBig(amount("localpack-bypass.config.batchWait", "localpack-bypass.unit.ms",
                config.batchWaitMs(), Config.MIN_BATCH_WAIT_MS, Config.MAX_BATCH_WAIT_MS, config::batchWaitMs));
        list.addBig(amount("localpack-bypass.config.timeout", "localpack-bypass.unit.seconds",
                config.downloadTimeoutSeconds(), Config.MIN_TIMEOUT_SECONDS, Config.MAX_TIMEOUT_SECONDS,
                config::downloadTimeoutSeconds));
        list.addBig(amount("localpack-bypass.config.attempts", "localpack-bypass.unit.times",
                config.downloadAttempts(), Config.MIN_ATTEMPTS, Config.MAX_ATTEMPTS, config::downloadAttempts));

        Screens.header(list, this, font, Component.translatable("localpack-bypass.config.header.history"));
        list.addBig(toggle("localpack-bypass.config.history", config.historyEnabled(), config::historyEnabled));
        list.addBig(amount("localpack-bypass.config.historyLimit", "localpack-bypass.unit.entries",
                config.historyLimit(), Config.MIN_HISTORY, Config.MAX_HISTORY, config::historyLimit));
        list.addBig(toggle("localpack-bypass.config.verbose", config.verboseLog(), config::verboseLog));

        Screens.header(list, this, font, Component.translatable("localpack-bypass.config.header.manage"));
        list.addSmall(
                Screens.small("localpack-bypass.config.managePacks", () -> open(new PackManagerScreen(this))),
                Screens.small("localpack-bypass.config.openHistory", () -> open(new HistoryScreen(this))));
    }

    private void open(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }

    /** A whole number with a unit: a slider, so the player can see the range as well as the value. */
    private static OptionInstance<Integer> amount(String key, String unit, int value, int min, int max,
                                                  IntConsumer setter) {
        return new OptionInstance<Integer>(
                key,
                OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
                (caption, chosen) -> CommonComponents.optionNameValue(caption, Component.translatable(unit, chosen)),
                new OptionInstance.IntRange(min, max),
                value,
                chosen -> {
                    setter.accept(chosen);
                    Config.save();
                });
    }

    private static OptionInstance<Boolean> toggle(String key, boolean value, Consumer<Boolean> setter) {
        return OptionInstance.createBoolean(
                key,
                OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
                value,
                chosen -> {
                    setter.accept(chosen);
                    Config.save();
                });
    }

    /**
     * Stores the typed folder on the way out, and says so when it is not usable. A folder that was
     * silently dropped would be worse than one that is refused: the player would believe packs were
     * being kept somewhere they are not.
     */
    @Override
    public void onClose() {
        Config config = Config.get();
        String typed = folderText == null ? "" : folderText.trim();
        String problem = Config.problem(typed, true);
        boolean changed = false;
        if (problem == null && !typed.equals(config.resourcePackDir())) {
            config.resourcePackDir(typed);
            Config.save();
            changed = true;
        }
        super.onClose();

        if (problem != null) {
            minecraft.setScreen(new InfoScreen(Component.translatable("localpack-bypass.config.title"),
                    Component.translatable(problem)));
        } else if (changed && Screens.separateFolder()) {
            // Said once, when the choice is made, so a pack appearing twice on disk is not a mystery.
            minecraft.setScreen(new InfoScreen(Component.translatable("localpack-bypass.config.title"),
                    Component.translatable("localpack-bypass.config.folder.copied",
                            PackStorage.gameDir().toString())));
        }
    }
}
