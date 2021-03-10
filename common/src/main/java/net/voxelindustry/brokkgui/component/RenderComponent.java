package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;

public interface RenderComponent
{
    void renderContent(IRenderCommandReceiver renderer, int mouseX, int mouseY);
}
