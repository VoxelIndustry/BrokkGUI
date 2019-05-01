package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.exp.component.GuiElement;
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

    private final int mouseX, mouseY;

    public GuiMouseEvent(GuiElement source, int mouseX, int mouseY)
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

    @Override
    public GuiMouseEvent copy(IEventEmitter source)
    {
        return new GuiMouseEvent((GuiElement) source, getMouseX(), getMouseY());
    }

    public static final class Wheel extends GuiMouseEvent
    {
        private final int dwheel;

        public Wheel(GuiElement source, int mouseX, int mouseY, int dwheel)
        {
            super(source, mouseX, mouseY);

            this.dwheel = dwheel;
        }

        public int getDwheel()
        {
            return this.dwheel;
        }

        @Override
        public Wheel copy(IEventEmitter source)
        {
            return new Wheel((GuiElement) source, getMouseX(), getMouseY(), getDwheel());
        }
    }

    public static class DragStart extends GuiMouseEvent
    {
        private final int key;

        public DragStart(GuiElement source, int mouseX, int mouseY, int mouseKey)
        {
            super(source, mouseX, mouseY);

            this.key = mouseKey;
        }

        public int getKey()
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
        private final int dragX, dragY;

        public Dragging(GuiElement source, int mouseX, int mouseY, int mouseKey, int dragX, int dragY)
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

        @Override
        public Dragging copy(IEventEmitter source)
        {
            return new Dragging((GuiElement) source, getMouseX(), getMouseY(), getKey(), getDragX(), getDragY());
        }
    }

    public static final class DragStop extends Dragging
    {
        public DragStop(GuiElement source, int mouseX, int mouseY, int mouseKey, int dragX, int dragY)
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