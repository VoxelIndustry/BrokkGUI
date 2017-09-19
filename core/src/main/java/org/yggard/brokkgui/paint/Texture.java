package org.yggard.brokkgui.paint;

public class Texture extends GuiPaint
{
    public static final Texture EMPTY = new Texture();

    private final String resource;
    private final float  uMin, vMin, uMax, vMax;

    public Texture(final String resource, final float uMin, final float vMin, final float uMax, final float vMax)
    {
        super();
        this.resource = resource;
        this.uMin = uMin;
        this.vMin = vMin;
        this.uMax = uMax;
        this.vMax = vMax;
    }

    public Texture(final String resource, final float u, final float v)
    {
        this(resource, u, v, 1, 1);
    }

    public Texture(final String resource)
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
}