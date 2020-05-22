package net.voxelindustry.brokkgui.internal.profiler;

import net.voxelindustry.brokkgui.component.GuiElement;

public interface IProfiler
{
    void preElementRender(GuiElement node);

    void postElementRender(GuiElement node);

    void preElementStyleRefresh(GuiElement node);

    void postElementStyleRefresh(GuiElement node);

    void beginRenderFrame();

    void endRenderFrame();
}
