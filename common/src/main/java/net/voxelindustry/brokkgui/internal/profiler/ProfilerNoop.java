package net.voxelindustry.brokkgui.internal.profiler;

import net.voxelindustry.brokkgui.component.GuiNode;

public class ProfilerNoop implements IProfiler
{
    @Override
    public void preElementRender(GuiNode node)
    {
        // NOOP
    }

    @Override
    public void postElementRender(GuiNode node)
    {
        // NOOP
    }

    @Override
    public void preElementStyleRefresh(GuiNode node)
    {
        // NOOP
    }

    @Override
    public void postElementStyleRefresh(GuiNode node)
    {
        // NOOP
    }

    @Override
    public void beginRenderFrame()
    {
        // NOOP
    }

    @Override
    public void endRenderFrame()
    {
        // NOOP
    }
}
