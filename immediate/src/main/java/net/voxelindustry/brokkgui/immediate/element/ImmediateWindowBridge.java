package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;

public interface ImmediateWindowBridge
{
    <T> void setStyleObject(T object, StyleType type);

    <T> T getStyleObject(StyleType type, Class<T> objectClass);

    IGuiRenderer getRenderer();

    int getMouseX();

    int getMouseY();

    int getLastClickX();

    int getLastClickY();
}
