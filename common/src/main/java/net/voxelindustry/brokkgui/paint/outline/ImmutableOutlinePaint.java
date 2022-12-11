package net.voxelindustry.brokkgui.paint.outline;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;

import java.util.Objects;

public class ImmutableOutlinePaint implements OutlinePaint
{
    private final Color colorLeft;
    private final Color colorRight;
    private final Color colorTop;
    private final Color colorBottom;

    private final RectBox box;

    private final boolean isColorUniform;

    public ImmutableOutlinePaint(Color colorLeft, Color colorRight, Color colorTop, Color colorBottom, RectBox box)
    {
        this.colorLeft = colorLeft;
        this.colorRight = colorRight;
        this.colorTop = colorTop;
        this.colorBottom = colorBottom;
        this.box = box;

        this.isColorUniform = colorLeft == colorRight && colorLeft == colorTop && colorLeft == colorBottom;
    }

    @Override
    public Color colorLeft()
    {
        return colorLeft;
    }

    @Override
    public Color colorRight()
    {
        return colorRight;
    }

    @Override
    public Color colorTop()
    {
        return colorTop;
    }

    @Override
    public Color colorBottom()
    {
        return colorBottom;
    }

    @Override
    public boolean isColorUniform()
    {
        return isColorUniform;
    }

    @Override
    public RectBox box()
    {
        return box;
    }

    @Override
    public void colorLeft(Color color)
    {
        throw new UnsupportedOperationException("This OutlinePaint is immutable");
    }

    @Override
    public void colorRight(Color color)
    {
        throw new UnsupportedOperationException("This OutlinePaint is immutable");
    }

    @Override
    public void colorTop(Color color)
    {
        throw new UnsupportedOperationException("This OutlinePaint is immutable");
    }

    @Override
    public void colorBottom(Color color)
    {
        throw new UnsupportedOperationException("This OutlinePaint is immutable");
    }

    @Override
    public void box(RectBox box)
    {
        throw new UnsupportedOperationException("This OutlinePaint is immutable");
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableOutlinePaint that = (ImmutableOutlinePaint) o;
        return Objects.equals(colorLeft, that.colorLeft) && Objects.equals(colorRight, that.colorRight) && Objects.equals(colorTop, that.colorTop) && Objects.equals(colorBottom, that.colorBottom) && Objects.equals(box, that.box);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(colorLeft, colorRight, colorTop, colorBottom, box);
    }
}
