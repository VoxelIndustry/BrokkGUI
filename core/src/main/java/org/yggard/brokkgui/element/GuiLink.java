package org.yggard.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.behavior.GuiLinkBehavior;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.skin.GuiLabeledSkinBase;
import org.yggard.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiLink extends GuiLabeled
{
    private final BaseProperty<String> urlProperty;

    public GuiLink(String url, String text, GuiNode icon)
    {
        super("link", text, icon);
        this.urlProperty = new BaseProperty<>(url, "urlProperty");
        this.setStyle("-text-color: blue");
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
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiLabeledSkinBase<>(this, new GuiLinkBehavior(this));
    }

    public BaseProperty<String> getUrlProperty()
    {
        return this.urlProperty;
    }

    public String getUrl()
    {
        return this.getUrlProperty().getValue();
    }

    public void setUrl(final String URL)
    {
        this.getUrlProperty().setValue(URL);
    }
}