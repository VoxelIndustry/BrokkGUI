package net.voxelindustry.brokkgui.wrapper;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyboardUtil implements IKeyboardUtil
{
    private Map<String, Integer> keyMap;

    public KeyboardUtil()
    {
        this.keyMap = new HashMap<>();

        this.keyMap.put("UP", GLFW.GLFW_KEY_UP);
        this.keyMap.put("DOWN", GLFW.GLFW_KEY_DOWN);
        this.keyMap.put("LEFT", GLFW.GLFW_KEY_LEFT);
        this.keyMap.put("RIGHT", GLFW.GLFW_KEY_RIGHT);

        this.keyMap.put("DELETE", GLFW.GLFW_KEY_DELETE);
        this.keyMap.put("BACK", GLFW.GLFW_KEY_BACKSPACE);

        this.keyMap.put("V", GLFW.GLFW_KEY_V);

        this.keyMap.put("RETURN", GLFW.GLFW_KEY_ENTER);
        this.keyMap.put("NUMPADENTER", GLFW.GLFW_KEY_KP_ENTER);
    }

    @Override
    public boolean isKeyValidChar(int key)
    {
        // key is 0 when no char has been received to distinguish keyPressed and charTyped GLFW events since they
        // are merged in the event bus
        return key != 0 && SharedConstants.isValidChar((char) key);
    }

    @Override
    public boolean isCtrlKeyDown()
    {
        return Screen.isControlPressed();
    }

    @Override
    public boolean isShiftKeyDown()
    {
        return Screen.isShiftPressed();
    }

    @Override
    public String getClipboardString()
    {
        return MinecraftClient.getInstance().keyboard.getClipboard();
    }

    @Override
    public int getKeyCode(String keyName)
    {
        if (this.keyMap.containsKey(keyName))
            return keyMap.get(keyName);
        throw new IllegalArgumentException("Key [" + keyName + "] is not mapped in the binding.");
    }

    @Override
    public String getKeyName(int keyCode)
    {
        throw new UnsupportedOperationException("GetKeyName is invalid with MC1.14");
    }
}
