package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Color;

import java.text.NumberFormat;

public class ColorStyleEncoder implements IStyleEncoder<Color>
{
    private final NumberFormat colorFormat;

    ColorStyleEncoder()
    {
        colorFormat = NumberFormat.getInstance();
        colorFormat.setMinimumFractionDigits(0);
        colorFormat.setMaximumFractionDigits(1);
    }

    @Override
    public String encode(Color value, boolean prettyPrint)
    {
        StringBuilder builder = new StringBuilder();

        if (prettyPrint)
        {
            builder.append(value.toHex())
                    .append(" (")
                    .append(colorFormat.format(value.getRed() * 255))
                    .append(",")
                    .append(colorFormat.format(value.getGreen() * 255))
                    .append(",")
                    .append(colorFormat.format(value.getBlue() * 255))
                    .append(",")
                    .append(colorFormat.format(value.getAlpha() * 255))
                    .append(")");
        }
        else
        {
            builder.append(value.toHex())
                    .append(" ")
                    .append(value.getAlpha() * 100)
                    .append("%");
        }
        return builder.toString();
    }
}
