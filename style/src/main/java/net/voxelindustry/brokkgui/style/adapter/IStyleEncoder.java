package net.voxelindustry.brokkgui.style.adapter;

@FunctionalInterface
public interface IStyleEncoder<T>
{
    String encode(T value, boolean prettyPrint);
}
