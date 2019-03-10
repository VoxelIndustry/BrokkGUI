package net.voxelindustry.brokkgui.style.adapter;

@FunctionalInterface
public interface IStyleDecoder<T>
{
    T decode(String style);
}
