package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiTogglableButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.element.IGuiTogglable;
import net.voxelindustry.brokkgui.event.SelectEvent;
import net.voxelindustry.brokkgui.skin.GuiButtonSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.hermod.EventHandler;

public class GuiToggleButton extends GuiButtonBase implements IGuiTogglable
{
    private final BaseProperty<Boolean>        selectedProperty;
    private final BaseProperty<GuiToggleGroup> toggleGroupProperty;

    private EventHandler<SelectEvent> onSelectEvent;

    public GuiToggleButton(String text, GuiElement icon)
    {
        super(text, icon);

        selectedProperty = new BaseProperty<>(false, "selectedProperty");
        toggleGroupProperty = new BaseProperty<>(null, "toggleGroupProperty");

        selectedProperty.addListener((obs, oldValue, newValue) ->
        {
            if (newValue)
                style().activePseudoClass().add("active");
            else
                style().activePseudoClass().remove("active");
            getEventDispatcher().dispatchEvent(SelectEvent.TYPE, new SelectEvent(this, isSelected()));
        });
    }

    public GuiToggleButton(String text)
    {
        this(text, null);
    }

    public GuiToggleButton()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "button";
    }

    @Override
    protected GuiLabeled createGuiLabel(String text, GuiElement icon)
    {
        return new GuiLabel(text, icon);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiButtonSkin<>(this, new GuiTogglableButtonBehavior<>(this));
    }

    public void setToggleGroup(GuiToggleGroup group)
    {
        getToggleGroupProperty().setValue(group);
        group.addButton(this);
    }

    @Override
    public GuiToggleGroup getToggleGroup()
    {
        return getToggleGroupProperty().getValue();
    }

    @Override
    public BaseProperty<Boolean> getSelectedProperty()
    {
        return selectedProperty;
    }

    public BaseProperty<GuiToggleGroup> getToggleGroupProperty()
    {
        return toggleGroupProperty;
    }

    @Override
    public boolean setSelected(boolean selected)
    {
        if (!selected && isSelected())
        {
            if (getToggleGroup() == null)
            {
                getSelectedProperty().setValue(false);
                return false;
            }
            if (getToggleGroup().getSelectedButton() == this && !getToggleGroup().allowNothing())
                return true;
            getToggleGroup().setSelectedButton(null);
            return false;
        }
        if (selected && !isSelected())
        {
            if (getToggleGroup() == null)
            {
                getSelectedProperty().setValue(true);
                return true;
            }
            else
            {
                if (getToggleGroup().getSelectedButton() == this)
                    return false;
                getToggleGroup().setSelectedButton(this);
                return true;
            }
        }
        return isSelected();
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnSelectEvent(EventHandler<SelectEvent> onSelectEvent)
    {
        getEventDispatcher().removeHandler(SelectEvent.TYPE, this.onSelectEvent);
        this.onSelectEvent = onSelectEvent;
        getEventDispatcher().addHandler(SelectEvent.TYPE, this.onSelectEvent);
    }
}