package org.yggard.brokkgui.style.adapter;

import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.ColorConstants;

public class ColorStyleAdapter implements IStyleAdapter<Color>
{
    @Override
    public Color decode(String style)
    {
        if (style.startsWith("#"))
            return Color.fromHex(style);
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
        if(ColorConstants.hasConstant(style))
            return ColorConstants.getColor(style);
        return null;
    }
}
