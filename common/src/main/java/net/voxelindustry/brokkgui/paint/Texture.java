package net.voxelindustry.brokkgui.paint;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Texture texture = (Texture) o;

        return Float.compare(texture.uMin, uMin) == 0 && Float.compare(texture.vMin, vMin) == 0 && Float.compare
                (texture.uMax, uMax) == 0 && Float.compare(texture.vMax, vMax) == 0 && (getResource() != null ?
                getResource().equals(texture.getResource()) : texture.getResource() == null);
    }

    @Override
    public int hashCode()
    {
        int result = getResource() != null ? getResource().hashCode() : 0;
        result = 31 * result + (uMin != +0.0f ? Float.floatToIntBits(uMin) : 0);
        result = 31 * result + (vMin != +0.0f ? Float.floatToIntBits(vMin) : 0);
        result = 31 * result + (uMax != +0.0f ? Float.floatToIntBits(uMax) : 0);
        return 31 * result + (vMax != +0.0f ? Float.floatToIntBits(vMax) : 0);
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
                "} " + super.toString();
    }
}