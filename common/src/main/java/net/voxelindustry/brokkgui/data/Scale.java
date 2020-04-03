package net.voxelindustry.brokkgui.data;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;

import java.util.Optional;

public class Scale
{
    private float x;
    private float y;
    private float z;

    private Optional<Position> pivot;

    public Scale(float x, float y, float z, Position pivot)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pivot = Optional.ofNullable(pivot);
    }

    public Scale(float x, float y, Position pivot)
    {
        this(x, y, y, pivot);
    }

    public Scale(float x, float y, float z)
    {
        this(x, y, z, null);
    }

    public Scale(float x, float y)
    {
        this(x, y, y);
    }

    public boolean apply(IGuiRenderer renderer, GuiNode node, boolean createdMatrix)
    {
        if (x == 1 && y == 1 && z == 1)
            return false;

        if (!createdMatrix)
            renderer.beginMatrix();

        renderer.translateMatrix(
                node.getLeftPos() + pivot.map(pos -> pos.getX(node)).orElse(0F),
                node.getTopPos() + pivot.map(pos -> pos.getY(node)).orElse(0F),
                0
        );

        renderer.scaleMatrix(x, y, z);

        renderer.translateMatrix(
                -(node.getLeftPos() + pivot.map(pos -> pos.getX(node)).orElse(0F)),
                -(node.getTopPos() + pivot.map(pos -> pos.getY(node)).orElse(0F)),
                0
        );

        return true;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public Optional<Position> getPivot()
    {
        return pivot;
    }
}
