package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
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

        if (bottomRightRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    paint.transform().leftPos() + paint.transform().width() - bottomRightRadius + borderRight - 1,
                    paint.transform().topPos() + paint.transform().height() - bottomRightRadius + borderBottom - 1,
                    bottomRightRadius, paint.transform().zLevel(), color, RectCorner.BOTTOM_RIGHT);
        if (bottomLeftRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    paint.transform().leftPos() - borderLeft + bottomLeftRadius,
                    paint.transform().topPos() + paint.transform().height() - bottomLeftRadius + borderBottom - 1,
                    bottomRightRadius, paint.transform().zLevel(), color, RectCorner.BOTTOM_LEFT);
        if (topLeftRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    paint.transform().leftPos() - borderLeft + topLeftRadius,
                    paint.transform().topPos() - borderTop + topLeftRadius,
                    topLeftRadius, paint.transform().zLevel(), color, RectCorner.TOP_LEFT);
        if (topRightRadius > 0)
            renderer.getHelper().drawColoredArc(renderer,
                    paint.transform().leftPos() + paint.transform().width() - topRightRadius + borderRight - 1,
                    paint.transform().topPos() - borderTop + topLeftRadius,
                    topLeftRadius, paint.transform().zLevel(), color, RectCorner.TOP_RIGHT);

        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.getHelper().drawColoredRect(renderer, paint.transform().leftPos() - borderLeft,
                    paint.transform().topPos() - borderTop + topLeftRadius, borderLeft,
                    paint.transform().height() + borderTop - bottomLeftRadiusOffset - topLeftRadius,
                    paint.transform().zLevel(), color);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 0;
        if (borderTop > 0)
            renderer.getHelper().drawColoredRect(renderer,
                    paint.transform().leftPos() + topLeftRadiusOffset,
                    paint.transform().topPos() - borderTop,
                    paint.transform().width() + borderRight - topLeftRadiusOffset - topRightRadius, borderTop,
                    paint.transform().zLevel(), color);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.getHelper().drawColoredRect(renderer, paint.transform().leftPos() + paint.transform().width(),
                    paint.transform().topPos() + topRightRadiusOffset, borderRight,
                    paint.transform().height() + borderBottom - bottomRightRadius - topRightRadiusOffset,
                    paint.transform().zLevel(), color);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                bottomRightRadius - borderRight : 0;
        if (borderBottom > 0)
            renderer.getHelper().drawColoredRect(renderer,
                    paint.transform().leftPos() - borderLeft + bottomLeftRadius,
                    paint.transform().topPos() + paint.transform().height(),
                    paint.transform().width() + borderLeft - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, paint.transform().zLevel(), color);
    }
}
