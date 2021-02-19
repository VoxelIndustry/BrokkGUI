package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BindingBase;
import fr.ourten.teabeans.property.specific.FloatProperty;
import fr.ourten.teabeans.value.ObservableValue;
import fr.ourten.teabeans.value.specific.FloatValue;
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

        rectArea.startX = (FloatValue) transform.xPosProperty().add(transform.xTranslateProperty());
        rectArea.startY = (FloatValue) transform.yPosProperty().add(transform.yTranslateProperty());

        rectArea.endX = rectArea.startX.combine(transform.widthProperty(),
                (startX, width) -> startX + width * widthFrac);
        rectArea.endY = rectArea.startY.combine(transform.heightProperty(),
                (startY, height) -> startY + height * heightFrac);

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

        ((BindingBase<Float>) startX).unbind(transform.xPosProperty(), transform.xTranslateProperty());
        ((BindingBase<Float>) startY).unbind(transform.yPosProperty(), transform.yTranslateProperty());

        ((BindingBase<Float>) endX).unbind(startX, transform.widthProperty());
        ((BindingBase<Float>) endY).unbind(startY, transform.heightProperty());
    }

    public boolean isPointInside(float x, float y)
    {
        return x > startX.getValue() && x < endX.getValue() && y > startY.getValue() && y < endY.getValue();
    }
}
