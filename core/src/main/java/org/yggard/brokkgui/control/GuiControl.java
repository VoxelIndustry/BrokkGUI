package org.yggard.brokkgui.control;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.skin.GuiSkinBase;
import org.yggard.brokkgui.skin.IGuiSkinnable;

import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiControl extends GuiFather implements IGuiSkinnable
{
    private final BaseProperty<GuiSkinBase<?>> skinProperty;

    public GuiControl()
    {
        this.skinProperty = new BaseProperty<>(null, "skinProperty");
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        super.renderNode(renderer, pass, mouseX, mouseY);
        this.getSkin().render(pass, renderer, mouseX, mouseY);
    }

    protected abstract GuiSkinBase<?> makeDefaultSkin();

    @Override
    public BaseProperty<GuiSkinBase<?>> getSkinProperty()
    {
        if (this.skinProperty.getValue() == null)
            this.setSkin(this.makeDefaultSkin());
        return this.skinProperty;
    }

    @Override
    public void setSkin(final GuiSkinBase<?> value)
    {
        this.skinProperty.setValue(value);
    }
}