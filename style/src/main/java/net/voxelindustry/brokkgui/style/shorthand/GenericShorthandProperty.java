package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;

import java.util.ArrayList;
import java.util.List;

public class GenericShorthandProperty extends StyleProperty<String>
{
    private final List<StyleProperty<?>> childProperties;

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
        this.setStyleRaw(StyleSource.USER_AGENT, 0, this.getDefaultValue());
    }

    @Override
    public boolean setStyleRaw(StyleSource source, int specificity, String rawValue)
    {
        String current = rawValue;
        boolean anySet;
        List<StyleProperty<?>> alreadySet = new ArrayList<>();

        while (!current.isEmpty())
        {
            anySet = false;
            for (StyleProperty<?> child : this.childProperties)
            {
                if (alreadySet.contains(child))
                    continue;

                int validated = StyleTranslator.getInstance().validate(current, child.getValueClass());

                if (validated != 0)
                {
                    child.setStyleRaw(source, specificity, current.substring(0, validated));
                    current = current.substring(validated).trim();

                    alreadySet.add(child);
                    anySet = true;
                    break;
                }
            }

            if (!anySet)
                return false;
        }
        return true;
    }
}
