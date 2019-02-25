package net.voxelindustry.brokkgui.util;

public class MathUtils
{
    public static float clamp(float minValue, float maxValue, float toClamp)
    {
        return toClamp < minValue ? minValue : (toClamp > maxValue ? maxValue : toClamp);
    }
}
