package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

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

    private final float mouseX;
    private final float mouseY;

    public GuiMouseEvent(GuiElement source, float mouseX, float mouseY)
    {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public float getMouseX()
    {
        return mouseX;
    }

    public float getMouseY()
    {
        return mouseY;
    }

    @Override
    public GuiMouseEvent copy(IEventEmitter source)
    {
        return new GuiMouseEvent((GuiElement) source, getMouseX(), getMouseY());
    }

    public static final class Wheel extends GuiMouseEvent
    {
        private final int dwheel;

        public Wheel(GuiElement source, float mouseX, float mouseY, int dwheel)
        {
            super(source, mouseX, mouseY);

            this.dwheel = dwheel;
        }

        public int getDwheel()
        {
            return dwheel;
        }

        @Override
        public Wheel copy(IEventEmitter source)
        {
            return new Wheel((GuiElement) source, getMouseX(), getMouseY(), getDwheel());
        }
    }

    public static class DragStart extends GuiMouseEvent
    {
        private final MouseInputCode key;

        public DragStart(GuiElement source, float mouseX, float mouseY, MouseInputCode mouseKey)
        {
            super(source, mouseX, mouseY);

            key = mouseKey;
        }

        public MouseInputCode getKey()
        {
            return key;
        }

        @Override
        public DragStart copy(IEventEmitter source)
        {
            return new DragStart((GuiElement) source, getMouseX(), getMouseY(), getKey());
        }
    }

    public static class Dragging extends DragStart
    {
        private final float dragX;
        private final float dragY;

        public Dragging(GuiElement source, float mouseX, float mouseY, MouseInputCode mouseKey, float dragX, float dragY)
        {
            super(source, mouseX, mouseY, mouseKey);

            this.dragX = dragX;
            this.dragY = dragY;
        }

        public float getDragX()
        {
            return dragX;
        }

        public float getDragY()
        {
            return dragY;
        }

        @Override
        public Dragging copy(IEventEmitter source)
        {
            return new Dragging((GuiElement) source, getMouseX(), getMouseY(), getKey(), getDragX(), getDragY());
        }
    }

    public static final class DragStop extends Dragging
    {
        public DragStop(GuiElement source, float mouseX, float mouseY, MouseInputCode mouseKey, float dragX, float dragY)
        {
            super(source, mouseX, mouseY, mouseKey, dragX, dragY);
        }

        @Override
        public DragStop copy(IEventEmitter source)
        {
            return new DragStop((GuiElement) source, getMouseX(), getMouseY(), getKey(), getDragX(), getDragY());
        }
    }
}