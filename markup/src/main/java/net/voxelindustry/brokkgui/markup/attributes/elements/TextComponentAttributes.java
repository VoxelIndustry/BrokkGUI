package net.voxelindustry.brokkgui.markup.attributes.elements;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttributesGroup;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class TextComponentAttributes implements MarkupAttributesGroup
{
    private static final TextComponentAttributes instance = new TextComponentAttributes();

    public static TextComponentAttributes instance()
    {
        return instance;
    }

    private final List<MarkupAttribute> attributes         = new ArrayList<>();
    private final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private TextComponentAttributes()
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
        attributes.add(new MarkupAttribute("line-spacing", ((attribute, element) ->
                element.get(TextComponent.class).lineSpacing(Integer.parseInt(attribute))
        )));
        attributes.add(new MarkupAttribute("text-alignment", ((attribute, element) ->
                element.get(TextComponent.class).textAlignment(RectAlignment.valueOf(attribute.toUpperCase()))
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
}
