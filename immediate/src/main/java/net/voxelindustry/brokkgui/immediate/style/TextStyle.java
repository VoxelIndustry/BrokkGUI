package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkgui.paint.Color;

public class TextStyle
{
    public final Color textColor;
    public final Color shadowColor;
    public final Color hoverTextColor;
    public final Color hoverShadowColor;

    public TextStyle(Color textColor, Color shadowColor, Color hoverTextColor, Color hoverShadowColor)
    {
        this.textColor = textColor;
        this.shadowColor = shadowColor;
        this.hoverTextColor = hoverTextColor;
        this.hoverShadowColor = hoverShadowColor;
    }

    public static TextStyleBuilder build()
    {
        return new TextStyleBuilder();
    }

    public static class TextStyleBuilder
    {
        private Color textColor;
        private Color shadowColor = Color.ALPHA;
        private Color hoverTextColor;
        private Color hoverShadowColor;

        public TextStyleBuilder setTextColor(Color textColor)
        {
            this.textColor = textColor;
            return this;
        }

        public TextStyleBuilder setShadowColor(Color shadowColor)
        {
            this.shadowColor = shadowColor;
            return this;
        }

        public TextStyleBuilder setHoverTextColor(Color hoverTextColor)
        {
            this.hoverTextColor = hoverTextColor;
            return this;
        }

        public TextStyleBuilder setHoverShadowColor(Color hoverShadowColor)
        {
            this.hoverShadowColor = hoverShadowColor;
            return this;
        }

        public TextStyle create()
        {
            if (hoverTextColor == null)
                hoverTextColor = textColor;
            if (hoverShadowColor == null)
                hoverShadowColor = shadowColor;

            return new TextStyle(textColor, shadowColor, hoverTextColor, hoverShadowColor);
        }
    }
}
