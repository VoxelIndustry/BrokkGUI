package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.shape.GuiShape;

public class ImageBorderDrawer
{
    public static void drawBorder(GuiShape shape, IGuiRenderer renderer)
    {
        Texture texture = shape.getBorderImage();
        float borderLeft = shape.getBorderWidth(RectSide.LEFT);
        float borderRight = shape.getBorderWidth(RectSide.RIGHT);
        float borderTop = shape.getBorderWidth(RectSide.UP);
        float borderBottom = shape.getBorderWidth(RectSide.DOWN);

        RectBox sliceBox = shape.getStyle().getStyleValue("border-image-slice", RectBox.class, RectBox.EMPTY);
        RectBox widthBox = shape.getStyle().getStyleValue("border-image-width", RectBox.class, RectBox.EMPTY);
        RectBox outsetBox = shape.getStyle().getStyleValue("border-image-outset", RectBox.class, RectBox.EMPTY);

        float leftPos = shape.getLeftPos() - outsetBox.getLeft();
        float topPos = shape.getTopPos() - outsetBox.getTop();
        float rightPos = shape.getRightPos() + outsetBox.getRight();
        float bottomPos = shape.getBottomPos() + outsetBox.getBottom();

        float width = shape.getWidth() + outsetBox.getLeft() + outsetBox.getRight();
        float height = shape.getHeight() + outsetBox.getTop() + outsetBox.getBottom();

        boolean doFill = shape.getStyle().getStyleValue("border-image-fill", Boolean.class, false);

        renderer.getHelper().bindTexture(texture);

        // Walls

        if (borderTop > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos, topPos - borderTop,
                    sliceBox.getLeft(), 0,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    width, borderTop * widthBox.getTop(), shape.getzLevel());
        }

        if (borderBottom > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos, bottomPos - (widthBox.getBottom() - 1) * borderBottom,
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    1 - sliceBox.getRight(), 1,
                    width, borderBottom * widthBox.getBottom(), shape.getzLevel());
        }

        if (borderLeft > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos - borderLeft, topPos,
                    0, sliceBox.getTop(),
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    borderLeft * widthBox.getLeft(), height, shape.getzLevel());
        }

        if (borderRight > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    rightPos - (widthBox.getRight() - 1) * borderRight, topPos,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    1, 1 - sliceBox.getBottom(),
                    borderRight * widthBox.getRight(), height, shape.getzLevel());
        }

        // Corners

        if (borderTop > 0 && borderLeft > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos - borderLeft, topPos - borderTop,
                    0, 0,
                    sliceBox.getLeft(), sliceBox.getTop(),
                    borderLeft, borderTop,
                    shape.getzLevel());
        }

        if (borderTop > 0 && borderRight > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    rightPos, topPos - borderTop,
                    1 - sliceBox.getRight(), 0,
                    1, sliceBox.getTop(),
                    borderRight, borderTop,
                    shape.getzLevel());
        }

        if (borderBottom > 0 && borderRight > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    rightPos, bottomPos,
                    1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    1, 1,
                    borderRight, borderBottom,
                    shape.getzLevel());
        }

        if (borderBottom > 0 && borderLeft > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos - borderLeft, bottomPos,
                    0, 1 - sliceBox.getBottom(),
                    sliceBox.getLeft(), 1,
                    borderLeft, borderBottom,
                    shape.getzLevel());
        }

        if (doFill)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos + widthBox.getLeft() * borderLeft, topPos + widthBox.getTop() * borderTop,
                    sliceBox.getLeft(), sliceBox.getTop(), 1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    width - widthBox.getLeft() * borderLeft - widthBox.getRight() * borderRight,
                    height - widthBox.getTop() * borderTop - widthBox.getBottom() * borderBottom,
                    shape.getzLevel());
        }
    }
}
