package org.yggard.brokkgui.element;

import org.yggard.brokkgui.behavior.GuiLinkBehavior;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.skin.GuiLabeledSkinBase;
import org.yggard.brokkgui.skin.GuiSkinBase;

import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiLink extends GuiLabeled
{
    private final BaseProperty<String> urlProperty;

    public GuiLink(final String url, final String text)
    {
        super("link", text);
        this.urlProperty = new BaseProperty<>(url, "urlProperty");
        this.setTextColor(Color.BLUE);
    }

    public GuiLink(final String url)
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