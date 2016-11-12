package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.control.GuiToggleGroup;

import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten
 *
 *         Interface for Togglable elements of BrokkGui. Allow implementation of
 *         ToggleGroups, they are mostly used with RadioButtons.
 */
public interface IGuiTogglable
{
    /**
     * @return the GuiToggleGroup to this Togglable element belongs.
     */
    GuiToggleGroup getToggleGroup();

    /**
     * @param selected
     *            boolean
     * @return the state after GuiToggleGroup checks. Useful when the
     *         ToggleGroup might disable empty selection or when this Togglable
     *         is already selected.
     */
    boolean setSelected(boolean selected);

    /**
     * @return the value of the selected property. When a GuiToggleGroup is set
     *         you are guaranteed that it's the group selected Togglable.
     */
    boolean isSelected();

    /**
     * @return the internal selected property of the Togglable object.
     */
    BaseProperty<Boolean> getSelectedProperty();
}