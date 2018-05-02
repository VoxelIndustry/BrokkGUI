package org.yggard.brokkgui.internal;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.component.IGuiPopup;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.style.ICascadeStyleable;
import org.yggard.brokkgui.style.IStyleable;
import org.yggard.brokkgui.style.tree.StyleList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
            ((ICascadeStyleable) popup).setStyleTree(this.styleSupplier);
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

    public void clearPopups()
    {
        this.popups.clear();
        this.toAdd.clear();
        this.toRemove.clear();
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
                ((ICascadeStyleable) popup).setStyleTree(styleSupplier);
                ((ICascadeStyleable) popup).refreshStyle();
            }
        });
        this.toAdd.forEach(popup ->
        {
            if (popup instanceof ICascadeStyleable)
            {
                ((ICascadeStyleable) popup).setStyleTree(styleSupplier);
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
                ((ICascadeStyleable) popup).setStyleTree(styleSupplier);
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
}
