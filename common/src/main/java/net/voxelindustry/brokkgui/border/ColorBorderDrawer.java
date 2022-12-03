package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class ColorBorderDrawer
{

    public static void drawOutline(Paint paint, IRenderCommandReceiver renderer)
    {
        var transform = paint.transform();
        var color = paint.outlineColor();

        if (color.isInvisible())
            return;

        var outlineLeft = paint.outlineWidth(RectSide.LEFT);
        var outlineRight = paint.outlineWidth(RectSide.RIGHT);
        var outlineTop = paint.outlineWidth(RectSide.UP);
        var outlineBottom = paint.outlineWidth(RectSide.DOWN);

        var topLeftRadius = paint.outlineRadius(RectCorner.TOP_LEFT);
        var topRightRadius = paint.outlineRadius(RectCorner.TOP_RIGHT);
        var bottomLeftRadius = paint.outlineRadius(RectCorner.BOTTOM_LEFT);
        var bottomRightRadius = paint.outlineRadius(RectCorner.BOTTOM_RIGHT);

        var leftPos = transform.leftPos();
        var rightPos = transform.rightPos();
        var bottomPos = transform.bottomPos();
        var topPos = transform.topPos();

        var width = transform.width();
        var height = transform.height();

        if (paint.outlineBox() != RectBox.EMPTY)
        {
            leftPos -= paint.outlineBox().getLeft();
            rightPos += paint.outlineBox().getRight();
            bottomPos += paint.outlineBox().getBottom();
            topPos -= paint.outlineBox().getTop();

            width += paint.outlineBox().getHorizontal();
            height += paint.outlineBox().getVertical();
        }
        else
        {
            var borderLeft = transform.borderWidth(RectSide.LEFT);
            var borderRight = transform.borderWidth(RectSide.RIGHT);
            var borderTop = transform.borderWidth(RectSide.UP);
            var borderBottom = transform.borderWidth(RectSide.DOWN);

            leftPos -= outlineLeft + borderLeft;
            rightPos += outlineRight + borderRight;
            bottomPos += outlineBottom + borderBottom;
            topPos -= outlineTop + borderTop;

            width += outlineLeft + outlineRight + borderLeft + borderRight;
            height += outlineTop + outlineBottom + borderTop + borderBottom;
        }

        drawBorderShape(renderer, transform, color, outlineLeft, outlineRight, outlineTop, outlineBottom, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, leftPos, rightPos, bottomPos, topPos, width, height);
    }

    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
    {
        var transform = paint.transform();
        var color = paint.borderColor();

        if (color.isInvisible())
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

        if (paint.borderBox() != RectBox.EMPTY)
        {
            leftPos -= paint.borderBox().getLeft();
            rightPos += paint.borderBox().getRight();
            bottomPos += paint.borderBox().getBottom();
            topPos -= paint.borderBox().getTop();

            width += paint.borderBox().getHorizontal();
            height += paint.borderBox().getVertical();
        }
        else
        {
            leftPos -= borderLeft;
            rightPos += borderRight;
            bottomPos += borderBottom;
            topPos -= borderTop;

            width += borderLeft + borderRight;
            height += borderTop + borderBottom;
        }

        drawBorderShape(renderer, transform, color, borderLeft, borderRight, borderTop, borderBottom, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, leftPos, rightPos, bottomPos, topPos, width, height);
    }

    private static void drawBorderShape(IRenderCommandReceiver renderer,
                                        Transform transform,
                                        Color color,
                                        float borderLeft,
                                        float borderRight,
                                        float borderTop,
                                        float borderBottom,
                                        int topLeftRadius,
                                        int topRightRadius,
                                        int bottomLeftRadius,
                                        int bottomRightRadius,
                                        float leftPos,
                                        float rightPos,
                                        float bottomPos,
                                        float topPos,
                                        float width,
                                        float height)
    {
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

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 1;
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
                bottomRightRadius - borderRight : 1;
        if (borderBottom > 0)
            renderer.drawColoredRect(leftPos + bottomLeftRadius,
                    bottomPos - borderBottom,
                    width - borderRight - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom, zLevel, color, RenderPass.BORDER);
    }
}
