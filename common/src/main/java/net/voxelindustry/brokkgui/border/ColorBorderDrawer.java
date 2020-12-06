package net.voxelindustry.brokkgui.border;

import com.google.common.base.Stopwatch;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.Color;

public class ColorBorderDrawer
{
    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
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

        float leftPos = paint.transform().leftPos();
        float rightPos = paint.transform().rightPos();
        float bottomPos = paint.transform().bottomPos();
        float topPos = paint.transform().topPos();
        float zLevel = paint.transform().zLevel();

        if (bottomLeftRadius == 0 && bottomRightRadius == 0 && topRightRadius == 0 && topLeftRadius == 0 &&
                borderLeft == borderRight && borderLeft == borderTop && borderLeft == borderBottom)
        {
            renderer.drawColoredEmptyRect(renderer,
                    leftPos - borderLeft,
                    topPos - borderLeft,
                    paint.transform().width() + borderLeft * 2,
                    paint.transform().height() + borderLeft * 2,
                    zLevel,
                    color,
                    borderLeft);
            return;
        }

        if (bottomRightRadius > 0)
            renderer.drawColoredArc(renderer,
                    rightPos - bottomRightRadius + borderRight - 1,
                    bottomPos - bottomRightRadius + borderBottom - 1,
                    bottomRightRadius, zLevel, color, RectCorner.BOTTOM_RIGHT);
        if (bottomLeftRadius > 0)
            renderer.drawColoredArc(renderer,
                    leftPos - borderLeft + bottomLeftRadius,
                    bottomPos - bottomLeftRadius + borderBottom - 1,
                    bottomRightRadius, zLevel, color, RectCorner.BOTTOM_LEFT);
        if (topLeftRadius > 0)
            renderer.drawColoredArc(renderer,
                    leftPos - borderLeft + topLeftRadius,
                    topPos - borderTop + topLeftRadius,
                    topLeftRadius, zLevel, color, RectCorner.TOP_LEFT);
        if (topRightRadius > 0)
            renderer.drawColoredArc(renderer,
                    rightPos - topRightRadius + borderRight - 1,
                    topPos - borderTop + topLeftRadius,
                    topLeftRadius, zLevel, color, RectCorner.TOP_RIGHT);

        Stopwatch watch = Stopwatch.createStarted();
        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.drawColoredRect(renderer, leftPos - borderLeft,
                    topPos - borderTop + topLeftRadius, borderLeft,
                    paint.transform().height() + borderTop - bottomLeftRadiusOffset - topLeftRadius,
                    zLevel, color);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 0;
        if (borderTop > 0)
            renderer.drawColoredRect(renderer,
                    leftPos + topLeftRadiusOffset,
                    topPos - borderTop,
                    paint.transform().width() + borderRight - topLeftRadiusOffset - topRightRadius, borderTop,
                    zLevel, color);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.drawColoredRect(renderer, rightPos,
                    topPos + topRightRadiusOffset, borderRight,
                    paint.transform().height() + borderBottom - bottomRightRadius - topRightRadiusOffset,
                    zLevel, color);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                                        bottomRightRadius - borderRight : 0;
        if (borderBottom > 0)
            renderer.drawColoredRect(renderer,
                    leftPos - borderLeft + bottomLeftRadius,
                    bottomPos,
                    paint.transform().width() + borderLeft - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, zLevel, color);
    }
}
