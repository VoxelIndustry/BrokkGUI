package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.immediate.style.TextStyle;
import net.voxelindustry.brokkgui.paint.Color;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;

public interface TextElement extends ImmediateElement
{
    String TEXT_TYPE = "text";

    default void setTextStyle(TextStyle style)
    {
        setTextStyle(style, NORMAL);
    }

    default void setTextStyle(TextStyle style, StyleType type)
    {
        setStyleObject(style, type);
    }

    default TextStyle textStyleFromType(StyleType type)
    {
        return TextStyle.build()
                .setTextColor(getStyleValue(TEXT_TYPE, type, "text-color", Color.BLACK))
                .setHoverTextColor(getStyleValue(TEXT_TYPE, type, "hover", "text-color", Color.BLACK))
                .setShadowColor(getStyleValue(TEXT_TYPE, type, "shadow-color", Color.ALPHA))
                .setHoverShadowColor(getStyleValue(TEXT_TYPE, type, "hover", "shadow-color", Color.ALPHA))
                .create();
    }

    default boolean text(String text, float x, float y)
    {
        return text(text, x, y, NORMAL);
    }

    default boolean text(String text, float x, float y, StyleType type)
    {
        return text(text, x, y, getStyleObject(type, TextStyle.class, this::textStyleFromType));
    }

    default boolean text(String text, float x, float y, TextStyle style)
    {
        return text(text, x, y, style.textColor, style.shadowColor, style.hoverTextColor, style.hoverShadowColor);
    }

    default boolean text(String text, float x, float y, Color color, Color shadowColor, Color hoverColor, Color hoverShadowColor)
    {
        boolean isHovered = isAreaHovered(x, y, x + getRenderer().getHelper().getStringWidthMultiLine(text), y + getRenderer().getHelper().getStringHeightMultiLine(text));

        if (!isHovered)
            getRenderer().getHelper().drawStringMultiline(text, x, y, 1, color, shadowColor);
        else
            getRenderer().getHelper().drawStringMultiline(text, x, y, 1, hoverColor, hoverShadowColor);

        return isHovered;
    }
}
