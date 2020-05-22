package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.impl.Transform;

public class RectArea
{
    private Transform              transform;
    private ObservableValue<Float> startX, startY, endX, endY;

    public static RectArea fitNode(Transform transform)
    {
        return nodePart(transform, 1, 1);
    }

    public static RectArea nodePart(Transform transform, float widthFrac, float heightFrac)
    {
        RectArea rectArea = new RectArea();
        rectArea.transform = transform;

        rectArea.startX = BaseExpression.biCombine(transform.xPosProperty(), transform.xTranslateProperty(), Float::sum);
        rectArea.startY = BaseExpression.biCombine(transform.yPosProperty(), transform.yTranslateProperty(), Float::sum);

        rectArea.endX = BaseExpression.biCombine(rectArea.startX, transform.widthProperty(),
                (startX, width) -> startX + width * widthFrac);
        rectArea.endY = BaseExpression.biCombine(rectArea.startY, transform.heightProperty(),
                (startY, height) -> startY + height * heightFrac);

        return rectArea;
    }

    public static RectArea withRegion(float startX, float startY, float endX, float endY)
    {
        RectArea rectArea = new RectArea();

        rectArea.startX = new BaseProperty<>(startX, "startXProperty");
        rectArea.startY = new BaseProperty<>(startY, "startYProperty");
        rectArea.endX = new BaseProperty<>(endX, "endXProperty");
        rectArea.endY = new BaseProperty<>(endY, "endYProperty");

        return rectArea;
    }

    public void dispose()
    {
        if (transform == null)
            return;

        ((BaseExpression<Float>) startX).unbind(transform.xPosProperty(), transform.xTranslateProperty());
        ((BaseExpression<Float>) startY).unbind(transform.yPosProperty(), transform.yTranslateProperty());

        ((BaseExpression<Float>) endX).unbind(startX, transform.widthProperty());
        ((BaseExpression<Float>) endY).unbind(startY, transform.heightProperty());
    }

    public boolean isPointInside(float x, float y)
    {
        return x > startX.getValue() && x < endX.getValue() && y > startY.getValue() && y < endY.getValue();
    }
}
