package org.yggard.brokkgui.paint;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.GuiNode;

import javax.annotation.Nonnull;

/**
 * @author Ourten 15 déc. 2016
 */
public class TextStyle
{
    private final BaseProperty<Color>   textColorProperty;
    private final BaseProperty<Color>   shadowColorProperty;
    private final BaseProperty<Boolean> shadowProperty;

    private GuiNode owner;

    public TextStyle(final Color textColor, final Color shadowColor, final boolean shadow)
    {
        this.textColorProperty = new BaseProperty<>(textColor, "textColorProperty");
        this.shadowColorProperty = new BaseProperty<>(shadowColor, "shadowColorProperty");
        this.shadowProperty = new BaseProperty<>(shadow, "shadowProperty");
    }

    public TextStyle(final @Nonnull Color textColor, final @Nonnull Color shadowColor)
    {
        this(textColor, shadowColor, true);
    }

    public TextStyle(final Color textColor)
    {
        this(textColor, null, false);
    }

    public Color getTextColor()
    {
        return this.owner.getStyle().getStyleProperty("-text-color", Color.class).getValue();
    }

    public Color getShadowColor()
    {
        return this.owner.getStyle().getStyleProperty("-text-shadow-color", Color.class).getValue();
    }

    public boolean useShadow()
    {
        return this.owner.getStyle().getStyleProperty("-text-shadow", Boolean.class).getValue();
    }

    public void bind(GuiNode node)
    {
        node.getStyle().registerProperty("-text-color", Color.BLACK, Color.class);
        node.getStyle().registerProperty("-text-shadow-color", Color.WHITE, Color.class);
        node.getStyle().registerProperty("-text-shadow", true, Boolean.class);

        this.owner = node;
    }

    public void unbind(GuiNode node)
    {
        node.getStyle().removeProperty("-text-color");
        node.getStyle().removeProperty("-text-shadow-color");
        node.getStyle().removeProperty("-text-shadow");

        this.owner = null;
    }
}