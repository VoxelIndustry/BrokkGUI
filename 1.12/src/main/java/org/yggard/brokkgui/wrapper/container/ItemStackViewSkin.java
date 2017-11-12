package org.yggard.brokkgui.wrapper.container;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.skin.GuiBehaviorSkinBase;
import org.yggard.brokkgui.wrapper.GuiHelper;

/**
 * @author Ourten 29 oct. 2016
 */
public class ItemStackViewSkin extends GuiBehaviorSkinBase<ItemStackView, ItemStackViewBehavior>
{
    public ItemStackViewSkin(final ItemStackView model, final ItemStackViewBehavior behavior)
    {
        super(model, behavior);
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        if (pass == EGuiRenderPass.SPECIAL)
        {
            ((GuiHelper) renderer.getHelper()).drawItemStack(renderer,
                    this.getModel().getxPos() + this.getModel().getxTranslate() + this.getModel().getWidth() / 2,
                    this.getModel().getyPos() + this.getModel().getyTranslate() + this.getModel().getHeight() / 2,
                    this.getModel().getWidth(), this.getModel().getHeight(), this.getModel().getzLevel(),
                    this.getModel().getItemStack(), this.getModel().getAlternateString(), this.getModel().getColor());
            if (this.getModel().isHovered() && this.getModel().hasTooltip())
                ((GuiHelper) renderer.getHelper()).drawItemStackTooltip(renderer, mouseX, mouseY,
                        this.getModel().getItemStack());
        }
    }
}