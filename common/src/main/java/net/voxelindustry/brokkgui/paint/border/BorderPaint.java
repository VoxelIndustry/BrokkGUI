package net.voxelindustry.brokkgui.paint.border;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.sprite.Texture;

public interface BorderPaint
{
    BorderPaint EMPTY = new ImmutableBorderPaint(Color.ALPHA, Color.ALPHA, Color.ALPHA, Color.ALPHA, Texture.EMPTY, RectBox.EMPTY, RectBox.EMPTY, RectBox.EMPTY, false, RectBox.EMPTY);

    Color colorLeft();

    Color colorRight();

    Color colorTop();

    Color colorBottom();

    boolean isColorUniform();

    Texture image();

    RectBox imageSlice();

    RectBox imageWidth();

    RectBox imageOutset();

    boolean imageFill();

    RectBox box();

    void colorLeft(Color color);

    void colorRight(Color color);

    void colorTop(Color color);

    void colorBottom(Color color);

    void image(Texture texture);

    void imageSlice(RectBox box);

    void imageWidth(RectBox box);

    void imageOutset(RectBox box);

    void imageFill(boolean doFill);

    void box(RectBox box);
}
