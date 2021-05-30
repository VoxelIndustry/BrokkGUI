package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.markup.DynamicAttributeResolver;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.style.HeldPropertyState;
import net.voxelindustry.brokkgui.style.StyleComponent;

import java.util.ArrayList;
import java.util.List;

public class StyleComponentAttributes
{
    private static final List<MarkupAttribute> attributes         = new ArrayList<>();
    private static final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private static final DynamicAttributeResolver dynamicAttributeResolver = ((attribute, value, element) ->
    {
        var style = element.get(StyleComponent.class);
        if (style.doesHoldProperty(attribute) != HeldPropertyState.ABSENT)
        {
            style.setPropertyFromMarkup(attribute, value);
            return true;
        }
        return false;
    });

    public static List<MarkupAttribute> getAttributes()
    {
        if (attributes.isEmpty())
            createAttributes();
        return attributes;
    }

    public static List<MarkupAttribute> getChildrenAttributes()
    {
        if (childrenAttributes.isEmpty())
            createChildrenAttributes();
        return childrenAttributes;
    }

    public static DynamicAttributeResolver getStyleResolver()
    {
        return dynamicAttributeResolver;
    }

    private static void createAttributes()
    {
        attributes.add(new MarkupAttribute("class", ((attribute, element) ->
        {
            var style = element.get(StyleComponent.class);
            var classes = attribute.split(" ");
            for (String clazz : classes)
                style.addStyleClass(clazz);
        })));
    }

    private static void createChildrenAttributes()
    {
    }
}
