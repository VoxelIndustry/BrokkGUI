package org.yggard.brokkgui.internal;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IKeyboardUtil
{
    /**
     * @return if a key passed in parameter is translatable to a char writtable
     * into a textfield
     */
    boolean isKeyValidChar(final int key);

    boolean isCtrlKeyDown();

    /**
     * @return if SHIFT_LEFT or SHIFT_RIGHT are down
     */
    boolean isShiftKeyDown();

    String getClipboardString();

    int getKeyCode(String keyName);

    String getKeyName(int keyCode);
}