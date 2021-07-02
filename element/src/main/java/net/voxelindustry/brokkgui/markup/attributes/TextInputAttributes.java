package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.TextInputComponent;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.MarkupAttributesGroup;

import java.util.ArrayList;
import java.util.List;

public class TextInputAttributes implements MarkupAttributesGroup
{
    private static final TextInputAttributes instance = new TextInputAttributes();

    public static TextInputAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private TextInputAttributes()
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
        attributes.add(new MarkupAttribute("editable", ((attribute, element) ->
                element.get(TextInputComponent.class).editable(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("text-max-length", ((attribute, element) ->
                element.get(TextInputComponent.class).maxTextLength(Integer.parseInt(attribute))
        )));
    }
}
