package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.MenuSelectComponent;
import net.voxelindustry.brokkgui.markup.ChildElementReceiver;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.MarkupAttributesGroup;
import net.voxelindustry.brokkgui.markup.MarkupElementReader;

import java.util.ArrayList;
import java.util.List;

public class MenuSelectAttributes implements MarkupAttributesGroup
{
    private static final MenuSelectAttributes instance = new MenuSelectAttributes();

    public static MenuSelectAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private final ChildElementReceiver childElementReceiver = new MenuSelectChildReceiver();

    private MenuSelectAttributes()
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

    @Override
    public ChildElementReceiver childElementReceiver()
    {
        return childElementReceiver;
    }

    private void createAttributes()
    {
        attributes.add(new MarkupAttribute("value", ((attribute, element) ->
                element.get(MenuSelectComponent.class).selectedValue(attribute)
        )));

        attributes.add(new MarkupAttribute("prompt-text", ((attribute, element) ->
                element.get(MenuSelectComponent.class).promptText(attribute)
        )));

        attributes.add(new MarkupAttribute("allow-empty", ((attribute, element) ->
                element.get(MenuSelectComponent.class).allowEmptySelection(Boolean.parseBoolean(attribute))
        )));
    }

    private static class MenuSelectChildReceiver implements ChildElementReceiver
    {
        @Override
        public boolean canReceive(String elementName)
        {
            return "option".equals(elementName);
        }

        @Override
        public void receive(String elementName, MarkupElementReader reader)
        {
            reader.getParentElement().get(MenuSelectComponent.class).addOption(reader.getText());
        }
    }
}
