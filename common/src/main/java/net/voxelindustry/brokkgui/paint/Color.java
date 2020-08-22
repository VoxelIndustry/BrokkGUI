package net.voxelindustry.brokkgui.paint;

public class Color extends GuiPaint
{
    public static final Color ALPHA      = new Color(0, 0, 0, 0);
    public static final Color BLACK      = ColorConstants.getColor("black");
    public static final Color AQUA       = ColorConstants.getColor("aqua");
    public static final Color WHITE      = ColorConstants.getColor("white");
    public static final Color RED        = ColorConstants.getColor("red");
    public static final Color LIGHT_GRAY = ColorConstants.getColor("lightgrey");
    public static final Color BLUE       = ColorConstants.getColor("blue");
    public static final Color GRAY       = ColorConstants.getColor("gray");
    public static final Color GREEN      = ColorConstants.getColor("green");
    public static final Color YELLOW     = ColorConstants.getColor("yellow");

    public static Color fromHex(String hex)
    {
        return Color.fromHex(hex, 1);
    }

    public static Color fromHex(String hex, float alpha)
    {
        Color rtn = new Color(0, 0, 0);

        int padding = hex.startsWith("#") ? 1 : 0;
        rtn.red = Integer.parseInt(hex.substring(padding, 2 + padding), 16) / 255.0F;
        rtn.green = Integer.parseInt(hex.substring(2 + padding, 4 + padding), 16) / 255.0F;
        rtn.blue = Integer.parseInt(hex.substring(4 + padding, 6 + padding), 16) / 255.0F;
        rtn.alpha = alpha;
        return rtn;
    }

    public String toHex()
    {
        return "#" + String.format("%02X", (int) (red * 255)) +
                String.format("%02X", (int) (green * 255)) +
                String.format("%02X", (int) (blue * 255));
    }

    public static Color fromRGBInt(int rgb)
    {
        return new Color((rgb >> 16 & 0xFF) / 255f, (rgb >> 8 & 0xFF) / 255f, (rgb & 0xFF) / 255f);
    }

    public static Color fromRGBAInt(int rgba)
    {
        return new Color((rgba >> 24 & 0xFF) / 255f, (rgba >> 16 & 0xFF) / 255f, (rgba >> 8 & 0xFF) / 255f, (rgba &
                0xFF) / 255f);
    }

    public int toRGBInt()
    {
        int rtn = 0;
        rtn |= (int) (getRed() * 255) << 16;
        rtn |= (int) (getGreen() * 255) << 8;
        rtn |= (int) (getBlue() * 255);

        return rtn;
    }

    public int toRGBAInt()
    {
        int rtn = 0;
        rtn |= (int) (getAlpha() * 255) << 24;
        rtn |= (int) (getRed() * 255) << 16;
        rtn |= (int) (getGreen() * 255) << 8;
        rtn |= (int) (getBlue() * 255);

        return rtn;
    }

    public static Color from(Color c)
    {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    private float red, blue, green, alpha;

    public Color(float red, float green, float blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(float red, float green, float blue)
    {
        this(red, green, blue, 1);
    }

    public Color addAlpha(float alpha)
    {
        Color rtn = Color.from(this);
        rtn.setAlpha(alpha + rtn.getAlpha());
        return rtn;
    }

    public Color addGreen(float green)
    {
        Color rtn = Color.from(this);
        rtn.setGreen(green + rtn.getGreen());
        return rtn;
    }

    public Color addBlue(float blue)
    {
        Color rtn = Color.from(this);
        rtn.setBlue(blue + rtn.getBlue());
        return rtn;
    }

    public Color addRed(float red)
    {
        Color rtn = Color.from(this);
        rtn.setRed(red + rtn.getRed());
        return rtn;
    }

    public Color shade(float shading)
    {
        Color rtn = Color.from(this);
        rtn.setRed(rtn.getRed() - shading);
        rtn.setGreen(rtn.getGreen() - shading);
        rtn.setBlue(rtn.getBlue() - shading);
        return rtn;
    }

    public Color interpolate(Color other, float delta)
    {
        if (delta >= 1)
            return other;
        if (delta <= 0)
            return this;

        return new Color(red + (other.red - red) * delta,
                green + (other.green - green) * delta,
                blue + (other.blue - blue) * delta,
                alpha + (other.alpha - alpha) * delta);
    }

    public float getRed()
    {
        return red;
    }

    public float getBlue()
    {
        return blue;
    }

    public float getGreen()
    {
        return green;
    }

    public float getAlpha()
    {
        return alpha;
    }

    public void setRed(float red)
    {
        this.red = red;
    }

    public void setBlue(float blue)
    {
        this.blue = blue;
    }

    public void setGreen(float green)
    {
        this.green = green;
    }

    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
    }

    public Color mixMultiply(Color color)
    {
        return new Color(
                mixMultiply(getRed(), color.getRed()),
                mixMultiply(getGreen(), color.getGreen()),
                mixMultiply(getBlue(), color.getBlue())
        );
    }

    public Color mixScreen(Color color)
    {
        return new Color(
                mixScreen(getRed(), color.getRed()),
                mixScreen(getGreen(), color.getGreen()),
                mixScreen(getBlue(), color.getBlue())
        );
    }

    public Color mixOverlay(Color color)
    {
        return new Color(
                mixOverlay(getRed(), color.getRed()),
                mixOverlay(getGreen(), color.getGreen()),
                mixOverlay(getBlue(), color.getBlue())
        );
    }

    @Override
    public String toString()
    {
        return "Color [red=" + red + ", blue=" + blue + ", green=" + green + ", alpha=" + alpha
                + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(alpha);
        result = prime * result + Float.floatToIntBits(blue);
        result = prime * result + Float.floatToIntBits(green);
        result = prime * result + Float.floatToIntBits(red);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Color other = (Color) obj;
        if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha))
            return false;
        if (Float.floatToIntBits(blue) != Float.floatToIntBits(other.blue))
            return false;
        if (Float.floatToIntBits(green) != Float.floatToIntBits(other.green))
            return false;
        return Float.floatToIntBits(red) == Float.floatToIntBits(other.red);
    }

    public static float mixMultiply(float a, float b)
    {
        return a * b;
    }

    public static float mixScreen(float a, float b)
    {
        return 1 - (1 - a) * (1 - b);
    }

    public static float mixOverlay(float a, float b)
    {
        return a < 0.5F ? 2 * a * b : 1 - 2 * (1 - a) * (1 - b);
    }
}