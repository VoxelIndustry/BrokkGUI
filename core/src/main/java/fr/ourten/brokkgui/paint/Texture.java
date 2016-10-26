package fr.ourten.brokkgui.paint;

public class Texture
{
    private final String resource;
    private final float  u, v, s, t;

    public Texture(final String resource, final float u, final float v, final float s, final float t)
    {
        super();
        this.resource = resource;
        this.u = u;
        this.v = v;
        this.s = s;
        this.t = t;
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

    public float getU()
    {
        return this.u;
    }

    public float getV()
    {
        return this.v;
    }

    public float getS()
    {
        return this.s;
    }

    public float getT()
    {
        return this.t;
    }

    @Override
    public String toString()
    {
        return "Texture [resource=" + this.resource + ", u=" + this.u + ", v=" + this.v + ", s=" + this.s + ", t="
                + this.t + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.resource == null ? 0 : this.resource.hashCode());
        result = prime * result + Float.floatToIntBits(this.s);
        result = prime * result + Float.floatToIntBits(this.t);
        result = prime * result + Float.floatToIntBits(this.u);
        result = prime * result + Float.floatToIntBits(this.v);
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
        final Texture other = (Texture) obj;
        if (this.resource == null)
        {
            if (other.resource != null)
                return false;
        }
        else if (!this.resource.equals(other.resource))
            return false;
        if (Float.floatToIntBits(this.s) != Float.floatToIntBits(other.s))
            return false;
        if (Float.floatToIntBits(this.t) != Float.floatToIntBits(other.t))
            return false;
        if (Float.floatToIntBits(this.u) != Float.floatToIntBits(other.u))
            return false;
        if (Float.floatToIntBits(this.v) != Float.floatToIntBits(other.v))
            return false;
        return true;
    }
}