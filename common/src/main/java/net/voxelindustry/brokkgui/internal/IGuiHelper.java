package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.Vector2i;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.sprite.Texture;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IGuiHelper
{
    void bindTexture(Texture texture);

    void beginScissor();

    void endScissor();

    void scissorBox(float f, float g, float h, float i);

    void drawString(String string, float x, float y, float zLevel, Color textColor, Color shadowColor);

    void drawString(String string, float x, float y, float zLevel, Color textColor);

    default void drawStringMultiline(String string, float x, float y, float zLevel, Color textColor)
    {
        drawStringMultiline(string, x, y, zLevel, textColor, getStringHeight() / 4);
    }

    default void drawStringMultiline(String string, float x, float y, float zLevel, Color textColor, float lineSpacing)
    {
        drawStringMultiline(string, x, y, zLevel, textColor, Color.ALPHA, lineSpacing);
    }

    default void drawStringMultiline(String string, float x, float y, float zLevel, Color textColor, Color shadowColor)
    {
        drawStringMultiline(string, x, y, zLevel, textColor, shadowColor, getStringHeight() / 4);
    }

    void drawStringMultiline(String string, float x, float y, float zLevel, Color textColor, Color shadowColor, float lineSpacing);

    void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                          float vMax, float width, float height, float zLevel, SpriteRotation rotation);

    void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                          float vMax, float width, float height, float zLevel);

    void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float width,
                          float height, float zLevel);

    void drawColoredEmptyRect(IGuiRenderer renderer, float startX, float startY, float width, float height,
                              float zLevel, Color c, float thin);

    void drawColoredRect(IGuiRenderer renderer, float startX, float startY, float width, float height, float zLevel,
                         Color color);

    void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                            float vMax, float radius, float zLevel);

    void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float radius,
                            float zLevel);

    void drawColoredEmptyCircle(IGuiRenderer renderer, float startX, float startY, float radius, float zLevel,
                                Color color, float thin);

    void drawColoredCircle(IGuiRenderer renderer, float startX, float startY, float radius, float zLevel, Color c);

    void drawColoredLine(IGuiRenderer renderer, float startX, float startY, float endX, float endY, float lineWeight,
                         float zLevel, Color c);

    void drawColoredArc(IGuiRenderer renderer, float centerX, float centerY, float radius, float zLevel, Color color, RectCorner corner);

    void translateVecToScreenSpace(Vector2i vec);

    String trimStringToPixelWidth(String str, int pixelWidth);

    float getStringWidth(String str);

    float getStringWidthMultiLine(String str);

    float getStringHeight();

    default float getStringHeightMultiLine(String str)
    {
        return getStringHeightMultiLine(str, getDefaultLineSpacing());
    }

    float getStringHeightMultiLine(String str, float lineSpacing);

    default float getDefaultLineSpacing()
    {
        return getStringHeight() / 4;
    }

    void startAlphaMask(double opacity);

    void closeAlphaMask();
}