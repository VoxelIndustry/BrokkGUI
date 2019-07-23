package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Icon;
import net.voxelindustry.brokkgui.component.impl.Text;
import net.voxelindustry.brokkgui.component.impl.TextRenderer;
import net.voxelindustry.brokkgui.style.StyleHolder;

public class Link extends GuiElement
{
    private final BaseProperty<String> urlProperty;

    public Link(String url, String text, GuiElement icon)
    {
        this.urlProperty = new BaseProperty<>(url, "urlProperty");

        add(Text.class);
        add(TextRenderer.class);
        add(Icon.class);

        this.get(StyleHolder.class).parseInlineCSS("text-color: blue");
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

    @Override
    public String type()
    {
        return "link";
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
}