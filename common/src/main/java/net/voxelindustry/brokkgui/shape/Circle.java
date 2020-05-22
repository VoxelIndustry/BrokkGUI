package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;

public class Circle extends GuiElement
{
    public static final CircleShape SHAPE = new CircleShape();

    public Circle(float xPosition, float yPosition, float radius)
    {
        transform().xTranslate(xPosition);
        transform().yTranslate(yPosition);

        transform().width(radius);
        transform().height(radius);

        paint().shape(SHAPE);
    }

    public Circle(float radius)
    {
        this(0, 0, radius);
    }

    public Circle()
    {
        this(0, 0, 0);
    }

    @Override
    public String type()
    {
        return "circle";
    }
}