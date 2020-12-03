package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ClickEvent extends GuiMouseEvent
{
    public static final EventType<ClickEvent> TYPE = new EventType<>(ANY, "MOUSE_CLICK_EVENT");

    private final MouseInputCode key;

    public ClickEvent(GuiElement source)
    {
        this(source, 0, 0, MouseInputCode.MOUSE_LEFT);
    }

    public ClickEvent(GuiElement source, int mouseX, int mouseY, MouseInputCode key)
    {
        super(source, mouseX, mouseY);

        this.key = key;
    }

    public MouseInputCode getKey()
    {
        return key;
    }

    @Override
    public ClickEvent copy(IEventEmitter source)
    {
        return new ClickEvent((GuiElement) source, getMouseX(), getMouseY(), getKey());
    }

    public static class Left extends ClickEvent
    {
        public static EventType<ClickEvent.Left> TYPE = new EventType<>(ClickEvent.TYPE,
                "MOUSE_LEFT_CLICK_EVENT");

        public Left(GuiElement source, int mouseX, int mouseY)
        {
            super(source, mouseX, mouseY, MouseInputCode.MOUSE_LEFT);
        }

        @Override
        public Left copy(IEventEmitter source)
        {
            return new Left((GuiElement) source, getMouseX(), getMouseY());
        }
    }

    public static class Right extends ClickEvent
    {
        public static EventType<ClickEvent.Right> TYPE = new EventType<>(ClickEvent.TYPE,
                "MOUSE_RIGHT_CLICK_EVENT");

        public Right(GuiElement source, int mouseX, int mouseY)
        {
            super(source, mouseX, mouseY, MouseInputCode.MOUSE_RIGHT);
        }

        @Override
        public Right copy(IEventEmitter source)
        {
            return new Right((GuiElement) source, getMouseX(), getMouseY());
        }
    }

    public static class Middle extends ClickEvent
    {
        public static EventType<ClickEvent.Middle> TYPE = new EventType<>(ClickEvent.TYPE,
                "MOUSE_MIDDLE_CLICK_EVENT");

        public Middle(GuiElement source, int mouseX, int mouseY)
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