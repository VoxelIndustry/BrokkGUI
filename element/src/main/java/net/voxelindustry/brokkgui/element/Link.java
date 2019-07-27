package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.TextRendererStyle;
import net.voxelindustry.brokkgui.component.delegate.IconDelegate;
import net.voxelindustry.brokkgui.component.delegate.TextDelegate;
import net.voxelindustry.brokkgui.component.impl.Icon;
import net.voxelindustry.brokkgui.component.impl.Text;
import net.voxelindustry.brokkgui.component.impl.TextRenderer;
import net.voxelindustry.brokkgui.element.shape.GuiNode;
import net.voxelindustry.brokkgui.element.shape.Rectangle;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.internal.DesktopUtils;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;

public class Link extends GuiNode implements TextDelegate, IconDelegate
{
    private Text text;
    private Icon icon;

    private TextRenderer textRenderer;

    private final BaseProperty<String> urlProperty;

    public Link(String url, String text, GuiElement icon)
    {
        text(text);

        // Setting the property will trigger invalidated listeners even with null
        if (icon != null)
            icon(icon);

        this.urlProperty = new BaseProperty<>(url, "urlProperty");

        getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::internalClick);
    }

    public Link(String url, String text)
    {
        this(url, text, null);
    }

    public Link(String url)
    {
        this(url, url);
    }

    public Link()
    {
        this("");
    }

    public BaseProperty<String> urlProperty()
    {
        return this.urlProperty;
    }

    public String url()
    {
        return this.urlProperty().getValue();
    }

    public void url(final String URL)
    {
        this.urlProperty().setValue(URL);
    }

    private void internalClick(ClickEvent.Left event)
    {
        DesktopUtils.openURL(url());
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
        return "link";
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