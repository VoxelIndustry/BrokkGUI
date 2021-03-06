package net.voxelindustry.brokkgui.animation;

import net.voxelindustry.brokkcolor.Color;

public class ValueInterpolators
{
    public static final ValueInterpolator<Color> COLOR    = ((delta, from, to) -> from.interpolate(to, delta, new Color()));
    public static final ValueInterpolator<Float> IDENTITY = (delta, from, to) -> delta;

    public static ValueInterpolator<Color> colorMutable(Color value)
    {
        return ((delta, from, to) -> from.interpolate(to, delta, value));
    }
}
