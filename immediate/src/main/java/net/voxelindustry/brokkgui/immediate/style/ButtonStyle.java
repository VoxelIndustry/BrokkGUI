package net.voxelindustry.brokkgui.immediate.style;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.paint.Color;

public class ButtonStyle
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

    public final Color clickBoxColor;
    public final Color clickBorderColor;
    public final Color clickTextColor;
    public final Color clickShadowColor;

    public ButtonStyle(Color boxColor,
                       Color borderColor,
                       float borderThin,
                       RectBox padding,
                       RectAlignment textAlignment,
                       Color textColor,
                       Color shadowColor,
                       Color hoverBoxColor,
                       Color hoverBorderColor,
                       Color hoverTextColor,
                       Color hoverShadowColor,
                       Color clickBoxColor,
                       Color clickBorderColor,
                       Color clickTextColor,
                       Color clickShadowColor)
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
        this.clickBoxColor = clickBoxColor;
        this.clickBorderColor = clickBorderColor;
        this.clickTextColor = clickTextColor;
        this.clickShadowColor = clickShadowColor;
    }

    public static ButtonStyleBuilder build()
    {
        return new ButtonStyleBuilder();
    }

    public static class ButtonStyleBuilder
    {
        private Color boxColor    = Color.ALPHA;
        private Color borderColor = Color.ALPHA;
        private float borderThin  = 1;

        private RectBox       padding       = RectBox.EMPTY;
        private RectAlignment textAlignment = RectAlignment.MIDDLE_CENTER;

        private Color hoverBoxColor;
        private Color hoverBorderColor;

        private Color textColor   = Color.BLACK;
        private Color shadowColor = Color.ALPHA;

        private Color hoverTextColor;
        private Color hoverShadowColor;

        private Color clickBoxColor;
        private Color clickBorderColor;
        private Color clickTextColor;
        private Color clickShadowColor;

        public ButtonStyleBuilder setBoxColor(Color boxColor)
        {
            this.boxColor = boxColor;
            return this;
        }

        public ButtonStyleBuilder setBorderColor(Color borderColor)
        {
            this.borderColor = borderColor;
            return this;
        }

        public ButtonStyleBuilder setBorderThin(float borderThin)
        {
            this.borderThin = borderThin;
            return this;
        }

        public ButtonStyleBuilder setPadding(RectBox padding)
        {
            this.padding = padding;
            return this;
        }

        public ButtonStyleBuilder setTextAlignment(RectAlignment textAlignment)
        {
            this.textAlignment = textAlignment;
            return this;
        }

        public ButtonStyleBuilder setTextColor(Color textColor)
        {
            this.textColor = textColor;
            return this;
        }

        public ButtonStyleBuilder setShadowColor(Color shadowColor)
        {
            this.shadowColor = shadowColor;
            return this;
        }

        public ButtonStyleBuilder setHoverBoxColor(Color hoverBoxColor)
        {
            this.hoverBoxColor = hoverBoxColor;
            return this;
        }

        public ButtonStyleBuilder setHoverBorderColor(Color hoverBorderColor)
        {
            this.hoverBorderColor = hoverBorderColor;
            return this;
        }

        public ButtonStyleBuilder setHoverTextColor(Color hoverTextColor)
        {
            this.hoverTextColor = hoverTextColor;
            return this;
        }

        public ButtonStyleBuilder setHoverShadowColor(Color hoverShadowColor)
        {
            this.hoverShadowColor = hoverShadowColor;
            return this;
        }

        public ButtonStyleBuilder setClickBoxColor(Color clickBoxColor)
        {
            this.clickBoxColor = clickBoxColor;
            return this;
        }

        public ButtonStyleBuilder setClickBorderColor(Color clickBorderColor)
        {
            this.clickBorderColor = clickBorderColor;
            return this;
        }

        public ButtonStyleBuilder setClickTextColor(Color clickTextColor)
        {
            this.clickTextColor = clickTextColor;
            return this;
        }

        public ButtonStyleBuilder setClickShadowColor(Color clickShadowColor)
        {
            this.clickShadowColor = clickShadowColor;
            return this;
        }

        public ButtonStyle create()
        {
            if (hoverBoxColor == null)
                hoverBoxColor = boxColor;
            if (hoverBorderColor == null)
                hoverBorderColor = borderColor;
            if (hoverTextColor == null)
                hoverTextColor = textColor;
            if (hoverShadowColor == null)
                hoverShadowColor = shadowColor;

            if (clickBoxColor == null)
                clickBoxColor = hoverBoxColor;
            if (clickBorderColor == null)
                clickBorderColor = hoverBorderColor;
            if (clickTextColor == null)
                clickTextColor = hoverTextColor;
            if (clickShadowColor == null)
                clickShadowColor = hoverShadowColor;

            return new ButtonStyle(boxColor,
                    borderColor,
                    borderThin,
                    padding,
                    textAlignment,
                    textColor,
                    shadowColor,
                    hoverBoxColor,
                    hoverBorderColor,
                    hoverTextColor,
                    hoverShadowColor,
                    clickBoxColor,
                    clickBorderColor,
                    clickTextColor,
                    clickShadowColor);
        }
    }
}
