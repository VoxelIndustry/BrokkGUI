package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.debug.hierarchy.AccordionItem;

import java.util.Objects;

public class DebugHierarchy implements AccordionItem
{
    private String  name;
    private float   scrollY;
    private boolean isCollapsed;
    private float   headerPos;

    public DebugHierarchy(String name, float scrollY, boolean isCollapsed, float headerPos)
    {
        this.name = name;
        this.scrollY = scrollY;
        this.isCollapsed = isCollapsed;
        this.headerPos = headerPos;
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

    @Override
    public boolean isCollapsed()
    {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed)
    {
        isCollapsed = collapsed;
    }

    @Override
    public float getHeaderPos()
    {
        return headerPos;
    }

    @Override
    public void setHeaderPos(float headerPos)
    {
        this.headerPos = headerPos;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DebugHierarchy that = (DebugHierarchy) o;
        return Float.compare(that.scrollY, scrollY) == 0 &&
                isCollapsed == that.isCollapsed &&
                Float.compare(that.headerPos, headerPos) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, scrollY, isCollapsed, headerPos);
    }

    @Override
    public String toString()
    {
        return "DebugHierarchy{" +
                "name='" + name + '\'' +
                ", scrollY=" + scrollY +
                ", isOpen=" + isCollapsed +
                ", headerPosY=" + headerPos +
                '}';
    }

    @Override
    public float getHeaderSize()
    {
        return 14;
    }

    @Override
    public float getMinimalSize()
    {
        return getHeaderSize() * 3;
    }
}
