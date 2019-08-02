package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.paint.Color;

public interface LabelDelegate extends CommonLib{

    default void label(int x, int y, String text) {
        label(x, y, text, 1);
    }

    default void label(int x, int y, String text, int zLevel) {
        label(x, y, text, zLevel, Color.BLACK);
    }

    default void label(int x, int y, String text, int zLevel, Color textColor) {
        guiHelper().drawString(text, x, y, zLevel, textColor);
    }

    default void label(int x, int y, String text, int zLevel,  Color textColor, Color shadowColor) {
        guiHelper().drawString(text, x, y, zLevel, textColor, shadowColor);
    }
}
