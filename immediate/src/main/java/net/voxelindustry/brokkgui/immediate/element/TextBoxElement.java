package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.immediate.style.TextBoxStyle;
import net.voxelindustry.brokkgui.paint.Color;

public interface TextBoxElement extends ImmediateElement, TextElement, BoxElement
{
    String TEXT_BOX_TYPE = "text-box";

    default void setTextBoxStyle(TextBoxStyle style, StyleType type)
    {
        this.setStyleObject(style, type);
    }

    default void setTextBoxStyle(TextBoxStyle style)
    {
        this.setTextBoxStyle(style, StyleType.NORMAL);
    }

    default TextBoxStyle textBoxStyleFromType(StyleType type)
    {
        return TextBoxStyle.build()
                .setBoxColor(getStyleValue(TEXT_BOX_TYPE, type, "background-color", Color.WHITE))
                .setHoverBoxColor(getStyleValue(TEXT_BOX_TYPE, type, "hover", "background-color", Color.WHITE))
                .setBorderColor(getStyleValue(TEXT_BOX_TYPE, type, "border-color", Color.BLACK))
                .setHoverBorderColor(getStyleValue(TEXT_BOX_TYPE, type, "hover", "border-color", Color.BLACK))
                .setBorderThin(getStyleValue(TEXT_BOX_TYPE, type, "border-width", 1F))
                .setTextAlignment(getStyleValue(TEXT_BOX_TYPE, type, "text-alignment", RectAlignment.MIDDLE_CENTER))
                .setPadding(getStyleValue(TEXT_BOX_TYPE, type, "padding", RectBox.EMPTY))
                .setTextColor(getStyleValue(TEXT_BOX_TYPE, type, "text-color", Color.BLACK))
                .setHoverTextColor(getStyleValue(TEXT_BOX_TYPE, type, "hover", "text-color", Color.BLACK))
                .setShadowColor(getStyleValue(TEXT_BOX_TYPE, type, "shadow-color", Color.ALPHA))
                .setHoverShadowColor(getStyleValue(TEXT_BOX_TYPE, type, "hover", "shadow-color", Color.ALPHA))
                .create();
    }

    default boolean textBox(String text, float x, float y, StyleType type)
    {
        return textBox(text, x, y, 0, 0, type);
    }

    default boolean textBox(String text, float x, float y, float width, float height, StyleType type)
    {
        return textBox(text, x, y, width, height, getStyleObject(type, TextBoxStyle.class, this::textBoxStyleFromType));
    }

    default boolean textBox(String text, float x, float y, TextBoxStyle style)
    {
        return textBox(text, x, y, 0, 0, style);
    }

    default boolean textBox(String text, float x, float y, float width, float height, TextBoxStyle style)
    {
        return textBox(text,
                x,
                y,
                width,
                height,
                style.textColor,
                style.shadowColor,
                style.boxColor,
                style.borderColor,
                style.borderThin,
                style.padding,
                style.hoverTextColor,
                style.hoverShadowColor,
                style.hoverBoxColor,
                style.hoverBorderColor);
    }

    default boolean textBox(String text,
                            float x,
                            float y,
                            float width,
                            float height,
                            Color textColor,
                            Color shadowColor,
                            Color boxColor,
                            Color borderColor,
                            float borderThin,
                            RectBox textPadding,
                            Color hoverTextColor,
                            Color hoverShadowColor,
                            Color hoverBoxColor,
                            Color hoverBorderColor)
    {
        if (width == 0)
            width = getRenderer().getHelper().getStringWidthMultiLine(text) + textPadding.getHorizontal() + borderThin;
        if (height == 0)
            height = getRenderer().getHelper().getStringHeightMultiLine(text) + textPadding.getVertical() + borderThin;

        boolean isHovered = box(x, y, width, height, boxColor, borderColor, borderThin, hoverBoxColor, hoverBorderColor);
        text(text, x + textPadding.getLeft() + borderThin, y + textPadding.getTop() + borderThin, textColor, shadowColor, hoverTextColor, hoverShadowColor);

        return isHovered;
    }
}
