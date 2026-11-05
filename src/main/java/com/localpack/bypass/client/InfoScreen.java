package com.localpack.bypass.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * One message and one button. Used to tell the player something that has no options to choose
 * between — a download that did not work, a folder that cannot be used.
 */
public class InfoScreen extends Screen {

    private final Screen parent;
    private final Component message;
    private final LinearLayout layout = LinearLayout.vertical().spacing(12);

    public InfoScreen(Screen parent, Component heading, Component message) {
        super(heading);
        this.parent = parent;
        this.message = message;
    }

    @Override
    protected void init() {
        layout.defaultCellSetting().alignHorizontallyCenter();
        layout.addChild(new StringWidget(title, font));
        layout.addChild(new MultiLineTextWidget(message, font)
                .setMaxWidth(Math.max(120, width - 50))
                .setMaxRows(Math.max(4, (height - 100) / 12))
                .setCentered(true));
        layout.addChild(Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(200).build());
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        FrameLayout.centerInRectangle(layout, getRectangle());
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }
}
