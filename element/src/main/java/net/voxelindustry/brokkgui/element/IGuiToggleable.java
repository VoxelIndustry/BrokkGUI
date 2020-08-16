package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.element.input.GuiToggleGroup;

/**
 * @author Ourten
 * <p>
 * Interface for Togglable elements of BrokkGui. Allow implementation of
 * ToggleGroups, they are mostly used with RadioButtons.
 */
public interface IGuiToggleable
{
    /**
     * @return the GuiToggleGroup to this Toggleable element belongs.
     */
    GuiToggleGroup getToggleGroup();

    /**
     * Set the ToggleGroup this Toggleable belongs to.
     *
     * @param group
     */
    void setToggleGroup(GuiToggleGroup group);

    /**
     * @param selected boolean
     * @return the state after GuiToggleGroup checks. Useful when the
     * ToggleGroup might disable empty selection or when this Toggleable
     * is already selected.
     */
    boolean setSelected(boolean selected);

    /**
     * @return the value of the selected property. When a GuiToggleGroup is set
     * you are guaranteed that it's the group selected Toggleable.
     */
    default boolean isSelected()
    {
        return getSelectedProperty().getValue();
    }

    /**
     * @return the internal selected property of the Togglable object.
     */
    Property<Boolean> getSelectedProperty();
}