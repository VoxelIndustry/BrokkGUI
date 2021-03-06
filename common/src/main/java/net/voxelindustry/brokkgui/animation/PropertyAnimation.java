package net.voxelindustry.brokkgui.animation;

import fr.ourten.teabeans.property.IProperty;

import java.util.concurrent.TimeUnit;

public class PropertyAnimation<T> extends Animation
{
    private final IProperty<T> property;

    private Interpolator interpolator;

    public PropertyAnimation(IProperty<T> property,
                             ValueInterpolator<T> valueInterpolator,
                             T startValue,
                             T endValue,
                             long duration,
                             TimeUnit unit)
    {
        super(duration, unit);

        this.property = property;

        interpolator = Interpolators.QUAD_BOTH;

        progressProperty().addListener(obs ->
                property.setValue(
                        valueInterpolator.interpolate(interpolator.apply(progress()), startValue, endValue)));
    }

    public Interpolator interpolator()
    {
        return interpolator;
    }

    public void interpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;
    }

    public IProperty<T> property()
    {
        return property;
    }

    public static <T> Builder<T> build()
    {
        return new Builder<>();
    }

    public static class Builder<T>
    {
        private IProperty<T> property;

        private Interpolator interpolator;

        private ValueInterpolator<T> valueInterpolator;

        private T startValue;
        private T endValue;

        private long     duration;
        private TimeUnit unit;

        private int     maxCycles;
        private boolean reverse;

        public Builder<T> property(IProperty<T> property)
        {
            this.property = property;
            return this;
        }

        public Builder<T> duration(long duration, TimeUnit unit)
        {
            this.duration = duration;
            this.unit = unit;
            return this;
        }

        public Builder<T> interpolator(Interpolator interpolator)
        {
            this.interpolator = interpolator;
            return this;
        }

        public Builder<T> valueInterpolator(ValueInterpolator<T> valueInterpolator)
        {
            this.valueInterpolator = valueInterpolator;
            return this;
        }

        public Builder<T> startValue(T startValue)
        {
            this.startValue = startValue;
            return this;
        }

        public Builder<T> endValue(T endValue)
        {
            this.endValue = endValue;
            return this;
        }

        public Builder<T> maxCycles(int maxCycles)
        {
            this.maxCycles = maxCycles;
            return this;
        }

        public Builder<T> reverse(boolean reverse)
        {
            this.reverse = reverse;
            return this;
        }

        public PropertyAnimation<T> create()
        {
            PropertyAnimation<T> animation = new PropertyAnimation<>(property, valueInterpolator, startValue, endValue, duration, unit);
            animation.interpolator(interpolator);
            animation.reverse(reverse);
            animation.maxCycles(maxCycles);
            return animation;
        }
    }
}
