package fr.ourten.brokkgui.wrapper;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import org.lwjgl.input.Keyboard;

import fr.ourten.brokkgui.internal.IKeyboardUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;

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