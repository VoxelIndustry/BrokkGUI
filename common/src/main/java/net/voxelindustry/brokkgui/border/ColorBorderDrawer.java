package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.exp.component.Paint;
import net.voxelindustry.brokkgui.exp.component.Transform;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;

public class ColorBorderDrawer
{
    public static void drawBorder(Paint paint, IGuiRenderer renderer)
    {
        Color color = paint.borderColor();
        float borderLeft = paint.borderWidth(RectSide.LEFT);
        float borderRight = paint.borderWidth(RectSide.RIGHT);
        float borderTop = paint.borderWidth(RectSide.UP);
        float borderBottom = paint.borderWidth(RectSide.DOWN);

        float topLeftRadius = paint.borderRadius(RectCorner.TOP_LEFT);
        float topRightRadius = paint.borderRadius(RectCorner.TOP_RIGHT);
        float bottomLeftRadius = paint.borderRadius(RectCorner.BOTTOM_LEFT);
        float bottomRightRadius = paint.borderRadius(RectCorner.BOTTOM_RIGHT);

        Transform transform = paint.getElement().transform();

        if (bottomRightRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    transform.rightPos() - bottomRightRadius + borderRight - 1,
                    transform.bottomPos() - bottomRightRadius + borderBottom - 1,
                    bottomRightRadius, transform.getzLevel(), color, RectCorner.BOTTOM_RIGHT);
        if (bottomLeftRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    transform.leftPos() - borderLeft + bottomLeftRadius,
                    transform.bottomPos() - bottomLeftRadius + borderBottom - 1,
                    bottomRightRadius, transform.getzLevel(), color, RectCorner.BOTTOM_LEFT);
        if (topLeftRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    transform.leftPos() - borderLeft + topLeftRadius,
                    transform.topPos() - borderTop + topLeftRadius,
                    topLeftRadius, transform.getzLevel(), color, RectCorner.TOP_LEFT);
        if (topRightRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    transform.rightPos() - topRightRadius + borderRight - 1,
                    transform.topPos() - borderTop + topLeftRadius,
                    topLeftRadius, transform.getzLevel(), color, RectCorner.TOP_RIGHT);

        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.getHelper().drawColoredRect(renderer, transform.leftPos() - borderLeft,
                    transform.topPos() - borderTop + topLeftRadius, borderLeft,
                    transform.height() + borderTop - bottomLeftRadiusOffset - topLeftRadius,
                    transform.getzLevel(), color);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 0;
        if (borderTop > 0)
            renderer.getHelper().drawColoredRect(renderer,
                    transform.leftPos() + topLeftRadiusOffset,
                    transform.topPos() - borderTop,
                    transform.width() + borderRight - topLeftRadiusOffset - topRightRadius, borderTop,
                    transform.getzLevel(), color);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.getHelper().drawColoredRect(renderer, transform.rightPos(),
                    transform.topPos() + topRightRadiusOffset, borderRight,
                    transform.height() + borderBottom - bottomRightRadius - topRightRadiusOffset,
                    transform.getzLevel(), color);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                bottomRightRadius - borderRight : 0;
        if (borderBottom > 0)
            renderer.getHelper().drawColoredRect(renderer,
                    transform.leftPos() - borderLeft + bottomLeftRadius,
                    transform.bottomPos(),
                    transform.width() + borderLeft - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, transform.getzLevel(), color);
    }
}
