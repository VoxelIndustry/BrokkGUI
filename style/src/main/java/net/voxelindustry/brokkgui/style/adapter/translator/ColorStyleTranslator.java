package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkcolor.ColorConstants;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import java.text.NumberFormat;
import java.util.regex.Pattern;

public class ColorStyleTranslator implements IStyleTranslator<Color>
{
    private final NumberFormat colorFormat;

    private final Pattern hexColorPattern      = Pattern.compile("^#\\d{6}");
    private final Pattern hexAlphaColorPattern = Pattern.compile("^#\\d{6}\\s+\\d{2}%");
    private final Pattern rgbColorPattern      = Pattern.compile("^rgb\\((\\s?\\d+\\s?,\\s?){2}(\\s?\\d+\\s?)\\)");
    private final Pattern rgbaColorPattern     = Pattern.compile("^rgba\\((\\s?\\d+\\s?,\\s?){3}(\\s?\\d+\\s?)\\)");

    public ColorStyleTranslator()
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
                        colorValue = Float.parseFloat(value.substring(0, value.length() - 1)) / 100f;
                    else
                        colorValue = Integer.parseInt(value) / 255f;
                    if (i == 0)
                        color.setRed(colorValue);
                    else if (i == 1)
                        color.setGreen(colorValue);
                    else if (i == 2)
                        color.setBlue(colorValue);
                }
                else if (alpha)
                    color.setAlpha(Float.parseFloat(value));
                i++;
            }
            return color;
        }

        if (style.contains(" "))
        {
            String[] split = style.split(" ");
            String colorName = split[0];
            float alpha = Float.parseFloat(split[1].substring(0, split[1].length() - 1)) / 100;

            if (ColorConstants.hasConstant(colorName))
            {
                Color color = ColorConstants.getColor(colorName).copy();
                color.setAlpha(alpha);
                return color;
            }
        }
        else if (ColorConstants.hasConstant(style))
            return ColorConstants.getColor(style);
        throw new RuntimeException("Cannot retrieve specified Color constant. constant=" + style);
    }

    @Override
    public int validate(String style)
    {
        if (hexColorPattern.matcher(style).matches())
        {
            if (hexAlphaColorPattern.matcher(style).matches())
                return style.substring(0, style.indexOf("%") + 1).length();
            return 7;
        }
        if (!rgbColorPattern.matcher(style).matches() && !rgbaColorPattern.matcher(style).matches())
            return 0;
        return style.substring(0, style.indexOf(")") + 1).length();
    }
}
