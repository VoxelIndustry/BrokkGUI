package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.IGuiPopup;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.window.IGuiSubWindow;
import net.voxelindustry.brokkgui.window.IGuiWindow;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The Popup system needs a complete rework.
 * Transforms will be used a the basis for determining floating elements.
 */
@Deprecated
public class PopupHandler
{
    private static Map<IGuiSubWindow, PopupHandler> instances = new IdentityHashMap<>();

    public static PopupHandler getInstance(IGuiSubWindow window)
    {
        Objects.requireNonNull(window);

        if (!instances.containsKey(window))
            instances.put(window, new PopupHandler());
        return instances.get(window);
    }

    private List<IGuiPopup> popups;
    private List<IGuiPopup> toRemove;
    private List<IGuiPopup> toAdd;

    private PopupHandler()
    {
        popups = new ArrayList<>();
        toRemove = new ArrayList<>();
        toAdd = new ArrayList<>();
    }

    public void addPopup(IGuiPopup popup)
    {
        toAdd.add(popup);
    }

    public boolean removePopup(IGuiPopup popup)
    {
        return toRemove.add(popup);
    }

    public boolean isPopupPresent(IGuiPopup popup)
    {
        return popups.contains(popup);
    }

    public void delete(IGuiWindow window)
    {
        popups.clear();
        toAdd.clear();
        toRemove.clear();

        instances.remove(window);
    }

    public void renderPopupInPass(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        popups.removeIf(toRemove::contains);
        toRemove.clear();

        popups.addAll(toAdd);
        toAdd.clear();

        popups.forEach(popup -> popup.renderNode(renderer, pass, mouseX, mouseY));
    }

    public void handleHover(int mouseX, int mouseY)
    {
        popups.forEach(popup ->
        {
            if (popup instanceof GuiElement)
                ((GuiElement) popup).handleHover(mouseX, mouseY, ((GuiElement) popup).isPointInside(mouseX, mouseY));
        });
    }

    public void handleClick(int mouseX, int mouseY, MouseInputCode key)
    {
        popups.forEach(popup ->
        {
            if (popup instanceof GuiElement)
                ((GuiElement) popup).handleClick(mouseX, mouseY, key);
        });
    }

    public int getPopupCount()
    {
        return popups.size();
    }

    public List<IGuiPopup> getPopups()
    {
        return popups;
    }
}
