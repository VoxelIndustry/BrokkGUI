package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;

public interface IGuiSkin
{
    void render(final RenderPass pass, final IGuiRenderer renderer, int mouseX, int mouseY);
}