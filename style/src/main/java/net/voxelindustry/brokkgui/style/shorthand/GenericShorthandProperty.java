package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.specificity.StyleSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GenericShorthandProperty extends StyleProperty<String>
{
    private final List<StyleProperty<?>> childProperties;
    private final List<StyleProperty<?>> alreadySet = new ArrayList<>();

    private final AtomicInteger styleConsumedLength = new AtomicInteger();


    public GenericShorthandProperty(String defaultValue, String name)
    {
        super(defaultValue, name, String.class);

        this.childProperties = new ArrayList<>();
    }

    public <C> void addChild(StyleProperty<C> childProperty)
    {
        childProperties.add(childProperty);
    }

    public void removeChild(StyleProperty<?> childProperty)
    {
        childProperties.remove(childProperty);
    }

    public void applyDefaultValue()
    {
        this.setStyleRaw("", StyleSource.USER_AGENT, 0, this.getDefaultValue(), null);
    }

    @Override
    public void setStyleRaw(String propertyName, StyleSource source, int specificity, String rawValue, @Nullable AtomicInteger consumedLength)
    {
        String current = rawValue;
        boolean anySet;

        while (!current.isBlank())
        {
            anySet = false;
            for (var child : this.childProperties)
            {
                if (alreadySet.contains(child))
                    continue;

                child.setStyleRaw(child.getName(), source, specificity, current, styleConsumedLength);

                current = current.substring(styleConsumedLength.get()).trim();

                alreadySet.add(child);
                anySet = true;
                break;
            }

            if (!anySet)
                break;
        }

        styleConsumedLength.set(0);
        alreadySet.clear();
    }
}
