package net.voxelindustry.brokkgui.layout;

public enum LayoutAlignment {
    LEFT_TOP,
    LEFT_CENTER,
    LEFT_BOTTOM,
    CENTER_TOP,
    CENTER,
    CENTER_BOTTOM,
    RIGHT_TOP,
    RIGHT_CENTER,
    RIGHT_BOTTOM;

    public boolean isLeft()
    {
        return this == LEFT_TOP || this == LEFT_CENTER || this == LEFT_TOP;
    }

    public boolean isRight()
    {
        return this == RIGHT_TOP || this == RIGHT_CENTER || this == RIGHT_BOTTOM;
    }

    public boolean isHorizontalCenter()
    {
        return this == CENTER_TOP || this == CENTER_BOTTOM || this == CENTER;
    }

    public boolean isTop()
    {
        return this == LEFT_TOP || this == RIGHT_TOP || this == CENTER_TOP;
    }

    public boolean isBottom()
    {
        return this == LEFT_BOTTOM || this == RIGHT_BOTTOM || this == CENTER_BOTTOM;
    }

    public boolean isVerticalCenter()
    {
        return this == LEFT_CENTER || this == RIGHT_CENTER || this == CENTER;
    }
}
