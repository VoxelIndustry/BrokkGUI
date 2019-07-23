package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Texture;

public class ImageBorderDrawer
{
    public static void drawBorder(Paint paint, IGuiRenderer renderer)
    {
        Texture texture = paint.borderImage();
        float borderLeft = paint.borderWidth(RectSide.LEFT);
        float borderRight = paint.borderWidth(RectSide.RIGHT);
        float borderTop = paint.borderWidth(RectSide.UP);
        float borderBottom = paint.borderWidth(RectSide.DOWN);

        RectBox sliceBox = paint.borderImageSlice();
        RectBox widthBox = paint.borderImageWidth();
        RectBox outsetBox = paint.borderImageOutset();

        Transform transform = paint.element().transform();
        float leftPos = transform.leftPos() - outsetBox.getLeft();
        float topPos = transform.topPos() - outsetBox.getTop();
        float rightPos = transform.rightPos() + outsetBox.getRight();
        float bottomPos = transform.bottomPos() + outsetBox.getBottom();

        float width = transform.width() + outsetBox.getLeft() + outsetBox.getRight();
        float height = transform.height() + outsetBox.getTop() + outsetBox.getBottom();

        boolean doFill = paint.borderImageFill();

        renderer.getHelper().bindTexture(texture);
        drawWalls(renderer, borderLeft, borderRight, borderTop, borderBottom, sliceBox, widthBox, transform, leftPos, topPos, rightPos, bottomPos, width, height);
        drawCorners(renderer, borderLeft, borderRight, borderTop, borderBottom, sliceBox, transform, leftPos, topPos, rightPos, bottomPos);
        drawFill(renderer, borderLeft, borderRight, borderTop, borderBottom, sliceBox, widthBox, transform, leftPos, topPos, width, height, doFill);
    }

    private static void drawFill(IGuiRenderer renderer, float borderLeft, float borderRight, float borderTop, float borderBottom, RectBox sliceBox, RectBox widthBox, Transform transform, float leftPos, float topPos, float width, float height, boolean doFill)
    {
        if (doFill)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos, topPos,
                    sliceBox.getLeft(), sliceBox.getTop(), 1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    width, height,
                    transform.zLevel());
        }
    }

    private static void drawCorners(IGuiRenderer renderer, float borderLeft, float borderRight, float borderTop, float borderBottom, RectBox sliceBox, Transform transform, float leftPos, float topPos, float rightPos, float bottomPos)
    {
        if (borderTop > 0 && borderLeft > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos - borderLeft, topPos - borderTop,
                    0, 0,
                    sliceBox.getLeft(), sliceBox.getTop(),
                    borderLeft, borderTop,
                    transform.zLevel());
        }

        if (borderTop > 0 && borderRight > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    rightPos, topPos - borderTop,
                    1 - sliceBox.getRight(), 0,
                    1, sliceBox.getTop(),
                    borderRight, borderTop,
                    transform.zLevel());
        }

        if (borderBottom > 0 && borderRight > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    rightPos, bottomPos,
                    1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    1, 1,
                    borderRight, borderBottom,
                    transform.zLevel());
        }

        if (borderBottom > 0 && borderLeft > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos - borderLeft, bottomPos,
                    0, 1 - sliceBox.getBottom(),
                    sliceBox.getLeft(), 1,
                    borderLeft, borderBottom,
                    transform.zLevel());
        }
    }

    private static void drawWalls(IGuiRenderer renderer, float borderLeft, float borderRight, float borderTop, float borderBottom, RectBox sliceBox, RectBox widthBox, Transform transform, float leftPos, float topPos, float rightPos, float bottomPos, float width, float height)
    {
        if (borderTop > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos, topPos - borderTop,
                    sliceBox.getLeft(), 0,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    width, borderTop * widthBox.getTop(), transform.zLevel());
        }

        if (borderBottom > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos, bottomPos - (widthBox.getBottom() - 1) * borderBottom,
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    1 - sliceBox.getRight(), 1,
                    width, borderBottom * widthBox.getBottom(), transform.zLevel());
        }

        if (borderLeft > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    leftPos - borderLeft, topPos,
                    0, sliceBox.getTop(),
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    borderLeft * widthBox.getLeft(), height, transform.zLevel());
        }

        if (borderRight > 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    rightPos - (widthBox.getRight() - 1) * borderRight, topPos,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    1, 1 - sliceBox.getBottom(),
                    borderRight * widthBox.getRight(), height, transform.zLevel());
        }
    }
}
