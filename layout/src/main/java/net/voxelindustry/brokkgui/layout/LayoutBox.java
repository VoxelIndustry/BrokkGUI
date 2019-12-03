package net.voxelindustry.brokkgui.layout;

import java.util.Objects;

public class LayoutBox implements ILayoutBox
{
    private float minWidth;
    private float minHeight;
    private float prefWidth;
    private float prefHeight;
    private float maxWidth;
    private float maxHeight;

    public LayoutBox(final float minWidth, final float minHeight, final float prefWidth, final float prefHeight, final float maxWidth, final float maxHeight)
    {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.prefWidth = prefWidth;
        this.prefHeight = prefHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public float minWidth()
    {
        return minWidth;
    }

    public void minX(final float minX)
    {
        minWidth = minX;
    }

    @Override
    public float minHeight()
    {
        return minHeight;
    }

    public void minY(final float minY)
    {
        minHeight = minY;
    }

    @Override
    public float prefWidth()
    {
        return prefWidth;
    }

    public void prefX(final float prefX)
    {
        prefWidth = prefX;
    }

    @Override
    public float prefHeight()
    {
        return prefHeight;
    }

    public void prefY(final float prefY)
    {
        prefHeight = prefY;
    }

    @Override
    public float maxWidth()
    {
        return maxWidth;
    }

    public void maxX(final float maxX)
    {
        maxWidth = maxX;
    }

    @Override
    public float maxHeight()
    {
        return maxHeight;
    }

    public void maxY(final float maxY)
    {
        maxHeight = maxY;
    }

    @Override
    public String toString()
    {
        return "LayoutBox{" +
                "minX=" + minWidth +
                ", minY=" + minHeight +
                ", prefX=" + prefWidth +
                ", prefY=" + prefHeight +
                ", maxX=" + maxWidth +
                ", maxY=" + maxHeight +
                '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LayoutBox layoutBox = (LayoutBox) o;
        return Float.compare(layoutBox.minWidth, minWidth) == 0 &&
                Float.compare(layoutBox.minHeight, minHeight) == 0 &&
                Float.compare(layoutBox.prefWidth, prefWidth) == 0 &&
                Float.compare(layoutBox.prefHeight, prefHeight) == 0 &&
                Float.compare(layoutBox.maxWidth, maxWidth) == 0 &&
                Float.compare(layoutBox.maxHeight, maxHeight) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(minWidth, minHeight, prefWidth, prefHeight, maxWidth, maxHeight);
    }
}
