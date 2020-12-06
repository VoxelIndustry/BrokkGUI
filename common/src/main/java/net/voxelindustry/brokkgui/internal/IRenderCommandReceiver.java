package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.sprite.Texture;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IRenderCommandReceiver extends ITextRenderer
{
    void beginMatrix();

    void endMatrix();

    void translateMatrix(float posX, float posY, float posZ);

    void rotateMatrix(float angle, float x, float y, float z);

    void scaleMatrix(float scaleX, float scaleY, float scaleZ);

    void drawTexturedRect(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                          float vMax, float width, float height, float zLevel, SpriteRotation rotation);

    default void drawTexturedRect(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                                  float vMax, float width, float height, float zLevel)
    {
        drawTexturedRect(renderer, xStart, yStart, uMin, vMin, uMax, vMax, width, height, zLevel, SpriteRotation.NONE);
    }

    default void drawTexturedRect(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float width,
                                  float height, float zLevel)
    {
        drawTexturedRect(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel);

    }

    default void drawColoredEmptyRect(IRenderCommandReceiver renderer, float startX, float startY, float width, float height,
                                      float zLevel, Color color, float thin)
    {
        drawColoredRect(renderer, startX, startY, width - thin, thin, zLevel, color);
        drawColoredRect(renderer, startX + width - thin, startY, thin, height - thin, zLevel, color);
        drawColoredRect(renderer, startX + thin, startY + height - thin, width - thin, thin, zLevel, color);
        drawColoredRect(renderer, startX, startY + thin, thin, height - thin, zLevel, color);
    }

    void drawColoredRect(IRenderCommandReceiver renderer, float startX, float startY, float width, float height, float zLevel,
                         Color color);

    void drawTexturedCircle(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                            float vMax, float radius, float zLevel);

    void drawTexturedCircle(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float radius,
                            float zLevel);

    void drawColoredEmptyCircle(IRenderCommandReceiver renderer, float startX, float startY, float radius, float zLevel,
                                Color color, float thin);

    void drawColoredCircle(IRenderCommandReceiver renderer, float startX, float startY, float radius, float zLevel, Color c);

    void drawColoredLine(IRenderCommandReceiver renderer, float startX, float startY, float endX, float endY, float lineWeight,
                         float zLevel, Color c);

    void drawColoredArc(IRenderCommandReceiver renderer, float centerX, float centerY, float radius, float zLevel, Color color, RectCorner corner);

    void startAlphaMask(double opacity);

    void closeAlphaMask();

    void beginScissor();

    void scissorBox(float fromX, float fromY, float toX, float toY);

    void endScissor();

    void bindTexture(Texture texture);
}