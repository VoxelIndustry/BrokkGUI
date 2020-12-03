package net.voxelindustry.brokkgui.event;

public enum MouseInputCode
{
    MOUSE_LEFT,
    MOUSE_RIGHT,
    MOUSE_BUTTON_MIDDLE,
    MOUSE_BUTTON_4,
    MOUSE_BUTTON_5,
    MOUSE_BUTTON_6,
    MOUSE_BUTTON_7,
    MOUSE_BUTTON_8;

    private static final MouseInputCode[] cachedValues = values();

    public static MouseInputCode fromGLFWCode(int glfwCode)
    {
        return cachedValues[glfwCode];
    }

    public int getGLFWCode()
    {
        return ordinal();
    }
}
