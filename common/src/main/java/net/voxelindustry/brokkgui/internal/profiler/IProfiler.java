package net.voxelindustry.brokkgui.internal.profiler;

import net.voxelindustry.brokkgui.component.GuiNode;

public interface IProfiler
{
    void preElementRender(GuiNode node);

    void postElementRender(GuiNode node);

    void preElementStyleRefresh(GuiNode node);

    void postElementStyleRefresh(GuiNode node);

    void beginRenderFrame();

    void endRenderFrame();
}
