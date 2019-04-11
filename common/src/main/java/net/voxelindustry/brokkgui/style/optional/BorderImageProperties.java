package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.StyleProperty;

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

        StyleProperty<RectBox> borderImageWidthProperty = holder.registerProperty("border-image-width",
                RectBox.build().all(1).create(), RectBox.class);

        StyleProperty<RectBox> borderImageOutsetProperty = holder.registerProperty("border-image-outset",
                RectBox.build().all(0).create(), RectBox.class);

        StyleProperty<RectBox> borderImageSliceProperty = holder.registerProperty("border-image-slice",
                RectBox.build().all(1).create(), RectBox.class);

        StyleProperty<Boolean> borderImageFill = holder.registerProperty("border-image-fill", false, Boolean.class);

        holder.registerGenericShorthand("border-image", "", borderTextureProperty,
                borderImageSliceProperty, borderImageWidthProperty, borderImageOutsetProperty, borderImageFill);
    }
}
