package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ClickPressEvent extends GuiMouseEvent
{
    public static final EventType<ClickPressEvent> TYPE = new EventType<>(ANY, "MOUSE_CLICK_PRESS_EVENT");

    private final MouseInputCode key;

    public ClickPressEvent(GuiElement source)
    {
        this(source, 0, 0, MouseInputCode.MOUSE_LEFT);
    }

    public ClickPressEvent(GuiElement source, float mouseX, float mouseY, MouseInputCode key)
    {
        super(source, mouseX, mouseY);

        this.key = key;
    }

    public MouseInputCode getKey()
    {
        return key;
    }

    @Override
    public ClickPressEvent copy(IEventEmitter source)
    {
        return new ClickPressEvent((GuiElement) source, getMouseX(), getMouseY(), getKey());
    }

    public static class Left extends ClickPressEvent
    {
        public static EventType<ClickPressEvent.Left> TYPE = new EventType<>(ClickPressEvent.TYPE,
                "MOUSE_LEFT_CLICK_PRESS_EVENT");

        public Left(GuiElement source, float mouseX, float mouseY)
        {
            super(source, mouseX, mouseY, MouseInputCode.MOUSE_LEFT);
        }

        @Override
        public Left copy(IEventEmitter source)
        {
            return new Left((GuiElement) source, getMouseX(), getMouseY());
        }
    }

    public static class Right extends ClickPressEvent
    {
        public static EventType<ClickPressEvent.Right> TYPE = new EventType<>(ClickPressEvent.TYPE,
                "MOUSE_RIGHT_CLICK_PRESS_EVENT");

        public Right(GuiElement source, float mouseX, float mouseY)
        {
            super(source, mouseX, mouseY, MouseInputCode.MOUSE_RIGHT);
        }

        @Override
        public Right copy(IEventEmitter source)
        {
            return new Right((GuiElement) source, getMouseX(), getMouseY());
        }
    }

    public static class Middle extends ClickPressEvent
    {
        public static EventType<ClickPressEvent.Middle> TYPE = new EventType<>(ClickPressEvent.TYPE,
                "MOUSE_MIDDLE_CLICK_PRESS_EVENT");

        public Middle(GuiElement source, float mouseX, float mouseY)
        {
            super(source, mouseX, mouseY, MouseInputCode.MOUSE_BUTTON_MIDDLE);
        }

        @Override
        public Middle copy(IEventEmitter source)
        {
            return new Middle((GuiElement) source, getMouseX(), getMouseY());
        }
    }
}