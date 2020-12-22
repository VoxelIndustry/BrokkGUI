package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.shape.TextComponent;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class TextComponentStyle extends TextComponent
{
    private StyleComponent style;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(StyleComponent.class))
            throw new GuiComponentException("Cannot attach TextStyle to an element not having StyleComponent!");

        style = element.get(StyleComponent.class);

        style.registerProperty("shadow-color", Color.WHITE, Color.class);
        style.registerProperty("shadow", false, Boolean.class);
        style.registerProperty("color", Color.BLACK, Color.class);
        style.registerProperty("font", "default", String.class);
    }

    private StyleComponent style()
    {
        return style;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @Override
    public Property<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = style().getOrCreateProperty("shadow-color", Color.class);
        return shadowColorProperty;
    }

    @Override
    public Property<Boolean> useShadowProperty()
    {
        if (useShadowProperty == null)
            useShadowProperty = style().getOrCreateProperty("shadow", Boolean.class);
        return useShadowProperty;
    }

    @Override
    public Property<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = style().getOrCreateProperty("color", Color.class);
        return colorProperty;
    }

    @Override
    public Property<String> fontProperty()
    {
        if (fontProperty == null)
            fontProperty = style().getOrCreateProperty("font", String.class);
        return fontProperty;
    }

    ////////////
    // VALUES //
    ////////////

    @Override
    public Color shadowColor()
    {
        return style().getValue("shadow-color", Color.class, Color.WHITE);
    }

    @Override
    public void shadowColor(Color shadowColor)
    {
        style().setPropertyDirect("shadow-color", shadowColor, Color.class);
    }

    @Override
    public boolean useShadow()
    {
        return style().getValue("shadow", Boolean.class, false);
    }

    @Override
    public void useShadow(boolean useShadow)
    {
        style().setPropertyDirect("shadow", useShadow, Boolean.class);
    }

    @Override
    public Color color()
    {
        return style().getValue("color", Color.class, Color.BLACK);
    }

    @Override
    public void color(Color color)
    {
        style().setPropertyDirect("color", color, Color.class);
    }

    @Override
    public String font()
    {
        return style().getValue("font", String.class, "default");
    }

    @Override
    public void font(String font)
    {
        style().setPropertyDirect("font", font, String.class);
    }
}
