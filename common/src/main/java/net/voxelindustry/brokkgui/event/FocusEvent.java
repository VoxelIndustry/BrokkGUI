package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class FocusEvent extends GuiStateEvent
{
    public static final EventType<FocusEvent> TYPE = new EventType<>(GuiStateEvent.ANY, "FOCUS_STATE_EVENT");

    private final boolean focused;

    public FocusEvent(GuiNode source)
    {
        this(source, false);
    }

    public FocusEvent(GuiNode source, boolean focused)
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
        return new FocusEvent((GuiNode) source, isFocused());
    }
}