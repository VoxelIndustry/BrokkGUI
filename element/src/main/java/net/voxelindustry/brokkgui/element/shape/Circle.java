package net.voxelindustry.brokkgui.element.shape;

import net.voxelindustry.brokkgui.shape.CircleShape;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;

public class Circle extends GuiNode
{
    public static final CircleShape SHAPE = new CircleShape();

    public Circle(float xPosition, float yPosition, float radius)
    {
        transform().xTranslate(xPosition);
        transform().yTranslate(yPosition);

        transform().size(radius, radius);
    }

    public Circle(float radius)
    {
        this(0, 0, radius);
    }

    public Circle()
    {
        this(0);
    }

    @Override
    public String type()
    {
        return "circle";
    }

    @Override
    public ShapeDefinition shape()
    {
        return SHAPE;
    }
}
