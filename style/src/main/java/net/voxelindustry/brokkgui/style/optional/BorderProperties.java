package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMappers;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandProperty;

import java.util.function.Consumer;

public class BorderProperties implements Consumer<StyleHolder>
{
    private static BorderProperties instance;

    public static BorderProperties getInstance()
    {
        if (instance == null)
            instance = new BorderProperties();
        return instance;
    }

    private BorderProperties()
    {

    }

    @Override
    public void accept(StyleHolder holder)
    {
        StyleProperty<Color> borderColorProperty = holder.registerProperty("border-color", Color.BLACK, Color.class);

        ShorthandProperty<Float> borderWidthProperty = holder.registerShorthand("border-width", 0f,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "border-top-width", "border-right-width", "border-bottom-width", "border-left-width");


        holder.registerShorthand("border-radius", 0, Integer.class, ShorthandArgMappers.BOX_MAPPER,
                "border-top-left-radius", "border-top-right-radius",
                "border-bottom-right-radius", "border-bottom-left-radius");

        holder.registerGenericShorthand("border", "", borderColorProperty, borderWidthProperty);
    }
}
