package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
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
                          float vMax, float width, float height, float zLevel, SpriteRotation rotation, RenderPass pass);

    void drawTexturedRectWithColor(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                                   float vMax, float width, float height, float zLevel, SpriteRotation rotation, RenderPass pass, Color color);

    default void drawTexturedRect(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                                  float vMax, float width, float height, float zLevel, RenderPass pass)
    {
        drawTexturedRect(renderer, xStart, yStart, uMin, vMin, uMax, vMax, width, height, zLevel, SpriteRotation.NONE, pass);
    }

    default void drawTexturedRect(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float width,
                                  float height, float zLevel, RenderPass pass)
    {
        drawTexturedRect(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel, pass);
    }

    default void drawTexturedRectWithColor(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                                           float vMax, float width, float height, float zLevel, RenderPass pass, Color color)
    {
        drawTexturedRectWithColor(renderer, xStart, yStart, uMin, vMin, uMax, vMax, width, height, zLevel, SpriteRotation.NONE, pass, color);
    }

    default void drawTexturedRectWithColor(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float width,
                                           float height, float zLevel, RenderPass pass, Color color)
    {
        drawTexturedRectWithColor(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel, pass, color);
    }

    default void drawColoredEmptyRect(IRenderCommandReceiver renderer, float startX, float startY, float width, float height,
                                      float zLevel, Color color, float thin, RenderPass pass)
    {
        drawColoredRect(renderer, startX, startY, width - thin, thin, zLevel, color, pass);
        drawColoredRect(renderer, startX + width - thin, startY, thin, height - thin, zLevel, color, pass);
        drawColoredRect(renderer, startX + thin, startY + height - thin, width - thin, thin, zLevel, color, pass);
        drawColoredRect(renderer, startX, startY + thin, thin, height - thin, zLevel, color, pass);
    }

    void drawColoredRect(IRenderCommandReceiver renderer, float startX, float startY, float width, float height, float zLevel,
                         Color color, RenderPass pass);

    void drawTexturedCircle(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                            float vMax, float radius, float zLevel, RenderPass pass);

    void drawTexturedCircle(IRenderCommandReceiver renderer, float xStart, float yStart, float uMin, float vMin, float radius,
                            float zLevel, RenderPass pass);

    void drawColoredEmptyCircle(IRenderCommandReceiver renderer, float startX, float startY, float radius, float zLevel,
                                Color color, float thin, RenderPass pass);

    void drawColoredCircle(IRenderCommandReceiver renderer, float startX, float startY, float radius, float zLevel, Color color, RenderPass pass);

    void drawColoredLine(IRenderCommandReceiver renderer, float startX, float startY, float endX, float endY, float lineWeight,
                         float zLevel, Color color, RenderPass pass);

    void drawColoredArc(IRenderCommandReceiver renderer, float centerX, float centerY, float radius, float zLevel, Color color, RectCorner corner, RenderPass pass);

    void startAlphaMask(double opacity);

    void closeAlphaMask();

    void beginScissor();

    void scissorBox(float fromX, float fromY, float toX, float toY);

    void endScissor();

    void bindTexture(Texture texture);
}