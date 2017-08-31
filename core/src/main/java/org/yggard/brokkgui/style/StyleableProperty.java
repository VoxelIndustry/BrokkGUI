package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseProperty;

public class StyleableProperty<T> extends BaseProperty<T>
{
    private final Class<T>    valueClass;
    private       int         specificitySet;
    private       StyleSource source;

    public StyleableProperty(T value, String name, Class<T> valueClass)
    {
        super(value, name);

        this.valueClass = valueClass;
        this.source = StyleSource.USER_AGENT;
        this.specificitySet = 0;
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

    // TODO: Update teabeans to allow overriding #setPropertyValue
    @Override
    public void setValue(T value)
    {
        this.source = StyleSource.CODE;
        this.specificitySet = 10_000;
        super.setValue(value);
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
}
