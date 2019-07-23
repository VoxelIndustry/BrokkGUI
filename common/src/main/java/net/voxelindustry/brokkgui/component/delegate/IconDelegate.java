package net.voxelindustry.brokkgui.component.delegate;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Icon;
import net.voxelindustry.brokkgui.data.RectSide;

public interface IconDelegate
{
    Icon iconComponent();

    default GuiElement icon()
    {
        return iconComponent().icon();
    }

    default void icon(GuiElement icon)
    {
        iconComponent().icon(icon);
    }

    default RectSide iconSide()
    {
        return iconComponent().iconSide();
    }

    default void iconSide(RectSide iconSide)
    {
        iconComponent().iconSide(iconSide);
    }

    default float iconPadding()
    {
        return iconComponent().iconPadding();
    }

    default void iconPadding(float iconPadding)
    {
        iconComponent().iconPadding(iconPadding);
    }
}
