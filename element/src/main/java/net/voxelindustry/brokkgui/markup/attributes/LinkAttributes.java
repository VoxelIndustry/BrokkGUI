package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.LinkComponent;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class LinkAttributes
{
    private static final List<MarkupAttribute> attributes         = new ArrayList<>();
    private static final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

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

    private static void createAttributes()
    {
        attributes.add(new MarkupAttribute("url", ((attribute, element) ->
                element.get(LinkComponent.class).url(attribute)
        )));
        attributes.add(new MarkupAttribute("show-link", ((attribute, element) ->
                element.get(LinkComponent.class).showLinkContent(Boolean.parseBoolean(attribute))
        )));
    }

    private static void createChildrenAttributes()
    {
    }
}
