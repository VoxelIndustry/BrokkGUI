package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.*;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.shape.GuiNode;
import net.voxelindustry.brokkgui.element.shape.Rectangle;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;

import javax.annotation.Nonnull;

public class Label extends GuiNode
{
    private Text text;
    private Icon icon;

    private TextRenderer textRenderer;

    public Label(String value)
    {
        text(value);
    }

    public Label()
    {
        this("");
    }

    @Override
    public void postConstruct()
    {
        text = add(Text.class);
        icon = add(Icon.class);
    }

    @Override
    public String type()
    {
        return "label";
    }

    @Override
    public ShapeDefinition shape()
    {
        return Rectangle.SHAPE;
    }

    @Override
    protected void refreshStyle(boolean useStyle)
    {
        super.refreshStyle(useStyle);

        if (useStyle)
        {
            textRenderer = new TextRendererStyle();
            remove(TextRenderer.class);
        }
        else
        {
            textRenderer = new TextRenderer();
            remove(TextRendererStyle.class);
        }

        this.textRenderer = add(textRenderer);
    }

    //////////
    // TEXT //
    //////////

    public Text textComponent()
    {
        return text;
    }

    public TextRenderer textRendererComponent()
    {
        return textRenderer;
    }

    public RectAlignment textAlignment()
    {
        return text.textAlignment();
    }

    public void textAlignment(RectAlignment alignment)
    {
        text.textAlignment(alignment);
    }

    public String text()
    {
        return text.text();
    }

    public void text(@Nonnull String text)
    {
        this.text.text(text);
    }

    public String ellipsis()
    {
        return text.ellipsis();
    }

    public void ellipsis(String ellipsis)
    {
        text.ellipsis(ellipsis);
    }

    public RectBox textPadding()
    {
        return text.textPadding();
    }

    public void textPadding(RectBox textPadding)
    {
        text.textPadding(textPadding);
    }

    public boolean expandToText()
    {
        return text.expandToText();
    }

    public void expandToText(boolean expandToText)
    {
        text.expandToText(expandToText);
    }

    //////////
    // ICON //
    //////////

    public Icon iconComponent()
    {
        return icon;
    }

    public GuiElement icon()
    {
        return icon.icon();
    }

    public void icon(GuiElement icon)
    {
        this.icon.icon(icon);
    }

    public RectSide iconSide()
    {
        return icon.iconSide();
    }

    public void iconSide(RectSide iconSide)
    {
        icon.iconSide(iconSide);
    }

    public float iconPadding()
    {
        return icon.iconPadding();
    }

    public void iconPadding(float iconPadding)
    {
        icon.iconPadding(iconPadding);
    }
}
