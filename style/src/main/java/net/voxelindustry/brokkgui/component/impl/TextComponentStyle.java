package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.text.TextComponent;

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
        style.registerProperty("font-size", BrokkGuiPlatform.getInstance().getTextHelper().getDefaultFontSize(), Float.class);

        style.registerProperty("strikethrough", false, Boolean.class);
        style.registerProperty("italic", false, Boolean.class);
        style.registerProperty("bold", false, Boolean.class);
        style.registerProperty("underline", false, Boolean.class);

        style.registerProperty("text-outline-color", Color.ALPHA, Color.class);
        style.registerProperty("text-outline-width", 0F, Float.class);
        style.registerProperty("text-glow-color", Color.ALPHA, Color.class);
        style.registerProperty("text-glow-width", 0F, Float.class);
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

    @Override
    public Property<Float> fontSizeProperty()
    {
        if (fontSizeProperty == null)
            fontSizeProperty = style().getOrCreateProperty("font-size", Float.class);
        return fontSizeProperty;
    }

    @Override
    public Property<Boolean> strikeThroughProperty()
    {
        if (strikeThroughProperty == null)
            strikeThroughProperty = style().getOrCreateProperty("strikethrough", Boolean.class);
        return strikeThroughProperty;
    }

    @Override
    public Property<Boolean> italicProperty()
    {
        if (italicProperty == null)
            italicProperty = style().getOrCreateProperty("italic", Boolean.class);
        return italicProperty;
    }

    @Override
    public Property<Boolean> boldProperty()
    {
        if (boldProperty == null)
            boldProperty = style().getOrCreateProperty("bold", Boolean.class);
        return boldProperty;
    }

    @Override
    public Property<Boolean> underlineProperty()
    {
        if (underlineProperty == null)
            underlineProperty = style().getOrCreateProperty("underline", Boolean.class);
        return underlineProperty;
    }


    @Override
    public Property<Color> outlineColorProperty()
    {
        if (outlineColorProperty == null)
            outlineColorProperty = style().getOrCreateProperty("text-outline-color", Color.class);
        return outlineColorProperty;
    }

    @Override
    public Property<Float> outlineWidthProperty()
    {
        if (outlineWidthProperty == null)
            outlineWidthProperty = style().getOrCreateProperty("text-outline-width", Float.class);
        return outlineWidthProperty;
    }

    @Override
    public Property<Color> glowColorProperty()
    {
        if (glowColorProperty == null)
            glowColorProperty = style().getOrCreateProperty("text-glow-color", Color.class);
        return glowColorProperty;
    }

    @Override
    public Property<Float> glowWidthProperty()
    {
        if (glowWidthProperty == null)
            glowWidthProperty = style().getOrCreateProperty("text-glow-width", Float.class);
        return glowWidthProperty;
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

    @Override
    public float fontSize()
    {
        return style().getValue("font-size", Float.class, BrokkGuiPlatform.getInstance().getTextHelper().getDefaultFontSize());
    }

    @Override
    public void fontSize(float fontSize)
    {
        style().setPropertyDirect("font-size", fontSize, Float.class);
    }

    @Override
    public boolean strikeThrough()
    {
        return style().getValue("strikethrough", Boolean.class, false);
    }

    @Override
    public void strikeThrough(boolean strikeThrough)
    {
        style().setPropertyDirect("strikethrough", strikeThrough, Boolean.class);
    }

    @Override
    public boolean italic()
    {
        return style().getValue("italic", Boolean.class, false);
    }

    @Override
    public void italic(boolean italic)
    {
        style().setPropertyDirect("italic", italic, Boolean.class);
    }

    @Override
    public boolean bold()
    {
        return style().getValue("bold", Boolean.class, false);
    }

    @Override
    public void bold(boolean bold)
    {
        style().setPropertyDirect("bold", bold, Boolean.class);
    }

    @Override
    public boolean underline()
    {
        return style().getValue("underline", Boolean.class, false);
    }

    @Override
    public void underline(boolean underline)
    {
        style().setPropertyDirect("underline", underline, Boolean.class);
    }

    @Override
    public Color outlineColor()
    {
        return style().getValue("text-outline-color", Color.class, Color.ALPHA);
    }

    @Override
    public void outlineColor(Color outlineColor)
    {
        style().setPropertyDirect("text-outline-color", outlineColor, Color.class);
    }

    @Override
    public float outlineWidth()
    {
        return style().getValue("text-outline-width", Float.class, 0F);
    }

    @Override
    public void outlineWidth(float outlineWidth)
    {
        style().setPropertyDirect("text-outline-width", outlineWidth, Float.class);
    }

    @Override
    public Color glowColor()
    {
        return style().getValue("text-glow-color", Color.class, Color.ALPHA);
    }

    @Override
    public void glowColor(Color glowColor)
    {
        style().setPropertyDirect("text-glow-color", glowColor, Color.class);
    }

    @Override
    public float glowWidth()
    {
        return style().getValue("text-glow-width", Float.class, 0F);
    }

    @Override
    public void glowWidth(float glowWidth)
    {
        style().setPropertyDirect("text-glow-width", glowWidth, Float.class);
    }
}
