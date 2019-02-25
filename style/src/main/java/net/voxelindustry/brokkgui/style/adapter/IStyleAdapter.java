package net.voxelindustry.brokkgui.style.adapter;

@FunctionalInterface
public interface IStyleAdapter<T>
{
    T decode(String style);
}
