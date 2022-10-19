package net.voxelindustry.brokkgui.style;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.listener.ValueChangeListener;
import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.SetProperty;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.style.event.StyleComponentEvent;
import net.voxelindustry.brokkgui.style.event.StyleRefreshEvent;
import net.voxelindustry.brokkgui.style.shorthand.GenericShorthandProperty;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMapper;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandProperty;
import net.voxelindustry.brokkgui.style.tree.StyleEntry;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static net.voxelindustry.brokkgui.style.PseudoClassConstants.*;

public class StyleComponent extends GuiComponent
{
    private final Map<String, StyleProperty<?>>               properties;
    private final Multimap<Pattern, Consumer<StyleComponent>> conditionalProperties;

    private Supplier<StyleList> styleSupplier;

    private final SetProperty<String> styleClass;
    private final SetProperty<String> activePseudoClass;

    private Runnable onStyleInit;

    private final ValueChangeListener<String>    styleRefreshListener = this::valueChanged;
    private final ValueChangeListener<Transform> styleParentListener  = this::parentChanged;

    private final ValueInvalidationListener focusListener   = this::focusListener;
    private final ValueInvalidationListener disableListener = this::disableListener;
    private final ValueInvalidationListener hoverListener   = this::hoverListener;

    public StyleComponent()
    {
        properties = new HashMap<>();
        conditionalProperties = ArrayListMultimap.create();

        styleClass = new SetProperty<>(Collections.emptySet());
        activePseudoClass = new SetProperty<>(Collections.emptySet());

        ListValueChangeListener<String> styleListRefreshListener = this::valueListChanged;
        styleClass.addChangeListener(styleListRefreshListener);
        activePseudoClass.addChangeListener(styleListRefreshListener);
    }

    @Override
    public void attach(GuiElement element)
    {
        if (element() != null)
        {
            element().idProperty().removeChangeListener(styleRefreshListener);
            element().transform().parentProperty().removeChangeListener(styleParentListener);

            element().hoveredProperty().removeListener(hoverListener);
            element().disabledProperty().removeListener(disableListener);
            element().focusedProperty().removeListener(focusListener);
        }

        super.attach(element);

        getEventDispatcher().singletonQueue().dispatch(StyleComponentEvent.TYPE, new StyleComponentEvent(element(), this));

        element.idProperty().addChangeListener(styleRefreshListener);
        element.transform().parentProperty().addChangeListener(styleParentListener);

        // Properties override
        element().replaceOpacityProperty(registerProperty("opacity", 1D, Double.class));

        element.hoveredProperty().addListener(hoverListener);
        element.disabledProperty().addListener(disableListener);
        element.focusedProperty().addListener(focusListener);
    }

    private void hoverListener(Observable obs)
    {
        if (element().isHovered())
            activePseudoClass().add(HOVER);
        else
            activePseudoClass().remove(HOVER);
    }

    private void disableListener(Observable obs)
    {
        if (element().isDisabled())
        {
            if (!activePseudoClass().contains(DISABLED) && activePseudoClass().contains(ENABLED))
                activePseudoClass().replace(ENABLED, DISABLED);
            else if (!activePseudoClass().contains(ENABLED) && !activePseudoClass().contains(DISABLED))
                activePseudoClass().add(DISABLED);
        }
        else
        {
            if (!activePseudoClass().contains(ENABLED) && activePseudoClass().contains(DISABLED))
                activePseudoClass().replace(DISABLED, ENABLED);
            else if (!activePseudoClass().contains(DISABLED) && !activePseudoClass().contains(ENABLED))
                activePseudoClass().add(ENABLED);
        }
    }

    private void focusListener(Observable obs)
    {
        if (element().isFocused())
            activePseudoClass().add(FOCUS);
        else
            activePseudoClass().remove(FOCUS);
    }

    private void valueListChanged(ObservableValue obs, String oldValue, String newValue)
    {
        refresh();
    }

    private void valueChanged(ObservableValue obs, String oldValue, String newValue)
    {
        refresh();
    }

