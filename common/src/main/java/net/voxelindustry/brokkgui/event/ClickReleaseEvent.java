package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ClickReleaseEvent extends GuiMouseEvent
{
    public static final EventType<ClickReleaseEvent> TYPE = new EventType<>(ANY, "MOUSE_CLICK_RELEASE_EVENT");

    private final MouseInputCode key;

    public ClickReleaseEvent(GuiElement source)
    {
        this(source, 0, 0, MouseInputCode.MOUSE_LEFT);
    }

    public ClickReleaseEvent(GuiElement source, float mouseX, float mouseY, MouseInputCode key)
    {
        super(source, mouseX, mouseY);

        this.key = key;
    }

    public MouseInputCode getKey()
    {
        return key;
    }

    @Override
    public ClickReleaseEvent copy(IEventEmitter source)
    {
        return new ClickReleaseEvent((GuiElement) source, getMouseX(), getMouseY(), getKey());
    }

    public static class Left extends ClickReleaseEvent
    {
        public static EventType<Left> TYPE = new EventType<>(ClickReleaseEvent.TYPE,
                "MOUSE_LEFT_CLICK_RELEASE_EVENT");

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

    public static class Right extends ClickReleaseEvent
    {
        public static EventType<Right> TYPE = new EventType<>(ClickReleaseEvent.TYPE,
                "MOUSE_RIGHT_CLICK_RELEASE_EVENT");

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

    public static class Middle extends ClickReleaseEvent
    {
        public static EventType<Middle> TYPE = new EventType<>(ClickReleaseEvent.TYPE,
                "MOUSE_MIDDLE_CLICK_RELEASE_EVENT");

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