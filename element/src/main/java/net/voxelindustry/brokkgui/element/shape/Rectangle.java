package net.voxelindustry.brokkgui.element.shape;

import net.voxelindustry.brokkgui.component.PaintStyle;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Paint;
import net.voxelindustry.brokkgui.shape.RectangleShape;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;

public class Rectangle extends GuiElement
{
    public static final RectangleShape SHAPE = new RectangleShape();

    private boolean             useStyle;
    private StyleHolder         style;

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
            add(new StyleHolder());
            paint = new PaintStyle();
            remove(Paint.class);
        }
        else
        {
            paint = new Paint();
            remove(PaintStyle.class);
            remove(StyleHolder.class);
        }
        paint.shape(SHAPE);
        add(paint);
    }
}
