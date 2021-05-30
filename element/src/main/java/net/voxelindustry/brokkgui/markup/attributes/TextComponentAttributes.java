package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class TextComponentAttributes
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
        attributes.add(new MarkupAttribute("line-spacing", ((attribute, element) ->
                element.get(TextComponent.class).lineSpacing(Integer.parseInt(attribute))
        )));
        attributes.add(new MarkupAttribute("text-alignment", ((attribute, element) ->
                element.get(TextComponent.class).textAlignment(RectAlignment.valueOf(attribute))
        )));
        attributes.add(new MarkupAttribute("multiline", ((attribute, element) ->
                element.get(TextComponent.class).multiline(Boolean.parseBoolean(attribute))
        )));

        var rectBoxDecoder = StyleTranslator.getInstance().getDecoder(RectBox.class);
        attributes.add(new MarkupAttribute("text-padding", ((attribute, element) ->
                element.get(TextComponent.class).textPadding(rectBoxDecoder.decode(attribute))
        )));

        attributes.add(new MarkupAttribute("text", ((attribute, element) ->
                element.get(TextComponent.class).text(attribute)
        )));
    }

    private static void createChildrenAttributes()
    {
    }
}
