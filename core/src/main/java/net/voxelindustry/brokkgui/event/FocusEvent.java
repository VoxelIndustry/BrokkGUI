package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;

public class FocusEvent extends GuiStateEvent
{
    public static final EventType<FocusEvent> TYPE = new EventType<>(GuiStateEvent.ANY, "FOCUS_STATE_EVENT");

    private final boolean focused;

    public FocusEvent(final GuiNode source)
    {
        this(source, false);
    }

    public FocusEvent(final GuiNode source, final boolean focused)
    {
        super(source);

        this.focused = focused;
    }

    public boolean isFocused()
    {
        return this.focused;
    }
}