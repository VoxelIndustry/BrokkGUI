package net.voxelindustry.brokkgui.style;

import fr.ourten.teabeans.property.named.NamedProperty;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.style.specificity.StyleSource;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class StyleProperty<T> extends NamedProperty<T>
{
    private final Class<T>    valueClass;
    private       int         specificitySet;
    private       StyleSource source;

    private final T defaultValue;

    public StyleProperty(T defaultValue, String name, Class<T> valueClass)
    {
        super(defaultValue, name);

        this.valueClass = valueClass;
        source = StyleSource.USER_AGENT;
        specificitySet = 0;
        this.defaultValue = defaultValue;
    }

    public void setStyleRaw(String propertyName, StyleSource source, int specificity, String rawValue)
    {
        this.setStyleRaw(propertyName, source, specificity, rawValue, null);
    }

    public void setStyleRaw(String propertyName, StyleSource source, int specificity, String rawValue, @Nullable AtomicInteger consumedLength)
    {
        if (source.ordinal() <= this.source.ordinal() &&
                (source.ordinal() != this.source.ordinal() || specificity < specificitySet))
            return;

        internalSetStyle(source, specificity, StyleTranslator.getInstance().decode(rawValue, getValueClass(), consumedLength));
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
