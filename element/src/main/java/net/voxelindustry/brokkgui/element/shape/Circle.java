package net.voxelindustry.brokkgui.element.shape;

import net.voxelindustry.brokkgui.component.PaintStyle;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Paint;
import net.voxelindustry.brokkgui.shape.CircleShape;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;

public class Circle extends GuiElement
{
    public static final CircleShape SHAPE = new CircleShape();

    private boolean     useStyle;
    private StyleHolder style;

    public Circle(float xPosition, float yPosition, float radius)
    {
        this.refreshStyle(StyleEngine.getInstance().getElementStyleStatus().enabled());

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
    protected String getType()
    {
        return "circle";
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
        }
        else
        {
            paint = new Paint();
            remove(PaintStyle.class);
        }
        paint.setShape(SHAPE);
        add(paint);
    }
}
