package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class ColorBorderDrawer
{

    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
    {
        var transform = paint.transform();
        var color = paint.borderColor();

        if (color.getAlpha() == 0)
            return;

        var borderLeft = transform.borderWidth(RectSide.LEFT);
        var borderRight = transform.borderWidth(RectSide.RIGHT);
        var borderTop = transform.borderWidth(RectSide.UP);
        var borderBottom = transform.borderWidth(RectSide.DOWN);

        var topLeftRadius = transform.borderRadius(RectCorner.TOP_LEFT);
        var topRightRadius = transform.borderRadius(RectCorner.TOP_RIGHT);
        var bottomLeftRadius = transform.borderRadius(RectCorner.BOTTOM_LEFT);
        var bottomRightRadius = transform.borderRadius(RectCorner.BOTTOM_RIGHT);

        var leftPos = transform.leftPos();
        var rightPos = transform.rightPos();
        var bottomPos = transform.bottomPos();
        var topPos = transform.topPos();

        var width = transform.width();
        var height = transform.height();

        if (paint.borderBox() == BorderBox.OUTSIDE)
        {
            leftPos -= borderLeft;
            rightPos += borderRight;
            bottomPos += borderBottom;
            topPos -= borderTop;

            width += borderLeft + borderRight;
            height += borderTop + borderBottom;
        }

        var zLevel = transform.zLevel();

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
                    RenderPass.BORDER);
            return;
        }

        if (bottomRightRadius > 0)
            renderer.drawColoredArc(rightPos - bottomRightRadius - 1,
                    bottomPos - bottomRightRadius - 1,
                    bottomRightRadius, zLevel, color, RectCorner.BOTTOM_RIGHT,
                    RenderPass.BORDER);
        if (bottomLeftRadius > 0)
            renderer.drawColoredArc(leftPos + bottomLeftRadius,
                    bottomPos - bottomLeftRadius - 1,
                    bottomRightRadius, zLevel, color, RectCorner.BOTTOM_LEFT,
                    RenderPass.BORDER);
        if (topLeftRadius > 0)
            renderer.drawColoredArc(leftPos + topLeftRadius,
                    topPos + topLeftRadius,
                    topLeftRadius, zLevel, color, RectCorner.TOP_LEFT,
                    RenderPass.BORDER);
        if (topRightRadius > 0)
            renderer.drawColoredArc(rightPos - topRightRadius - 1,
                    topPos + topLeftRadius,
                    topLeftRadius, zLevel, color, RectCorner.TOP_RIGHT,
                    RenderPass.BORDER);

        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.drawColoredRect(leftPos,
                    topPos + topLeftRadius, borderLeft,
                    height - borderBottom - bottomLeftRadiusOffset - topLeftRadius,
                    zLevel, color, RenderPass.BORDER);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 0;
        if (borderTop > 0)
            renderer.drawColoredRect(leftPos + borderLeft + topLeftRadiusOffset,
                    topPos,
                    width - borderLeft - topLeftRadiusOffset - topRightRadius, borderTop,
                    zLevel, color, RenderPass.BORDER);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.drawColoredRect(rightPos - borderRight,
                    topPos + topRightRadiusOffset + borderTop,
                    borderRight,
                    height - borderTop - bottomRightRadius - topRightRadiusOffset,
                    zLevel, color, RenderPass.BORDER);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                bottomRightRadius - borderRight : 0;
        if (borderBottom > 0)
            renderer.drawColoredRect(leftPos + bottomLeftRadius,
                    bottomPos - borderBottom,
                    width - borderRight - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, zLevel, color, RenderPass.BORDER);
    }
}
