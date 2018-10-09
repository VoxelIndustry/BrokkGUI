package org.yggard.brokkgui.data;

public enum RectAlignment
{
    LEFT_CENTER, RIGHT_CENTER, MIDDLE_CENTER, LEFT_UP, RIGHT_UP, MIDDLE_UP, LEFT_DOWN, MIDDLE_DOWN, RIGHT_DOWN;

    public boolean isVerticalCentered()
    {
        return this == LEFT_CENTER || this == RIGHT_CENTER || this == MIDDLE_CENTER;
    }

    public boolean isHorizontalCentered()
    {
        return this == MIDDLE_UP || this == MIDDLE_CENTER || this == MIDDLE_DOWN;
    }

    public boolean isLeft()
    {
        return this == LEFT_CENTER || this == LEFT_DOWN || this == LEFT_UP;
    }

    public boolean isRight()
    {
        return this == RIGHT_CENTER || this == RIGHT_DOWN || this == RIGHT_UP;
    }

    public boolean isUp()
    {
        return this == LEFT_UP || this == RIGHT_UP || this == MIDDLE_UP;
    }

    public boolean isDown()
    {
        return this == LEFT_DOWN || this == RIGHT_DOWN || this == MIDDLE_DOWN;
    }
}