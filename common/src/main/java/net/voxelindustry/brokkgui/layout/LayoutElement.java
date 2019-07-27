package net.voxelindustry.brokkgui.layout;

public class LayoutElement
{
    private LayoutBox       area;
    private LayoutComponent owner;

    public LayoutElement(LayoutBox area, LayoutComponent owner)
    {
        this.area = area;
        this.owner = owner;
    }

    public LayoutBox getArea()
    {
        return area;
    }

    public LayoutComponent getOwner()
    {
        return owner;
    }
}
