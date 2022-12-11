package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;
import net.voxelindustry.brokkgui.util.PrimitivesParser;

import java.util.concurrent.atomic.AtomicInteger;

public class DoubleTranslator implements IStyleTranslator<Double>
{
    @Override
    public String encode(Double value, boolean prettyPrint)
    {
        return value.toString();
    }

    @Override
    public Double decode(String style, AtomicInteger consumedLength)
    {
        var length = validate(style);
        if (consumedLength != null)
            consumedLength.set(length);

        var serializedFloat = style.substring(0, length);
        if (serializedFloat.endsWith("%"))
            return Double.parseDouble(serializedFloat.substring(0, length - 1)) / 100;
        return Double.valueOf(style);
    }

    public int validate(String style)
    {
        int doubleLength = PrimitivesParser.floatLength(style);

        if (doubleLength == 0)
            return 0;

        if (doubleLength < style.length() && style.charAt(doubleLength) == '%')
            return doubleLength + 1;
        return doubleLength;
    }
}
