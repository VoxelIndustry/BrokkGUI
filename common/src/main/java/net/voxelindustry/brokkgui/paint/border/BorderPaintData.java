package net.voxelindustry.brokkgui.paint.border;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.sprite.Texture;

import java.util.Objects;

public class BorderPaintData implements BorderPaint
{
    private Color colorLeft   = Color.ALPHA;
    private Color colorRight  = Color.ALPHA;
    private Color colorTop    = Color.ALPHA;
    private Color colorBottom = Color.ALPHA;

    private Texture borderImage       = Texture.EMPTY;
    private RectBox borderImageSlice  = RectBox.EMPTY;
    private RectBox borderImageWidth  = RectBox.EMPTY;
    private RectBox borderImageOutset = RectBox.EMPTY;
    private boolean borderImageFill;

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
    public Texture image()
    {
        return borderImage;
    }

    @Override
    public void image(Texture borderImage)
    {
        this.borderImage = borderImage;
    }

    @Override
    public RectBox imageSlice()
    {
        return borderImageSlice;
    }

    @Override
    public void imageSlice(RectBox borderImageSlice)
    {
        this.borderImageSlice = borderImageSlice;
    }

    @Override
    public RectBox imageWidth()
    {
        return borderImageWidth;
    }

    @Override
    public void imageWidth(RectBox borderImageWidth)
    {
        this.borderImageWidth = borderImageWidth;
    }

    @Override
    public RectBox imageOutset()
    {
        return borderImageOutset;
    }

    @Override
    public void imageOutset(RectBox borderImageOutset)
    {
        this.borderImageOutset = borderImageOutset;
    }

    @Override
    public boolean imageFill()
    {
        return borderImageFill;
    }

    @Override
    public void imageFill(boolean borderImageFill)
    {
        this.borderImageFill = borderImageFill;
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
        BorderPaintData that = (BorderPaintData) o;
        return borderImageFill == that.borderImageFill && Objects.equals(colorLeft, that.colorLeft) && Objects.equals(colorRight, that.colorRight) && Objects.equals(colorTop, that.colorTop) && Objects.equals(colorBottom, that.colorBottom) && Objects.equals(borderImage, that.borderImage) && Objects.equals(borderImageSlice, that.borderImageSlice) && Objects.equals(borderImageWidth, that.borderImageWidth) && Objects.equals(borderImageOutset, that.borderImageOutset) && Objects.equals(box, that.box);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(colorLeft, colorRight, colorTop, colorBottom, borderImage, borderImageSlice, borderImageWidth, borderImageOutset, borderImageFill, box);
    }
}
