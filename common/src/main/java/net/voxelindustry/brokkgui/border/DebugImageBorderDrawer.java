package net.voxelindustry.brokkgui.border;

import net.voxelindustry.brokkgui.component.Paint;
import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;

public class DebugImageBorderDrawer
{
    public static void drawBorder(Paint paint, IGuiRenderer renderer)
    {
        float borderLeft = paint.borderWidth(RectSide.LEFT);
        float borderRight = paint.borderWidth(RectSide.RIGHT);
        float borderTop = paint.borderWidth(RectSide.UP);
        float borderBottom = paint.borderWidth(RectSide.DOWN);

        RectBox outsetBox = paint.borderImageOutset();

        Transform transform = paint.element().transform();
        float leftPos = transform.leftPos() - outsetBox.getLeft();
        float topPos = transform.topPos() - outsetBox.getTop();
        float rightPos = transform.rightPos() + outsetBox.getRight();
        float bottomPos = transform.bottomPos() + outsetBox.getBottom();

        renderer.beginMatrix();
        renderer.scaleMatrix(0.5f, 0.5f, 0.5f);

        renderer.getHelper().drawString("Border Image", leftPos * 2, (topPos - renderer.getHelper().getStringHeight()) * 2, 0, Color.AQUA);
        renderer.endMatrix();

        renderer.getHelper().drawColoredCross(renderer, transform.leftPos(), transform.topPos(), 4, 1, 0, ColorConstants.getColor("lawngreen"));
        renderer.getHelper().drawColoredCross(renderer, transform.leftPos(), transform.bottomPos(), 4, 1, 0, ColorConstants.getColor("lawngreen"));
        renderer.getHelper().drawColoredCross(renderer, transform.rightPos(), transform.topPos(), 4, 1, 0, ColorConstants.getColor("lawngreen"));
        renderer.getHelper().drawColoredCross(renderer, transform.rightPos(), transform.bottomPos(), 4, 1, 0, ColorConstants.getColor("lawngreen"));

        renderer.getHelper().drawColoredCross(renderer, leftPos, topPos, 3, 1, 0, ColorConstants.getColor("salmon"));
        renderer.getHelper().drawColoredCross(renderer, leftPos, bottomPos, 3, 1, 0, ColorConstants.getColor("salmon"));
        renderer.getHelper().drawColoredCross(renderer, rightPos, topPos, 3, 1, 0, ColorConstants.getColor("salmon"));
        renderer.getHelper().drawColoredCross(renderer, rightPos, bottomPos, 3, 1, 0, ColorConstants.getColor("salmon"));

        renderer.getHelper().drawColoredCross(renderer, leftPos - borderLeft, topPos - borderTop, 4, 1, 0, ColorConstants.getColor("palegoldenrod"));
        renderer.getHelper().drawColoredCross(renderer, leftPos - borderLeft, bottomPos + borderBottom, 4, 1, 0, ColorConstants.getColor("palegoldenrod"));
        renderer.getHelper().drawColoredCross(renderer, rightPos + borderRight, topPos - borderTop, 4, 1, 0, ColorConstants.getColor("palegoldenrod"));
        renderer.getHelper().drawColoredCross(renderer, rightPos + borderRight, bottomPos + borderBottom, 4, 1, 0, ColorConstants.getColor("palegoldenrod"));
    }
}
