package net.voxelindustry.brokkgui.paint;

import java.util.HashMap;
import java.util.Map;

public class Color1DGradient
{
    private Map<Float, Color> frames;
    private Color[]           cachedColors;

    private Color1DGradient(Map<Float, Color> frames)
    {
        this.frames = frames;
    }

    private Color1DGradient(Map<Float, Color> frames, float precision)
    {
        cachedColors = new Color[(int) (1 / precision) + 1];

        for (int step = 0; step < 1 / precision; step++)
            cachedColors[step] = this.getInterpolated(frames, step * precision);

        cachedColors[cachedColors.length - 1] = this.getInterpolated(frames, 1);
    }

    public Color getValue(float delta)
    {
        if (cachedColors != null)
        {
            if (delta == 0)
                return cachedColors[0];
            return cachedColors[(int) ((cachedColors.length - 1) / (1 / delta))];
        }

        return this.getInterpolated(this.frames, delta);
    }

    private Color getInterpolated(Map<Float, Color> frames, float delta)
    {
        Map.Entry<Float, Color> low = getLower(frames, delta);
        Map.Entry<Float, Color> high = getHigher(frames, delta);

        if (low.getValue().equals(high.getValue()))
            return low.getValue();
        return low.getValue().interpolate(high.getValue(),
                (delta - low.getKey()) / (high.getKey() - low.getKey()));
    }

    Map.Entry<Float, Color> getLower(Map<Float, Color> frames, float current)
    {
        Map.Entry<Float, Color> closest = null;

        for (Map.Entry<Float, Color> entry : frames.entrySet())
        {
            if (entry.getKey() == current)
                return entry;
            if (entry.getKey() > current)
                continue;

            if (closest == null)
                closest = entry;
            else if (entry.getKey() > closest.getKey())
                closest = entry;
        }

        return closest;
    }

    Map.Entry<Float, Color> getHigher(Map<Float, Color> frames, float current)
    {
        Map.Entry<Float, Color> closest = null;

        for (Map.Entry<Float, Color> entry : frames.entrySet())
        {
            if (entry.getKey() == current)
                return entry;
            if (entry.getKey() < current)
                continue;

            if (closest == null)
                closest = entry;
            else if (entry.getKey() < closest.getKey())
                closest = entry;
        }

        return closest;
    }

    public static Builder build()
    {
        return new Builder();
    }

    public static class Builder
    {
        private Map<Float, Color> frames;
        private boolean           precompute;
        private float             precision;

        public Builder()
        {
            this.frames = new HashMap<>();
        }

        public Builder color(Color color, float delta)
        {
            this.frames.put(delta, color);
            return this;
        }

        public Builder precompute(float precision)
        {
            this.precision = precision;
            this.precompute = true;
            return this;
        }

        public Color1DGradient create()
        {
            if (precompute)
                return new Color1DGradient(frames, precision);
            return new Color1DGradient(frames);
        }
    }
}
