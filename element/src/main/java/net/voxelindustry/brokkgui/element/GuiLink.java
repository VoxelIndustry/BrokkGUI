package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.behavior.GuiLinkBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.skin.GuiLabeledSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiLink extends GuiLabeled
{
    private final Property<String> urlProperty;

    public GuiLink(String url, String text, GuiElement icon)
    {
        super(text, icon);
        urlProperty = new Property<>(url);
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
    public String type()
    {
        return "type";
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiLabeledSkin<>(this, new GuiLinkBehavior(this));
    }

    public Property<String> getUrlProperty()
    {
        return urlProperty;
    }

    public String getUrl()
    {
        return getUrlProperty().getValue();
    }

    public void setUrl(String URL)
    {
        getUrlProperty().setValue(URL);
    }
}