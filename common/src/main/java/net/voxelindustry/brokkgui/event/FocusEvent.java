package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class FocusEvent extends GuiStateEvent
{
    public static final EventType<FocusEvent> TYPE = new EventType<>(GuiStateEvent.ANY, "FOCUS_STATE_EVENT");

    private final boolean focused;

    public FocusEvent(GuiElement source)
    {
        this(source, false);
    }

    public FocusEvent(GuiElement source, boolean focused)
    {
        super(source);

        this.focused = focused;
    }

    public boolean isFocused()
    {
        return this.focused;
    }

    @Override
    public FocusEvent copy(IEventEmitter source)
    {
        return new FocusEvent((GuiElement) source, isFocused());
    }
}