package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public interface IGuiPopup
{
    void renderNode(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY);
}
