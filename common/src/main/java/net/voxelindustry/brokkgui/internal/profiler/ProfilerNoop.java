package net.voxelindustry.brokkgui.internal.profiler;

import net.voxelindustry.brokkgui.component.GuiElement;

public class ProfilerNoop implements IProfiler
{
    @Override
    public void preElementRender(GuiElement node)
    {
        // NOOP
    }

    @Override
    public void postElementRender(GuiElement node)
    {
        // NOOP
    }

    @Override
    public void preElementStyleRefresh(GuiElement node)
    {
        // NOOP
    }

    @Override
    public void postElementStyleRefresh(GuiElement node)
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
