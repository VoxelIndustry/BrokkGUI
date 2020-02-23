package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkgui.paint.Color;

public class TextStyle
{
    public final Color textColor;
    public final Color shadowColor;

    public TextStyle(Color textColor, Color shadowColor)
    {
        this.textColor = textColor;
        this.shadowColor = shadowColor;
    }

    public static TextStyleBuilder build()
    {
        return new TextStyleBuilder();
    }

    public static class TextStyleBuilder
    {
        private Color textColor;
        private Color shadowColor = Color.ALPHA;

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

        public TextStyle create()
        {
            return new TextStyle(textColor, shadowColor);
        }
    }
}
