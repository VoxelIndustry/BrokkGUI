package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.LinkComponent;

import java.util.ArrayList;
import java.util.List;

public class LinkAttributes implements MarkupAttributesGroup
{
    private static final LinkAttributes instance = new LinkAttributes();

    public static LinkAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private LinkAttributes()
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

    private void createAttributes()
    {
        attributes.add(new MarkupAttribute("url", ((attribute, element) ->
                element.get(LinkComponent.class).url(attribute)
        )));
        attributes.add(new MarkupAttribute("show-link", ((attribute, element) ->
                element.get(LinkComponent.class).showLinkContent(Boolean.parseBoolean(attribute))
        )));
    }
}
