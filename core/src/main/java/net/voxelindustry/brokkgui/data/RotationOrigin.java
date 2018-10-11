package net.voxelindustry.brokkgui.data;

public class RotationOrigin
{
    public static final RotationOrigin CENTER = new RotationOrigin(0.5f, 0.5f, true);

    public static final RotationOrigin LEFT_TOP     = new RotationOrigin(0, 0, true);
    public static final RotationOrigin LEFT_BOTTOM  = new RotationOrigin(0, 1, true);
    public static final RotationOrigin RIGHT_TOP    = new RotationOrigin(1, 0, true);
    public static final RotationOrigin RIGHT_BOTTOM = new RotationOrigin(1, 1, true);


    private boolean relativePos;
    private float   originX, originY;

    RotationOrigin(float originX, float originY, boolean relativePos)
    {
        this.originX = originX;
        this.originY = originY;
        this.relativePos = relativePos;
    }

    public boolean isRelativePos()
    {
        return relativePos;
    }

    public float getOriginX()
    {
        return originX;
    }

    public float getOriginY()
    {
        return originY;
    }
}
