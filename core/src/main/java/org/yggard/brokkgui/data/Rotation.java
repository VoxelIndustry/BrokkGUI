package org.yggard.brokkgui.data;

import fr.ourten.teabeans.value.BaseProperty;

public class Rotation
{
    public static final Rotation NONE = Rotation.build().fromCenter().angle(0).create();

    private final BaseProperty<Float>          angleProperty;
    private final BaseProperty<RotationOrigin> originProperty;

    private Rotation(float angle, RotationOrigin origin)
    {
        this.angleProperty = new BaseProperty<>(angle, "angleProperty");
        this.originProperty = new BaseProperty<>(origin, "originProperty");
    }

    public BaseProperty<Float> getAngleProperty()
    {
        return angleProperty;
    }

    public BaseProperty<RotationOrigin> getOriginProperty()
    {
        return originProperty;
    }

    public float getAngle()
    {
        return this.getAngleProperty().getValue();
    }

    public void setAngle(float angle)
    {
        this.getAngleProperty().setValue(angle);
    }

    public RotationOrigin getOrigin()
    {
        return this.getOriginProperty().getValue();
    }

    public void setOrigin(RotationOrigin origin)
    {
        this.getOriginProperty().setValue(origin);
    }

    public static Builder build()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private float          angle;
        private RotationOrigin origin;

        Builder()
        {

        }

        public Builder from(RotationOrigin origin)
        {
            this.origin = origin;
            return this;
        }

        public Builder fromCenter()
        {
            this.origin = RotationOrigin.CENTER;
            return this;
        }

        public Builder from(float fromX, float fromY)
        {
            this.origin = new RotationOrigin(fromX, fromY, false);
            return this;
        }

        public Builder fromRelative(float fromX, float fromY)
        {
            this.origin = new RotationOrigin(fromX, fromY, true);
            return this;
        }

        public Builder angle(float angle)
        {
            this.angle = angle;
            return this;
        }

        public Rotation create()
        {
            if (origin != null)
                return new Rotation(angle, origin);
            throw new RuntimeException("A rotation origin must be specified!");
        }
    }
}
