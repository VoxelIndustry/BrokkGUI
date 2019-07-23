package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.PaintStyle;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.element.shape.Rectangle;
import net.voxelindustry.brokkgui.panel.AbsolutePaneBase;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;

public class AbsolutePane extends AbsolutePaneBase
{
    private   boolean     useStyle;
    private   StyleHolder style;
    protected Paint       paint;

    public AbsolutePane()
    {
        this.refreshStyle(StyleEngine.getInstance().elementStyleStatus().enabled());
    }

    public StyleHolder style()
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
        paint.shape(Rectangle.SHAPE);
        this.paint = add(paint);
    }

    public Paint paint()
    {
        return paint;
    }
}
