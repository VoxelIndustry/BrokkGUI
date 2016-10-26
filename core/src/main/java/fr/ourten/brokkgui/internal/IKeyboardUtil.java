package fr.ourten.brokkgui.internal;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IKeyboardUtil
{
    /**
     *
     * @return if a key passed in parameter is translatable to a char writtable
     *         into a textfield
     */
    public boolean isKeyValidChar(final int key);

    public boolean isCtrlKeyDown();

    public String getClipboardString();

    public int getKeyCode(String keyName);

    public String getKeyName(int keyCode);
}