package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.paint.Color;

public interface ITextRenderer extends ITextHelper
{
    void drawString(String text, float x, float y, float zLevel, Color textColor, Color shadowColor);

    void drawString(String text, float x, float y, float zLevel, Color textColor);

    default void drawStringMultiline(String text, float x, float y, float zLevel, Color textColor)
    {
        drawStringMultiline(text, x, y, zLevel, textColor, getStringHeight() / 4);
    }

    default void drawStringMultiline(String text, float x, float y, float zLevel, Color textColor, float lineSpacing)
    {
        drawStringMultiline(text, x, y, zLevel, textColor, Color.ALPHA, lineSpacing);
    }

    default void drawStringMultiline(String text, float x, float y, float zLevel, Color textColor, Color shadowColor)
    {
        drawStringMultiline(text, x, y, zLevel, textColor, shadowColor, getStringHeight() / 4);
    }

    void drawStringMultiline(String text, float x, float y, float zLevel, Color textColor, Color shadowColor, float lineSpacing);
}
