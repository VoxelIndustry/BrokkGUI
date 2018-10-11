package net.voxelindustry.brokkgui.data;

public class Vector2i
{
    private int x, y;

    public Vector2i(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2i(final Vector2i from)
    {
        this(from.x, from.y);
    }

    public int getX()
    {
        return this.x;
    }

    public void setX(final int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setY(final int y)
    {
        this.y = y;
    }
}