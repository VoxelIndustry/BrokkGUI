package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.component.IGuiPopup;
import net.voxelindustry.brokkgui.gui.IGuiSubWindow;
import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.style.ICascadeStyleable;
import net.voxelindustry.brokkgui.style.IStyleable;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

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

    private Supplier<StyleList> styleSupplier;

    private PopupHandler()
    {
        this.popups = new ArrayList<>();
        this.toRemove = new ArrayList<>();
        this.toAdd = new ArrayList<>();
    }

    public void addPopup(IGuiPopup popup)
    {
        this.toAdd.add(popup);

        if (popup instanceof ICascadeStyleable)
        {
            ((ICascadeStyleable) popup).setStyleListSupplier(this.styleSupplier);
            ((ICascadeStyleable) popup).refreshStyle();
        }
    }

    public boolean removePopup(IGuiPopup popup)
    {
        return this.toRemove.add(popup);
    }

    public boolean isPopupPresent(IGuiPopup popup)
    {
        return this.popups.contains(popup);
    }

    public void delete(IGuiWindow window)
    {
        this.popups.clear();
        this.toAdd.clear();
        this.toRemove.clear();

        instances.remove(window);
    }

    public void renderPopupInPass(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        this.popups.removeIf(toRemove::contains);
        toRemove.clear();

        this.popups.addAll(toAdd);
        toAdd.clear();

        this.popups.forEach(popup -> popup.renderNode(renderer, pass, mouseX, mouseY));
    }

    public void setStyleSupplier(Supplier<StyleList> styleSupplier)
    {
        this.styleSupplier = styleSupplier;

        this.popups.forEach(popup ->
        {
            if (popup instanceof ICascadeStyleable)
            {
                ((ICascadeStyleable) popup).setStyleListSupplier(styleSupplier);
                ((ICascadeStyleable) popup).refreshStyle();
            }
        });
        this.toAdd.forEach(popup ->
        {
            if (popup instanceof ICascadeStyleable)
            {
                ((ICascadeStyleable) popup).setStyleListSupplier(styleSupplier);
                ((ICascadeStyleable) popup).refreshStyle();
            }
        });
    }

    public void refreshStyle()
    {
        this.popups.forEach(popup ->
        {
            if (popup instanceof IStyleable)
                ((IStyleable) popup).refreshStyle();
        });
        this.toAdd.forEach(popup ->
        {
            if (popup instanceof ICascadeStyleable)
            {
                ((ICascadeStyleable) popup).setStyleListSupplier(styleSupplier);
                ((ICascadeStyleable) popup).refreshStyle();
            }
        });
    }

    public void handleHover(int mouseX, int mouseY)
    {
        popups.forEach(popup ->
        {
            if (popup instanceof GuiNode)
                ((GuiNode) popup).handleHover(mouseX, mouseY, ((GuiNode) popup).isPointInside(mouseX, mouseY));
        });
    }

    public void handleClick(int mouseX, int mouseY, int key)
    {
        popups.forEach(popup ->
        {
            if (popup instanceof GuiNode)
                ((GuiNode) popup).handleClick(mouseX, mouseY, key);
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
