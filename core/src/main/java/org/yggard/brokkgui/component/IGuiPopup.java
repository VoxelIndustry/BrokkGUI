package org.yggard.brokkgui.component;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;

public interface IGuiPopup
{
    void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY);
}
