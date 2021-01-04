package net.voxelindustry.brokkgui.text;

import net.voxelindustry.brokkgui.paint.Color;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TextSettings
{
    private String fontName;
    private float  fontSize;

    private Color textColor;
    private Color shadowColor;

    @Nonnull
    private Color outlineColor;
    private float outlineWidth;

    @Nonnull
    private Color glowColor;
    private float glowWidth;

    private float lineSpacingMultiplier;

    private boolean strikethrough;
    private boolean italic;
    private boolean bold;
    private boolean underline;

    public TextSettings(String fontName, float fontSize, Color textColor, Color shadowColor, Color outlineColor, float outlineWidth, Color glowColor, float glowWidth, float lineSpacingMultiplier, boolean strikethrough, boolean italic, boolean bold, boolean underline)
    {
        this.fontName = fontName;
        this.fontSize = fontSize;
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

    public float fontSize()
    {
        return fontSize;
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

    public Color glowColor()
    {
        return glowColor;
    }

    public float glowWidth()
    {
        return glowWidth;
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

    public TextSettings fontSize(float fontSize)
    {
        this.fontSize = fontSize;
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

    public TextSettings glowColor(Color glowColor)
    {
        this.glowColor = glowColor;
        return this;
    }

    public TextSettings glowWidth(float glowWidth)
    {
        this.glowWidth = glowWidth;
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
        return Float.compare(that.fontSize, fontSize) == 0 && Float.compare(that.outlineWidth, outlineWidth) == 0 && Float.compare(that.glowWidth, glowWidth) == 0 && Float.compare(that.lineSpacingMultiplier, lineSpacingMultiplier) == 0 && strikethrough == that.strikethrough && italic == that.italic && bold == that.bold && underline == that.underline && Objects.equals(fontName, that.fontName) && Objects.equals(textColor, that.textColor) && Objects.equals(shadowColor, that.shadowColor) && outlineColor.equals(that.outlineColor) && glowColor.equals(that.glowColor);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(fontName, fontSize, textColor, shadowColor, outlineColor, outlineWidth, glowColor, glowWidth, lineSpacingMultiplier, strikethrough, italic, bold, underline);
    }

    @Override
    public String toString()
    {
        return "TextSettings{" +
                "fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                ", textColor=" + textColor +
                ", shadowColor=" + shadowColor +
                ", outlineColor=" + outlineColor +
                ", outlineWidth=" + outlineWidth +
                ", glowColor=" + glowColor +
                ", glowWidth=" + glowWidth +
                ", lineSpacingMultiplier=" + lineSpacingMultiplier +
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
        private float  fontSize;
        private Color  textColor;
        private Color  shadowColor;

        private Color outlineColor;
        private float outlineWidth;

        private Color glowColor;
        private float glowWidth;

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

        public Builder fontSize(float fontSize)
        {
            this.fontSize = fontSize;
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

        public Builder glowColor(Color glowColor)
        {
            this.glowColor = glowColor;
            return this;
        }

        public Builder glowWidth(float glowWidth)
        {
            this.glowWidth = glowWidth;
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
            return new TextSettings(fontName, fontSize, textColor, shadowColor, outlineColor, outlineWidth, glowColor, glowWidth, lineSpacingMultiplier, strikethrough, italic, bold, underline);
        }
    }
}
