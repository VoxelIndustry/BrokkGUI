package org.yggard.brokkgui.data;

public enum EAlignment
{
    LEFT_CENTER, RIGHT_CENTER, MIDDLE_CENTER, LEFT_UP, RIGHT_UP, MIDDLE_UP, LEFT_DOWN, MIDDLE_DOWN, RIGHT_DOWN;

    public boolean isVerticalCentered()
    {
        return this.equals(LEFT_CENTER) || this.equals(RIGHT_CENTER) || this.equals(MIDDLE_CENTER);
    }

    public boolean isHorizontalCentered()
    {
        return this.equals(MIDDLE_UP) || this.equals(MIDDLE_CENTER) || this.equals(MIDDLE_DOWN);
    }

    public boolean isLeft()
    {
        return this.equals(LEFT_CENTER) || this.equals(LEFT_DOWN) || this.equals(LEFT_UP);
    }

    public boolean isRight()
    {
        return this.equals(RIGHT_CENTER) || this.equals(RIGHT_DOWN) || this.equals(RIGHT_UP);
    }

    public boolean isUp()
    {
        return this.equals(LEFT_UP) || this.equals(RIGHT_UP) || this.equals(MIDDLE_UP);
    }

    public boolean isDown()
    {
        return this.equals(LEFT_DOWN) || this.equals(RIGHT_DOWN) || this.equals(MIDDLE_DOWN);
    }
}