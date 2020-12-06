package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.IGuiTooltip;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.IGuiSkinnable;
import net.voxelindustry.brokkgui.style.StyleComponent;

@Deprecated
public abstract class GuiSkinedElement extends GuiFather implements IGuiSkinnable
{
    private final Property<GuiSkinBase<?>> skinProperty;
    private final Property<IGuiTooltip>    tooltipProperty;

    private StyleComponent style;

    public GuiSkinedElement()
    {
        skinProperty = new Property<>(null);
        tooltipProperty = new Property<>(null);

        tooltipProperty.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
                oldValue.setOwner(null);
            if (newValue != null)
                newValue.setOwner(this);
        });
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        style = get(StyleComponent.class);

        style.onStyleInit(() ->
        {
            if (!skinProperty.isPresent())
                skinProperty.setValue(makeDefaultSkin());
        });
    }

    public StyleComponent style()
    {
        return style;
    }

    @Override
    public void renderContent(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (getSkin() != null)
            getSkin().render(pass, renderer, mouseX, mouseY);
        super.renderContent(renderer, pass, mouseX, mouseY);
    }

    protected abstract GuiSkinBase<?> makeDefaultSkin();

    @Override
    public GuiSkinBase<?> getSkin()
    {
        if (!skinProperty.isPresent())
            skinProperty.setValue(makeDefaultSkin());
        return skinProperty.getValue();
    }

    public Property<IGuiTooltip> getTooltipProperty()
    {
        return tooltipProperty;
    }

    public IGuiTooltip getTooltip()
    {
        return getTooltipProperty().getValue();
    }

    public void setTooltip(IGuiTooltip tooltip)
    {
        getTooltipProperty().setValue(tooltip);
    }
}