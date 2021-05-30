package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;

import java.util.ArrayList;
import java.util.List;

public class TextLayoutAttributes
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
        attributes.add(new MarkupAttribute("expand-to-text", ((attribute, element) ->
                element.get(TextLayoutComponent.class).expandToText(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("text-overflow", ((attribute, element) ->
                element.get(TextLayoutComponent.class).textOverflow(GuiOverflow.valueOf(attribute))
        )));
        attributes.add(new MarkupAttribute("ellipsis", ((attribute, element) ->
                element.get(TextLayoutComponent.class).ellipsis(attribute)
        )));
    }

    private static void createChildrenAttributes()
    {
    }
}
