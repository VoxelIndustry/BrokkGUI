package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.component.IGuiTooltip;
import net.voxelindustry.brokkgui.control.GuiElement;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.GuiTooltipSkin;

public class GuiTooltip extends GuiElement implements IGuiTooltip
{
    private BaseProperty<GuiNode> contentProperty;

    public GuiTooltip(GuiNode content)
    {
        super("tooltip");

        this.setxTranslate(5);
        this.setyTranslate(5);

        this.addChild(content);
        RelativeBindingHelper.bindToPos(content, this);

        this.contentProperty = new BaseProperty<>(content, "contentProperty");
        this.contentProperty.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                this.removeChild(oldValue);
                oldValue.getxPosProperty().unbind();
                newValue.getyPosProperty().unbind();
                this.getWidthProperty().unbind();
                this.getHeightProperty().unbind();
            }
            if (newValue != null)
            {
                this.addChild(newValue);
                RelativeBindingHelper.bindToPos(content, this);
                RelativeBindingHelper.bindSizeRelative(this, newValue, 1, 1);
            }
        });
    }

    public GuiTooltip(String text, GuiNode icon)
    {
        this(new GuiLabel(text, icon));
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

    public void setOwner(GuiElement control)
    {
        if (control != null)
            this.getVisibleProperty().bind(control.getHoveredProperty());
        else
            this.getVisibleProperty().unbind();
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiTooltipSkin(this, new GuiBehaviorBase<>(this));
    }
}
