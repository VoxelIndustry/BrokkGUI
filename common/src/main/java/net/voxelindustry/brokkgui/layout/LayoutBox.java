package net.voxelindustry.brokkgui.layout;

import java.util.Objects;

public class LayoutBox
{
    private float posX, posY;
    private float width, height;

    public LayoutBox()
    {
    }

    public LayoutBox(float posX, float posY, float width, float height)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public float getPosX()
    {
        return posX;
    }

    public void setPosX(float posX)
    {
        this.posX = posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public void setPosY(float posY)
    {
        this.posY = posY;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LayoutBox layoutBox = (LayoutBox) o;
        return Float.compare(layoutBox.posX, posX) == 0 &&
                Float.compare(layoutBox.posY, posY) == 0 &&
                Float.compare(layoutBox.width, width) == 0 &&
                Float.compare(layoutBox.height, height) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(posX, posY, width, height);
    }

    @Override
    public String toString()
    {
        return "LayoutBox{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
