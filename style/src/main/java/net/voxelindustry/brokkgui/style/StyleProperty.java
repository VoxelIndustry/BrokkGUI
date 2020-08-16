package net.voxelindustry.brokkgui.style;

import fr.ourten.teabeans.property.named.NamedProperty;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;

public class StyleProperty<T> extends NamedProperty<T>
{
    private final Class<T>    valueClass;
    private       int         specificitySet;
    private       StyleSource source;
    private       T           defaultValue;

    public StyleProperty(T defaultValue, String name, Class<T> valueClass)
    {
        super(defaultValue, name);

        this.valueClass = valueClass;
        source = StyleSource.USER_AGENT;
        specificitySet = 0;
        this.defaultValue = defaultValue;
    }

    public boolean setStyleRaw(StyleSource source, int specificity, String rawValue)
    {
        if (source.ordinal() > this.source.ordinal() ||
                (source.ordinal() == this.source.ordinal() && specificity >= specificitySet))
        {
            internalSetStyle(source, specificity, StyleTranslator.getInstance().decode(rawValue, getValueClass()));
            return true;
        }
        return false;
    }

    public boolean setStyle(StyleSource source, int specificity, T value)
    {
        if (source.ordinal() > this.source.ordinal() ||
                (source.ordinal() == this.source.ordinal() && specificity >= specificitySet))
        {
            internalSetStyle(source, specificity, value);
            return true;
        }
        return false;
    }

    protected void internalSetStyle(StyleSource source, int specificity, T value)
    {
        this.source = source;
        specificitySet = specificity;
        super.setValue(value);
    }

    @Override
    public void setValue(T value)
    {
        source = StyleSource.CODE;
        specificitySet = 10_000;
        super.setValue(value);
    }

    public void setToDefault()
    {
        internalSetStyle(StyleSource.USER_AGENT, 0, defaultValue);
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
