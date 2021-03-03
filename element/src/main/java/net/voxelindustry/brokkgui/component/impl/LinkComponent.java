package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.internal.DesktopUtils;

public class LinkComponent extends GuiComponent
{
    private final Property<String> urlProperty     = new Property<>();
    private final BooleanProperty  showLinkContent = new BooleanProperty();

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::onClick);
    }

    public void onClick(ClickEvent.Left e)
    {
        DesktopUtils.openURL(url());
    }

    public Property<String> urlProperty()
    {
        return urlProperty;
    }

    public String url()
    {
        return urlProperty().getValue();
    }

    public void url(String url)
    {
        urlProperty().setValue(url);
    }

    public BooleanProperty showLinkContentProperty()
    {
        return showLinkContent;
    }

    public boolean showLinkContent()
    {
        return showLinkContentProperty().get();
    }

    public void showLinkContent(boolean showLinkContent)
    {
        showLinkContentProperty().setValue(showLinkContent);
    }
}
