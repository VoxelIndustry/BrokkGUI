package net.voxelindustry.brokkgui.animation;

public class Interpolators
{
    public static final Interpolator DISCRETE = delta -> delta == 1F ? 1 : 0;

    public static final Interpolator LINEAR = delta -> delta;

    public static final Interpolator QUAD_BOTH    = delta -> powBoth(delta, 2);
    public static final Interpolator CUBIC_BOTH   = delta -> powBoth(delta, 3);
    public static final Interpolator QUARTIC_BOTH = delta -> powBoth(delta, 4);
    public static final Interpolator QUINTIC_BOTH = delta -> powBoth(delta, 5);

    public static final Interpolator QUAD_IN    = delta -> powIn(delta, 2);
    public static final Interpolator CUBIC_IN   = delta -> powIn(delta, 3);
    public static final Interpolator QUARTIC_IN = delta -> powIn(delta, 4);
    public static final Interpolator QUINTIC_IN = delta -> powIn(delta, 5);

    public static final Interpolator QUAD_OUT    = delta -> powOut(delta, 2);
    public static final Interpolator CUBIC_OUT   = delta -> powOut(delta, 3);
    public static final Interpolator QUARTIC_OUT = delta -> powOut(delta, 4);
    public static final Interpolator QUINTIC_OUT = delta -> powOut(delta, 5);

    public static final Interpolator SINE_BOTH = delta -> (float) ((1 - Math.cos(delta * Math.PI)) / 2);
    public static final Interpolator SINE_IN   = delta -> (float) ((1 - Math.cos(delta * Math.PI / 2)));
    public static final Interpolator SINE_OUT  = delta -> (float) (Math.sin(delta * Math.PI / 2));

    public static final Interpolator EXP_BOTH = delta ->
    {
        if (delta < 0.5f)
            return (float) (Math.pow(2, 10 * (delta - 0.5f)) / 2);
        delta -= 0.5f;
        return (float) ((-Math.pow(2, -10 * delta) + 2) / 2);
    };

    public static final Interpolator EXP_IN  = delta -> (float) Math.pow(2, 10 * (delta - 1));
    public static final Interpolator EXP_OUT = delta -> (float) -Math.pow(2, -10 * delta) + 1;

    public static final Interpolator CIRC_BOTH = delta ->
    {
        if (delta < 0.5f)
            return (float) (-(Math.sqrt(1 - (delta - 0.5f) * (delta - 0.5f)) - 1) / 2);
        return (float) ((Math.sqrt(1 - (delta - 1) * (delta - 1)) + 1) / 2);
    };
    public static final Interpolator CIRC_IN   = delta -> (float) -(Math.sqrt(1 - delta * delta) - 1);
    public static final Interpolator CIRC_OUT  = delta -> (float) (Math.sqrt(1 - (delta - 1) * (delta - 1)));


    public static float powIn(float value, int power)
    {
        return internalPow(value, power);
    }

    public static float powOut(float value, int power)
    {
        return internalPow(value - 1, power) * (power % 2 == 0 ? -1 : 1) + 1;
    }

    public static float powBoth(float value, int power)
    {
        if (value <= 0.5f)
            return internalPow(value * 2, power) / 2;

        return internalPow((value - 1) * 2, power) / (power % 2 == 0 ? -2 : 2) + 1;
    }

    private static float internalPow(float value, int power)
    {
        if (power == 2)
            return value * value;
        return internalPow(value * value, power - 1);
    }

    public static Interpolator fromName(String interpolatorName) throws RuntimeException
    {
        switch (interpolatorName.toUpperCase())
        {
            case "DISCRETE":
                return DISCRETE;
            case "LINEAR":
                return LINEAR;
            case "QUAD_BOTH":
                return QUAD_BOTH;
            case "CUBIC_BOTH":
                return CUBIC_BOTH;
            case "QUARTIC_BOTH":
                return QUARTIC_BOTH;
            case "QUINTIC_BOTH":
                return QUINTIC_BOTH;
            case "QUAD_IN":
                return QUAD_IN;
            case "CUBIC_IN":
                return CUBIC_IN;
            case "QUARTIC_IN":
                return QUARTIC_IN;
            case "QUINTIC_IN":
                return QUINTIC_IN;
            case "QUAD_OUT":
                return QUAD_OUT;
            case "CUBIC_OUT":
                return CUBIC_OUT;
            case "QUARTIC_OUT":
                return QUARTIC_OUT;
            case "QUINTIC_OUT":
                return QUINTIC_OUT;
            case "SINE_BOTH":
                return SINE_BOTH;
            case "SINE_IN":
                return SINE_IN;
            case "SINE_OUT":
                return SINE_OUT;
            case "EXP_BOTH":
                return EXP_BOTH;
            case "EXP_IN":
                return EXP_IN;
            case "EXP_OUT":
                return EXP_OUT;
            case "CIRC_BOTH":
                return CIRC_BOTH;
            case "CIRC_IN":
                return CIRC_IN;
            case "CIRC_OUT":
                return CIRC_OUT;
        }
        throw new RuntimeException("Unknown interpolator [" + interpolatorName + "]");
    }
}
