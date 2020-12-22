package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.internal.ITextHelper;

import java.util.function.Function;

public interface ImmediateWindowBridge
{
    <T> void setStyleObject(T object, StyleType type);

    <T> T getStyleObject(StyleType type, Class<T> objectClass, Function<StyleType, T> styleObjectSupplier);

    default <T> T getStyleValue(String element, StyleType styleType, String rule, T defaultValue)
    {
        return getStyleValue(element, styleType, "", rule, defaultValue);
    }

    <T> T getStyleValue(String element, StyleType styleType, String pseudoClass, String rule, T defaultValue);

    IRenderCommandReceiver getRenderer();

    ITextHelper getTextHelper();

    int getMouseX();

    int getMouseY();

    int getLastClickX();

    int getLastClickY();

    boolean isAreaHovered(float startX, float startY, float endX, float endY);

    boolean isAreaClicked(float startX, float startY, float endX, float endY);

    boolean isAreaWheeled(float startX, float startY, float endX, float endY);
}
