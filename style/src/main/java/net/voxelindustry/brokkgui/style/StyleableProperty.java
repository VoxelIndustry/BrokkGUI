package net.voxelindustry.brokkgui.style;

import fr.ourten.teabeans.value.BaseProperty;

public class StyleableProperty<T> extends BaseProperty<T>
{
    private final Class<T>    valueClass;
    private       int         specificitySet;
    private       StyleSource source;
    private       T           defaultValue;

    public StyleableProperty(T defaultValue, String name, Class<T> valueClass)
    {
        super(defaultValue, name);

        this.valueClass = valueClass;
        this.source = StyleSource.USER_AGENT;
        this.specificitySet = 0;
        this.defaultValue = defaultValue;
    }

    public boolean setStyle(StyleSource source, int specificity, T value)
    {
        if (source.ordinal() > this.source.ordinal() || (source.ordinal() == this.source.ordinal() &&
                specificity >= this.specificitySet))
        {
            internalSetStyle(source, specificity, value);
            return true;
        }
        return false;
    }

    private void internalSetStyle(StyleSource source, int specificity, T value)
    {
        this.source = source;
        this.specificitySet = specificity;
        super.setValue(value);
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
        this.internalSetStyle(StyleSource.USER_AGENT, 0, defaultValue);
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
