package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;

import java.text.NumberFormat;

public class ColorStyleTranslator implements IStyleDecoder<Color>, IStyleEncoder<Color>, IStyleValidator<Color>
{
    private final NumberFormat colorFormat;

    ColorStyleTranslator()
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

    @Override
    public Color decode(String style)
    {
        // Hexa Color ex: #FF0011 20%
        if (style.startsWith("#"))
        {
            if (!style.contains(" "))
                return Color.fromHex(style);
            else
            {
                String[] split = style.split(" ");
                String rgb = split[0];
                float alpha = Float.parseFloat(split[1].substring(0, split[1].length() - 1)) / 100;

                return Color.fromHex(rgb, alpha);
            }
        }
        // RGB or RGBA Color ex: rgba(255, 255, 255, 255)
        if (style.startsWith("rgb"))
        {
            boolean alpha = style.startsWith("rgba");

            String[] colorNames = style.substring(alpha ? 5 : 4, style.length() - 1).split(",");

            Color color = new Color(1, 1, 1);
            int i = 0;
            for (String value : colorNames)
            {
                if (i != 3)
                {
                    float colorValue;
                    value = value.trim();
                    if (value.endsWith("%"))
                        colorValue = Float.valueOf(value.substring(0, value.length() - 1)) / 100f;
                    else
                        colorValue = Integer.valueOf(value) / 255f;
                    if (i == 0)
                        color.setRed(colorValue);
                    else if (i == 1)
                        color.setGreen(colorValue);
                    else if (i == 2)
                        color.setBlue(colorValue);
                }
                else if (alpha)
                    color.setAlpha(Float.valueOf(value));
                i++;
            }
            return color;
        }
        if (ColorConstants.hasConstant(style))
            return ColorConstants.getColor(style);
        return null;
    }

    @Override
    public int validate(String style)
    {
        if (style.matches("^#\\d{6}"))
        {
            if (style.matches("^#\\d{6}\\s+\\d{2}%"))
                return style.substring(0, style.indexOf("%") + 1).length();
            return 7;
        }
        if (!style.matches("^rgb\\((\\s?\\d+\\s?,\\s?){2}(\\s?\\d+\\s?)\\)") && !style.matches("^rgba\\((\\s?\\d+\\s" +
                "?,\\s?){3}(\\s?\\d+\\s?)\\)"))
            return 0;
        return style.substring(0, style.indexOf(")") + 1).length();
    }
}
