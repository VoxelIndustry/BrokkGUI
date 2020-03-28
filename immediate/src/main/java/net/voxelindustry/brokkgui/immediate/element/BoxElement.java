package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.EmptyBoxStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.paint.Color;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;

public interface BoxElement extends ImmediateElement
{
    String BOX_TYPE = "box";

    default void setBoxStyle(BoxStyle style)
    {
        setBoxStyle(style, NORMAL);
    }

    default void setBoxStyle(BoxStyle style, StyleType type)
    {
        setStyleObject(style, type);
    }

    default void setEmptyBoxStyle(EmptyBoxStyle style)
    {
        setEmptyBoxStyle(style, NORMAL);
    }

    default void setEmptyBoxStyle(EmptyBoxStyle style, StyleType type)
    {
        setStyleObject(style, type);
    }

    default boolean emptyBox(float x, float y, float width, float height, StyleType type)
    {
        EmptyBoxStyle style = getStyleObject(type, EmptyBoxStyle.class);

        if (style == null)
        {
            BoxStyle boxStyle = getStyleObject(type, BoxStyle.class);
            return emptyBox(x, y, width, height, boxStyle.borderColor, boxStyle.hoverBorderColor, boxStyle.borderThin);
        }
        return emptyBox(x, y, width, height, style);
    }

    default boolean emptyBox(float x, float y, float width, float height, EmptyBoxStyle style)
    {
        return emptyBox(x, y, width, height, style.borderColor, style.hoverBorderColor, style.borderThin);
    }

    default boolean emptyBox(float x, float y, float width, float height, Color borderColor, Color hoverBorderColor, float borderThin)
    {
        boolean isHovered = isAreaHovered(x, y, x + width, y + height);

        if (!isHovered)
        {
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin);
        }
        else
        {
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, hoverBorderColor, borderThin);
        }
        return isHovered;
    }

    default boolean box(float x, float y, float width, float height)
    {
        return box(x, y, width, height, NORMAL);
    }

    default boolean box(float x, float y, float width, float height, StyleType type)
    {
        BoxStyle boxStyle = getStyleObject(type, BoxStyle.class);

        if (boxStyle != null)
            return box(x, y, width, height, boxStyle);
        else
            return box(x, y, width, height,
                    getStyleValue(BOX_TYPE, type, "background-color", Color.WHITE),
                    getStyleValue(BOX_TYPE, type, "border-color", Color.BLACK),
                    getStyleValue(BOX_TYPE, type, "border-width", 1F),
                    getStyleValue(BOX_TYPE, type, "hover", "background-color", Color.WHITE),
                    getStyleValue(BOX_TYPE, type, "hover", "border-color", Color.BLACK)
            );
    }

    default boolean box(float x, float y, float width, float height, BoxStyle style)
    {
        return box(x, y, width, height, style.boxColor, style.borderColor, style.borderThin, style.hoverBoxColor, style.hoverBorderColor);
    }

    default boolean box(float x, float y, float width, float height, Color color, Color borderColor, float borderThin, Color hoverColor, Color hoverBorderColor)
    {
        boolean isHovered = isAreaHovered(x, y, x + width, y + height);

        if (!isHovered)
        {
            if (borderColor.getAlpha() != 0 && borderThin > 0)
                getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin);
            getRenderer().getHelper().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, color);
        }
        else
        {
            if (borderColor.getAlpha() != 0 && borderThin > 0)
                getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, hoverBorderColor, borderThin);
            getRenderer().getHelper().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, hoverColor);
        }
        return isHovered;
    }
}
