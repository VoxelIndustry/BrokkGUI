package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.util.StringCountUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public class DoubleTranslator implements IStyleDecoder<Double>, IStyleEncoder<Double>, IStyleValidator<Double>
{
    private final Pattern percentPattern = Pattern.compile("^\\d*\\.?\\d*%");

    private final DecimalFormat format = new DecimalFormat("0.0");

    @Override
    public String encode(Double value, boolean prettyPrint)
    {
        return value.toString();
    }

    @Override
    public Double decode(String style)
    {
        double value;

        try
        {
            value = format.parse(style).doubleValue();
        } catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }

        if (percentPattern.matcher(style).matches())
            return value / 100;
        return value;
    }

    @Override
    public int validate(String style)
    {
        int doubleLength = StringCountUtils.floatAtStart(style);

        if (doubleLength == 0)
            return 0;

        if (doubleLength < style.length() && style.charAt(doubleLength) == '%')
            return doubleLength + 1;
        return doubleLength;
    }
}
