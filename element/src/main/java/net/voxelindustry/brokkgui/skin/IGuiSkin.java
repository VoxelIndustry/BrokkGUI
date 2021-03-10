package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;

public interface IGuiSkin
{
    void render(IRenderCommandReceiver renderer, int mouseX, int mouseY);
}