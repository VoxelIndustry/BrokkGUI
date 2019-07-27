package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.TextRendererStyle;
import net.voxelindustry.brokkgui.component.delegate.IconDelegate;
import net.voxelindustry.brokkgui.component.delegate.TextDelegate;
import net.voxelindustry.brokkgui.component.impl.Icon;
import net.voxelindustry.brokkgui.component.impl.Text;
import net.voxelindustry.brokkgui.component.impl.TextRenderer;
import net.voxelindustry.brokkgui.element.shape.GuiNode;
import net.voxelindustry.brokkgui.element.shape.Rectangle;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;

public class Label extends GuiNode implements TextDelegate, IconDelegate
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

        icon.elementContentPaddingProperty(text.textPaddingProperty());
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

        TextRenderer textRenderer;
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

    @Override
    public Text textComponent()
    {
        return text;
    }

    @Override
    public TextRenderer textRendererComponent()
    {
        return textRenderer;
    }

    //////////
    // ICON //
    //////////

    @Override
    public Icon iconComponent()
    {
        return icon;
    }
}
