package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.TextInputComponent;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class TextInputAttributes
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
        attributes.add(new MarkupAttribute("editable", ((attribute, element) ->
                element.get(TextInputComponent.class).editable(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("text-max-length", ((attribute, element) ->
                element.get(TextInputComponent.class).maxTextLength(Integer.parseInt(attribute))
        )));
    }

    private static void createChildrenAttributes()
    {
    }
}
