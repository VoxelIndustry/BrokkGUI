package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.impl.TextAssistComponent;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;

import java.util.ArrayList;
import java.util.List;

public class TextAssistAttributes
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
        attributes.add(new MarkupAttribute("prompt-always-displayed", ((attribute, element) ->
                element.get(TextAssistComponent.class).promptTextAlwaysDisplayed(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("prompt-text", ((attributeValue, element) ->
                element.get(TextAssistComponent.class).promptText(attributeValue)
        )));
        attributes.add(new MarkupAttribute("helper-text", ((attributeValue, element) ->
                element.get(TextAssistComponent.class).helperText(attributeValue)
        )));
        attributes.add(new MarkupAttribute("error-text", ((attributeValue, element) ->
                element.get(TextAssistComponent.class).errorText(attributeValue)
        )));
    }

    private static void createChildrenAttributes()
    {
        childrenAttributes.add(new MarkupAttribute("prompt-label", ((attributeValue, element) ->
                element.transform().parent().element().get(TextAssistComponent.class).promptTextLabel((GuiLabeled) element)
        )));
        childrenAttributes.add(new MarkupAttribute("helper-label", ((attributeValue, element) ->
                element.transform().parent().element().get(TextAssistComponent.class).helperTextLabel((GuiLabeled) element)
        )));
        childrenAttributes.add(new MarkupAttribute("error-label", ((attributeValue, element) ->
                element.transform().parent().element().get(TextAssistComponent.class).errorTextLabel((GuiLabeled) element)
        )));
    }
}
