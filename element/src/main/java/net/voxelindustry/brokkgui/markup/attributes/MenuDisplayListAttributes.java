package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.MenuDisplayListComponent;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.MarkupAttributesGroup;

import java.util.ArrayList;
import java.util.List;

public class MenuDisplayListAttributes implements MarkupAttributesGroup
{
    private static final MenuDisplayListAttributes instance = new MenuDisplayListAttributes();

    public static MenuDisplayListAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private MenuDisplayListAttributes()
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
        attributes.add(new MarkupAttribute("max-height", ((attribute, element) ->
                element.get(MenuDisplayListComponent.class).displayList().maxHeight(Float.parseFloat(attribute))
        )));
    }
}
