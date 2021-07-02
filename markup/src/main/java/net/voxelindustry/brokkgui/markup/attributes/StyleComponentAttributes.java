package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.markup.DynamicAttributeResolver;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.MarkupAttributesGroup;
import net.voxelindustry.brokkgui.style.StyleComponent;

import java.util.ArrayList;
import java.util.List;

public class StyleComponentAttributes implements MarkupAttributesGroup
{
    private static final StyleComponentAttributes instance = new StyleComponentAttributes();

    public static StyleComponentAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private final DynamicAttributeResolver dynamicAttributeResolver = ((attribute, value, element) ->
    {
        var style = element.get(StyleComponent.class);
        return style.setPropertyFromMarkup(attribute, value);
    });

    private StyleComponentAttributes()
    {

    }

    @Override
    public List<MarkupAttribute> getAttributes()
    {
        if (attributes.isEmpty())
            createAttributes();
        return attributes;
    }

    @Override
    public List<MarkupAttribute> getChildrenAttributes()
    {
        return childrenAttributes;
    }

    @Override
    public DynamicAttributeResolver getDynamicResolver()
    {
        return dynamicAttributeResolver;
    }

    private void createAttributes()
    {
        attributes.add(new MarkupAttribute("class", ((attribute, element) ->
        {
            var style = element.get(StyleComponent.class);
            var classes = attribute.split(" ");
            for (String clazz : classes)
                style.addStyleClass(clazz);
        })));
    }
}
