package org.yggard.brokkgui.internal;

import org.yggard.brokkgui.component.IGuiPopup;
import org.yggard.brokkgui.paint.RenderPass;

import java.util.ArrayList;
import java.util.List;

public class PopupHandler
{
    private static PopupHandler INSTANCE;

    public static PopupHandler getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new PopupHandler();
        return INSTANCE;
    }

    private List<IGuiPopup> popups;

    private PopupHandler()
    {
        this.popups = new ArrayList<>();
    }

    public void addPopup(IGuiPopup popup)
    {
        this.popups.add(popup);
    }

    public boolean removePopup(IGuiPopup popup)
    {
        return this.popups.remove(popup);
    }

    public boolean isPopupPresent(IGuiPopup popup)
    {
        return this.popups.contains(popup);
    }

    public void clearPopups()
    {
        this.popups.clear();
    }

    public void renderPopupInPass(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        this.popups.forEach(popup -> popup.renderNode(renderer, pass, mouseX, mouseY));
    }
}
