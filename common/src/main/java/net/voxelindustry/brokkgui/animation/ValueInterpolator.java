package net.voxelindustry.brokkgui.animation;

@FunctionalInterface
public interface ValueInterpolator<T>
{
    T interpolate(float delta, T from, T to);
}
