package net.voxelindustry.brokkgui.component.delegate;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.CheckMark;

public interface CheckMarkDelegate
{
    CheckMark checkMarkComponent();

    default GuiElement box()
    {
        return checkMarkComponent().box();
    }

    default GuiElement mark()
    {
        return checkMarkComponent().mark();
    }

    default BaseProperty<Float> markPaddingProperty()
    {
        return checkMarkComponent().markPaddingProperty();
    }

    default float markPadding()
    {
        return checkMarkComponent().markPadding();
    }

    default void markPadding(float markPadding)
    {
        checkMarkComponent().markPadding(markPadding);
    }
}
