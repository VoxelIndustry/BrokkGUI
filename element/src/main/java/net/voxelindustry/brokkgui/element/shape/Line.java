package net.voxelindustry.brokkgui.element.shape;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.PaintStyle;
import net.voxelindustry.brokkgui.exp.component.GuiElement;
import net.voxelindustry.brokkgui.exp.component.Paint;
import net.voxelindustry.brokkgui.shape.LineShape;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;

public class Line extends GuiElement
{
    private boolean             useStyle;
    private StyleHolder         style;
    private BaseProperty<Float> lineWidthProperty;

    public Line(float startX, float startY, float endX, float endY)
    {
        this.refreshStyle(StyleEngine.getInstance().getElementStyleStatus().enabled());

        transform().setxTranslate(startX);
        transform().setyTranslate(startY);

        transform().setWidth(Math.abs(startX - endX));
        transform().setHeight(Math.abs(startY - endY));
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
    protected String getType()
    {
        return "line";
    }

    public StyleHolder getStyle()
    {
        if (style == null)
            style = get(StyleHolder.class);
        return style;
    }

    public boolean useStyle()
    {
        return useStyle;
    }

    public void useStyle(boolean useStyle)
    {
        this.useStyle = useStyle;
        this.refreshStyle(useStyle);
    }

    private void refreshStyle(boolean useStyle)
    {
        Paint paint;
        if (useStyle)
        {
            paint = new PaintStyle();
            remove(Paint.class);

            getStyle().registerProperty("line-width", 1f, Float.class);
        }
        else
        {
            paint = new Paint();
            remove(PaintStyle.class);
        }
        paint.setShape(new LineShape(this::getLineWidth));
        add(paint);
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public BaseProperty<Float> lineWidthProperty()
    {
        if (useStyle)
            return this.getStyle().getProperty("line-width", Float.class);

        if (this.lineWidthProperty == null)
            this.lineWidthProperty = new BaseProperty<>(1f, "line-width");
        return this.lineWidthProperty;
    }

    public float getLineWidth()
    {
        if (useStyle)
            return this.getStyle().getStyleValue("line-width", Float.class, 1f);

        return this.lineWidthProperty().getValue();
    }

    public void setLineWidth(float lineWidth)
    {
        if (useStyle)
            this.getStyle().setPropertyDirect("line-width", lineWidth, Float.class);
        else
            this.lineWidthProperty().setValue(lineWidth);
    }
}
