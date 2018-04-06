package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiTooltip;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;

public class GuiTooltipSkin extends GuiBehaviorSkinBase<GuiTooltip, GuiBehaviorBase<GuiTooltip>>
{
    public GuiTooltipSkin(GuiTooltip model, GuiBehaviorBase<GuiTooltip> behavior)
    {
        super(model, behavior);
    }

    @Override
    public void render(RenderPass pass, IGuiRenderer renderer, int mouseX, int mouseY)
    {
        if (this.getModel().getxPos() != mouseX)
            this.getModel().getxPosProperty().setValue((float) mouseX);
        if (this.getModel().getyPos() != mouseY)
            this.getModel().getyPosProperty().setValue((float) mouseY);

        super.render(pass, renderer, mouseX, mouseY);
    }
}
