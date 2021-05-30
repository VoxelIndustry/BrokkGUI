package net.voxelindustry.brokkgui.mock;

import net.voxelindustry.brokkgui.internal.ITextHelper;
import net.voxelindustry.brokkgui.text.TextSettings;
import org.apache.commons.lang3.StringUtils;

public class MockTextHelper implements ITextHelper
{
    @Override
    public String trimStringToWidth(String text, float width, TextSettings settings)
    {
        return text;
    }

    @Override
    public String trimStringToWidth(String text, float width, String ellipsis, TextSettings settings)
    {
        return text;
    }

    @Override
    public float getStringWidth(String text, TextSettings settings)
    {
        return text.length();
    }

    @Override
    public float getStringWidthMultiLine(String text, TextSettings settings)
    {
        return text.length() / (float) StringUtils.countMatches(text, '\n');
    }

    @Override
    public float getStringHeight(TextSettings settings)
    {
        return 9;
    }

    @Override
    public float getStringHeightMultiLine(String text, TextSettings settings)
    {
        return 9 * StringUtils.countMatches(text, '\n');
    }

    @Override
    public float getDefaultFontSize()
    {
        return 9;
    }
}
