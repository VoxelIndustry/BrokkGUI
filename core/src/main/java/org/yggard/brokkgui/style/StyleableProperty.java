package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseProperty;

public class StyleableProperty<T> extends BaseProperty<T>
{
    private final Class<T> valueClass;
    private int            specificitySet;
    private StyleSource    source;
    private T              defaultValue;

    public StyleableProperty(T defaultValue, String name, Class<T> valueClass)
    {
        super(defaultValue, name);

        this.valueClass = valueClass;
        this.source = StyleSource.USER_AGENT;
        this.specificitySet = 0;
        this.defaultValue = defaultValue;
    }

    boolean setStyle(StyleSource source, int specificity, T value)
    {
        if (source.ordinal() >= this.source.ordinal() && specificity >= this.specificitySet)
        {
            this.source = source;
            this.specificitySet = specificity;
            super.setValue(value);
            return true;
        }
        return false;
    }

    @Override
    public void setValue(T value)
    {
        this.source = StyleSource.CODE;
        this.specificitySet = 10_000;
        super.setValue(value);
    }

    public void setToDefault()
    {
        this.setStyle(StyleSource.USER_AGENT, 0, defaultValue);
    }

    public Class<T> getValueClass()
    {
        return valueClass;
    }

    public int getSpecificity()
    {
        return specificitySet;
    }

    public StyleSource getSource()
    {
        return source;
    }

    public T getDefaultValue()
    {
        return defaultValue;
    }
}
