package net.voxelindustry.brokkgui.markup.attributes.elements;

import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttributesGroup;

import java.util.ArrayList;
import java.util.List;

public class GuiElementAttributes implements MarkupAttributesGroup
{
    private static final GuiElementAttributes instance = new GuiElementAttributes();

    public static GuiElementAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private GuiElementAttributes()
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
        attributes.add(new MarkupAttribute("id", ((attribute, element) ->
                element.id(attribute))
        ));

        attributes.add(new MarkupAttribute("disable", ((attribute, element) ->
                element.setDisabled(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("focused", ((attribute, element) ->
        {
            var focused = Boolean.parseBoolean(attribute);
            if (focused)
                element.setFocused();
            else
                GuiFocusManager.instance.removeFocusedNode(element, element.window());
        })));
    }
}
