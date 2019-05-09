package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.listener.ValueChangeListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.BaseSetProperty;
import net.voxelindustry.brokkgui.component.IGuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.element.popup.PopupHandler;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.function.Supplier;

public class GuiTooltip implements IGuiTooltip, ICascadeStyleable
{
    private BaseProperty<GuiNode>        contentProperty;
    private GuiElement                   owner;
    private ValueChangeListener<Boolean> ownerHoverListener;

    private float mouseXOffset, mouseYOffset;

    public GuiTooltip(GuiNode content, float mouseXOffset, float mouseYOffset)
    {
        this.ownerHoverListener = (observable, oldValue, newValue) ->
        {
            if (newValue)
                PopupHandler.getInstance(owner.getWindow()).addPopup(this);
            else
                PopupHandler.getInstance(owner.getWindow()).removePopup(this);
        };

        this.mouseXOffset = mouseXOffset;
        this.mouseYOffset = mouseYOffset;

        this.contentProperty = new BaseProperty<>(null, "contentProperty");

        this.setContent(content);
    }

    public GuiTooltip(GuiNode content)
    {
        this(content, 5, 5);
    }

    public GuiTooltip(String text, GuiNode icon)
    {
        this(new Label(text, icon), 5, 5);
    }

    public GuiTooltip(String text)
    {
        this(text, null);
    }

    public BaseProperty<GuiNode> getContentProperty()
    {
        return contentProperty;
    }

    public GuiNode getContent()
    {
        return this.getContentProperty().getValue();
    }

    public void setContent(GuiNode content)
    {
        this.getContentProperty().setValue(content);
    }

    @Override
    public void setOwner(GuiElement newOwner)
    {
        if (owner != null && owner != newOwner)
            owner.getHoveredProperty().removeListener(ownerHoverListener);
        if (newOwner != null)
            newOwner.getHoveredProperty().addListener(ownerHoverListener);
        this.owner = newOwner;
    }

    public float getMouseXOffset()
    {
        return mouseXOffset;
    }

    public void setMouseXOffset(float mouseXOffset)
    {
        this.mouseXOffset = mouseXOffset;
    }

    public float getMouseYOffset()
    {
        return mouseYOffset;
    }

    public void setMouseYOffset(float mouseYOffset)
    {
        this.mouseYOffset = mouseYOffset;
    }

    @Override
    public void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (getContent() == null)
            return;

        if (getContent().getxPos() != mouseX + mouseXOffset)
            getContent().getxPosProperty().setValue(mouseX + mouseXOffset);
        if (getContent().getyPos() != mouseY + mouseYOffset)
            getContent().getyPosProperty().setValue(mouseY + mouseYOffset);

        getContent().renderNode(renderer, pass, mouseX, mouseY);
    }

    ///////////
    // STYLE //
    ///////////

    @Override
    public ICascadeStyleable getParent()
    {
        return this.owner;
    }

    @Override
    public void setParent(ICascadeStyleable styleable)
    {
        throw new RuntimeException("Cannot set style parent of a Tooltip!");
    }

    @Override
    public void setStyleTree(Supplier<StyleList> treeSupplier)
    {
        this.getContent().setStyleTree(treeSupplier);
    }

    @Override
    public void setID(String id)
    {
        this.getContent().setID(id);
    }

    @Override
    public String getID()
    {
        return this.getContent().getID();
    }

    @Override
    public BaseSetProperty<String> getStyleClass()
    {
        return this.getContent().getStyleClass();
    }

    @Override
    public BaseSetProperty<String> getActivePseudoClass()
    {
        return this.getContent().getActivePseudoClass();
    }

    @Override
    public StyleHolder getStyle()
    {
        return this.getContent().getStyle();
    }

    @Override
    public String getType()
    {
        return this.getContent().getType();
    }

    @Override
    public void refreshStyle()
    {
        this.getContent().refreshStyle();
    }
}
