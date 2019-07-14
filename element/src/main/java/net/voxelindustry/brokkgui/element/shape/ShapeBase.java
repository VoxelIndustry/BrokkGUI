package net.voxelindustry.brokkgui.element.shape;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Paint;
import net.voxelindustry.brokkgui.component.PaintStyle;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;
import net.voxelindustry.brokkgui.style.StyleHolder;

public abstract class ShapeBase extends GuiElement
{
    private boolean     useStyle;
    private StyleHolder style;

    public abstract ShapeDefinition getShape();

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

    protected void refreshStyle(boolean useStyle)
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
        paint.shape(getShape());
        add(paint);
    }
}
