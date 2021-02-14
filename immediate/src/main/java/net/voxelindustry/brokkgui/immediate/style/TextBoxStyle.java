package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;

public class TextBoxStyle
{
    public final Color boxColor;
    public final Color borderColor;
    public final float borderThin;

    public final RectBox       padding;
    public final RectAlignment textAlignment;

    public final Color textColor;
    public final Color shadowColor;

    public final Color hoverBoxColor;
    public final Color hoverBorderColor;
    public final Color hoverTextColor;
    public final Color hoverShadowColor;

    public TextBoxStyle(Color boxColor,
                        Color borderColor,
                        float borderThin,
                        RectBox padding,
                        RectAlignment textAlignment,
                        Color textColor,
                        Color shadowColor,
                        Color hoverBoxColor,
                        Color hoverBorderColor,
                        Color hoverTextColor,
                        Color hoverShadowColor)
    {
        this.boxColor = boxColor;
        this.borderColor = borderColor;
        this.borderThin = borderThin;
        this.padding = padding;
        this.textAlignment = textAlignment;
        this.textColor = textColor;
        this.shadowColor = shadowColor;
        this.hoverBoxColor = hoverBoxColor;
        this.hoverBorderColor = hoverBorderColor;
        this.hoverTextColor = hoverTextColor;
        this.hoverShadowColor = hoverShadowColor;
    }

    public static TextBoxStyleBuilder build()
    {
        return new TextBoxStyleBuilder();
    }

    public static class TextBoxStyleBuilder
    {
        private Color boxColor;
        private Color borderColor = Color.ALPHA;
        private float borderThin  = 1;

        private RectBox       padding       = RectBox.EMPTY;
        private RectAlignment textAlignment = RectAlignment.MIDDLE_CENTER;

        private Color hoverBoxColor;
        private Color hoverBorderColor;

        private Color textColor;
        private Color shadowColor = Color.ALPHA;

        private Color hoverTextColor;
        private Color hoverShadowColor;

        public TextBoxStyleBuilder setBoxColor(Color boxColor)
        {
            this.boxColor = boxColor;
            return this;
        }

        public TextBoxStyleBuilder setBorderColor(Color borderColor)
        {
            this.borderColor = borderColor;
            return this;
        }

        public TextBoxStyleBuilder setBorderThin(float borderThin)
        {
            this.borderThin = borderThin;
            return this;
        }

        public TextBoxStyleBuilder setPadding(RectBox padding)
        {
            this.padding = padding;
            return this;
        }

        public TextBoxStyleBuilder setTextAlignment(RectAlignment textAlignment)
        {
            this.textAlignment = textAlignment;
            return this;
        }

        public TextBoxStyleBuilder setTextColor(Color textColor)
        {
            this.textColor = textColor;
            return this;
        }

        public TextBoxStyleBuilder setShadowColor(Color shadowColor)
        {
            this.shadowColor = shadowColor;
            return this;
        }

        public TextBoxStyleBuilder setHoverBoxColor(Color hoverBoxColor)
        {
            this.hoverBoxColor = hoverBoxColor;
            return this;
        }

        public TextBoxStyleBuilder setHoverBorderColor(Color hoverBorderColor)
        {
            this.hoverBorderColor = hoverBorderColor;
            return this;
        }

        public TextBoxStyleBuilder setHoverTextColor(Color hoverTextColor)
        {
            this.hoverTextColor = hoverTextColor;
            return this;
        }

        public TextBoxStyleBuilder setHoverShadowColor(Color hoverShadowColor)
        {
            this.hoverShadowColor = hoverShadowColor;
            return this;
        }

        public TextBoxStyle create()
        {
            if (hoverBoxColor == null)
                hoverBoxColor = boxColor;
            if (hoverBorderColor == null)
                hoverBorderColor = borderColor;
            if (hoverTextColor == null)
                hoverTextColor = textColor;
            if (hoverShadowColor == null)
                hoverShadowColor = shadowColor;

            return new TextBoxStyle(boxColor,
                    borderColor,
                    borderThin,
                    padding,
                    textAlignment,
                    textColor,
                    shadowColor,
                    hoverBoxColor,
                    hoverBorderColor,
                    hoverTextColor,
                    hoverShadowColor
            );
        }
    }
}
