package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

public interface IGuiSkin
{
    void render(final RenderPass pass, final IGuiRenderer renderer, int mouseX, int mouseY);
}