package net.voxelindustry.brokkgui.element.shape;

import net.voxelindustry.brokkgui.shape.RectangleShape;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;

public class Rectangle extends ShapeBase
{
    public static final RectangleShape SHAPE = new RectangleShape();

    public Rectangle(float xLeft, float yLeft, float width, float height)
    {
        this.refreshStyle(StyleEngine.getInstance().elementStyleStatus().enabled());

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
        this(0, 0);
    }

    @Override
    public String getType()
    {
        return "rectangle";
    }

    @Override
    public ShapeDefinition getShape()
    {
        return SHAPE;
    }
}
