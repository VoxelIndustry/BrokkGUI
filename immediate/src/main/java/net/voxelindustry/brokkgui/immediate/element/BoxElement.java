package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.EmptyBoxStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;

public interface BoxElement extends ImmediateElement
{
    String BOX_TYPE       = "box";
    String EMPTY_BOX_TYPE = "empty-box";

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

    default EmptyBoxStyle emptyBoxStyleFromType(StyleType type)
    {
        return EmptyBoxStyle.build()
                .setBorderColor(getStyleValue(EMPTY_BOX_TYPE, type, "border-color", Color.BLACK))
                .setHoverBorderColor(getStyleValue(EMPTY_BOX_TYPE, type, "hover", "border-color", Color.BLACK))
                .setBorderThin(getStyleValue(EMPTY_BOX_TYPE, type, "border-width", 1F))
                .create();
    }

    default BoxStyle boxStyleFromType(StyleType type)
    {
        return BoxStyle.build()
                .setBoxColor(getStyleValue(BOX_TYPE, type, "background-color", Color.WHITE))
                .setHoverBoxColor(getStyleValue(BOX_TYPE, type, "hover", "background-color", Color.WHITE))
                .setBorderColor(getStyleValue(BOX_TYPE, type, "border-color", Color.BLACK))
                .setHoverBorderColor(getStyleValue(BOX_TYPE, type, "hover", "border-color", Color.BLACK))
                .setBorderThin(getStyleValue(BOX_TYPE, type, "border-width", 1F))
                .create();
    }

    default boolean emptyBox(float x, float y, float width, float height, StyleType type)
    {
        return emptyBox(x, y, width, height, getStyleObject(type, EmptyBoxStyle.class, this::emptyBoxStyleFromType));
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
            getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin, RenderPass.BACKGROUND);
        }
        else
        {
            getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, hoverBorderColor, borderThin, RenderPass.BACKGROUND);
        }
        return isHovered;
    }

    default boolean box(float x, float y, float width, float height)
    {
        return box(x, y, width, height, NORMAL);
    }

    default boolean box(float x, float y, float width, float height, StyleType type)
    {
        return box(x, y, width, height, getStyleObject(type, BoxStyle.class, this::boxStyleFromType));
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
                getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin, RenderPass.BACKGROUND);
            getRenderer().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, color, RenderPass.BACKGROUND);
        }
        else
        {
            if (borderColor.getAlpha() != 0 && borderThin > 0)
                getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, hoverBorderColor, borderThin, RenderPass.BACKGROUND);
            getRenderer().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, hoverColor, RenderPass.BACKGROUND);
        }
        return isHovered;
    }
}
