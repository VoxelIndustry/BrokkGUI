package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiLinkBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.skin.GuiLabeledSkinBase;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiLink extends GuiLabeled
{
    private final BaseProperty<String> urlProperty;

    public GuiLink(String url, String text, GuiElement icon)
    {
        super(text, icon);
        urlProperty = new BaseProperty<>(url, "urlProperty");
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
        return new GuiLabeledSkinBase<>(this, new GuiLinkBehavior(this));
    }

    public BaseProperty<String> getUrlProperty()
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