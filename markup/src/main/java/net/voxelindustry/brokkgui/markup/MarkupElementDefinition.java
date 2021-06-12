package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.component.GuiElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MarkupElementDefinition<T extends GuiElement>
{
    private final Map<String, MarkupAttribute> attributeMap         = new HashMap<>();
    private final Map<String, MarkupAttribute> childrenAttributeMap = new HashMap<>();

    private final Map<List<String>, Consumer<? super T>> attributeEvents = new HashMap<>();

    private final List<DynamicAttributeResolver> dynamicAttributeResolvers = new ArrayList<>(1);

    private       AttributeDecoder textChildReceiver;
    private final Supplier<T>      elementCreator;

    public MarkupElementDefinition(Supplier<T> elementCreator)
    {
        this.elementCreator = elementCreator;
    }

    public MarkupElementDefinition<T> textChildReceiver(AttributeDecoder textChildReceiver)
    {
        this.textChildReceiver = textChildReceiver;
        return this;
    }

    public MarkupElementDefinition<T> attributeGroup(MarkupAttributesGroup attributeGroup)
    {
        attributes(attributeGroup.getAttributes());
        childrenAttributes(attributeGroup.getChildrenAttributes());

        var dynamicResolver = attributeGroup.getDynamicResolver();
        if (dynamicResolver != null)
            dynamicAttributeResolver(dynamicResolver);

        var attributeNames = attributeGroup.getAttributesNames();
        if (!attributeNames.isEmpty())
            onAttributesAdded(attributeNames, attributeGroup.onAttributeAdded());
        
        return this;
    }

    public MarkupElementDefinition<T> attributes(Collection<MarkupAttribute> attributes)
    {
        for (MarkupAttribute attribute : attributes)
            attributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupElementDefinition<T> attribute(MarkupAttribute attribute)
    {
        attributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupElementDefinition<T> attribute(String name, AttributeDecoder decoder)
    {
        return attribute(new MarkupAttribute(name, decoder));
    }

    public MarkupElementDefinition<T> childrenAttributes(Collection<MarkupAttribute> attributes)
    {
        for (MarkupAttribute attribute : attributes)
            childrenAttributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupElementDefinition<T> childrenAttribute(MarkupAttribute attribute)
    {
        childrenAttributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupElementDefinition<T> childrenAttribute(String name, AttributeDecoder decoder)
    {
        return childrenAttribute(new MarkupAttribute(name, decoder));
    }

    public MarkupElementDefinition<T> dynamicAttributeResolver(DynamicAttributeResolver resolver)
    {
        dynamicAttributeResolvers.add(resolver);
        return this;
    }

    public MarkupElementDefinition<T> onAttributesAdded(List<String> attributes, Consumer<? super T> event)
    {
        attributeEvents.put(attributes, event);
        return this;
    }

    public Map<String, MarkupAttribute> attributeMap()
    {
        return attributeMap;
    }

    public Map<String, MarkupAttribute> childrenAttributeMap()
    {
        return childrenAttributeMap;
    }

    public List<DynamicAttributeResolver> dynamicAttributeResolvers()
    {
        return dynamicAttributeResolvers;
    }

    public AttributeDecoder textChildReceiver()
    {
        return textChildReceiver;
    }

    public Supplier<T> elementCreator()
    {
        return elementCreator;
    }

    public Map<List<String>, Consumer<? super T>> attributeEvents()
    {
        return attributeEvents;
    }
}
