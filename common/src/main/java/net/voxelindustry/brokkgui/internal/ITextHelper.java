package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.text.TextSettings;

public interface ITextHelper
{
    String trimStringToWidth(String text, float width, TextSettings settings);

    String trimStringToWidth(String text, float width, String ellipsis, TextSettings settings);

    float getStringWidth(String text, TextSettings settings);

    float getStringWidthMultiLine(String text, TextSettings settings);

    float getStringHeight(TextSettings settings);

    float getStringHeightMultiLine(String text, TextSettings settings);

    float getDefaultFontSize();
}
