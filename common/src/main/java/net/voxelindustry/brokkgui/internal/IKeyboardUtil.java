package net.voxelindustry.brokkgui.internal;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IKeyboardUtil
{
    boolean isCtrlKeyDown();

    /**
     * @return if SHIFT_LEFT or SHIFT_RIGHT are down
     */
    boolean isShiftKeyDown();

    String getClipboardString();

    void setClipboardString(String text);

    int getKeyCode(String keyName);

    String getKeyName(int keyCode);
}