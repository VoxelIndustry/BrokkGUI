package net.voxelindustry.brokkgui.markup.attributes.elements;

import net.voxelindustry.brokkgui.component.impl.VerticalLayout;
import net.voxelindustry.brokkgui.data.AlignmentMode;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttributesGroup;

import java.util.ArrayList;
import java.util.List;

public class VerticalLayoutAttributes implements MarkupAttributesGroup
{
    private static final VerticalLayoutAttributes instance = new VerticalLayoutAttributes();

    public static VerticalLayoutAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private VerticalLayoutAttributes()
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
        attributes.add(new MarkupAttribute("alignment", ((attribute, element) ->
                element.get(VerticalLayout.class).alignment(AlignmentMode.valueOf(attribute.toUpperCase()))
        )));
    }
}
