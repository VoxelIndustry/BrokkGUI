package org.yggard.brokkgui.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.yggard.brokkgui.internal.IKeyboardUtil;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class KeyboardUtil implements IKeyboardUtil
{
    @Override
    public final boolean isKeyValidChar(final int key)
    {
        return ChatAllowedCharacters.isAllowedCharacter(Keyboard.getEventCharacter());
    }

    @Override
    public final boolean isCtrlKeyDown()
    {
        return Minecraft.isRunningOnMac
                ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)
                : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    @Override
    public final String getClipboardString()
    {
        try
        {
            final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getContents((Object) null);

            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
        } catch (final Exception exception)
        {
            ;
        }

        return "";
    }

    @Override
    public int getKeyCode(final String keyName)
    {
        return Keyboard.getKeyIndex(keyName);
    }

    @Override
    public String getKeyName(final int keyCode)
    {
        return Keyboard.getKeyName(keyCode);
    }
}