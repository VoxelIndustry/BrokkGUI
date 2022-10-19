package net.voxelindustry.brokkgui.mock;

import net.voxelindustry.brokkgui.internal.IKeyboardUtil;

public class MockKeyboardUtil implements IKeyboardUtil
{

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
    public boolean isAltDown()
    {
        return false;
    }

    @Override
    public boolean isEnterDown()
    {
        return false;
    }

    @Override
    public String getClipboardString()
    {
        return "";
    }

    @Override
    public void setClipboardString(String text)
    {
        
    }

    @Override
    public int getScanCode(String keyName)
    {
        return 0;
    }

    @Override
    public String getKeyName(int scanCode)
    {
        return "";
    }

    @Override
    public boolean isEnterKey(int keyCode)
    {
        return false;
    }

    @Override
    public boolean isCtrlKey(int keyCode)
    {
        return false;
    }

    @Override
    public boolean isShiftKey(int keyCode)
    {
        return false;
    }

    @Override
    public boolean isAltKey(int keyCode)
    {
        return false;
    }
}
