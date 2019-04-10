package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMappers;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandProperty;

import java.util.function.Consumer;

public class BorderImageProperties implements Consumer<StyleHolder>
{
    private static BorderImageProperties instance;

    public static BorderImageProperties getInstance()
    {
        if (instance == null)
            instance = new BorderImageProperties();
        return instance;
    }

    private BorderImageProperties()
    {

    }

    @Override
    public void accept(StyleHolder holder)
    {
        StyleProperty<Texture> borderTextureProperty = holder.registerProperty("border-image-source", Texture.EMPTY,
                Texture.class);

        ShorthandProperty<Float> borderImageWidthProperty = holder.registerShorthand("border-image-width", 1f,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "border-image-top-width", "border-image-right-width",
                "border-image-bottom-width", "border-image-left-width");

        ShorthandProperty<Float> borderImageOutsetProperty = holder.registerShorthand("border-image-outset", 1f,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "border-image-top-outset", "border-image-right-outset",
                "border-image-bottom-outset", "border-image-left-outset");

        ShorthandProperty<Float> borderImageSliceProperty = holder.registerShorthand("border-image-slice", 1f,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "border-image-top-slice", "border-image-right-slice",
                "border-image-bottom-slice", "border-image-left-slice");

        StyleProperty<Boolean> borderImageFill = holder.registerProperty("border-image-fill", false, Boolean.class);

        holder.registerGenericShorthand("border-image", "", borderTextureProperty,
                borderImageSliceProperty, borderImageWidthProperty, borderImageOutsetProperty, borderImageFill);
    }
}
