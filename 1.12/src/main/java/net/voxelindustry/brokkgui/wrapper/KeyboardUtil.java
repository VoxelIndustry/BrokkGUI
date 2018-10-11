package net.voxelindustry.brokkgui.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class KeyboardUtil implements IKeyboardUtil
{
    @Override
    public boolean isKeyValidChar(int key)
    {
        return ChatAllowedCharacters.isAllowedCharacter(Keyboard.getEventCharacter());
    }

    @Override
    public boolean isCtrlKeyDown()
    {
        return Minecraft.IS_RUNNING_ON_MAC
                ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)
                : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    @Override
    public boolean isShiftKeyDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    @Override
    public String getClipboardString()
    {
        try
        {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getContents(null);

            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception exception)
        {
            ;
        }

        return "";
    }

    @Override
    public int getKeyCode(String keyName)
    {
        return Keyboard.getKeyIndex(keyName);
    }

    @Override
    public String getKeyName(int keyCode)
    {
        return Keyboard.getKeyName(keyCode);
    }
}