    private void parentChanged(ObservableValue obs, Transform oldValue, Transform newValue)
    {
        if (newValue == null || !newValue.element().has(StyleComponent.class))
            return;

        setStyleSupplier(newValue.element().get(StyleComponent.class).getStyleSupplier());
        refresh();
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public SetProperty<String> styleClass()
    {
        return styleClass;
    }

    public void addStyleClass(String styleClass)
    {
        styleClass().add(styleClass);
    }

    public void removeStyleClass(String styleClass)
    {
        styleClass().remove(styleClass);
    }

    public boolean hasStyleClass(String styleClass)
    {
        return styleClass().contains(styleClass);
    }

    public String type()
    {
        return element().type();
    }

    public SetProperty<String> activePseudoClass()
    {
        return activePseudoClass;
    }

    public void addPseudoClass(String pseudoClass)
    {
        activePseudoClass().add(pseudoClass);
    }

    public void removePseudoClass(String pseudoClass)
    {
        activePseudoClass().remove(pseudoClass);
    }

    public boolean hasPseudoClass(String pseudoClass)
    {
        return activePseudoClass().contains(pseudoClass);
    }

    public String id()
    {
        return element().id();
    }

    public void id(String id)
    {
        element().id(id);
    }

    public GuiElement parent()
    {
        Transform parentTransform = element().transform().parent();
        return parentTransform != null ? parentTransform.element() : null;
    }

    /////////////
    // STYLING //
    /////////////

    public void parseInlineCSS(String css)
    {
        for (String property : css.split(";"))
        {
            String[] split = property.split(":", 2);
            String propertyName = split[0].trim();

            if (hasProperty(propertyName))
                setProperty(propertyName, split[1].trim(), StyleSource.INLINE, 10_000);
        }
    }

    private boolean hasProperty(String property)
    {
        if (properties.containsKey(property))
            return true;

        long count = 0L;
        for (var entry : conditionalProperties.entries())
        {
            if (entry.getKey().matcher(property).matches())
            {
                entry.getValue().accept(this);
                count++;
            }
        }
        return count > 0;
    }

    /**
     * Return the held state of the specified property.
     * It will query the currently registered properties and the conditional patterns as well.
     *
     * @param property key identifying the css-property
     * @return the held state PRESENT if currently in properties map, CONDITIONAL if a pattern match but has not yet
     * added its properties, ABSENT if no references can be found.
     */
    public HeldPropertyState doesHoldProperty(String property)
    {
        if (properties.containsKey(property))
            return HeldPropertyState.PRESENT;

        for (Pattern pattern : conditionalProperties.keySet())
        {
            if (pattern.matcher(property).matches())
                return HeldPropertyState.CONDITIONAL;
        }

        return HeldPropertyState.ABSENT;
    }

    private void setProperty(String propertyName, String value, StyleSource source, int specificity)
    {
        StyleProperty<?> property = properties.get(propertyName);

        if (property != null)
            property.setStyleRaw(source, specificity, value);
    }

    public boolean setPropertyFromMarkup(String propertyName, String value)
    {
        // Check needed in order to force load conditional properties
        if (hasProperty(propertyName))
        {
            setProperty(propertyName, value, StyleSource.INLINE, 10_000);
            return true;
        }
        return false;
    }

    public <T> void setPropertyDirect(String propertyName, T value, Class<T> valueClass)
    {
        if (!hasProperty(propertyName))
            return;

        StyleProperty<T> property = getProperty(propertyName, valueClass);

        if (property != null)
        {
            property.setStyle(StyleSource.CODE, 10_000, value);
            element().markRenderDirty();
        }
    }

    /**
     * Register conditionally enabled properties. It allows to lazily add properties to a Styleable node, increasing
     * the memory efficiency of simple nodes.
     * <p>
     * For example borders are conditionals, if any property matching "border*" is called all border related
     * properties are added.
     *
     * @param matchKey          key to transform into a regular expression. See this example syntax "border*" and
     *                          "*-image*"
     * @param propertiesCreator Consumer parameterized with this StyleHolder. Add the properties or execute
     *                          invalidating operations for conflicting properties.
     */
    public void registerConditionalProperties(String matchKey, Consumer<StyleComponent> propertiesCreator)
    {
        String regex = '^' + matchKey.replaceAll("\\*", "\\\\S*") + '$';

        conditionalProperties.put(Pattern.compile(regex), propertiesCreator);
    }

    /**
     * Register a generic shorthand property.
     * For example border is a shorthand to border-width, border-style and border-color
     * <p>
     * This method will not create the child properties.
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
            properties.put(child.getName(), child);
        }

        properties.put(name, shorthand);
        return shorthand;
    }

    /**
     * Register a non-generic shorthand property.
     * For example border-width is a shorthand for border-top-width, border-right-width, border-bottom-width,
     * border-left-width
     * <p>
     * This method will create and add the children properties of the same type to the StyleHolder.
     *
     * @param name         of the property
     * @param defaultValue initial value
     * @param valueClass   Class representing the generic parameter of this method
     * @param mapper       Interface for mapping css parts to children @see ShorthandArgMappers#BOX_MAPPER for an
     *                     example
     * @param children     array of children property keys
     * @param <T>          type of the shorthand property and its children
     * @return the created shorthand
     */
    public <T> ShorthandProperty<T> registerShorthand(String name, T defaultValue, Class<T> valueClass,
                                                      ShorthandArgMapper mapper, String... children)
    {
        ShorthandProperty<T> shorthand = new ShorthandProperty<>(defaultValue, name, valueClass, mapper);

        for (String child : children)
        {
            StyleProperty<T> childProperty = new StyleProperty<>(defaultValue, child, valueClass);
            shorthand.addChild(childProperty);
            properties.put(child, childProperty);
        }
        properties.put(name, shorthand);

        return shorthand;
    }

    public <T> StyleProperty<T> registerProperty(String name, T defaultValue, Class<T> valueClass)
    {
        var property = new StyleProperty<T>(defaultValue, name, valueClass);
        properties.put(name, property);
        return property;
    }

    public void removeProperty(String name)
    {
        properties.remove(name);
    }

    public <T> StyleProperty<T> getProperty(String name, Class<T> valueClass)
    {
        return (StyleProperty<T>) properties.get(name);
    }

    public <T> StyleProperty<T> getOrCreateProperty(String name, Class<T> valueClass)
    {
        if (!hasProperty(name))
            return null;
        return getProperty(name, valueClass);
    }

    public <T> T getValue(String propertyName, Class<T> valueClass)
    {
        StyleProperty<T> property = getProperty(propertyName, valueClass);

        if (property == null)
            return null;
        return property.getValue();
    }

    public <T> T getValue(String propertyName, Class<T> valueClass, T defaultValue)
    {
        StyleProperty<T> property = getProperty(propertyName, valueClass);

        if (property == null)
            return defaultValue;
        return property.getValue();
    }

    public Supplier<StyleList> getStyleSupplier()
    {
        return styleSupplier;
    }

    public void setStyleSupplier(Supplier<StyleList> styleSupplier)
    {
        this.styleSupplier = styleSupplier;

        if (onStyleInit != null)
            onStyleInit.run();

        if (element() != null)
        {
            element().transform().children().forEach(child ->
                    child.element().ifHas(StyleComponent.class, childStyle -> childStyle.setStyleSupplier(styleSupplier)));
        }
    }

    public void refresh()
    {
        if (styleSupplier == null)
            return;

        StyleList styleList = styleSupplier.get();
        if (styleList == null)
            return;

        if (element() != null)
            getEventDispatcher().singletonQueue().dispatch(StyleRefreshEvent.BEFORE, new StyleRefreshEvent.BeforeEvent(this));

        List<StyleEntry> entries = styleList.getEntriesMatching(this);


        for (var value : properties.values())
            value.mute();

        resetToDefault();
        entries.forEach(entry -> entry.getRules().forEach(rule ->
        {
            if (hasProperty(rule.getRuleIdentifier()))
                setProperty(rule.getRuleIdentifier(),
                        rule.getRuleValue(),
                        StyleSource.AUTHOR,
                        entry.getSelector().getSpecificity());
        }));

        for (var value : properties.values())
            value.unmute();

        if (element() != null)
        {
            element().transform().children().forEach(child ->
                    child.element().ifHas(StyleComponent.class, StyleComponent::refresh));
            getEventDispatcher().singletonQueue().dispatch(StyleRefreshEvent.AFTER, new StyleRefreshEvent.AfterEvent(this));
        }

        element().markRenderDirty();
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
        for (StyleProperty<?> property : properties.values())
        {
            if (property.getSource() == StyleSource.AUTHOR || property.getSource() == StyleSource.USER_AGENT)
                property.setToDefault();
        }
    }

    public void beginStyleProfiling()
    {
        element().getEventDispatcher().addHandler(StyleRefreshEvent.BEFORE, e -> BrokkGuiPlatform.getInstance().getProfiler().preElementStyleRefresh(element()));
        element().getEventDispatcher().addHandler(StyleRefreshEvent.AFTER, e -> BrokkGuiPlatform.getInstance().getProfiler().postElementStyleRefresh(element()));
    }

    public void onStyleInit(Runnable onStyleInit)
    {
        this.onStyleInit = onStyleInit;
    }
}
