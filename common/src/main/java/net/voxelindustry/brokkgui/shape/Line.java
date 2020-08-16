package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;

public class Line extends GuiElement
{
    private final Property<Float> lineThinProperty;

    public Line(float startX, float startY, float endX, float endY)
    {
        transform().xTranslate(startX);
        transform().yTranslate(startY);

        transform().width(Math.abs(startX - endX));
        transform().height(Math.abs((startY - endY)));

        lineThinProperty = new Property<>(1F);

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

    public Property<Float> lineThinProperty()
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