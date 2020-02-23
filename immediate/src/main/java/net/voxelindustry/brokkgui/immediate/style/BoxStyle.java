package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkgui.paint.Color;

public class BoxStyle
{
    public final Color boxColor;
    public final Color borderColor;
    public final float borderThin;

    public BoxStyle(Color boxColor, Color borderColor, float borderThin)
    {
        this.boxColor = boxColor;
        this.borderColor = borderColor;
        this.borderThin = borderThin;
    }

    public static BoxStyleBuilder build()
    {
        return new BoxStyleBuilder();
    }

    public static class BoxStyleBuilder
    {
        private Color boxColor;
        private Color borderColor = Color.ALPHA;
        private float borderThin  = 1;

        public BoxStyleBuilder setBoxColor(Color boxColor)
        {
            this.boxColor = boxColor;
            return this;
        }

        public BoxStyleBuilder setBorderColor(Color borderColor)
        {
            this.borderColor = borderColor;
            return this;
        }

        public BoxStyleBuilder setBorderThin(float borderThin)
        {
            this.borderThin = borderThin;
            return this;
        }

        public BoxStyle create()
        {
            return new BoxStyle(boxColor, borderColor, borderThin);
        }
    }
}
