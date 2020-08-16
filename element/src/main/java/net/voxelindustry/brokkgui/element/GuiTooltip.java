package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.listener.ValueChangeListener;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.IGuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.hermod.EventDispatcher;

public class GuiTooltip implements IGuiTooltip
{
    private EventDispatcher eventDispatcher;

    private Property<GuiElement>         contentProperty;
    private GuiElement                   owner;
    private ValueChangeListener<Boolean> ownerHoverListener;

    private float mouseXOffset, mouseYOffset;

    public GuiTooltip(GuiElement content, float mouseXOffset, float mouseYOffset)
    {
        ownerHoverListener = (observable, oldValue, newValue) ->
        {
            if (newValue)
                PopupHandler.getInstance(owner.getWindow()).addPopup(this);
            else
                PopupHandler.getInstance(owner.getWindow()).removePopup(this);
        };

        this.mouseXOffset = mouseXOffset;
        this.mouseYOffset = mouseYOffset;

        contentProperty = new Property<>(null);

        setContent(content);
    }

    public GuiTooltip(GuiElement content)
    {
        this(content, 5, 5);
    }

    public GuiTooltip(String text, GuiElement icon)
    {
        this(new GuiLabel(text, icon), 5, 5);
    }

    public GuiTooltip(String text)
    {
        this(text, null);
    }

    public Property<GuiElement> getContentProperty()
    {
        return contentProperty;
    }

    public GuiElement getContent()
    {
        return getContentProperty().getValue();
    }

    public void setContent(GuiElement content)
    {
        getContentProperty().setValue(content);
    }

    @Override
    public void setOwner(GuiElement newOwner)
    {
        if (owner != null && owner != newOwner)
            owner.hoveredProperty().removeListener(ownerHoverListener);
        if (newOwner != null)
            newOwner.hoveredProperty().addListener(ownerHoverListener);
        owner = newOwner;
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

        if (getContent().transform().xPos() != mouseX + mouseXOffset)
            getContent().transform().xPosProperty().setValue(mouseX + mouseXOffset);
        if (getContent().transform().yPos() != mouseY + mouseYOffset)
            getContent().transform().yPosProperty().setValue(mouseY + mouseYOffset);

        getContent().renderNode(renderer, pass, mouseX, mouseY);
    }
}
