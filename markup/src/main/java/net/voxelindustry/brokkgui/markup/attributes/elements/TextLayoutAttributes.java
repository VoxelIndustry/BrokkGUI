package net.voxelindustry.brokkgui.markup.attributes.elements;

import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttributesGroup;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;

import java.util.ArrayList;
import java.util.List;

public class TextLayoutAttributes implements MarkupAttributesGroup
{
    private static final TextLayoutAttributes instance = new TextLayoutAttributes();

    public static TextLayoutAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private TextLayoutAttributes()
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
        attributes.add(new MarkupAttribute("expand-to-text", ((attribute, element) ->
                element.get(TextLayoutComponent.class).expandToText(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("text-overflow", ((attribute, element) ->
                element.get(TextLayoutComponent.class).textOverflow(GuiOverflow.valueOf(attribute.toUpperCase()))
        )));
        attributes.add(new MarkupAttribute("ellipsis", ((attribute, element) ->
                element.get(TextLayoutComponent.class).ellipsis(attribute)
        )));
    }
}
