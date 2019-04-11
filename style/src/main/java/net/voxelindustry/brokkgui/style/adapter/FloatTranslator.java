package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.util.StringCountUtils;

public class FloatTranslator implements IStyleDecoder<Float>, IStyleEncoder<Float>, IStyleValidator<Float>
{
    @Override
    public String encode(Float value, boolean prettyPrint)
    {
        return value.toString();
    }

    @Override
    public Float decode(String style)
    {
        if (style.contains("%"))
            return Float.valueOf(style.replace('%', '\0')) / 100;
        return Float.valueOf(style);
    }

    @Override
    public int validate(String style)
    {
        int floatLength = StringCountUtils.floatAtStart(style);

        if (floatLength == 0)
            return 0;

        if (floatLength < style.length() && style.charAt(floatLength) == '%')
            return floatLength + 1;
        return floatLength;
    }
}
