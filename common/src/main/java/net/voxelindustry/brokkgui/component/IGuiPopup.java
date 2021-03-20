package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;

public interface IGuiPopup
{
    void renderNode(IRenderCommandReceiver renderer, float mouseX, float mouseY);
}
