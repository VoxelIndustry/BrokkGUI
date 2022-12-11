package net.voxelindustry.brokkgui.paint.outline;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;

public interface OutlinePaint
{
    OutlinePaint EMPTY = new ImmutableOutlinePaint(Color.ALPHA, Color.ALPHA, Color.ALPHA, Color.ALPHA, RectBox.EMPTY);

    Color colorLeft();

    Color colorRight();

    Color colorTop();

    Color colorBottom();

    boolean isColorUniform();

    RectBox box();

    void colorLeft(Color color);

    void colorRight(Color color);

    void colorTop(Color color);

    void colorBottom(Color color);

    void box(RectBox box);
}
