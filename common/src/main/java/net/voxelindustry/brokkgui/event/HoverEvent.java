package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class HoverEvent extends GuiStateEvent
{
    public static final EventType<HoverEvent> TYPE = new EventType<>(GuiStateEvent.ANY, "HOVER_STATE_EVENT");

    private final boolean entering;

    public HoverEvent(GuiNode source)
    {
        this(source, false);
    }

    public HoverEvent(GuiNode source, boolean entering)
    {
        super(source);

        this.entering = entering;
    }

    public boolean isEntering()
    {
        return this.entering;
    }

    @Override
    public HoverEvent copy(IEventEmitter source)
    {
        return new HoverEvent((GuiNode) source, isEntering());
    }
}