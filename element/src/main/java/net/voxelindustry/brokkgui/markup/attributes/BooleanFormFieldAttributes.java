package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.BooleanFormFieldComponent;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class BooleanFormFieldAttributes
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
        attributes.add(new MarkupAttribute("button-side", ((attribute, element) ->
                element.get(BooleanFormFieldComponent.class).buttonSide(RectSide.valueOf(attribute))
        )));
        attributes.add(new MarkupAttribute("button-size", ((attribute, element) ->
                element.get(BooleanFormFieldComponent.class).buttonSize(Float.parseFloat(attribute))
        )));
    }

    private static void createChildrenAttributes()
    {
        childrenAttributes.add(new MarkupAttribute("button-node", ((attributeValue, element) ->
                element.transform().parent().element().get(BooleanFormFieldComponent.class).buttonNode(element)
        )));
    }
}
