package net.voxelindustry.brokkgui.component.delegate;

import net.voxelindustry.brokkgui.component.impl.Text;
import net.voxelindustry.brokkgui.component.impl.TextRenderer;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;

import javax.annotation.Nonnull;

public interface TextDelegate
{
    Text textComponent();

    TextRenderer textRendererComponent();

    default RectAlignment textAlignment()
    {
        return textComponent().textAlignment();
    }

    default void textAlignment(RectAlignment alignment)
    {
        textComponent().textAlignment(alignment);
    }

    default String text()
    {
        return textComponent().text();
    }

    default void text(@Nonnull String text)
    {
        this.textComponent().text(text);
    }

    default String ellipsis()
    {
        return textComponent().ellipsis();
    }

    default void ellipsis(String ellipsis)
    {
        textComponent().ellipsis(ellipsis);
    }

    default RectBox textPadding()
    {
        return textComponent().textPadding();
    }

    default void textPadding(RectBox textPadding)
    {
        textComponent().textPadding(textPadding);
    }

    default boolean expandToText()
    {
        return textComponent().expandToText();
    }

    default void expandToText(boolean expandToText)
    {
        textComponent().expandToText(expandToText);
    }
}
