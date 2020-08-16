package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.property.Property;

public class Rotation
{
    public static final Rotation NONE = Rotation.build().fromCenter().angle(0).create();

    private final Property<Float>          angleProperty;
    private final Property<RotationOrigin> originProperty;

    private Rotation(float angle, RotationOrigin origin)
    {
        angleProperty = new Property<>(angle);
        originProperty = new Property<>(origin);
    }

    public Property<Float> getAngleProperty()
    {
        return angleProperty;
    }

    public Property<RotationOrigin> getOriginProperty()
    {
        return originProperty;
    }

    public float getAngle()
    {
        return getAngleProperty().getValue();
    }

    public void setAngle(float angle)
    {
        getAngleProperty().setValue(angle);
    }

    public RotationOrigin getOrigin()
    {
        return getOriginProperty().getValue();
    }

    public void setOrigin(RotationOrigin origin)
    {
        getOriginProperty().setValue(origin);
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
            origin = RotationOrigin.CENTER;
            return this;
        }

        public Builder from(float fromX, float fromY)
        {
            origin = new RotationOrigin(fromX, fromY, false);
            return this;
        }

        public Builder fromRelative(float fromX, float fromY)
        {
            origin = new RotationOrigin(fromX, fromY, true);
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
