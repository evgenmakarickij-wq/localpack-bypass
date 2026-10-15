package com.localpack.bypass.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * What the mod's three screens have in common: a title, a scrolling column of rows, and a way back.
 *
 * <p>The game's options screen would have given all of that, but it comes attached to a list that
 * only accepts settings — see {@link ModList} for why that does not fit here. What is left to do by
 * hand is small: put the list below the title and a Done button at the bottom, and draw the title
 * where the game draws one.
 */
abstract class ModScreen extends Screen {

    /** Rebuilt on every resize and every refresh, so a screen fills it rather than keeping it. */
    protected ModList list;

    private final Screen parent;

    protected ModScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    /** Fills the list. Runs again from the top on every resize and refresh. */
    protected abstract void fill();

    @Override
    protected void init() {
        list = new ModList(minecraft, width, height);
        fill();
        addRenderableWidget(list);
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(width / 2 - 100, height - 27, 200, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
