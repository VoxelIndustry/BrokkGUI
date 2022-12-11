package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkcolor.ColorConstants;
import net.voxelindustry.brokkcolor.ColorLike;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ColorStyleTranslator implements IStyleTranslator<ColorLike>
{
    private final NumberFormat colorFormat;

    private final Pattern hexColorPattern      = Pattern.compile("^#\\p{XDigit}{6}");
    private final Pattern hexAlphaColorPattern = Pattern.compile("^#(\\p{XDigit}{6})\\s+(\\d{2,3})%");
    private final Pattern rgbColorPattern      = Pattern.compile("^rgb\\(\\s?(\\d+%?)\\s?,\\s?(\\d+%?)\\s?,\\s?(\\d+%?)\\s?\\)");
    private final Pattern rgbaColorPattern     = Pattern.compile("^rgba\\(\\s?(\\d+%?)\\s?,\\s?(\\d+%?)\\s?,\\s?(\\d+%?)\\s?,\\s?(\\d+%?)\\s?\\)");

    private final Pattern colorConstantPattern = Pattern.compile("^(\\w+)\\s?(\\d+)?%?");

    public ColorStyleTranslator()
    {
        colorFormat = NumberFormat.getInstance();
        colorFormat.setMinimumFractionDigits(0);
        colorFormat.setMaximumFractionDigits(1);
    }

    @Override
    public String encode(ColorLike value, boolean prettyPrint)
    {
        var colorFloat = value.toColorFloat();
        StringBuilder builder = new StringBuilder();

        if (prettyPrint)
        {
            builder.append(colorFloat.toHex())
                    .append(" (")
                    .append(colorFormat.format(colorFloat.getRed() * 255))
                    .append(",")
                    .append(colorFormat.format(colorFloat.getGreen() * 255))
                    .append(",")
                    .append(colorFormat.format(colorFloat.getBlue() * 255))
                    .append(",")
                    .append(colorFormat.format(colorFloat.getAlpha() * 255))
                    .append(")");
        }
        else
        {
            builder.append(colorFloat.toHex())
                    .append(" ")
                    .append(colorFloat.getAlpha() * 100)
                    .append("%");
        }
        return builder.toString();
    }

    @Override
    public Color decode(String style, AtomicInteger consumedLength)
    {
        // Hex Color ex: #FF0011 20%
        if (style.startsWith("#"))
        {
            var hexAlphaMatch = hexAlphaColorPattern.matcher(style);

            if (!hexAlphaMatch.matches())
            {
                if (consumedLength != null)
                    consumedLength.set(7);
                return Color.fromHex(style);
            }
            else
            {
                var rgb = hexAlphaMatch.group(1);
                float alpha = Float.parseFloat(hexAlphaMatch.group(2)) / 100;

                if (consumedLength != null)
                    consumedLength.set(hexAlphaMatch.regionEnd());
                return Color.fromHex(rgb, alpha);
            }
        }
        // RGB or RGBA Color ex: rgba(255, 255, 255, 255)
        var rgbMatcher = rgbColorPattern.matcher(style);
        if (rgbMatcher.matches())
        {
            var color = new Color();
            for (int i = 0; i < 3; i++)
            {
                var value = rgbMatcher.group(1 + i);
                float colorValue;
                if (value.endsWith("%"))
                    colorValue = Float.parseFloat(value.substring(0, value.length() - 1)) / 100F;
                else
                    colorValue = Integer.parseInt(value) / 255F;
                color.set(i, colorValue);
            }

            if (consumedLength != null)
                consumedLength.set(rgbMatcher.regionEnd());
            return color;
        }
        var rgbaMatcher = rgbaColorPattern.matcher(style);
        if (rgbaMatcher.matches())
        {
            var color = new Color();
            for (int i = 0; i < 4; i++)
            {
                var value = rgbaMatcher.group(1 + i);
                float colorValue;
                if (value.endsWith("%"))
                    colorValue = Float.parseFloat(value.substring(0, value.length() - 1)) / 100F;
                else
                    colorValue = Integer.parseInt(value) / 255F;
                color.set(i, colorValue);
            }

            if (consumedLength != null)
                consumedLength.set(rgbaMatcher.regionEnd());
            return color;
        }

        var colorConstantMatcher = colorConstantPattern.matcher(style);
        if (colorConstantMatcher.matches())
        {
            var colorName = colorConstantMatcher.group(1);

            if (!ColorConstants.hasConstant(colorName))
                throw new IllegalArgumentException("Cannot retrieve specified Color constant. constant=" + style);

            var alpha = 1F;
            var alphaPart = colorConstantMatcher.group(2);
            if (alphaPart != null)
                alpha = Float.parseFloat(alphaPart) / 100F;

            if (consumedLength != null)
                consumedLength.set(colorConstantMatcher.regionEnd());
            var color = ColorConstants.getColor(colorName).copy();
            color.setAlpha(alpha);
            return color;
        }
        throw new UnsupportedOperationException("Cannot parse Color from style. style=" + style);
    }
}
