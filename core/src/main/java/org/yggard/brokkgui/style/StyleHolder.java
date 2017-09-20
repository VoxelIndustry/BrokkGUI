package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.style.tree.StyleEntry;
import org.yggard.brokkgui.style.tree.StyleList;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class StyleHolder
{
    private HashMap<String, StyleableProperty<?>> properties;
    private BaseProperty<ICascadeStyleable>       parent;
    private ICascadeStyleable                     owner;

    private Supplier<StyleList> styleSupplier;

    private HashMap<String, StyleHolder> subAliases;

    public StyleHolder(ICascadeStyleable owner)
    {
        this.properties = new HashMap<>();
        this.owner = owner;
        this.parent = new BaseProperty<>(null, "parentProperty");

        this.subAliases = new HashMap<>();
    }

    public void parseInlineCSS(String css)
    {
        for (String property : css.split(";"))
        {
            String[] splitted = property.split(":");
            String propertyName = splitted[0].trim();

            if (this.hasProperty(propertyName))
                this.setProperty(propertyName, splitted[1].trim(), StyleSource.INLINE,
                        10_000);
            this.subAliases.keySet().stream().filter(name -> propertyName.startsWith('-' + name)).findFirst()
                    .ifPresent(name ->
                    {
                        String mappedName = propertyName.replaceFirst('-' + name, "");
                        if (this.subAliases.get(name).hasProperty(mappedName))
                            this.subAliases.get(name).setProperty(mappedName, splitted[1].trim(), StyleSource
                                    .INLINE, 10_000);
                    });
        }
    }

    private boolean hasProperty(String property)
    {
        return this.properties.containsKey(property);
    }

    private void setProperty(String name, String value, StyleSource source, int specificity)
    {
        this.properties.get(name).setStyle(source, specificity, StyleDecoder.getInstance().decode(value, this
                .properties.get(name).getValueClass()));
    }

    public <T> void registerProperty(String name, T defaultValue, Class<T> valueClass)
    {
        this.properties.put(name, new StyleableProperty<>(defaultValue, name, valueClass));
    }

    public <T> StyleableProperty<T> getStyleProperty(String name, Class<T> clazz)
    {
        return (StyleableProperty<T>) this.properties.get(name);
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
        Set<StyleEntry> entries = tree.getEntries(this);

        this.properties.values().stream().filter(property ->
                property.getSource() == StyleSource.AUTHOR || property.getSource() == StyleSource.USER_AGENT)
                .forEach(StyleableProperty::setToDefault);
        entries.forEach(entry -> entry.getRules().forEach(rule ->
        {
            if (this.hasProperty(rule.getRuleIdentifier()))
                this.setProperty(rule.getRuleIdentifier(), rule.getRuleValue(), StyleSource.AUTHOR,
                        entry.getSelector().getSpecificity());
            this.subAliases.keySet().stream().filter(name -> rule.getRuleIdentifier().startsWith('-' + name))
                    .findFirst()
                    .ifPresent(name ->
                    {
                        String mappedName = rule.getRuleIdentifier().replaceFirst('-' + name, "");
                        if (this.subAliases.get(name).hasProperty(mappedName))
                            this.subAliases.get(name).setProperty(mappedName, rule.getRuleValue(), StyleSource
                                    .AUTHOR, entry.getSelector().getSpecificity());
                    });
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

    public void registerAlias(String name, StyleHolder subType)
    {
        this.subAliases.put(name, subType);
    }

    public void removeAlias(String name)
    {
        this.subAliases.remove(name);
    }
}
