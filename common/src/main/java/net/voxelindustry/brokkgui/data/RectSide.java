package net.voxelindustry.brokkgui.data;

public enum RectSide
{
    UP("top"),
    DOWN("bottom"),
    LEFT("left"),
    RIGHT("right");

    private String cssString;

    RectSide(String cssString)
    {
        this.cssString = cssString;
    }

    public String getCssString()
    {
        return cssString;
    }

    public boolean isVertical()
    {
        return this == UP || this == DOWN;
    }

    public boolean isHorizontal()
    {
        return this == LEFT || this == RIGHT;
    }
}