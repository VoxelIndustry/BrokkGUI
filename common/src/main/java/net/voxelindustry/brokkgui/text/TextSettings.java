package net.voxelindustry.brokkgui.text;

import com.google.common.base.Objects;
import net.voxelindustry.brokkgui.paint.Color;

public class TextSettings
{
    private String fontName;

    private Color textColor;
    private Color shadowColor;
    private Color outlineColor;
    private float outlineWidth;

    private float lineSpacingMultiplier;

    private boolean strikethrough;
    private boolean italic;
    private boolean bold;
    private boolean underline;

    public TextSettings(String fontName, Color textColor, Color shadowColor, Color outlineColor, float outlineWidth, float lineSpacingMultiplier, boolean strikethrough, boolean italic, boolean bold, boolean underline)
    {
        this.fontName = fontName;
        this.textColor = textColor;
        this.shadowColor = shadowColor;
        this.outlineColor = outlineColor;
        this.outlineWidth = outlineWidth;
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        this.strikethrough = strikethrough;
        this.italic = italic;
        this.bold = bold;
        this.underline = underline;
    }

    public String fontName()
    {
        return fontName;
    }

    public Color textColor()
    {
        return textColor;
    }

    public Color shadowColor()
    {
        return shadowColor;
    }

    public Color outlineColor()
    {
        return outlineColor;
    }

    public float outlineWidth()
    {
        return outlineWidth;
    }

    public float lineSpacingMultiplier()
    {
        return lineSpacingMultiplier;
    }

    public boolean strikethrough()
    {
        return strikethrough;
    }

    public boolean italic()
    {
        return italic;
    }

    public boolean bold()
    {
        return bold;
    }

    public boolean underline()
    {
        return underline;
    }

    public TextSettings fontName(String fontName)
    {
        this.fontName = fontName;
        return this;
    }

    public TextSettings textColor(Color textColor)
    {
        this.textColor = textColor;
        return this;
    }

    public TextSettings shadowColor(Color shadowColor)
    {
        this.shadowColor = shadowColor;
        return this;
    }

    public TextSettings outlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
        return this;
    }

    public TextSettings outlineWidth(float outlineWidth)
    {
        this.outlineWidth = outlineWidth;
        return this;
    }

    public TextSettings lineSpacingMultiplier(float lineSpacingMultiplier)
    {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    public TextSettings strikethrough(boolean strikethrough)
    {
        this.strikethrough = strikethrough;
        return this;
    }

    public TextSettings italic(boolean italic)
    {
        this.italic = italic;
        return this;
    }

    public TextSettings bold(boolean bold)
    {
        this.bold = bold;
        return this;
    }

    public TextSettings underline(boolean underline)
    {
        this.underline = underline;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSettings that = (TextSettings) o;
        return Float.compare(that.outlineWidth, outlineWidth) == 0 && strikethrough == that.strikethrough && italic == that.italic && bold == that.bold && underline == that.underline && Objects.equal(fontName, that.fontName) && Objects.equal(textColor, that.textColor) && Objects.equal(shadowColor, that.shadowColor) && Objects.equal(outlineColor, that.outlineColor);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(fontName, textColor, shadowColor, outlineColor, outlineWidth, strikethrough, italic, bold, underline);
    }

    @Override
    public String toString()
    {
        return "TextSettings{" +
                "fontName='" + fontName + '\'' +
                ", textColor=" + textColor +
                ", shadowColor=" + shadowColor +
                ", outlineColor=" + outlineColor +
                ", outlineWidth=" + outlineWidth +
                ", strikethrough=" + strikethrough +
                ", italic=" + italic +
                ", bold=" + bold +
                ", underline=" + underline +
                '}';
    }

    public static Builder build()
    {
        return new Builder();
    }

    public static class Builder
    {
        private String fontName;
        private Color  textColor;
        private Color  shadowColor;
        private Color  outlineColor;
        private float  outlineWidth;

        private float lineSpacingMultiplier = 1;

        private boolean strikethrough;
        private boolean italic;
        private boolean bold;
        private boolean underline;

        public Builder fontName(String fontName)
        {
            this.fontName = fontName;
            return this;
        }

        public Builder textColor(Color textColor)
        {
            this.textColor = textColor;
            return this;
        }

        public Builder shadowColor(Color shadowColor)
        {
            this.shadowColor = shadowColor;
            return this;
        }

        public Builder outlineColor(Color outlineColor)
        {
            this.outlineColor = outlineColor;
            return this;
        }

        public Builder outlineWidth(float outlineWidth)
        {
            this.outlineWidth = outlineWidth;
            return this;
        }

        public Builder lineSpacingMultiplir(float lineSpacingMultiplier)
        {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            return this;
        }

        public Builder strikethrough(boolean strikethrough)
        {
            this.strikethrough = strikethrough;
            return this;
        }

        public Builder italic(boolean italic)
        {
            this.italic = italic;
            return this;
        }

        public Builder bold(boolean bold)
        {
            this.bold = bold;
            return this;
        }

        public Builder underline(boolean underline)
        {
            this.underline = underline;
            return this;
        }

        public TextSettings create()
        {
            return new TextSettings(fontName, textColor, shadowColor, outlineColor, outlineWidth, lineSpacingMultiplier, strikethrough, italic, bold, underline);
        }
    }
}
