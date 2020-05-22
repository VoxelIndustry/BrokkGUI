package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;

public class Rectangle extends GuiElement
{
    public static final RectangleShape SHAPE = new RectangleShape();

    public Rectangle(float xLeft, float yLeft, float width, float height)
    {
        transform().xTranslate(xLeft);
        transform().yTranslate(yLeft);

        transform().width(width);
        transform().height(height);
    }

    public Rectangle(float width, float height)
    {
        this(0, 0, width, height);
    }

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }

    @Override
    public String type()
    {
        return "rectangle";
    }
}