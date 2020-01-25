package net.voxelindustry.brokkgui.sprite;

import net.voxelindustry.brokkgui.paint.GuiPaint;

import java.util.Objects;

public class Texture extends GuiPaint
{
    public static final Texture EMPTY = new Texture();

    private final String resource;
    private final float  uMin, vMin, uMax, vMax;

    private final int pixelWidth, pixelHeight;

    public Texture(String resource, float uMin, float vMin, float uMax, float vMax, int pixelWidth, int pixelHeight)
    {
        this.resource = resource;
        this.uMin = uMin;
        this.vMin = vMin;
        this.uMax = uMax;
        this.vMax = vMax;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    public Texture(String resource, float uMin, float vMin, float uMax, float vMax)
    {
        this(resource, uMin, vMin, uMax, vMax, 0, 0);
    }


    public Texture(String resource, float u, float v)
    {
        this(resource, u, v, 1, 1);
    }

    public Texture(String resource)
    {
        this(resource, 0, 0);
    }

    public Texture()
    {
        this(null);
    }

    public boolean isEmpty()
    {
        return this.resource == null;
    }

    public String getResource()
    {
        return this.resource;
    }

    public float getUMin()
    {
        return this.uMin;
    }

    public float getVMin()
    {
        return this.vMin;
    }

    public float getUMax()
    {
        return this.uMax;
    }

    public float getVMax()
    {
        return this.vMax;
    }

    public int getPixelWidth()
    {
        return pixelWidth;
    }

    public int getPixelHeight()
    {
        return pixelHeight;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Texture texture = (Texture) o;
        return Float.compare(texture.uMin, uMin) == 0 &&
                Float.compare(texture.vMin, vMin) == 0 &&
                Float.compare(texture.uMax, uMax) == 0 &&
                Float.compare(texture.vMax, vMax) == 0 &&
                pixelWidth == texture.pixelWidth &&
                pixelHeight == texture.pixelHeight &&
                Objects.equals(resource, texture.resource);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(resource, uMin, vMin, uMax, vMax, pixelWidth, pixelHeight);
    }

    @Override
    public String toString()
    {
        return "Texture{" +
                "resource='" + resource + '\'' +
                ", uMin=" + uMin +
                ", vMin=" + vMin +
                ", uMax=" + uMax +
                ", vMax=" + vMax +
                ", pixelWidth=" + pixelWidth +
                ", pixelHeight=" + pixelHeight +
                '}';
    }
}