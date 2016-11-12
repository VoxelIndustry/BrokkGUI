package org.yggard.brokkgui.paint;

public class Color
{
    public static final Color ALPHA      = new Color(0, 0, 0, 0);
    public static final Color GRAY       = new Color(.5f, .5f, .5f);
    public static final Color LIGHT_GRAY = new Color(.827f, .827f, .827f);
    public static final Color RED        = new Color(1, 0, 0);
    public static final Color GREEN      = new Color(0, 1, 0);
    public static final Color YELLOW     = new Color(1, 1, 0);
    public static final Color BLUE       = new Color(0, 0, 1);
    public static final Color AQUA       = new Color(0, 1, 1);
    public static final Color WHITE      = new Color(1, 1, 1);
    public static final Color BLACK      = new Color(0, 0, 0);

    public static Color fromHex(final String hex)
    {
        return hex.startsWith("#") ? new Color(Integer.valueOf(hex.substring(1, 3), 16) / 255.0F,
                Integer.valueOf(hex.substring(3, 5), 16) / 255.0F, Integer.valueOf(hex.substring(5, 7)) / 255.0F)
                : new Color(Integer.valueOf(hex.substring(0, 2), 16) / 255.0F,
                        Integer.valueOf(hex.substring(2, 4), 16) / 255.0F,
                        Integer.valueOf(hex.substring(4, 6)) / 255.0F);
    }

    public static Color fromInt(final int rgb)
    {
        return new Color(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF);
    }

    public int toInt()
    {
        int rtn = (int) (this.getRed() * 255);
        rtn = (int) ((rtn << 8) + this.getGreen() * 255);
        rtn = (int) ((rtn << 8) + this.getBlue() * 255);
        return rtn;
    }

    public static Color from(final Color c)
    {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    private float red, blue, green, alpha;

    public Color(final float red, final float green, final float blue, final float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(final float red, final float green, final float blue)
    {
        this(red, green, blue, 1);
    }

    public Color addAlpha(final float alpha)
    {
        final Color rtn = Color.from(this);
        rtn.setAlpha(alpha + rtn.getAlpha());
        return rtn;
    }

    public Color addGreen(final float green)
    {
        final Color rtn = Color.from(this);
        rtn.setGreen(green + rtn.getGreen());
        return rtn;
    }

    public Color addBlue(final float blue)
    {
        final Color rtn = Color.from(this);
        rtn.setBlue(blue + rtn.getBlue());
        return rtn;
    }

    public Color addRed(final float red)
    {
        final Color rtn = Color.from(this);
        rtn.setRed(red + rtn.getRed());
        return rtn;
    }

    public Color shade(final float shading)
    {
        final Color rtn = Color.from(this);
        rtn.setRed(rtn.getRed() - shading);
        rtn.setGreen(rtn.getGreen() - shading);
        rtn.setBlue(rtn.getBlue() - shading);
        return rtn;
    }

    public float getRed()
    {
        return this.red;
    }

    public float getBlue()
    {
        return this.blue;
    }

    public float getGreen()
    {
        return this.green;
    }

    public float getAlpha()
    {
        return this.alpha;
    }

    public void setRed(final float red)
    {
        this.red = red;
    }

    public void setBlue(final float blue)
    {
        this.blue = blue;
    }

    public void setGreen(final float green)
    {
        this.green = green;
    }

    public void setAlpha(final float alpha)
    {
        this.alpha = alpha;
    }

    @Override
    public String toString()
    {
        return "Color [red=" + this.red + ", blue=" + this.blue + ", green=" + this.green + ", alpha=" + this.alpha
                + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(this.alpha);
        result = prime * result + Float.floatToIntBits(this.blue);
        result = prime * result + Float.floatToIntBits(this.green);
        result = prime * result + Float.floatToIntBits(this.red);
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final Color other = (Color) obj;
        if (Float.floatToIntBits(this.alpha) != Float.floatToIntBits(other.alpha))
            return false;
        if (Float.floatToIntBits(this.blue) != Float.floatToIntBits(other.blue))
            return false;
        if (Float.floatToIntBits(this.green) != Float.floatToIntBits(other.green))
            return false;
        if (Float.floatToIntBits(this.red) != Float.floatToIntBits(other.red))
            return false;
        return true;
    }
}