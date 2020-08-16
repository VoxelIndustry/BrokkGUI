package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class SelectEvent extends GuiInputEvent
{
    public static final EventType<SelectEvent> TYPE = new EventType<>(ANY, "SELECT_INPUT_EVENT");

    private boolean isSelected;

    public SelectEvent(GuiElement source, boolean isSelected)
    {
        super(source);

        this.isSelected = isSelected;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    @Override
    public SelectEvent copy(IEventEmitter source)
    {
        return new SelectEvent((GuiElement) source, isSelected);
    }
}
