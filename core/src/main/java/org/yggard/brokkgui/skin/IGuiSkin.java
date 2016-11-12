package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;

public interface IGuiSkin
{
    void render(final EGuiRenderPass pass, final IGuiRenderer renderer, int mouseX, int mouseY);
}