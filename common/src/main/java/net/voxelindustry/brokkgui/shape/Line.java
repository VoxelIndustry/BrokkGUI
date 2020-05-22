package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;

public class Line extends GuiElement
{
    private final BaseProperty<Float> lineThinProperty;

    public Line(float startX, float startY, float endX, float endY)
    {
        transform().xTranslate(startX);
        transform().yTranslate(startY);

        transform().width(Math.abs(startX - endX));
        transform().height(Math.abs((startY - endY)));

        lineThinProperty = new BaseProperty<>(1F, "lineThinProperty");

        paint().shape(new LineShape(this::getLineThin));
    }

    public Line(float endX, float endY)
    {
        this(0, 0, endX, endY);
    }

    public Line()
    {
        this(0, 0);
    }

    public BaseProperty<Float> lineThinProperty()
    {
        return lineThinProperty;
    }

    public float getLineThin()
    {
        return lineThinProperty().getValue();
    }

    public void setLineThin(float lineThin)
    {
        lineThinProperty().setValue(lineThin);
    }

    @Override
    public String type()
    {
        return "line";
    }
}