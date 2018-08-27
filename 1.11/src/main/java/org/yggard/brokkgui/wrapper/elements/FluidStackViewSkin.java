package org.yggard.brokkgui.wrapper.elements;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.skin.GuiBehaviorSkinBase;
import org.yggard.brokkgui.wrapper.GuiHelper;

public class FluidStackViewSkin extends GuiBehaviorSkinBase<FluidStackView, FluidStackViewBehavior>
{
    public FluidStackViewSkin(FluidStackView model, FluidStackViewBehavior behavior)
    {
        super(model, behavior);
    }

    @Override
    public void render(RenderPass pass, IGuiRenderer renderer, int mouseX, int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (pass == RenderPass.MAIN)
        {
            ((GuiHelper) renderer.getHelper()).drawFluidStack(renderer,
                    this.getModel().getxPos() + this.getModel().getxTranslate(),
                    this.getModel().getyPos() + this.getModel().getyTranslate(),
                    this.getModel().getWidth(), this.getModel().getHeight(), this.getModel().getzLevel(),
                    this.getModel().getFluidStack(), this.getModel().isFlowing());
        }
    }
}
