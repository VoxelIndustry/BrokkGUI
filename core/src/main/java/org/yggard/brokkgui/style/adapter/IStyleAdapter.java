package org.yggard.brokkgui.style.adapter;

@FunctionalInterface
public interface IStyleAdapter<T>
{
    T decode(String style);
}
