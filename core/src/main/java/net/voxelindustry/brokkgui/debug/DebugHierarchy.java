package net.voxelindustry.brokkgui.debug;

public class DebugHierarchy
{
    private String name;
    private float scrollY;
    private boolean isOpen;

    public DebugHierarchy(String name, float scrollY, boolean isOpen)
    {
        this.name = name;
        this.scrollY = scrollY;
        this.isOpen = isOpen;
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
}
