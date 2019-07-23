package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.impl.TextRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.style.StyleHolder;

public class TextRendererStyle extends TextRenderer
{
    private StyleHolder style;

    public TextRendererStyle()
    {

    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(StyleHolder.class))
            throw new GuiComponentException("Cannot attach TextRendererStyle to an element not having StyleHolder!");

        style = element.get(StyleHolder.class);

        style.registerProperty("color", Color.WHITE, Color.class);
        style.registerProperty("shadow-color", Color.BLACK, Color.class);
        style.registerProperty("shadow", false, Boolean.class);
    }

    private StyleHolder style()
    {
        return style;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @Override
    public BaseProperty<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = style().getOrCreateProperty("color", Color.class);
        return colorProperty;
    }

    @Override
    public BaseProperty<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = style().getOrCreateProperty("shadow-color", Color.class);
        return shadowColorProperty;
    }

    @Override
    public BaseProperty<Boolean> shadowProperty()
    {
        if (shadowProperty == null)
            shadowProperty = style().getOrCreateProperty("shadow", Boolean.class);
        return shadowProperty;
    }

    ////////////
    // VALUES //
    ////////////

    @Override
    public Color color()
    {
        return style().getStyleValue("color", Color.class, Color.WHITE);
    }

    @Override
    public void color(Color color)
    {
        style().setPropertyDirect("color", color, Color.class);
    }

    @Override
    public Color shadowColor()
    {
        return style().getStyleValue("shadow-color", Color.class, Color.BLACK);
    }

    @Override
    public void shadowColor(Color color)
    {
        style().setPropertyDirect("shadow-color", color, Color.class);
    }

    @Override
    public boolean shadow()
    {
        return style().getStyleValue("shadow", Boolean.class, false);
    }

    @Override
    public void shadow(boolean shadow)
    {
        style().setPropertyDirect("shadow", shadow, Boolean.class);
    }
}
