package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class GuiElementAttributes
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
        attributes.add(new MarkupAttribute("id", ((attribute, element) ->
                element.id(attribute))
        ));

        attributes.add(new MarkupAttribute("visible", ((attribute, element) ->
                element.setVisible(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("disable", ((attribute, element) ->
                element.setDisabled(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("focusable", ((attribute, element) ->
                element.setFocusable(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("focused", ((attribute, element) ->
        {
            var focused = Boolean.parseBoolean(attribute);
            if (focused)
                element.setFocused();
            else
                GuiFocusManager.instance.removeFocusedNode(element, element.getWindow());
        })));
    }

    private static void createChildrenAttributes()
    {
    }
}
