package net.voxelindustry.brokkgui.markup.attributes.elements;

import net.voxelindustry.brokkgui.component.impl.HorizontalLayout;
import net.voxelindustry.brokkgui.data.AlignmentMode;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttributesGroup;

import java.util.ArrayList;
import java.util.List;

public class HorizontalLayoutAttributes implements MarkupAttributesGroup
{
    private static final HorizontalLayoutAttributes instance = new HorizontalLayoutAttributes();

    public static HorizontalLayoutAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private HorizontalLayoutAttributes()
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
                element.get(HorizontalLayout.class).alignment(AlignmentMode.valueOf(attribute.toUpperCase()))
        )));
    }
}
