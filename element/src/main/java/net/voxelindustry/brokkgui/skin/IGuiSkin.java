package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public interface IGuiSkin
{
    void render(RenderPass pass, IRenderCommandReceiver renderer, int mouseX, int mouseY);
}