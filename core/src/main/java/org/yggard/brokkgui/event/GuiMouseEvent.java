package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.hermod.EventType;

public class GuiMouseEvent extends GuiInputEvent
{
    public static final EventType<GuiMouseEvent>           ANY        = new EventType<>(GuiInputEvent.ANY,
            "INPUT_MOUSE_EVENT");
    public static final EventType<GuiMouseEvent.Wheel>     WHEEL      = new EventType<>(GuiMouseEvent.ANY,
            "WHEEL_MOUSE_EVENT");
    public static final EventType<GuiMouseEvent.DragStart> DRAG_START = new EventType<>(GuiMouseEvent.ANY,
            "DRAG_START_EVENT");
    public static final EventType<GuiMouseEvent.Dragging>  DRAGGING   = new EventType<>(GuiMouseEvent.ANY,
            "DRAGGING_EVENT");
    public static final EventType<GuiMouseEvent.DragStop>  DRAG_STOP  = new EventType<>(GuiMouseEvent.ANY,
            "DRAG_STOP_EVENT");


    private final int mouseX, mouseY;

    public GuiMouseEvent(final GuiNode source, final int mouseX, final int mouseY)
    {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getMouseX()
    {
        return this.mouseX;
    }

    public int getMouseY()
    {
        return this.mouseY;
    }

    public static final class Wheel extends GuiMouseEvent
    {
        private final int dwheel;

        public Wheel(final GuiNode source, final int dwheel)
        {
            super(source, 0, 0);

            this.dwheel = dwheel;
        }

        public int getDwheel()
        {
            return this.dwheel;
        }
    }

    public static class DragStart extends GuiMouseEvent
    {
        private final int key;

        public DragStart(GuiNode source, int mouseX, int mouseY, int mouseKey)
        {
            super(source, mouseX, mouseY);

            this.key = mouseKey;
        }

        public int getKey()
        {
            return key;
        }
    }

    public static class Dragging extends DragStart
    {
        private final int dragX, dragY;

        public Dragging(GuiNode source, int mouseX, int mouseY, int mouseKey, int dragX, int dragY)
        {
            super(source, mouseX, mouseY, mouseKey);

            this.dragX = dragX;
            this.dragY = dragY;
        }

        public int getDragX()
        {
            return dragX;
        }

        public int getDragY()
        {
            return dragY;
        }
    }

    public static final class DragStop extends Dragging
    {
        public DragStop(GuiNode source, int mouseX, int mouseY, int mouseKey, int dragX, int dragY)
        {
            super(source, mouseX, mouseY, mouseKey, dragX, dragY);
        }
    }
}