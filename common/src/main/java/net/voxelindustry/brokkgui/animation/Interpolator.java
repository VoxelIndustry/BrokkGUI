package net.voxelindustry.brokkgui.animation;

@FunctionalInterface
public interface Interpolator
{
    float apply(float delta);
}
