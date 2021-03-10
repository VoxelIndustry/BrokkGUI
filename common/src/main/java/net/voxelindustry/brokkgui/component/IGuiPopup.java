package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;

public interface IGuiPopup
{
    void renderNode(IRenderCommandReceiver renderer, int mouseX, int mouseY);
}
