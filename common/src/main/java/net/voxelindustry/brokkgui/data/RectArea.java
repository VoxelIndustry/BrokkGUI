package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.impl.Transform;

public class RectArea
{
    private Transform              node;
    private ObservableValue<Float> startX, startY, endX, endY;

    public static RectArea fitNode(Transform transform)
    {
        return nodePart(transform, 1, 1);
    }

    public static RectArea nodePart(Transform transform, float widthFrac, float heightFrac)
    {
        RectArea rectArea = new RectArea();
        rectArea.node = transform;

        rectArea.startX = BaseExpression.biCombine(transform.xPosProperty(), transform.xTranslateProperty(),
                Float::sum);
        rectArea.startY = BaseExpression.biCombine(transform.yPosProperty(), transform.yTranslateProperty(),
                Float::sum);

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
        if (node == null)
            return;

        ((BaseExpression<Float>) this.startX).unbind(node.xPosProperty(), node.xTranslateProperty());
        ((BaseExpression<Float>) this.startY).unbind(node.yPosProperty(), node.yTranslateProperty());

        ((BaseExpression<Float>) this.endX).unbind(this.startX, node.widthProperty());
        ((BaseExpression<Float>) this.endY).unbind(this.startY, node.heightProperty());
    }

    public boolean isPointInside(float x, float y)
    {
        return x > startX.getValue() && x < endX.getValue() && y > startY.getValue() && y < endY.getValue();
    }
}
