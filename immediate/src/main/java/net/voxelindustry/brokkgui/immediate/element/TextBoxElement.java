package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.immediate.style.TextBoxStyle;
import net.voxelindustry.brokkgui.immediate.style.TextStyle;
import net.voxelindustry.brokkgui.paint.Color;

public interface TextBoxElement extends ImmediateElement, TextElement, BoxElement
{
    default void setTextBoxStyle(TextBoxStyle style, StyleType type)
    {
        this.setStyleObject(style, type);
    }

    default void setTextBoxStyle(TextBoxStyle style)
    {
        this.setTextBoxStyle(style, StyleType.NORMAL);
    }

    default boolean textBox(String text, float x, float y, StyleType type)
    {
        return textBox(text, x, y, 0, 0, type);
    }

    default boolean textBox(String text, float x, float y, float width, float height, StyleType type)
    {
        TextBoxStyle textBoxStyle = getStyleObject(type, TextBoxStyle.class);

        if (textBoxStyle == null)
        {
            TextStyle textStyle = getStyleObject(type, TextStyle.class);
            BoxStyle boxStyle = getStyleObject(type, BoxStyle.class);

            return textBox(text,
                    x,
                    y,
                    width,
                    height,
                    textStyle.textColor,
                    textStyle.shadowColor,
                    boxStyle.boxColor,
                    boxStyle.borderColor,
                    boxStyle.borderThin,
                    RectBox.EMPTY,
                    textStyle.hoverTextColor,
                    textStyle.hoverShadowColor,
                    boxStyle.hoverBoxColor,
                    boxStyle.hoverBorderColor);
        }
        else
        {
            return textBox(text, x, y, width, height, textBoxStyle);
        }
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
