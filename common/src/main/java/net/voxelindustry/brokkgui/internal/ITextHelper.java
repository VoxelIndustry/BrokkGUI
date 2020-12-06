package net.voxelindustry.brokkgui.internal;

public interface ITextHelper
{
    String trimStringToPixelWidth(String text, int pixelWidth);

    float getStringWidth(String text);

    float getStringWidthMultiLine(String text);

    float getStringHeight();

    default float getStringHeightMultiLine(String text)
    {
        return getStringHeightMultiLine(text, getDefaultLineSpacing());
    }

    float getStringHeightMultiLine(String text, float lineSpacing);

    default float getDefaultLineSpacing()
    {
        return getStringHeight() / 4;
    }
}
