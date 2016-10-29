package fr.ourten.brokkgui.wrapper.container;

import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.skin.GuiBehaviorSkinBase;
import fr.ourten.brokkgui.wrapper.GuiHelper;

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
        if (pass == EGuiRenderPass.MAIN)
            ((GuiHelper) renderer.getHelper()).drawItemStack(renderer,
                    this.getModel().getxPos() + this.getModel().getxTranslate() + this.getModel().getWidth() / 2,
                    this.getModel().getyPos() + this.getModel().getyTranslate() + this.getModel().getHeight() / 2,
                    this.getModel().getWidth(), this.getModel().getHeight(), this.getModel().getzLevel(),
                    this.getModel().getItemStack(), this.getModel().getAlternateString());
    }
}