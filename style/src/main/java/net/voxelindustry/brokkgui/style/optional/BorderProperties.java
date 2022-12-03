package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.StyleProperty;

import java.util.function.Consumer;

public class BorderProperties implements Consumer<StyleComponent>
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
    public void accept(StyleComponent holder)
    {
        StyleProperty<Color> borderColorProperty = holder.registerProperty("border-color", Color.BLACK, Color.class);
        holder.registerGenericShorthand("border", "", borderColorProperty, holder.getProperty("border-width", Float.class));

        holder.registerProperty("border-box", RectBox.EMPTY, RectBox.class);
    }
}
