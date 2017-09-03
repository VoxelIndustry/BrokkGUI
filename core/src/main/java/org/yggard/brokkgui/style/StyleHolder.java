package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.style.tree.StyleEntry;
import org.yggard.brokkgui.style.tree.StyleTree;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class StyleHolder
{
    private HashMap<String, StyleableProperty<?>> properties;
    private BaseProperty<ICascadeStyleable>       parent;
    private ICascadeStyleable                     owner;

    private Supplier<StyleTree>                   styleSupplier;

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
            String[] splitted = property.split(":");
            String propertyName = splitted[0].trim();

            if (properties.containsKey(propertyName))
            {
                StyleableProperty<?> styleProp = properties.get(propertyName);
                styleProp.setStyle(StyleSource.INLINE, 10_000,
                        StyleDecoder.getInstance().decode(splitted[1].trim(), styleProp.getValueClass()));
            }
        }
    }

    public <T> void registerProperty(String name, T defaultValue, Class<T> valueClass)
    {
        this.properties.put(name, new StyleableProperty<>(defaultValue, name, valueClass));
    }

    public <T> StyleableProperty<T> getStyleProperty(String name, Class<T> clazz)
    {
        return (StyleableProperty<T>) this.properties.get(name);
    }

    public void setStyleSupplier(Supplier<StyleTree> styleSupplier)
    {
        this.styleSupplier = styleSupplier;
    }

    public void refresh()
    {
        if (this.styleSupplier == null)
            return;

        StyleTree tree = this.styleSupplier.get();
        if (tree == null)
            return;
        Set<StyleEntry> entries = tree.getEntries(this);

        this.properties.values().forEach(StyleableProperty::setToDefault);
        entries.forEach(entry -> entry.getRules().forEach(rule ->
        {
            if (this.properties.containsKey(rule.getRuleIdentifier()))
                this.properties.get(rule.getRuleIdentifier()).setStyle(StyleSource.AUTHOR,
                        entry.getSelector().getSpecificity(), StyleDecoder.getInstance().decode(rule.getRuleValue(),
                                this.properties.get(rule.getRuleIdentifier()).getValueClass()));
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
}
