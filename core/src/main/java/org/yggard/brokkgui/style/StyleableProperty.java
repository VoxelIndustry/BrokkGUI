package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseProperty;

public class StyleableProperty<T> extends BaseProperty<T>
{
    private final Class<T> valueClass;

    public StyleableProperty(T value, String name, Class<T> valueClass)
    {
        super(value, name);

        this.valueClass = valueClass;
    }

    public Class<T> getValueClass()
    {
        return valueClass;
    }
}
