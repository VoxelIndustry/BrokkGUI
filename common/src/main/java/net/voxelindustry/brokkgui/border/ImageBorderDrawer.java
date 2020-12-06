package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.sprite.Texture;

public class ImageBorderDrawer
{
    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
    {
        Texture texture = paint.borderImage();
        float borderLeft = paint.borderWidth(RectSide.LEFT);
        float borderRight = paint.borderWidth(RectSide.RIGHT);
        float borderTop = paint.borderWidth(RectSide.UP);
        float borderBottom = paint.borderWidth(RectSide.DOWN);

        RectBox sliceBox = paint.borderImageSlice();
        RectBox widthBox = paint.borderImageWidth();
        RectBox outsetBox = paint.borderImageOutset();

        float leftPos = paint.transform().leftPos() - outsetBox.getLeft();
        float topPos = paint.transform().topPos() - outsetBox.getTop();
        float rightPos = paint.transform().rightPos() + outsetBox.getRight();
        float bottomPos = paint.transform().bottomPos() + outsetBox.getBottom();

        float width = paint.transform().width() + outsetBox.getLeft() + outsetBox.getRight();
        float height = paint.transform().height() + outsetBox.getTop() + outsetBox.getBottom();

        boolean doFill = paint.borderImageFill();

        renderer.bindTexture(texture);

        // Walls

        if (borderTop > 0)
        {
            renderer.drawTexturedRect(renderer,
                    leftPos, topPos - borderTop,
                    sliceBox.getLeft(), 0,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    width, borderTop * widthBox.getTop(), paint.transform().zLevel());
        }

        if (borderBottom > 0)
        {
            renderer.drawTexturedRect(renderer,
                    leftPos, bottomPos - (widthBox.getBottom() - 1) * borderBottom,
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    1 - sliceBox.getRight(), 1,
                    width, borderBottom * widthBox.getBottom(), paint.transform().zLevel());
        }

        if (borderLeft > 0)
        {
            renderer.drawTexturedRect(renderer,
                    leftPos - borderLeft, topPos,
                    0, sliceBox.getTop(),
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    borderLeft * widthBox.getLeft(), height, paint.transform().zLevel());
        }

        if (borderRight > 0)
        {
            renderer.drawTexturedRect(renderer,
                    rightPos - (widthBox.getRight() - 1) * borderRight, topPos,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    1, 1 - sliceBox.getBottom(),
                    borderRight * widthBox.getRight(), height, paint.transform().zLevel());
        }

        // Corners

        if (borderTop > 0 && borderLeft > 0)
        {
            renderer.drawTexturedRect(renderer,
                    leftPos - borderLeft, topPos - borderTop,
                    0, 0,
                    sliceBox.getLeft(), sliceBox.getTop(),
                    borderLeft, borderTop,
                    paint.transform().zLevel());
        }

        if (borderTop > 0 && borderRight > 0)
        {
            renderer.drawTexturedRect(renderer,
                    rightPos, topPos - borderTop,
                    1 - sliceBox.getRight(), 0,
                    1, sliceBox.getTop(),
                    borderRight, borderTop,
                    paint.transform().zLevel());
        }

        if (borderBottom > 0 && borderRight > 0)
        {
            renderer.drawTexturedRect(renderer,
                    rightPos, bottomPos,
                    1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    1, 1,
                    borderRight, borderBottom,
                    paint.transform().zLevel());
        }

        if (borderBottom > 0 && borderLeft > 0)
        {
            renderer.drawTexturedRect(renderer,
                    leftPos - borderLeft, bottomPos,
                    0, 1 - sliceBox.getBottom(),
                    sliceBox.getLeft(), 1,
                    borderLeft, borderBottom,
                    paint.transform().zLevel());
        }

        if (doFill)
        {
            renderer.drawTexturedRect(renderer,
                    leftPos + widthBox.getLeft() * borderLeft, topPos + widthBox.getTop() * borderTop,
                    sliceBox.getLeft(), sliceBox.getTop(), 1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    width - widthBox.getLeft() * borderLeft - widthBox.getRight() * borderRight,
                    height - widthBox.getTop() * borderTop - widthBox.getBottom() * borderBottom,
                    paint.transform().zLevel());
        }
    }
}
