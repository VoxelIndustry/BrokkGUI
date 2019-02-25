package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.IGuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.IGuiSkinnable;
import net.voxelindustry.brokkgui.style.StyleSource;

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

        this.getStyle().registerProperty("opacity", 1D, Double.class);
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (this.getOpacity() != 1)
            renderer.getHelper().startAlphaMask(this.getOpacity());

        this.getSkin().render(pass, renderer, mouseX, mouseY);
        super.renderContent(renderer, pass, mouseX, mouseY);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();
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

    public BaseProperty<Double> getOpacityProperty()
    {
        return this.getStyle().getStyleProperty("opacity", Double.class);
    }

    public double getOpacity()
    {
        return this.getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        this.getStyle().getStyleProperty("opacity", Double.class).setStyle(StyleSource.CODE, 0, opacity);
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