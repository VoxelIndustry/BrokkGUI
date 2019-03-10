package net.voxelindustry.brokkgui.style;

import com.google.common.collect.ImmutableMap;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.style.parser.StyleTranslator;
import net.voxelindustry.brokkgui.style.tree.StyleEntry;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class StyleHolder
{
    private HashMap<String, StyleableProperty<?>> properties;
    private BaseProperty<ICascadeStyleable>       parent;
    private ICascadeStyleable                     owner;

    private Supplier<StyleList> styleSupplier;

    public StyleHolder(ICascadeStyleable owner)
    {
        this.properties = new HashMap<>();
        this.owner = owner;
        this.parent = new BaseProperty<>(null, "parentProperty");
    }

    public void parseInlineCSS(String css)
    {
        for (String property : css.split(";"))
        {
            String[] splitted = property.split(":", 2);
            String propertyName = splitted[0].trim();

            if (this.hasProperty(propertyName))
                this.setProperty(propertyName, splitted[1].trim(), StyleSource.INLINE, 10_000);
        }
    }

    private boolean hasProperty(String property)
    {
        return this.properties.containsKey(property);
    }

    private void setProperty(String propertyName, String value, StyleSource source, int specificity)
    {
        if (this.properties.containsKey(propertyName))
        {
            this.properties.get(propertyName).setStyle(source, specificity,
                    StyleTranslator.getInstance().decode(value, this.properties.get(propertyName).getValueClass()));
        }
    }

    public <T> void registerProperty(String name, T defaultValue, Class<T> valueClass)
    {
        this.properties.put(name, new StyleableProperty<>(defaultValue, name, valueClass));
    }

    public void removeProperty(String name)
    {
        this.properties.remove(name);
    }

    public <T> StyleableProperty<T> getStyleProperty(String name, Class<T> clazz)
    {
        return (StyleableProperty<T>) this.properties.get(name);
    }

    public Supplier<StyleList> getStyleSupplier()
    {
        return this.styleSupplier;
    }

    public void setStyleSupplier(Supplier<StyleList> styleSupplier)
    {
        this.styleSupplier = styleSupplier;
    }

    public void refresh()
    {
        if (this.styleSupplier == null)
            return;

        StyleList tree = this.styleSupplier.get();
        if (tree == null)
            return;
        List<StyleEntry> entries = tree.getEntries(this);

        this.resetToDefault();
        entries.forEach(entry -> entry.getRules().forEach(rule ->
        {
            if (this.hasProperty(rule.getRuleIdentifier()))
                this.setProperty(rule.getRuleIdentifier(), rule.getRuleValue(), StyleSource.AUTHOR,
                        entry.getSelector().getSpecificity());
        }));
    }

    /**
     * @return the parent
     */
    public BaseProperty<ICascadeStyleable> getParent()
    {
        return parent;
    }

    public ICascadeStyleable getOwner()
    {
        return owner;
    }

    /**
     * @return the contained properties of this holder. Intended only for debug info.
     */
    public ImmutableMap<String, StyleableProperty<?>> getProperties()
    {
        return ImmutableMap.copyOf(properties);
    }

    void resetToDefault()
    {
        this.properties.values().stream().filter(property ->
                property.getSource() == StyleSource.AUTHOR || property.getSource() == StyleSource.USER_AGENT)
                .forEach(StyleableProperty::setToDefault);
    }
}
