package net.voxelindustry.brokkgui.paint.outline;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;

import java.util.Objects;

public class OutlinePaintData implements OutlinePaint
{
    private Color colorLeft   = Color.ALPHA;
    private Color colorRight  = Color.ALPHA;
    private Color colorTop    = Color.ALPHA;
    private Color colorBottom = Color.ALPHA;

    private RectBox box = RectBox.EMPTY;

    @Override
    public Color colorLeft()
    {
        return colorLeft;
    }

    @Override
    public void colorLeft(Color colorLeft)
    {
        this.colorLeft = colorLeft;
    }

    @Override
    public Color colorRight()
    {
        return colorRight;
    }

    @Override
    public void colorRight(Color colorRight)
    {
        this.colorRight = colorRight;
    }

    @Override
    public Color colorTop()
    {
        return colorTop;
    }

    @Override
    public void colorTop(Color colorTop)
    {
        this.colorTop = colorTop;
    }

    @Override
    public Color colorBottom()
    {
        return colorBottom;
    }

    @Override
    public void colorBottom(Color colorBottom)
    {
        this.colorBottom = colorBottom;
    }

    @Override
    public boolean isColorUniform()
    {
        return colorLeft == colorRight && colorLeft == colorBottom && colorLeft == colorTop;
    }

    @Override
    public RectBox box()
    {
        return box;
    }

    @Override
    public void box(RectBox box)
    {
        this.box = box;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutlinePaintData that = (OutlinePaintData) o;
        return Objects.equals(colorLeft, that.colorLeft) && Objects.equals(colorRight, that.colorRight) && Objects.equals(colorTop, that.colorTop) && Objects.equals(colorBottom, that.colorBottom) && Objects.equals(box, that.box);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(colorLeft, colorRight, colorTop, colorBottom, box);
    }
}
