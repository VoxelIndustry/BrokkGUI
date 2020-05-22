package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
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
    }

    private StyleComponent style()
    {
        return style;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @Override
    public BaseProperty<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = style().getOrCreateProperty("shadow-color", Color.class);
        return shadowColorProperty;
    }

    @Override
    public BaseProperty<Boolean> useShadowProperty()
    {
        if (useShadowProperty == null)
            useShadowProperty = style().getOrCreateProperty("shadow", Boolean.class);
        return useShadowProperty;
    }

    @Override
    public BaseProperty<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = style().getOrCreateProperty("color", Color.class);
        return colorProperty;
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
}
