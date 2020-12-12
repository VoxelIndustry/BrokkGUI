package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.immediate.InteractionResult;
import net.voxelindustry.brokkgui.immediate.style.ButtonStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;

public interface ButtonElement extends ImmediateElement
{
    String BUTTON_TYPE = "button";

    default void setButtonStyle(ButtonStyle style)
    {
        setButtonStyle(style, NORMAL);
    }

    default void setButtonStyle(ButtonStyle style, StyleType type)
    {
        setStyleObject(style, type);
    }

    default ButtonStyle buttonStyleFromType(StyleType type)
    {
        return ButtonStyle.build()
                .setBoxColor(getStyleValue(BUTTON_TYPE, type, "background-color", Color.ALPHA))
                .setHoverBoxColor(getStyleValue(BUTTON_TYPE, type, "hover", "background-color", Color.ALPHA))
                .setClickBoxColor(getStyleValue(BUTTON_TYPE, type, "active", "background-color", Color.ALPHA))
                .setBorderColor(getStyleValue(BUTTON_TYPE, type, "border-color", Color.ALPHA))
                .setHoverBorderColor(getStyleValue(BUTTON_TYPE, type, "hover", "border-color", Color.ALPHA))
                .setClickBorderColor(getStyleValue(BUTTON_TYPE, type, "active", "border-color", Color.ALPHA))
                .setBorderThin(getStyleValue(BUTTON_TYPE, type, "border-width", 1F))
                .setTextAlignment(getStyleValue(BUTTON_TYPE, type, "text-alignment", RectAlignment.MIDDLE_CENTER))
                .setPadding(getStyleValue(BUTTON_TYPE, type, "padding", RectBox.EMPTY))
                .setTextColor(getStyleValue(BUTTON_TYPE, type, "text-color", Color.BLACK))
                .setHoverTextColor(getStyleValue(BUTTON_TYPE, type, "hover", "text-color", Color.BLACK))
                .setClickTextColor(getStyleValue(BUTTON_TYPE, type, "active", "text-color", Color.BLACK))
                .setShadowColor(getStyleValue(BUTTON_TYPE, type, "shadow-color", Color.ALPHA))
                .setHoverShadowColor(getStyleValue(BUTTON_TYPE, type, "hover", "shadow-color", Color.ALPHA))
                .setClickShadowColor(getStyleValue(BUTTON_TYPE, type, "active", "shadow-color", Color.ALPHA))
                .create();
    }

    default InteractionResult button(String text, float x, float y, StyleType type)
    {
        return button(text, x, y, getStyleObject(type, ButtonStyle.class, this::buttonStyleFromType));
    }

    default InteractionResult button(String text, float x, float y, ButtonStyle style)
    {
        return button(text,
                x,
                y,
                getRenderer().getStringWidth(text) + style.padding.getHorizontal(),
                getRenderer().getStringHeight() + style.padding.getVertical(),
                style);
    }

    default InteractionResult button(String text, float x, float y, float width, float height, StyleType type)
    {
        return button(text, x, y, width, height, getStyleObject(type, ButtonStyle.class, this::buttonStyleFromType));
    }

    default InteractionResult button(String text, float x, float y, float width, float height, ButtonStyle style)
    {
        boolean isHovered = isAreaHovered(x, y, x + width, y + height);
        boolean isClicked = isAreaClicked(x, y, x + width, y + height);

        float textWidth = getRenderer().getStringWidthMultiLine(text);
        float textHeight = getRenderer().getStringHeightMultiLine(text);
        float textOffsetX = style.textAlignment.isLeft() ? style.padding.getLeft() :
                            (style.textAlignment.isRight() ? width - textWidth - style.padding.getRight() :
                             width / 2 - textWidth / 2 + style.padding.getLeft() - style.padding.getRight());
        float textOffsetY = style.textAlignment.isUp() ? style.padding.getTop() :
                            (style.textAlignment.isDown() ? height - textHeight - style.padding.getBottom() :
                             height / 2 - textHeight / 2 + style.padding.getTop() - style.padding.getBottom());

        if (isClicked)
        {
            getRenderer().drawColoredRect(getRenderer(), x, y, width, height, 1, style.clickBoxColor, RenderPass.BACKGROUND);
            getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.clickBorderColor, style.borderThin, RenderPass.BACKGROUND);

            getRenderer().drawString(text, x + textOffsetX, y + textOffsetY, 1, style.clickTextColor, style.clickShadowColor);

            return InteractionResult.CLICKED;
        }
        if (isHovered)
        {
            getRenderer().drawColoredRect(getRenderer(), x, y, width, height, 1, style.hoverBoxColor, RenderPass.BACKGROUND);
            getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.hoverBorderColor, style.borderThin, RenderPass.BACKGROUND);

            getRenderer().drawString(text, x + textOffsetX, y + textOffsetY, 1, style.hoverTextColor, style.hoverShadowColor);

            return InteractionResult.HOVERED;
        }

        getRenderer().drawColoredRect(getRenderer(), x, y, width, height, 1, style.boxColor, RenderPass.BACKGROUND);
        getRenderer().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.borderColor, style.borderThin, RenderPass.BACKGROUND);

        getRenderer().drawString(text, x + textOffsetX, y + textOffsetY, 1, style.textColor, style.shadowColor);

        return InteractionResult.NONE;
    }
}
