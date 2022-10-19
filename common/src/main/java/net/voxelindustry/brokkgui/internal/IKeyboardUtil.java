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

    boolean isAltDown();

    boolean isEnterDown();

    String getClipboardString();

    void setClipboardString(String text);

    int getScanCode(String keyName);

    String getKeyName(int scanCode);

    boolean isEnterKey(int keyCode);

    boolean isCtrlKey(int keyCode);

    boolean isShiftKey(int keyCode);

    boolean isAltKey(int keyCode);
}