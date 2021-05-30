package net.voxelindustry.brokkgui.mock;

import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IMouseUtil;
import net.voxelindustry.brokkgui.window.IGuiWindow;

public class MockMouseUtil implements IMouseUtil
{
    @Override
    public float getMouseX(IGuiWindow window)
    {
        return 0;
    }

    @Override
    public float getMouseY(IGuiWindow window)
    {
        return 0;
    }

    @Override
    public float getMouseX(IBrokkGuiImpl window)
    {
        return 0;
    }

    @Override
    public float getMouseY(IBrokkGuiImpl window)
    {
        return 0;
    }
}
