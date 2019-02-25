package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.element.GuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.skin.GuiBehaviorSkinBase;

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
