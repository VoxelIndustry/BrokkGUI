package net.voxelindustry.brokkgui.debug;

import java.util.Objects;

public class DebugHierarchy
{
    private String name;
    private float scrollY;
    private boolean isOpen;
    private float headerPosY;

    public DebugHierarchy(String name, float scrollY, boolean isOpen, float headerPosY)
    {
        this.name = name;
        this.scrollY = scrollY;
        this.isOpen = isOpen;
        this.headerPosY = headerPosY;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public float getScrollY()
    {
        return scrollY;
    }

    public void setScrollY(float scrollY)
    {
        this.scrollY = scrollY;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public void setOpen(boolean open)
    {
        isOpen = open;
    }

    public float getHeaderPosY()
    {
        return headerPosY;
    }

    public void setHeaderPosY(float headerPosY)
    {
        this.headerPosY = headerPosY;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DebugHierarchy that = (DebugHierarchy) o;
        return Float.compare(that.scrollY, scrollY) == 0 &&
                isOpen == that.isOpen &&
                Float.compare(that.headerPosY, headerPosY) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, scrollY, isOpen, headerPosY);
    }

    @Override
    public String toString()
    {
        return "DebugHierarchy{" +
                "name='" + name + '\'' +
                ", scrollY=" + scrollY +
                ", isOpen=" + isOpen +
                ", headerPosY=" + headerPosY +
                '}';
    }
}
