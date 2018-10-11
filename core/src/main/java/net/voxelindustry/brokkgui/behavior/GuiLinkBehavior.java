package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.element.GuiLink;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.internal.DesktopUtils;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiLinkBehavior extends GuiBehaviorBase<GuiLink>
{
    public GuiLinkBehavior(final GuiLink model)
    {
        super(model);

        // TODO: hover system to display link content
        this.getModel().getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::onClick);
    }

    public void onClick(final ClickEvent.Left e)
    {
        DesktopUtils.openURL(this.getModel().getUrl());
    }
}