package net.voxelindustry.brokkgui.markup.definitions;

import net.voxelindustry.brokkgui.markup.ChildElementReceiver;
import net.voxelindustry.brokkgui.markup.attributes.AttributeDecoder;
import net.voxelindustry.brokkgui.markup.attributes.AttributeMetaDecoder;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.attributes.MarkupMetaAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MarkupMetaElementDefinition<T> implements ParentDefinition
{
    private final Map<String, MarkupMetaAttribute<T>> attributeMap         = new HashMap<>();
    private final Map<String, MarkupAttribute>        childrenAttributeMap = new HashMap<>();

    private final Map<List<String>, Consumer<? super T>> attributeEvents = new HashMap<>();

    private final List<ChildElementReceiver> childElementReceivers = new ArrayList<>(1);

    private AttributeDecoder textChildReceiver;

    private final Supplier<T> elementCreator;

    public MarkupMetaElementDefinition(Supplier<T> elementCreator)
    {
        this.elementCreator = elementCreator;
    }

    public MarkupMetaElementDefinition<T> textChildReceiver(AttributeDecoder textChildReceiver)
    {
        this.textChildReceiver = textChildReceiver;
        return this;
    }

    public MarkupMetaElementDefinition<T> attributes(Collection<MarkupMetaAttribute<T>> attributes)
    {
        for (var attribute : attributes)
            attributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupMetaElementDefinition<T> attribute(MarkupMetaAttribute<T> attribute)
    {
        attributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupMetaElementDefinition<T> attribute(String name, AttributeMetaDecoder<T> decoder)
    {
        return attribute(new MarkupMetaAttribute<>(name, decoder));
    }

    public MarkupMetaElementDefinition<T> childrenAttributes(Collection<MarkupAttribute> attributes)
    {
        for (MarkupAttribute attribute : attributes)
            childrenAttributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupMetaElementDefinition<T> childrenAttribute(MarkupAttribute attribute)
    {
        childrenAttributeMap.put(attribute.name(), attribute);
        return this;
    }

    public MarkupMetaElementDefinition<T> childrenAttribute(String name, AttributeDecoder decoder)
    {
        return childrenAttribute(new MarkupAttribute(name, decoder));
    }

    public MarkupMetaElementDefinition<T> childElementReceiver(ChildElementReceiver childElementReceiver)
    {
        childElementReceivers.add(childElementReceiver);
        return this;
    }

    public Map<String, MarkupMetaAttribute<T>> attributeMap()
    {
        return attributeMap;
    }

    public Map<String, MarkupAttribute> childrenAttributeMap()
    {
        return childrenAttributeMap;
    }

    public List<ChildElementReceiver> childElementReceivers()
    {
        return childElementReceivers;
    }

    @Override
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
