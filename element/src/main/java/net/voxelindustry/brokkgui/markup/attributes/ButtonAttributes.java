package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class ButtonAttributes
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
    }

    private static void createChildrenAttributes()
    {
    }
}
