package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMappers;

import java.util.function.Consumer;

public class OutlineProperties implements Consumer<StyleComponent>
{
    private static OutlineProperties instance;

    public static OutlineProperties getInstance()
    {
        if (instance == null)
            instance = new OutlineProperties();
        return instance;
    }

    private OutlineProperties()
    {

    }

    @Override
    public void accept(StyleComponent holder)
    {
        StyleProperty<Color> outlineColorProperty = holder.registerProperty("outline-color", Color.ALPHA, Color.class);

        holder.registerShorthand("outline-width", 0F,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "outline-top-width", "outline-right-width", "outline-bottom-width", "outline-left-width");

        holder.registerGenericShorthand("outline", "", outlineColorProperty, holder.getProperty("outline-width", Float.class));

        holder.registerProperty("outline-box", RectBox.EMPTY, RectBox.class);

        holder.registerShorthand("outline-radius", 0, Integer.class, ShorthandArgMappers.BOX_MAPPER,
                "outline-top-left-radius", "outline-top-right-radius",
                "outline-bottom-right-radius", "outline-bottom-left-radius");
    }
}
