package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;

public interface RenderComponent
{
    void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY);
}
