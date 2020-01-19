package net.voxelindustry.brokkgui.data;

import net.voxelindustry.brokkgui.component.GuiNode;

public class Position
{
    private float x;
    private float y;

    private PositionType type;

    private Position(float x, float y, PositionType type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public static Position absolute(float x, float y)
    {
        return new Position(x, y, PositionType.ABSOLUTE);
    }

    public static Position relative(float x, float y)
    {
        return new Position(x, y, PositionType.RELATIVE);
    }

    public float getX(GuiNode node)
    {
        if (type == PositionType.RELATIVE)
            return node.getWidth() * x;
        return x;
    }

    public float getY(GuiNode node)
    {
        if (type == PositionType.RELATIVE)
            return node.getHeight() * y;
        return y;
    }
}
