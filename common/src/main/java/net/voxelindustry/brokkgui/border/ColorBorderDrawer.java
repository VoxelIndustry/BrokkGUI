package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class ColorBorderDrawer
{

    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
    {
        Transform transform = paint.transform();
        Color color = paint.borderColor();

        float borderLeft = transform.borderWidth(RectSide.LEFT);
        float borderRight = transform.borderWidth(RectSide.RIGHT);
        float borderTop = transform.borderWidth(RectSide.UP);
        float borderBottom = transform.borderWidth(RectSide.DOWN);

        float topLeftRadius = transform.borderRadius(RectCorner.TOP_LEFT);
        float topRightRadius = transform.borderRadius(RectCorner.TOP_RIGHT);
        float bottomLeftRadius = transform.borderRadius(RectCorner.BOTTOM_LEFT);
        float bottomRightRadius = transform.borderRadius(RectCorner.BOTTOM_RIGHT);

        float leftPos = transform.leftPos() - borderLeft;
        float rightPos = transform.rightPos() + borderRight;
        float bottomPos = transform.bottomPos() + borderBottom;
        float topPos = transform.topPos() - borderTop;

        float width = transform.width() + borderLeft + borderRight;
        float height = transform.height() + borderTop + borderBottom;

        float zLevel = transform.zLevel();

        // Straightforward empty rect tracing in case of same border width
        if (bottomLeftRadius == 0 && bottomRightRadius == 0 && topRightRadius == 0 && topLeftRadius == 0 &&
                borderLeft == borderRight && borderLeft == borderTop && borderLeft == borderBottom)
        {
            renderer.drawColoredEmptyRect(leftPos,
                    topPos,
                    width,
                    height,
                    zLevel,
                    color,
                    borderLeft,
                    RenderPass.BACKGROUND);
            return;
        }

        if (bottomRightRadius > 0)
            renderer.drawColoredArc(rightPos - bottomRightRadius - 1,
                    bottomPos - bottomRightRadius - 1,
                    bottomRightRadius, zLevel, color, RectCorner.BOTTOM_RIGHT,
                    RenderPass.BACKGROUND);
        if (bottomLeftRadius > 0)
            renderer.drawColoredArc(leftPos + bottomLeftRadius,
                    bottomPos - bottomLeftRadius - 1,
                    bottomRightRadius, zLevel, color, RectCorner.BOTTOM_LEFT,
                    RenderPass.BACKGROUND);
        if (topLeftRadius > 0)
            renderer.drawColoredArc(leftPos + topLeftRadius,
                    topPos + topLeftRadius,
                    topLeftRadius, zLevel, color, RectCorner.TOP_LEFT,
                    RenderPass.BACKGROUND);
        if (topRightRadius > 0)
            renderer.drawColoredArc(rightPos - topRightRadius - 1,
                    topPos + topLeftRadius,
                    topLeftRadius, zLevel, color, RectCorner.TOP_RIGHT,
                    RenderPass.BACKGROUND);

        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.drawColoredRect(leftPos,
                    topPos + topLeftRadius, borderLeft,
                    height - borderBottom - bottomLeftRadiusOffset - topLeftRadius,
                    zLevel, color, RenderPass.BACKGROUND);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 0;
        if (borderTop > 0)
            renderer.drawColoredRect(leftPos + borderLeft + topLeftRadiusOffset,
                    topPos,
                    width - borderLeft - topLeftRadiusOffset - topRightRadius, borderTop,
                    zLevel, color, RenderPass.BACKGROUND);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.drawColoredRect(rightPos - borderRight,
                    topPos + topRightRadiusOffset + borderTop,
                    borderRight,
                    height - borderTop - bottomRightRadius - topRightRadiusOffset,
                    zLevel, color, RenderPass.BACKGROUND);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                                        bottomRightRadius - borderRight : 0;
        if (borderBottom > 0)
            renderer.drawColoredRect(leftPos + bottomLeftRadius,
                    bottomPos - borderBottom,
                    width - borderRight - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, zLevel, color, RenderPass.BACKGROUND);
    }
}
