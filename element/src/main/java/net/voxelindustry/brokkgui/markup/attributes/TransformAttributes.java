package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.markup.MarkupAttribute;
import net.voxelindustry.brokkgui.text.GuiOverflow;

import java.util.ArrayList;
import java.util.List;

import static net.voxelindustry.brokkgui.data.RelativeBindingHelper.*;

public class TransformAttributes
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
        attributes.add(new MarkupAttribute("translate-x", (attribute, element) ->
                element.transform().xTranslate(Float.parseFloat(attribute))
        ));
        attributes.add(new MarkupAttribute("translate-y", (attribute, element) ->
                element.transform().yTranslate(Float.parseFloat(attribute))
        ));
        attributes.add(new MarkupAttribute("translate-z", (attribute, element) ->
        {
            var pos = Float.parseFloat(attribute);
            element.transform().zTranslate(pos);
        }));
        attributes.add(new MarkupAttribute("width", (attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                element.transform().widthRatio(ratio);
            }
            else
                element.transform().width(Float.parseFloat(attribute));
        }));
        attributes.add(new MarkupAttribute("height", (attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                element.transform().heightRatio(ratio);
            }
            else
                element.transform().height(Float.parseFloat(attribute));
        }));

        attributes.add(new MarkupAttribute("scale-x", ((attribute, element) ->
                element.transform().scaleX(Float.parseFloat(attribute)))
        ));
        attributes.add(new MarkupAttribute("scale-y", ((attribute, element) ->
                element.transform().scaleY(Float.parseFloat(attribute)))
        ));
        attributes.add(new MarkupAttribute("scale-z", ((attribute, element) ->
                element.transform().scaleZ(Float.parseFloat(attribute)))
        ));

        attributes.add(new MarkupAttribute("scale", ((attribute, element) ->
                element.transform().scale(Float.parseFloat(attribute)))
        ));

        attributes.add(new MarkupAttribute("overflow", ((attribute, element) ->
                element.transform().overflow(GuiOverflow.valueOf(attribute.toUpperCase())))
        ));
    }

    private static void createChildrenAttributes()
    {
        childrenAttributes.add(new MarkupAttribute("x", (attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                bindXToRelative(element.transform(), element.transform().parent(), ratio);
            }
            else
            {
                var pos = Float.parseFloat(attribute);
                bindXToPos(element.transform(), element.transform().parent(), pos);
            }
        }));

        childrenAttributes.add(new MarkupAttribute("y", (attribute, element) ->
        {
            if (attribute.endsWith("%"))
            {
                var ratio = Float.parseFloat(attribute.substring(0, attribute.length() - 1)) / 100F;
                bindYToRelative(element.transform(), element.transform().parent(), ratio);
            }
            else
            {
                var pos = Float.parseFloat(attribute);
                bindYToPos(element.transform(), element.transform().parent(), pos);
            }
        }));

        childrenAttributes.add(new MarkupAttribute("position", (attibute, element) ->
        {
            switch (attibute.toLowerCase())
            {
                case "centered" -> bindToCenter(element.transform(), element.transform().parent());
                case "left-top" -> bindToPos(element.transform(), element.transform().parent());
                case "right-top" -> bindToPos(element.transform(),
                        element.transform().parent(),
                        element.transform().parent().widthProperty().subtract(element.transform().widthProperty()),
                        null);
                case "left-bottom" -> bindToPos(element.transform(),
                        element.transform().parent(),
                        null,
                        element.transform().parent().heightProperty().subtract(element.transform().heightProperty()));
                case "right-bottom" -> bindToPos(element.transform(),
                        element.transform().parent(),
                        element.transform().parent().widthProperty().subtract(element.transform().widthProperty()),
                        element.transform().parent().heightProperty().subtract(element.transform().heightProperty()));
            }
        }));
    }
}
