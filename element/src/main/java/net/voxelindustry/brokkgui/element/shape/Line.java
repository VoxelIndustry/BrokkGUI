package net.voxelindustry.brokkgui.element.shape;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.shape.LineShape;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;

public class Line extends GuiNode
{
    private BaseProperty<Float> lineWidthProperty;

    public Line(float startX, float startY, float endX, float endY)
    {
        transform().xTranslate(startX);
        transform().yTranslate(startY);

        transform().width(Math.abs(startX - endX));
        transform().height(Math.abs(startY - endY));
    }

    public Line(float endX, float endY)
    {
        this(0, 0, endX, endY);
    }

    public Line()
    {
        this(0, 0);
    }

    @Override
    public String type()
    {
        return "line";
    }

    @Override
    public ShapeDefinition shape()
    {
        return new LineShape(this::getLineWidth);
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public BaseProperty<Float> lineWidthProperty()
    {
        if (this.useStyle())
            return this.style().getProperty("line-width", Float.class);

        if (this.lineWidthProperty == null)
            this.lineWidthProperty = new BaseProperty<>(1f, "line-width");
        return this.lineWidthProperty;
    }

    public float getLineWidth()
    {
        if (this.useStyle())
            return this.style().getStyleValue("line-width", Float.class, 1f);

        return this.lineWidthProperty().getValue();
    }

    public void setLineWidth(float lineWidth)
    {
        if (this.useStyle())
            this.style().setPropertyDirect("line-width", lineWidth, Float.class);
        else
            this.lineWidthProperty().setValue(lineWidth);
    }
}
