package com.localpack.bypass.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.List;

/**
 * The scrolling column the mod's screens are built from: rows of widgets, one or two to a row.
 *
 * <p>The game's own options list would be the obvious thing to reuse, and it is what these screens
 * used before, but in this version it will take a setting and nothing else. addBig takes an option,
 * addSmall takes widgets, and a heading or a button the mod built fits through neither door — and
 * the row type and the method that adds one are both protected, so a screen outside the game's
 * package cannot reach past them either. This list is the mod's own, which is what makes all of it
 * reachable; a row is drawn the way the game draws one, so nothing looks different.
 */
final class ModList extends ContainerObjectSelectionList<ModList.Row> {

    /** The space between two widgets sharing a row — the same gap the game leaves in addSmall. */
    private static final int GAP = 4;

    ModList(Minecraft minecraft, int width, int height) {
        // 32 is where the game's options list begins, under the title; 25 is the height of a row.
        super(minecraft, width, height, 32, 25);
    }

    /** One widget across the whole row. */
    void add(AbstractWidget widget) {
        addEntry(new Row(List.of(widget)));
    }

    /** Two widgets side by side, a half row each. */
    void add(AbstractWidget left, AbstractWidget right) {
        addEntry(new Row(List.of(left, right)));
    }

    /** A setting, drawn by the game itself — the button the option knows how to make. */
    void add(OptionInstance<?> option) {
        addEntry(new Row(List.of(option.createButton(minecraft.options, 0, 0, getRowWidth()))));
    }

    @Override
    public int getRowWidth() {
        return Screens.BIG;
    }

    /** One row: the widgets in it, laid out left to right. */
    static final class Row extends ContainerObjectSelectionList.Entry<Row> {

        private final List<AbstractWidget> widgets;

        private Row(List<AbstractWidget> widgets) {
            this.widgets = widgets;
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered,
                                  float partialTick) {
            int x = getContentX();
            int y = getContentY();
            for (AbstractWidget widget : widgets) {
                widget.setX(x);
                widget.setY(y);
                widget.render(graphics, mouseX, mouseY, partialTick);
                x += widget.getWidth() + GAP;
            }
        }

        /** How a click reaches the widgets: the screen walks down into the row through this. */
        @Override
        public List<? extends GuiEventListener> children() {
            return widgets;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return widgets;
        }
    }
}
