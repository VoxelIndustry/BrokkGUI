package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;
import net.voxelindustry.brokkgui.util.PrimitivesParser;

import java.util.concurrent.atomic.AtomicInteger;

public class FloatTranslator implements IStyleTranslator<Float>
{
    @Override
    public String encode(Float value, boolean prettyPrint)
    {
        return value.toString();
    }

    @Override
    public Float decode(String style, AtomicInteger consumedLength)
    {
        var length = validate(style);
        if (consumedLength != null)
            consumedLength.set(length);

        var serializedFloat = style.substring(0, length);
        if (serializedFloat.endsWith("%"))
            return Float.parseFloat(serializedFloat.substring(0, length - 1)) / 100;
        return Float.valueOf(style);
    }

    private int validate(String style)
    {
        int floatLength = PrimitivesParser.floatLength(style);

        if (floatLength == 0)
            return 0;

        if (floatLength < style.length() && style.charAt(floatLength) == '%')
            return floatLength + 1;
        return floatLength;
    }
}
