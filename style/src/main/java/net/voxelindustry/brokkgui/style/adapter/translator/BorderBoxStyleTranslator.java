package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.border.BorderBox;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class BorderBoxStyleTranslator implements IStyleTranslator<BorderBox>
{
    private final String[] allowedValues = Arrays.stream(BorderBox.values())
            .map(value -> value.name().toLowerCase())
            .toArray(String[]::new);

    @Override
    public BorderBox decode(String style)
    {
        return BorderBox.valueOf(style.toUpperCase());
    }

    @Override
    public String encode(BorderBox value, boolean prettyPrint)
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
