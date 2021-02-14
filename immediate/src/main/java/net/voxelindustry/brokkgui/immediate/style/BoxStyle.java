package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkcolor.Color;

public class BoxStyle
{
    public final Color boxColor;
    public final Color borderColor;
    public final float borderThin;

    public final Color hoverBoxColor;
    public final Color hoverBorderColor;

    public BoxStyle(Color boxColor, Color borderColor, float borderThin, Color hoverBoxColor, Color hoverBorderColor)
    {
        this.boxColor = boxColor;
        this.borderColor = borderColor;
        this.borderThin = borderThin;
        this.hoverBoxColor = hoverBoxColor;
        this.hoverBorderColor = hoverBorderColor;
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

        private Color hoverBoxColor;
        private Color hoverBorderColor;

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

        public BoxStyleBuilder setHoverBoxColor(Color hoverBoxColor)
        {
            this.hoverBoxColor = hoverBoxColor;
            return this;
        }

        public BoxStyleBuilder setHoverBorderColor(Color hoverBorderColor)
        {
            this.hoverBorderColor = hoverBorderColor;
            return this;
        }

        public BoxStyle create()
        {
            if (hoverBoxColor == null)
                hoverBoxColor = boxColor;
            if (hoverBorderColor == null)
                hoverBorderColor = borderColor;

            return new BoxStyle(boxColor, borderColor, borderThin, hoverBoxColor, hoverBorderColor);
        }
    }
}
