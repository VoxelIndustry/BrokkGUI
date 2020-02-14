package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;

import java.util.ArrayList;
import java.util.List;

public class ShorthandProperty<T> extends StyleProperty<T>
{
    private final List<StyleProperty<T>> childProperties;
    private final ShorthandArgMapper     argMapper;

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
    public boolean setStyleRaw(StyleSource source, int specificity, String rawValue)
    {
        List<T> values = new ArrayList<>();
        String current = rawValue;

        while (!current.isEmpty())
        {
            int validated = StyleTranslator.getInstance().validate(current, this.getValueClass());

            if (validated == 0)
                break;

            values.add(StyleTranslator.getInstance().decode(current.substring(0, validated), this.getValueClass()));
            current = current.substring(validated).trim();
        }

        for (int valueIndex = 0; valueIndex < values.size(); valueIndex++)
        {
            for (int childIndex : argMapper.map(valueIndex, values.size()))
                childProperties.get(childIndex).setStyle(source, specificity, values.get(valueIndex));
        }

        if (!values.isEmpty())
            this.internalSetStyle(source, specificity, values.get(0));
        return true;
    }
}
