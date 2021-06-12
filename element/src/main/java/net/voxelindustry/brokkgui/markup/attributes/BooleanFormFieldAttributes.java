package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.BooleanFormFieldComponent;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.MarkupAttributesGroup;

import java.util.ArrayList;
import java.util.List;

public class BooleanFormFieldAttributes implements MarkupAttributesGroup
{
    private static final BooleanFormFieldAttributes instance = new BooleanFormFieldAttributes();

    public static BooleanFormFieldAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private BooleanFormFieldAttributes()
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
        attributes.add(new MarkupAttribute("button-side", ((attribute, element) ->
                element.get(BooleanFormFieldComponent.class).buttonSide(RectSide.valueOf(attribute))
        )));
        attributes.add(new MarkupAttribute("button-size", ((attribute, element) ->
                element.get(BooleanFormFieldComponent.class).buttonSize(Float.parseFloat(attribute))
        )));
    }

    private void createChildrenAttributes()
    {
        childrenAttributes.add(new MarkupAttribute("button-node", ((attributeValue, element) ->
                element.transform().parent().element().get(BooleanFormFieldComponent.class).buttonNode(element)
        )));
    }
}
