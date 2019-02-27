package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiNode;

import java.util.concurrent.TimeUnit;

public class ScaleTransition extends Transition
{
    private Float startX, startY, startZ;
    private Float endX, endY, endZ;

    private float fromX, fromY, fromZ;
    private float translateX, translateY, translateZ;

    public ScaleTransition(GuiNode node, long duration, TimeUnit unit)
    {
        super(node, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (getNode() == null)
            return;

        getNode().setScaleX(fromX + (endX - fromX) * interpolated);
        getNode().setScaleY(fromY + (endY - fromY) * interpolated);
        getNode().setScaleZ(fromZ + (endZ - fromZ) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startX == null)
            fromX = getNode().getScaleX();
        else
            fromX = startX;

        if (startY == null)
            fromY = getNode().getScaleY();
        else
            fromY = startY;

        if (startZ == null)
            fromZ = getNode().getScaleZ();
        else
            fromZ = startZ;

        if (endX == null)
            endX = fromX + translateX;
        if (endY == null)
            endY = fromY + translateY;
        if (endZ == null)
            endZ = fromZ + translateZ;
    }

    public Float getStartX()
    {
        return startX;
    }

    public void setStartX(Float startX)
    {
        this.startX = startX;
    }

    public Float getStartY()
    {
        return startY;
    }

    public void setStartY(Float startY)
    {
        this.startY = startY;
    }

    public Float getStartZ()
    {
        return startZ;
    }

    public void setStartZ(Float startZ)
    {
        this.startZ = startZ;
    }

    public Float getEndX()
    {
        return endX;
    }

    public void setEndX(Float endX)
    {
        this.endX = endX;
    }

    public Float getEndY()
    {
        return endY;
    }

    public void setEndY(Float endY)
    {
        this.endY = endY;
    }

    public Float getEndZ()
    {
        return endZ;
    }

    public void setEndZ(Float endZ)
    {
        this.endZ = endZ;
    }

    public float getTranslateX()
    {
        return translateX;
    }

    public void setTranslateX(float translateX)
    {
        this.translateX = translateX;
    }

    public float getTranslateY()
    {
        return translateY;
    }

    public void setTranslateY(float translateY)
    {
        this.translateY = translateY;
    }

    public float getTranslateZ()
    {
        return translateZ;
    }

    public void setTranslateZ(float translateZ)
    {
        this.translateZ = translateZ;
    }
}
