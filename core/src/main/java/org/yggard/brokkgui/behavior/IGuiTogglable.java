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
    public default boolean setSelected(final boolean selected)
    {
        if (!selected && this.isSelected())
        {
            if (this.getToggleGroup() == null)
            {
                this.getSelectedProperty().setValue(false);
                return false;
            }
            if (this.getToggleGroup().getSelectedButton() == this && !this.getToggleGroup().allowNothing())
                return true;
            this.getToggleGroup().setSelectedButton(null);
            return false;
        }
        if (selected && !this.isSelected())
        {
            if (this.getToggleGroup() == null)
            {
                this.getSelectedProperty().setValue(true);
                return true;
            }
            else
            {
                if (this.getToggleGroup().getSelectedButton() == this)
                    return false;
                this.getToggleGroup().setSelectedButton(this);
                return true;
            }
        }
        return this.isSelected();
    }

    /**
     * @return the value of the selected property. When a GuiToggleGroup is set
     *         you are guaranteed that it's the group selected Togglable.
     */
    public default boolean isSelected()
    {
        return this.getSelectedProperty().getValue();
    }

    /**
     * @return the internal selected property of the Togglable object.
     */
    BaseProperty<Boolean> getSelectedProperty();
}