package net.voxelindustry.brokkgui.markup.attributes;

import java.util.ArrayList;
import java.util.List;

public class ButtonAttributes implements MarkupAttributesGroup
{
    private static final ButtonAttributes instance = new ButtonAttributes();

    public static ButtonAttributes instance()
    {
        return instance;
    }

    private ButtonAttributes()
    {

    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    @Override
    public List<MarkupAttribute> getAttributes()
    {
        return attributes;
    }

    @Override
    public List<MarkupAttribute> getChildrenAttributes()
    {
        return childrenAttributes;
    }
}
