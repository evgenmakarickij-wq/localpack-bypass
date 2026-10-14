package com.localpack.bypass.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * The Mod Menu entrypoint: what Mod Menu loads to find out that this mod has a settings screen.
 *
 * <p>This is the only class in the mod that names Mod Menu, and it is named in {@code fabric.mod.json}
 * as a string rather than a class reference, so a player who does not have Mod Menu never loads it.
 * That is why the dependency is {@code compileOnly}: the mod works without Mod Menu, and when Mod
 * Menu is there it simply gains a "Config" button.
 */
public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SettingsScreen::new;
    }
}
