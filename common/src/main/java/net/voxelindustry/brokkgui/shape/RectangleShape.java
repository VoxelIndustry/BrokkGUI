package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;

public class RectangleShape implements ShapeDefinition
{
    @Override
    public void drawColored(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel)
    {
        renderer.getHelper().drawColoredRect(renderer, startX, startY, shape.getWidth(), shape.getHeight(), zLevel,
                color);
    }

    @Override
    public void drawColoredEmpty(GuiShape shape, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredEmptyRect(renderer, startX, startY, shape.getWidth(), shape.getHeight(),
                zLevel, color, lineWidth);
    }

    @Override
    public void drawBorder(GuiShape shape, IGuiRenderer renderer)
    {
        Color color = shape.getBorderColor();
        float borderLeft = shape.getBorderWidth(RectSide.LEFT);
        float borderRight = shape.getBorderWidth(RectSide.RIGHT);
        float borderTop = shape.getBorderWidth(RectSide.UP);
        float borderBottom = shape.getBorderWidth(RectSide.DOWN);

        float topLeftRadius = shape.getBorderRadius(RectCorner.TOP_LEFT);
        float topRightRadius = shape.getBorderRadius(RectCorner.TOP_RIGHT);
        float bottomLeftRadius = shape.getBorderRadius(RectCorner.BOTTOM_LEFT);
        float bottomRightRadius = shape.getBorderRadius(RectCorner.BOTTOM_RIGHT);

        if (bottomRightRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    shape.getxPos() + shape.getxTranslate() + shape.getWidth() - bottomRightRadius + borderRight - 1,
                    shape.getyPos() + shape.getyTranslate() + shape.getHeight() - bottomRightRadius + borderBottom - 1,
                    bottomRightRadius, shape.getzLevel(), color, RectCorner.BOTTOM_RIGHT);
        if (bottomLeftRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    shape.getxPos() + shape.getxTranslate() - borderLeft + bottomLeftRadius,
                    shape.getyPos() + shape.getyTranslate() + shape.getHeight() - bottomLeftRadius + borderBottom - 1,
                    bottomRightRadius, shape.getzLevel(), color, RectCorner.BOTTOM_LEFT);
        if (topLeftRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    shape.getxPos() + shape.getxTranslate() - borderLeft + topLeftRadius,
                    shape.getyPos() + shape.getyTranslate() - borderTop + topLeftRadius,
                    topLeftRadius, shape.getzLevel(), color, RectCorner.TOP_LEFT);
        if (topRightRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    shape.getxPos() + shape.getxTranslate() + shape.getWidth() - topRightRadius + borderRight - 1,
                    shape.getyPos() + shape.getyTranslate() - borderTop + topLeftRadius,
                    topLeftRadius, shape.getzLevel(), color, RectCorner.TOP_RIGHT);

        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.getHelper().drawColoredRect(renderer, shape.getxPos() + shape.getxTranslate() - borderLeft,
                    shape.getyPos() + shape.getyTranslate() - borderTop + topLeftRadius, borderLeft,
                    shape.getHeight() + borderTop - bottomLeftRadiusOffset - topLeftRadius,
                    shape.getzLevel(), color);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 0;
        if (borderTop > 0)
            renderer.getHelper().drawColoredRect(renderer,
                    shape.getxPos() + shape.getxTranslate() + topLeftRadiusOffset,
                    shape.getyPos() + shape.getyTranslate() - borderTop,
                    shape.getWidth() + borderRight - topLeftRadiusOffset - topRightRadius, borderTop,
                    shape.getzLevel(), color);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.getHelper().drawColoredRect(renderer, shape.getxPos() + shape.getxTranslate() + shape.getWidth(),
                    shape.getyPos() + shape.getyTranslate() + topRightRadiusOffset, borderRight,
                    shape.getHeight() + borderBottom - bottomRightRadius - topRightRadiusOffset,
                    shape.getzLevel(), color);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                bottomRightRadius - borderRight : 0;
        if (borderBottom > 0)
            renderer.getHelper().drawColoredRect(renderer,
                    shape.getxPos() + shape.getxTranslate() - borderLeft + bottomLeftRadius,
                    shape.getyPos() + shape.getyTranslate() + shape.getHeight(),
                    shape.getWidth() + borderLeft - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, shape.getzLevel(), color);
    }

    @Override
    public void drawTextured(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel)
    {
        renderer.getHelper().drawTexturedRect(renderer, startX, startY, texture.getUMin(), texture.getVMin(),
                texture.getUMax(), texture.getVMax(), shape.getWidth(), shape.getHeight(), zLevel);
    }

    @Override
    public boolean isMouseInside(GuiShape shape, int mouseX, int mouseY)
    {
        return mouseX > shape.getxPos() + shape.getxTranslate() && mouseX < shape.getxPos() + shape.getxTranslate() + shape.getWidth() &&
                mouseY > shape.getyPos() + shape.getyTranslate() && mouseY < shape.getyPos() + shape.getyTranslate() + shape.getHeight();
    }
}
