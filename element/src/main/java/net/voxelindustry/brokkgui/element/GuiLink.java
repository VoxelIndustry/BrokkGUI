package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.LinkComponent;
import net.voxelindustry.brokkgui.control.GuiLabeled;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiLink extends GuiLabeled
{
    private LinkComponent linkComponent;

    public GuiLink(String url, String text, GuiElement icon)
    {
        super(text, icon);

        linkComponent.url(url);
    }

    public GuiLink(String url, String text)
    {
        this(url, text, null);
    }

    public GuiLink(String url)
    {
        this(url, url);
    }

    public GuiLink()
    {
        this("");
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        linkComponent = provide(LinkComponent.class);
    }

    public LinkComponent linkComponent()
    {
        return linkComponent;
    }

    @Override
    public String type()
    {
        return "link";
    }

    public Property<String> urlProperty()
    {
        return linkComponent.urlProperty();
    }

    public String url()
    {
        return linkComponent.url();
    }

    public void url(String url)
    {
        linkComponent.url(url);
    }
}