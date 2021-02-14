package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkcolor.Color;

public class EmptyBoxStyle
{
    public final Color borderColor;
    public final float borderThin;

    public final Color hoverBorderColor;

    public EmptyBoxStyle(Color borderColor, float borderThin, Color hoverBorderColor)
    {
        this.borderColor = borderColor;
        this.borderThin = borderThin;
        this.hoverBorderColor = hoverBorderColor;
    }

    public static EmptyBoxStyleBuilder build()
    {
        return new EmptyBoxStyleBuilder();
    }

    public static class EmptyBoxStyleBuilder
    {
        private Color borderColor = Color.ALPHA;
        private float borderThin  = 1;

        private Color hoverBorderColor;

        public EmptyBoxStyleBuilder setBorderColor(Color borderColor)
        {
            this.borderColor = borderColor;
            return this;
        }

        public EmptyBoxStyleBuilder setBorderThin(float borderThin)
        {
            this.borderThin = borderThin;
            return this;
        }

        public EmptyBoxStyleBuilder setHoverBorderColor(Color hoverBorderColor)
        {
            this.hoverBorderColor = hoverBorderColor;
            return this;
        }

        public EmptyBoxStyle create()
        {
            if (hoverBorderColor == null)
                hoverBorderColor = borderColor;

            return new EmptyBoxStyle(borderColor, borderThin, hoverBorderColor);
        }
    }
}
