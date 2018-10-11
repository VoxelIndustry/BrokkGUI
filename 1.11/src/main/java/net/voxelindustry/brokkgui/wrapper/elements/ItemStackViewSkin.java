package net.voxelindustry.brokkgui.wrapper.elements;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.skin.GuiBehaviorSkinBase;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;

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
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        if (pass == GuiHelper.ITEM_MAIN)
        {
            ((GuiHelper) renderer.getHelper()).drawItemStack(renderer,
                    this.getModel().getxPos() + this.getModel().getxTranslate() + this.getModel().getWidth() / 2,
                    this.getModel().getyPos() + this.getModel().getyTranslate() + this.getModel().getHeight() / 2,
                    this.getModel().getWidth(), this.getModel().getHeight(), this.getModel().getzLevel(),
                    this.getModel().getItemStack(), this.getModel().getAlternateString(), this.getModel().getColor());
        }
        else if (pass == GuiHelper.ITEM_HOVER)
        {
            if (this.getModel().isHovered() && this.getModel().hasItemTooltip() &&
                    !this.getModel().getItemStack().isEmpty())
                ((GuiHelper) renderer.getHelper()).drawItemStackTooltip(renderer, mouseX, mouseY,
                        this.getModel().getItemStack());
        }
    }
}