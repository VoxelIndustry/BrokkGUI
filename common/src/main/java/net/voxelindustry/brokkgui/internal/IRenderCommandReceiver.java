package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkcolor.ColorLike;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
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

    void drawTexturedRect(float xStart, float yStart, float uMin, float vMin, float uMax,
                          float vMax, float width, float height, float zLevel, SpriteRotation rotation, RenderPass pass);

    void drawTexturedRectWithColor(float xStart, float yStart, float uMin, float vMin, float uMax,
                                   float vMax, float width, float height, float zLevel, SpriteRotation rotation, RenderPass pass, ColorLike color);

    default void drawTexturedRect(float xStart, float yStart, float uMin, float vMin, float uMax,
                                  float vMax, float width, float height, float zLevel, RenderPass pass)
    {
        drawTexturedRect(xStart, yStart, uMin, vMin, uMax, vMax, width, height, zLevel, SpriteRotation.NONE, pass);
    }

    default void drawTexturedRect(float xStart, float yStart, float uMin, float vMin, float width,
                                  float height, float zLevel, RenderPass pass)
    {
        drawTexturedRect(xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel, pass);
    }

    default void drawTexturedRectWithColor(float xStart, float yStart, float uMin, float vMin, float uMax,
                                           float vMax, float width, float height, float zLevel, RenderPass pass, ColorLike color)
    {
        drawTexturedRectWithColor(xStart, yStart, uMin, vMin, uMax, vMax, width, height, zLevel, SpriteRotation.NONE, pass, color);
    }

    default void drawTexturedRectWithColor(float xStart, float yStart, float uMin, float vMin, float width,
                                           float height, float zLevel, RenderPass pass, ColorLike color)
    {
        drawTexturedRectWithColor(xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel, pass, color);
    }

    default void drawColoredEmptyRect(float startX, float startY, float width, float height,
                                      float zLevel, ColorLike color, float thin, RenderPass pass)
    {
        drawColoredRect(startX, startY, width - thin, thin, zLevel, color, pass);
        drawColoredRect(startX + width - thin, startY, thin, height - thin, zLevel, color, pass);
        drawColoredRect(startX + thin, startY + height - thin, width - thin, thin, zLevel, color, pass);
        drawColoredRect(startX, startY + thin, thin, height - thin, zLevel, color, pass);
    }

    void drawColoredRect(float startX, float startY, float width, float height, float zLevel,
                         ColorLike color, RenderPass pass);

    void drawColoredQuad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float zLevel, ColorLike color, RenderPass pass);

    void drawColoredTriangles(float zLevel, ColorLike color, RenderPass pass, float... vertices);

    void drawTexturedCircle(float xStart, float yStart, float uMin, float vMin, float uMax,
                            float vMax, float radius, float zLevel, RenderPass pass);

    void drawTexturedCircle(float xStart, float yStart, float uMin, float vMin, float radius,
                            float zLevel, RenderPass pass);

    void drawColoredEmptyCircle(float startX, float startY, float radius, float zLevel,
                                ColorLike color, float thin, RenderPass pass);

    void drawColoredCircle(float startX, float startY, float radius, float zLevel, ColorLike color, RenderPass pass);

    void drawColoredLine(float startX, float startY, float endX, float endY, float lineWeight,
                         float zLevel, ColorLike color, RenderPass pass);

    void drawColoredArc(float centerX, float centerY, float radius, float zLevel, ColorLike color, RectCorner corner, RenderPass pass);

    void startAlphaMask(double opacity);

    void closeAlphaMask();

    void bindTexture(Texture texture);

    default void pushMask(RectBox mask)
    {
        pushMask(mask.getLeft(), mask.getTop(), mask.getRight(), mask.getBottom());
    }

    void pushMask(float leftPos, float topPos, float rightPos, float bottomPos);

    void popMask();
}