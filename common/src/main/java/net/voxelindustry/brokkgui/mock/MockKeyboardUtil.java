package net.voxelindustry.brokkgui.mock;

import net.voxelindustry.brokkgui.internal.IKeyboardUtil;

public class MockKeyboardUtil implements IKeyboardUtil
{
    @Override
    public boolean isKeyValidChar(int key)
    {
        return true;
    }

    @Override
    public boolean isCtrlKeyDown()
    {
        return false;
    }

    @Override
    public boolean isShiftKeyDown()
    {
        return false;
    }

    @Override
    public String getClipboardString()
    {
        return "";
    }

    @Override
    public int getKeyCode(String keyName)
    {
        return 0;
    }

    @Override
    public String getKeyName(int keyCode)
    {
        return "";
    }
}
