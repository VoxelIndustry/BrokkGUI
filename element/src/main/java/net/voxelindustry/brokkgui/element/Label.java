package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Icon;
import net.voxelindustry.brokkgui.component.Text;
import net.voxelindustry.brokkgui.component.TextRenderer;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.style.StyleHolder;

import javax.annotation.Nonnull;

public class Label extends GuiElement
{
    private final Text         text;
    private final TextRenderer textRenderer;
    private final Icon         icon;

    public Label(String value)
    {
        text = add(Text.class);
        textRenderer = add(TextRenderer.class);
        icon = add(Icon.class);

        text(value);
    }

    public Label()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "label";
    }

    public StyleHolder style()
    {
        return this.get(StyleHolder.class);
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
