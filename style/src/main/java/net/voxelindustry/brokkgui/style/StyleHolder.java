package net.voxelindustry.brokkgui.style;

import com.google.common.collect.ImmutableMap;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.style.shorthand.GenericShorthandProperty;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMapper;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandProperty;
import net.voxelindustry.brokkgui.style.tree.StyleEntry;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class StyleHolder
{
    private HashMap<String, StyleProperty<?>> properties;
    private BaseProperty<ICascadeStyleable>   parent;
    private ICascadeStyleable                 owner;

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
            String[] split = property.split(":", 2);
            String propertyName = split[0].trim();

            if (this.hasProperty(propertyName))
                this.setProperty(propertyName, split[1].trim(), StyleSource.INLINE, 10_000);
        }
    }

    private boolean hasProperty(String property)
    {
        return this.properties.containsKey(property);
    }

    private void setProperty(String propertyName, String value, StyleSource source, int specificity)
    {
        if (this.properties.containsKey(propertyName))
            this.properties.get(propertyName).setStyleRaw(source, specificity, value);
    }

    /**
     * Register a generic shorthand property.
     * For example border is a shorthand to border-width, border-style and border-color
     * For example border-with is a shorthand for border-top-width, border-right-width, border-bottom-width,
     * border-left-width
     * <p>
     * This method will create and add the childs properties of the same type to the StyleHolder.
     *
     * @param name         of the property
     * @param defaultValue initial value (css string)
     * @param children     all properties the parent is a shorthand for
     * @return the added GenericShorthandProperty
     */
    public GenericShorthandProperty registerGenericShorthand(String name, String defaultValue,
                                                             StyleProperty<?>... children)
    {
        GenericShorthandProperty shorthand = new GenericShorthandProperty(defaultValue, name);
        for (StyleProperty<?> child : children)
        {
            shorthand.addChild(child);
            this.properties.put(child.getName(), child);
        }

        this.properties.put(name, shorthand);
        return shorthand;
    }

    public <T> ShorthandProperty<T> registerShorthand(String name, T defaultValue, Class<T> valueClass,
                                                      ShorthandArgMapper mapper, String... children)
    {
        ShorthandProperty<T> shorthand = new ShorthandProperty<>(defaultValue, name, valueClass, mapper);

        for (String child : children)
        {
            StyleProperty<T> childProperty = new StyleProperty<>(defaultValue, child, valueClass);
            shorthand.addChild(childProperty);
            this.properties.put(child, childProperty);
        }
        this.properties.put(name, shorthand);

        return shorthand;
    }

    public <T> StyleProperty<T> registerProperty(String name, T defaultValue, Class<T> valueClass)
    {
        StyleProperty<T> property = new StyleProperty<>(defaultValue, name, valueClass);
        this.properties.put(name, property);
        return property;
    }

    public void removeProperty(String name)
    {
        this.properties.remove(name);
    }

    @SuppressWarnings("unchecked")
    public <T> StyleProperty<T> getStyleProperty(String name, Class<T> clazz)
    {
        return (StyleProperty<T>) this.properties.get(name);
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
    public ImmutableMap<String, StyleProperty<?>> getProperties()
    {
        return ImmutableMap.copyOf(properties);
    }

    void resetToDefault()
    {
        this.properties.values().stream().filter(property ->
                property.getSource() == StyleSource.AUTHOR || property.getSource() == StyleSource.USER_AGENT)
                .forEach(StyleProperty::setToDefault);
    }
}
