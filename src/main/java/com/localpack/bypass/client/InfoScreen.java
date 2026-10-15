package com.localpack.bypass.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

/**
 * One message and one button. Used to tell the player something that has no options to choose
 * between — a download that did not work, a folder that cannot be used.
 */
public class InfoScreen extends Screen {

    private final Component heading;
    private final Component message;
    private final Runnable afterClose;
    private List<FormattedCharSequence> lines = List.of();
    private int textTop;

    public InfoScreen(Component heading, Component message) {
        this(heading, message, null);
    }

    public InfoScreen(Component heading, Component message, Runnable afterClose) {
        super(heading);
        this.heading = heading;
        this.message = message;
        this.afterClose = afterClose;
    }

    @Override
    protected void init() {
        lines = font.split(message, Math.max(120, width - 80));
        textTop = Math.max(40, height / 2 - lines.size() * 6 - 20);
        int buttonY = Math.min(height - 28, textTop + lines.size() * 12 + 16);
        addRenderableWidget(Button.builder(Component.translatable("localpack-bypass.gui.close"), b -> onClose())
                .bounds(width / 2 - 100, buttonY, 200, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, heading, width / 2, Math.max(16, textTop - 24), 0xFFFFFFFF);
        int y = textTop;
        for (FormattedCharSequence line : lines) {
            graphics.drawCenteredString(font, line, width / 2, y, 0xFFFFFFFF);
            y += 12;
        }
    }

    @Override
    public void onClose() {
        if (afterClose != null) {
            afterClose.run();
        } else {
            minecraft.setScreen(null);
        }
    }
}
