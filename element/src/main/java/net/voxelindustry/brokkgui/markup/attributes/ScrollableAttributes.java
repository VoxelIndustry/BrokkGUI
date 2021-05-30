package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Scrollable;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.policy.GuiScrollbarPolicy;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScrollableAttributes
{
    private static final List<MarkupAttribute> attributes         = new ArrayList<>();
    private static final List<MarkupAttribute> childrenAttributes = new ArrayList<>();

    private static final List<String> attributesNames = new ArrayList<>();

    private static final Consumer<GuiElement> onAttributeAdded = element ->
    {
        if (!element.has(Scrollable.class))
            element.provide(Scrollable.class);
    };

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

    public static List<String> getAttributesNames()
    {
        if (attributesNames.isEmpty())
            attributes.stream().map(MarkupAttribute::name).forEach(attributesNames::add);
        return attributesNames;
    }

    public static Consumer<GuiElement> onAttributeAdded()
    {
        return onAttributeAdded;
    }

    private static void createAttributes()
    {
        attributes.add(new MarkupAttribute("grip-x-width", ((attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                element.get(Scrollable.class).gripX().transform().widthRatio(ratio);
            }
            else
                element.get(Scrollable.class).gripX().width(Float.parseFloat(attribute));
        })));
        attributes.add(new MarkupAttribute("grip-x-height", ((attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                element.get(Scrollable.class).gripX().transform().heightRatio(ratio);
            }
            else
                element.get(Scrollable.class).gripX().height(Float.parseFloat(attribute));
        })));
        attributes.add(new MarkupAttribute("grip-y-width", ((attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                element.get(Scrollable.class).gripY().transform().widthRatio(ratio);
            }
            else
                element.get(Scrollable.class).gripY().width(Float.parseFloat(attribute));
        })));
        attributes.add(new MarkupAttribute("grip-y-height", ((attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                element.get(Scrollable.class).gripY().transform().heightRatio(ratio);
            }
            else
                element.get(Scrollable.class).gripY().height(Float.parseFloat(attribute));
        })));
        attributes.add(new MarkupAttribute("scroll-x-policy", ((attribute, element) ->
                element.get(Scrollable.class).scrollXPolicy(GuiScrollbarPolicy.valueOf(attribute))
        )));
        attributes.add(new MarkupAttribute("scroll-y-policy", ((attribute, element) ->
                element.get(Scrollable.class).scrollYPolicy(GuiScrollbarPolicy.valueOf(attribute))
        )));

        attributes.add(new MarkupAttribute("scroll-speed", ((attribute, element) ->
                element.get(Scrollable.class).scrollSpeed(Float.parseFloat(attribute))
        )));
        attributes.add(new MarkupAttribute("pan-speed", ((attribute, element) ->
                element.get(Scrollable.class).panSpeed(Float.parseFloat(attribute))
        )));

        var rectBoxDecoder = StyleTranslator.getInstance().getDecoder(RectBox.class);
        attributes.add(new MarkupAttribute("scroll-padding", ((attribute, element) ->
                element.get(Scrollable.class).padding(rectBoxDecoder.decode(attribute))
        )));

        attributes.add(new MarkupAttribute("scrollable", ((attribute, element) ->
                element.get(Scrollable.class).scrollable(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("pannable", ((attribute, element) ->
                element.get(Scrollable.class).scrollable(Boolean.parseBoolean(attribute))
        )));
        attributes.add(new MarkupAttribute("scalable", ((attribute, element) ->
                element.get(Scrollable.class).scrollable(Boolean.parseBoolean(attribute))
        )));

        attributes.add(new MarkupAttribute("track-buttons", ((attribute, element) ->
                element.get(Scrollable.class).showTrackButtons(Boolean.parseBoolean(attribute))
        )));
    }

    private static void createChildrenAttributes()
    {
    }
}
