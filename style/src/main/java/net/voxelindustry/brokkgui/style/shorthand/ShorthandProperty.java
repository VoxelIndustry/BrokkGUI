package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.style.specificity.StyleSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShorthandProperty<T> extends StyleProperty<T>
{
    private final List<StyleProperty<T>> childProperties;
    private final ShorthandArgMapper     argMapper;

    private final List<T>       splitValues         = new ArrayList<>();
    private final AtomicInteger styleConsumedLength = new AtomicInteger();

    public ShorthandProperty(T defaultValue, String name, Class<T> valueClass, ShorthandArgMapper argMapper)
    {
        super(defaultValue, name, valueClass);

        this.childProperties = new ArrayList<>();
        this.argMapper = argMapper;
    }

    public void addChild(StyleProperty<T> childProperty)
    {
        childProperties.add(childProperty);
    }

    public void removeChild(StyleProperty<T> childProperty)
    {
        childProperties.remove(childProperty);
    }

    @Override
    public boolean setStyle(StyleSource source, int specificity, T value)
    {
        this.childProperties.forEach(child -> child.setStyle(source, specificity, value));
        return super.setStyle(source, specificity, value);
    }

    @Override
    public void setStyleRaw(String propertyName, StyleSource source, int specificity, String rawValue, @Nullable AtomicInteger consumedLength)
    {
        var current = rawValue;

        while (!current.isBlank())
        {
            splitValues.add(StyleTranslator.getInstance().decode(current, this.getValueClass(), styleConsumedLength));
            current = current.substring(styleConsumedLength.get()).trim();
        }

        for (int valueIndex = 0; valueIndex < splitValues.size(); valueIndex++)
        {
            for (int childIndex : argMapper.map(valueIndex, splitValues.size()))
                childProperties.get(childIndex).setStyle(source, specificity, splitValues.get(valueIndex));
        }

        if (!splitValues.isEmpty())
            this.internalSetStyle(source, specificity, splitValues.get(0));

        styleConsumedLength.set(0);
        splitValues.clear();
    }
}
