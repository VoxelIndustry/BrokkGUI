package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class CursorMoveEvent extends GuiMouseEvent
{
    public static final EventType<CursorMoveEvent> TYPE = new EventType<>(GuiMouseEvent.ANY, "MOUSE_MOVE_EVENT");

    public CursorMoveEvent(GuiNode source)
    {
        this(source, 0, 0);
    }

    public CursorMoveEvent(GuiNode source, int mouseX, int mouseY)
    {
        super(source, mouseX, mouseY);
    }

    @Override
    public CursorMoveEvent copy(IEventEmitter source)
    {
        return new CursorMoveEvent((GuiNode) source, getMouseX(), getMouseY());
    }
}