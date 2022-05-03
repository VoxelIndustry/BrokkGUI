package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.sprite.Texture;

public class ImageBorderDrawer
{
    public static void drawBorder(Paint paint, IRenderCommandReceiver renderer)
    {
        Transform transform = paint.transform();

        Texture texture = paint.borderImage();
        float borderLeft = transform.borderWidth(RectSide.LEFT);
        float borderRight = transform.borderWidth(RectSide.RIGHT);
        float borderTop = transform.borderWidth(RectSide.UP);
        float borderBottom = transform.borderWidth(RectSide.DOWN);

        RectBox sliceBox = paint.borderImageSlice();
        RectBox widthBox = paint.borderImageWidth();
        RectBox outsetBox = paint.borderImageOutset();

        float leftPos = transform.leftPos();
        float topPos = transform.topPos();
        float rightPos = transform.rightPos();
        float bottomPos = transform.bottomPos();

        float width = transform.width();
        float height = transform.height();

        if (paint.borderBox() == BorderBox.OUTSIDE)
        {
            leftPos -= outsetBox.getLeft();
            rightPos += outsetBox.getRight();
            bottomPos += outsetBox.getBottom();
            topPos -= outsetBox.getTop();

            width += outsetBox.getHorizontal();
            height += outsetBox.getVertical();
        }

        boolean doFill = paint.borderImageFill();

        renderer.bindTexture(texture);

        // Walls

        if (borderTop > 0)
        {
            renderer.drawTexturedRect(leftPos, topPos - borderTop,
                    sliceBox.getLeft(), 0,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    width, borderTop * widthBox.getTop(), transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (borderBottom > 0)
        {
            renderer.drawTexturedRect(leftPos, bottomPos - (widthBox.getBottom() - 1) * borderBottom,
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    1 - sliceBox.getRight(), 1,
                    width, borderBottom * widthBox.getBottom(), transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (borderLeft > 0)
        {
            renderer.drawTexturedRect(leftPos - borderLeft, topPos,
                    0, sliceBox.getTop(),
                    sliceBox.getLeft(), 1 - sliceBox.getBottom(),
                    borderLeft * widthBox.getLeft(), height, transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (borderRight > 0)
        {
            renderer.drawTexturedRect(rightPos - (widthBox.getRight() - 1) * borderRight, topPos,
                    1 - sliceBox.getRight(), sliceBox.getTop(),
                    1, 1 - sliceBox.getBottom(),
                    borderRight * widthBox.getRight(), height, transform.zLevel(),
                    RenderPass.BORDER);
        }

        // Corners

        if (borderTop > 0 && borderLeft > 0)
        {
            renderer.drawTexturedRect(leftPos - borderLeft, topPos - borderTop,
                    0, 0,
                    sliceBox.getLeft(), sliceBox.getTop(),
                    borderLeft, borderTop,
                    transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (borderTop > 0 && borderRight > 0)
        {
            renderer.drawTexturedRect(rightPos, topPos - borderTop,
                    1 - sliceBox.getRight(), 0,
                    1, sliceBox.getTop(),
                    borderRight, borderTop,
                    transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (borderBottom > 0 && borderRight > 0)
        {
            renderer.drawTexturedRect(rightPos, bottomPos,
                    1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    1, 1,
                    borderRight, borderBottom,
                    transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (borderBottom > 0 && borderLeft > 0)
        {
            renderer.drawTexturedRect(leftPos - borderLeft, bottomPos,
                    0, 1 - sliceBox.getBottom(),
                    sliceBox.getLeft(), 1,
                    borderLeft, borderBottom,
                    transform.zLevel(),
                    RenderPass.BORDER);
        }

        if (doFill)
        {
            renderer.drawTexturedRect(leftPos + widthBox.getLeft() * borderLeft, topPos + widthBox.getTop() * borderTop,
                    sliceBox.getLeft(), sliceBox.getTop(), 1 - sliceBox.getRight(), 1 - sliceBox.getBottom(),
                    width - widthBox.getLeft() * borderLeft - widthBox.getRight() * borderRight,
                    height - widthBox.getTop() * borderTop - widthBox.getBottom() * borderBottom,
                    transform.zLevel(),
                    RenderPass.BORDER);
        }
    }
}
