package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.data.FillMethod;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class FillMethodStyleTranslator implements IStyleTranslator<FillMethod>
{
    private final String[] allowedValues = Arrays.stream(FillMethod.values())
            .map(value -> value.name().toLowerCase())
            .toArray(String[]::new);

    @Override
    public FillMethod decode(String style)
    {
        return FillMethod.valueOf(style.toUpperCase());
    }

    @Override
    public String encode(FillMethod value, boolean prettyPrint)
    {
        return value.name().toLowerCase();
    }

    @Override
    public int validate(String style)
    {
        if (ArrayUtils.contains(allowedValues, style.toLowerCase()))
            return style.length();
        return 0;
    }
}
