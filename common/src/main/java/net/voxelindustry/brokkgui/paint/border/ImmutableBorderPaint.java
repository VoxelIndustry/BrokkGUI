package net.voxelindustry.brokkgui.paint.border;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.sprite.Texture;

import java.util.Objects;

public class ImmutableBorderPaint implements BorderPaint
{
    private final Color colorLeft;
    private final Color colorRight;
    private final Color colorTop;
    private final Color colorBottom;

    private final Texture borderImage;
    private final RectBox borderImageSlice;
    private final RectBox borderImageWidth;
    private final RectBox borderImageOutset;
    private final boolean borderImageFill;

    private final RectBox box;

    private final boolean isColorUniform;

    public ImmutableBorderPaint(Color colorLeft, Color colorRight, Color colorTop, Color colorBottom, Texture borderImage, RectBox borderImageSlice, RectBox borderImageWidth, RectBox borderImageOutset, boolean borderImageFill, RectBox box)
    {
        this.colorLeft = colorLeft;
        this.colorRight = colorRight;
        this.colorTop = colorTop;
        this.colorBottom = colorBottom;
        this.borderImage = borderImage;
        this.borderImageSlice = borderImageSlice;
        this.borderImageWidth = borderImageWidth;
        this.borderImageOutset = borderImageOutset;
        this.borderImageFill = borderImageFill;
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
    public Texture image()
    {
        return borderImage;
    }

    @Override
    public RectBox imageSlice()
    {
        return borderImageSlice;
    }

    @Override
    public RectBox imageWidth()
    {
        return borderImageWidth;
    }

    @Override
    public RectBox imageOutset()
    {
        return borderImageOutset;
    }

    @Override
    public boolean imageFill()
    {
        return borderImageFill;
    }

    @Override
    public RectBox box()
    {
        return box;
    }

    @Override
    public boolean isColorUniform()
    {
        return isColorUniform;
    }

    @Override
    public void colorLeft(Color color)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void colorRight(Color color)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void colorTop(Color color)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void colorBottom(Color color)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void image(Texture texture)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void imageSlice(RectBox box)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void imageWidth(RectBox box)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void imageOutset(RectBox box)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void imageFill(boolean doFill)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public void box(RectBox box)
    {
        throw new UnsupportedOperationException("This BorderPaint is immutable");
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableBorderPaint that = (ImmutableBorderPaint) o;
        return borderImageFill == that.borderImageFill && isColorUniform == that.isColorUniform && Objects.equals(colorLeft, that.colorLeft) && Objects.equals(colorRight, that.colorRight) && Objects.equals(colorTop, that.colorTop) && Objects.equals(colorBottom, that.colorBottom) && Objects.equals(borderImage, that.borderImage) && Objects.equals(borderImageSlice, that.borderImageSlice) && Objects.equals(borderImageWidth, that.borderImageWidth) && Objects.equals(borderImageOutset, that.borderImageOutset) && Objects.equals(box, that.box);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(colorLeft, colorRight, colorTop, colorBottom, borderImage, borderImageSlice, borderImageWidth, borderImageOutset, borderImageFill, box, isColorUniform);
    }
}
