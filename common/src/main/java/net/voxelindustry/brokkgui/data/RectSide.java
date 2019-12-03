package net.voxelindustry.brokkgui.data;

import net.voxelindustry.brokkgui.layout.LayoutAlignment;

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

    public LayoutAlignment toLayout()
    {
        switch (this)
        {
            case UP:
                return LayoutAlignment.CENTER_TOP;
            case DOWN:
                return LayoutAlignment.CENTER_BOTTOM;
            case LEFT:
                return LayoutAlignment.LEFT_CENTER;
            case RIGHT:
                return LayoutAlignment.RIGHT_CENTER;
            default:
                return LayoutAlignment.CENTER;
        }
    }

    public RectSide opposite()
    {
        switch (this)
        {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new RuntimeException("Unknown value of Enum RectSide " + this);
        }
    }
}