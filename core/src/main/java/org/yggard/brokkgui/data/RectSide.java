package org.yggard.brokkgui.data;

/**
 * @author Ourten 15 oct. 2016
 */
public enum RectSide
{
    UP, DOWN, LEFT, RIGHT;

    public boolean isVertical()
    {
        return this == UP || this == DOWN;
    }

    public boolean isHorizontal()
    {
        return this == LEFT || this == RIGHT;
    }
}