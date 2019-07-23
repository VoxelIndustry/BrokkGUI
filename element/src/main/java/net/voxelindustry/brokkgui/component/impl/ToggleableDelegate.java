package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.element.input.ToggleGroup;

public interface ToggleableDelegate
{
    Toggleable toggleableComponent();

    default void toggleGroup(ToggleGroup group)
    {
        toggleableComponent().toggleGroup(group);
    }

    default ToggleGroup toggleGroup()
    {
        return toggleableComponent().toggleGroup();
    }

    default BaseProperty<Boolean> selectedProperty()
    {
        return toggleableComponent().selectedProperty();
    }

    default BaseProperty<ToggleGroup> toggleGroupProperty()
    {
        return toggleableComponent().toggleGroupProperty();
    }

    default boolean select(boolean selected)
    {
        return toggleableComponent().select(selected);
    }

    default boolean selected()
    {
        return toggleableComponent().selected();
    }
}
