package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.IGuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.IGuiSkinnable;

public abstract class GuiElement extends GuiFather implements IGuiSkinnable
{
    private final BaseProperty<GuiSkinBase<?>> skinProperty;
    private final BaseProperty<IGuiTooltip>    tooltipProperty;

    public GuiElement(String type)
    {
        super(type);

        this.skinProperty = new BaseProperty<>(null, "skinProperty");
        this.tooltipProperty = new BaseProperty<>(null, "tooltipProperty");

        this.tooltipProperty.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                PopupHandler.getInstance().removePopup(oldValue);
                oldValue.setOwner(null);
            }
            if (newValue != null)
            {
                PopupHandler.getInstance().addPopup(newValue);
                newValue.setOwner(this);
            }
        });
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        this.getSkin().render(pass, renderer, mouseX, mouseY);
        super.renderContent(renderer, pass, mouseX, mouseY);
    }

    protected abstract GuiSkinBase<?> makeDefaultSkin();

    @Override
    public GuiSkinBase<?> getSkin()
    {
        if (!this.skinProperty.isPresent())
            this.skinProperty.setValue(this.makeDefaultSkin());
        return this.skinProperty.getValue();
    }

    @Override
    public void setStyle(String style)
    {
        if (!this.skinProperty.isPresent())
            this.skinProperty.setValue(this.makeDefaultSkin());
        super.setStyle(style);
    }

    @Override
    public void refreshStyle()
    {
        if (!this.skinProperty.isPresent())
            this.skinProperty.setValue(this.makeDefaultSkin());
        super.refreshStyle();
    }

    public BaseProperty<IGuiTooltip> getTooltipProperty()
    {
        return this.tooltipProperty;
    }

    public IGuiTooltip getTooltip()
    {
        return this.getTooltipProperty().getValue();
    }

    public void setTooltip(IGuiTooltip tooltip)
    {
        this.getTooltipProperty().setValue(tooltip);
    }
}