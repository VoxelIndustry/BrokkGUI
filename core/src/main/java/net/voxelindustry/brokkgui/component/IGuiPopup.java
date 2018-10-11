package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

public interface IGuiPopup
{
    void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY);
}
