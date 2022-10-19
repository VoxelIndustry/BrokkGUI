package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.LabelIconComponent;
import net.voxelindustry.brokkgui.data.RectSide;

import java.util.ArrayList;
import java.util.List;

public class LabelIconAttributes implements MarkupAttributesGroup
{
    private static final LabelIconAttributes instance = new LabelIconAttributes();

    public static LabelIconAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private LabelIconAttributes()
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
        if (childrenAttributes.isEmpty())
            createChildrenAttributes();
        return childrenAttributes;
    }

    private void createAttributes()
    {
        attributes.add(new MarkupAttribute("icon-side", ((attribute, element) ->
                element.get(LabelIconComponent.class).iconSide(RectSide.valueOf(attribute.toUpperCase()))
        )));
        attributes.add(new MarkupAttribute("icon-padding", ((attribute, element) ->
                element.get(LabelIconComponent.class).iconPadding(Float.parseFloat(attribute))
        )));
    }

    private void createChildrenAttributes()
    {
        childrenAttributes.add(new MarkupAttribute("icon-node", ((attributeValue, element) ->
                element.transform().parent().element().get(LabelIconComponent.class).icon(element)
        )));
    }
}
