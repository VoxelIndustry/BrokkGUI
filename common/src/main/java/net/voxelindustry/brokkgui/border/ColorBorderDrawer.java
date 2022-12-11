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
        var outline = paint.outline();

        var isColorUniform = outline.isColorUniform();
        if (isColorUniform && outline.colorLeft().isInvisible())
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

        var box = outline.box();
        if (box != RectBox.EMPTY)
        {
            leftPos -= box.getLeft();
            rightPos += box.getRight();
            bottomPos += box.getBottom();
            topPos -= box.getTop();

            width += box.getHorizontal();
            height += box.getVertical();
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

        drawBorderShape(renderer, transform, outline.colorLeft(), outline.colorRight(), outline.colorTop(), outline.colorBottom(), isColorUniform, outlineLeft, outlineRight, outlineTop, outlineBottom, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, leftPos, rightPos, bottomPos, topPos, width, height);
    }

    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
    {
        var transform = paint.transform();
        var border = paint.border();

        if (border.isColorUniform() && border.colorLeft().isInvisible())
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

        var box = border.box();
        if (box != RectBox.EMPTY)
        {
            leftPos -= box.getLeft();
            rightPos += box.getRight();
            bottomPos += box.getBottom();
            topPos -= box.getTop();

            width += box.getHorizontal();
            height += box.getVertical();
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

        drawBorderShape(renderer, transform, border.colorLeft(), border.colorRight(), border.colorTop(), border.colorBottom(), border.isColorUniform(), borderLeft, borderRight, borderTop, borderBottom, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, leftPos, rightPos, bottomPos, topPos, width, height);
    }

    private static void drawBorderShape(IRenderCommandReceiver renderer,
                                        Transform transform,
                                        Color colorLeft,
                                        Color colorRight,
                                        Color colorTop,
                                        Color colorBottom,
                                        boolean isColorUniform,
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
            if (isColorUniform)
            {
                renderer.drawColoredEmptyRect(leftPos,
                        topPos,
                        width,
                        height,
                        zLevel,
                        colorLeft,
                        borderLeft,
                        RenderPass.BORDER);
                return;
            }

            renderer.drawColoredRect(leftPos,
                    topPos,
                    borderLeft,
                    height - borderBottom,
                    zLevel,
                    colorLeft,
                    RenderPass.BORDER);

            renderer.drawColoredRect(leftPos + borderLeft,
                    topPos,
                    width - borderLeft,
                    borderTop,
                    zLevel,
                    colorTop,
                    RenderPass.BORDER);

            renderer.drawColoredRect(rightPos - borderRight,
                    topPos + borderTop,
                    borderRight,
                    height - borderTop,
                    zLevel,
                    colorRight,
                    RenderPass.BORDER);

            renderer.drawColoredRect(leftPos,
                    bottomPos - borderBottom,
                    width - borderRight,
                    borderBottom,
                    zLevel,
                    colorBottom,
                    RenderPass.BORDER);
            return;
        }

        drawCurvedCorners(renderer, colorLeft, colorRight, colorTop, colorBottom, isColorUniform, borderLeft, borderRight, borderTop, borderBottom, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, leftPos, rightPos, bottomPos, topPos, zLevel);

        float bottomLeftRadiusOffset = bottomLeftRadius > 0 ? bottomLeftRadius - borderBottom : 0;
        if (borderLeft > 0)
            renderer.drawColoredRect(leftPos,
                    topPos + topLeftRadius,
                    borderLeft,
                    height - borderBottom - bottomLeftRadiusOffset - topLeftRadius,
                    zLevel,
                    colorLeft,
                    RenderPass.BORDER);

        float topLeftRadiusOffset = topLeftRadius > 0 && topLeftRadius <= borderLeft ? topLeftRadius - borderLeft : 1;
        if (borderTop > 0)
            renderer.drawColoredRect(leftPos + borderLeft + topLeftRadiusOffset,
                    topPos,
                    width - borderLeft - topLeftRadiusOffset - topRightRadius,
                    borderTop,
                    zLevel,
                    colorTop,
                    RenderPass.BORDER);

        float topRightRadiusOffset = topRightRadius > 0 ? topRightRadius - borderTop : 0;
        if (borderRight > 0)
            renderer.drawColoredRect(rightPos - borderRight,
                    topPos + topRightRadiusOffset + borderTop,
                    borderRight,
                    height - borderTop - bottomRightRadius - topRightRadiusOffset,
                    zLevel,
                    colorRight,
                    RenderPass.BORDER);

        float bottomRightRadiusOffset = bottomRightRadius > 0 && bottomRightRadius <= borderRight ?
                bottomRightRadius - borderRight : 1;
        if (borderBottom > 0)
            renderer.drawColoredRect(leftPos + bottomLeftRadius,
                    bottomPos - borderBottom,
                    width - borderRight - bottomRightRadiusOffset - bottomLeftRadius,
                    borderBottom,
                    zLevel,
                    colorBottom,
                    RenderPass.BORDER);
    }

    private static void drawCurvedCorners(IRenderCommandReceiver renderer,
                                          Color colorLeft,
                                          Color colorRight,
                                          Color colorTop,
                                          Color colorBottom,
                                          boolean isColorUniform,
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
                                          float zLevel)
    {
        if (bottomRightRadius > 0)
        {
            var maxBorder = Math.max(borderBottom, borderRight);

            for (int i = 0; i < maxBorder; i++)
            {
                if (isColorUniform)
                    renderer.drawColoredArc(rightPos - bottomRightRadius - 1,
                            bottomPos - bottomRightRadius - 1,
                            bottomRightRadius - i,
                            zLevel,
                            colorRight,
                            RectCorner.BOTTOM_RIGHT,
                            RenderPass.BORDER);
                else
                {
                    renderer.drawColoredArc(rightPos - bottomRightRadius - 1,
                            bottomPos - bottomRightRadius - 1,
                            bottomRightRadius - i,
                            zLevel,
                            colorBottom,
                            RectCorner.BOTTOM_RIGHT,
                            0,
                            RenderPass.BORDER);
                    renderer.drawColoredArc(rightPos - bottomRightRadius - 1,
                            bottomPos - bottomRightRadius - 1,
                            bottomRightRadius - i,
                            zLevel,
                            colorRight,
                            RectCorner.BOTTOM_RIGHT,
                            1,
                            RenderPass.BORDER);
                }
            }
        }
        if (bottomLeftRadius > 0)
        {
            var maxBorder = Math.max(borderBottom, borderLeft);

            for (int i = 0; i < maxBorder; i++)
            {
                if (isColorUniform)
                    renderer.drawColoredArc(leftPos + bottomLeftRadius,
                            bottomPos - bottomLeftRadius - 1,
                            bottomRightRadius - i,
                            zLevel,
                            colorBottom,
                            RectCorner.BOTTOM_LEFT,
                            RenderPass.BORDER);
                else
                {
                    renderer.drawColoredArc(leftPos + bottomLeftRadius,
                            bottomPos - bottomLeftRadius - 1,
                            bottomRightRadius - i,
                            zLevel,
                            colorBottom,
                            RectCorner.BOTTOM_LEFT,
                            0,
                            RenderPass.BORDER);
                    renderer.drawColoredArc(leftPos + bottomLeftRadius,
                            bottomPos - bottomLeftRadius - 1,
                            bottomRightRadius - i,
                            zLevel,
                            colorLeft,
                            RectCorner.BOTTOM_LEFT,
                            1,
                            RenderPass.BORDER);
                }
            }
        }
        if (topLeftRadius > 0)
        {
            var maxBorder = Math.max(borderTop, borderLeft);

            for (int i = 0; i < maxBorder; i++)
            {
                if (isColorUniform)
                    renderer.drawColoredArc(leftPos + topLeftRadius,
                            topPos + topLeftRadius,
                            topLeftRadius - i,
                            zLevel,
                            colorLeft,
                            RectCorner.TOP_LEFT,
                            RenderPass.BORDER);
                else
                {
                    renderer.drawColoredArc(leftPos + topLeftRadius,
                            topPos + topLeftRadius,
                            topLeftRadius - i,
                            zLevel,
                            colorTop,
                            RectCorner.TOP_LEFT,
                            0,
                            RenderPass.BORDER);
                    renderer.drawColoredArc(leftPos + topLeftRadius,
                            topPos + topLeftRadius,
                            topLeftRadius - i,
                            zLevel,
                            colorLeft,
                            RectCorner.TOP_LEFT,
                            1,
                            RenderPass.BORDER);
                }
            }
        }
        if (topRightRadius > 0)
        {
            var maxBorder = Math.max(borderTop, borderRight);

            for (int i = 0; i < maxBorder; i++)
            {
                if (isColorUniform)
                    renderer.drawColoredArc(rightPos - topRightRadius - 1,
                            topPos + topLeftRadius,
                            topLeftRadius - i,
                            zLevel,
                            colorTop,
                            RectCorner.TOP_RIGHT,
                            RenderPass.BORDER);
                else
                {
                    renderer.drawColoredArc(rightPos - topRightRadius - 1,
                            topPos + topLeftRadius,
                            topLeftRadius - i,
                            zLevel,
                            colorTop,
                            RectCorner.TOP_RIGHT,
                            0,
                            RenderPass.BORDER);
                    renderer.drawColoredArc(rightPos - topRightRadius - 1,
                            topPos + topLeftRadius,
                            topLeftRadius - i,
                            zLevel,
                            colorRight,
                            RectCorner.TOP_RIGHT,
                            1,
                            RenderPass.BORDER);
                }
            }
        }
    }
}
