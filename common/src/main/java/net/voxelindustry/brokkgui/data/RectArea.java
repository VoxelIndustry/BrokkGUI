package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.property.Property;
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

        rectArea.startX = Expression.biCombine(transform.xPosProperty(), transform.xTranslateProperty(), Float::sum);
        rectArea.startY = Expression.biCombine(transform.yPosProperty(), transform.yTranslateProperty(), Float::sum);

        rectArea.endX = Expression.biCombine(rectArea.startX, transform.widthProperty(),
                (startX, width) -> startX + width * widthFrac);
        rectArea.endY = Expression.biCombine(rectArea.startY, transform.heightProperty(),
                (startY, height) -> startY + height * heightFrac);

        return rectArea;
    }

    public static RectArea withRegion(float startX, float startY, float endX, float endY)
    {
        RectArea rectArea = new RectArea();

        rectArea.startX = new Property<>(startX);
        rectArea.startY = new Property<>(startY);
        rectArea.endX = new Property<>(endX);
        rectArea.endY = new Property<>(endY);

        return rectArea;
    }

    public void dispose()
    {
        if (transform == null)
            return;

        ((Expression<Float>) startX).unbind(transform.xPosProperty(), transform.xTranslateProperty());
        ((Expression<Float>) startY).unbind(transform.yPosProperty(), transform.yTranslateProperty());

        ((Expression<Float>) endX).unbind(startX, transform.widthProperty());
        ((Expression<Float>) endY).unbind(startY, transform.heightProperty());
    }

    public boolean isPointInside(float x, float y)
    {
        return x > startX.getValue() && x < endX.getValue() && y > startY.getValue() && y < endY.getValue();
    }
}
