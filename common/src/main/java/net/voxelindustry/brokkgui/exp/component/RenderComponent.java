package net.voxelindustry.brokkgui.exp.component;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

public interface RenderComponent
{
    void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY);
}
