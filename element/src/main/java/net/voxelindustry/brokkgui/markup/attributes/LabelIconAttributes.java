package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.LabelIconComponent;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class LabelIconAttributes
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
        attributes.add(new MarkupAttribute("icon-side", ((attribute, element) ->
                element.get(LabelIconComponent.class).iconSide(RectSide.valueOf(attribute))
        )));
        attributes.add(new MarkupAttribute("icon-padding", ((attribute, element) ->
                element.get(LabelIconComponent.class).iconPadding(Float.parseFloat(attribute))
        )));
    }

    private static void createChildrenAttributes()
    {
        childrenAttributes.add(new MarkupAttribute("icon-node", ((attributeValue, element) ->
                element.transform().parent().element().get(LabelIconComponent.class).icon(element)
        )));
    }
}
