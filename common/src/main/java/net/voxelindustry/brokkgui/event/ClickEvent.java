package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.exp.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ClickEvent extends GuiMouseEvent
{
    public static final EventType<ClickEvent> TYPE = new EventType<>(ANY, "MOUSE_CLICK_EVENT");

    private final int key;

    public ClickEvent(GuiElement source)
    {
        this(source, 0, 0, 0);
    }

    public ClickEvent(GuiElement source, int mouseX, int mouseY, int key)
    {
        super(source, mouseX, mouseY);

        this.key = key;
    }

    public int getKey()
    {
        return this.key;
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
            super(source, mouseX, mouseY, 0);
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
            super(source, mouseX, mouseY, 1);
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
            super(source, mouseX, mouseY, 2);
        }

        @Override
        public Middle copy(IEventEmitter source)
        {
            return new Middle((GuiElement) source, getMouseX(), getMouseY());
        }
    }
}