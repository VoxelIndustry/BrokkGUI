package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BindingBase;
import fr.ourten.teabeans.property.specific.FloatProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.impl.Transform;

public class RectArea
{
    private Transform               transform;
    private ObservableValue<Number> startX, startY, endX, endY;

    public static RectArea fitNode(Transform transform)
    {
        return nodePart(transform, 1, 1);
    }

    public static RectArea nodePart(Transform transform, float widthFrac, float heightFrac)
    {
        RectArea rectArea = new RectArea();
        rectArea.transform = transform;

        rectArea.startX = transform.xPosProperty().add(transform.xTranslateProperty());
        rectArea.startY = transform.yPosProperty().add(transform.yTranslateProperty());

        rectArea.endX = rectArea.startX.combine(transform.widthProperty(),
                (startX, width) -> startX.floatValue() + width.floatValue() * widthFrac);
        rectArea.endY = rectArea.startY.combine(transform.heightProperty(),
                (startY, height) -> startY.floatValue() + height.floatValue() * heightFrac);

        return rectArea;
    }

    public static RectArea withRegion(float startX, float startY, float endX, float endY)
    {
        RectArea rectArea = new RectArea();

        rectArea.startX = new FloatProperty(startX);
        rectArea.startY = new FloatProperty(startY);
        rectArea.endX = new FloatProperty(endX);
        rectArea.endY = new FloatProperty(endY);

        return rectArea;
    }

    public void dispose()
    {
        if (transform == null)
            return;

        ((BindingBase<Number>) startX).unbind(transform.xPosProperty(), transform.xTranslateProperty());
        ((BindingBase<Number>) startY).unbind(transform.yPosProperty(), transform.yTranslateProperty());

        ((BindingBase<Number>) endX).unbind(startX, transform.widthProperty());
        ((BindingBase<Number>) endY).unbind(startY, transform.heightProperty());
    }

    public boolean isPointInside(float x, float y)
    {
        return x > startX.getValue().floatValue() && x < endX.getValue().floatValue() && y > startY.getValue().floatValue() && y < endY.getValue().floatValue();
    }
}
