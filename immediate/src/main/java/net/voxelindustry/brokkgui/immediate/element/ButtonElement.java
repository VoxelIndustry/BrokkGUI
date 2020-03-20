package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.immediate.InteractionResult;
import net.voxelindustry.brokkgui.immediate.style.ButtonStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;

public interface ButtonElement extends ImmediateElement
{
    default void setButtonStyle(ButtonStyle style)
    {
        setButtonStyle(style, NORMAL);
    }

    default void setButtonStyle(ButtonStyle style, StyleType type)
    {
        setStyleObject(style, type);
    }

    default InteractionResult button(String text, float x, float y, StyleType type)
    {
        return button(text, x, y, getStyleObject(type, ButtonStyle.class));
    }

    default InteractionResult button(String text, float x, float y, ButtonStyle style)
    {
        return button(text,
                x,
                y,
                getRenderer().getHelper().getStringWidth(text) + style.padding.getHorizontal(),
                getRenderer().getHelper().getStringHeight() + style.padding.getVertical(),
                style);
    }

    default InteractionResult button(String text, float x, float y, float width, float height, StyleType type)
    {
        return button(text, x, y, width, height, getStyleObject(type, ButtonStyle.class));
    }

    default InteractionResult button(String text, float x, float y, float width, float height, ButtonStyle style)
    {
        boolean isHovered = getMouseX() > x && getMouseY() > y && getMouseX() < x + width && getMouseY() < y + height;
        boolean isClicked = getLastClickX() > x && getLastClickY() > y && getLastClickX() < x + width && getLastClickY() < y + height;

        if (isClicked)
        {
            getRenderer().getHelper().drawColoredRect(getRenderer(), x, y, width, height, 1, style.clickBoxColor);
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.clickBorderColor, style.borderThin);

            getRenderer().getHelper().drawString(text, x + style.padding.getLeft(), y + style.padding.getTop(), 1, style.clickTextColor, style.clickShadowColor);

            return InteractionResult.CLICKED;
        }
        if (isHovered)
        {
            getRenderer().getHelper().drawColoredRect(getRenderer(), x, y, width, height, 1, style.hoverBoxColor);
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.hoverBorderColor, style.borderThin);

            getRenderer().getHelper().drawString(text, x + style.padding.getLeft(), y + style.padding.getTop(), 1, style.hoverTextColor, style.hoverShadowColor);

            return InteractionResult.HOVERED;
        }

        getRenderer().getHelper().drawColoredRect(getRenderer(), x, y, width, height, 1, style.boxColor);
        getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.borderColor, style.borderThin);

        getRenderer().getHelper().drawString(text, x + style.padding.getLeft(), y + style.padding.getTop(), 1, style.textColor, style.shadowColor);

        return InteractionResult.NONE;
    }
}
