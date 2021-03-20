package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ClickDragEvent extends GuiMouseEvent
{
    public static final EventType<ClickDragEvent> TYPE = new EventType<>(ANY, "MOUSE_CLICK_DRAG_EVENT");

    private final MouseInputCode key;

    private final float originalMouseX;
    private final float originalMouseY;

    public ClickDragEvent(GuiElement source)
    {
        this(source, 0, 0, 0, 0, MouseInputCode.MOUSE_LEFT);
    }

    public ClickDragEvent(GuiElement source, float mouseX, float mouseY, float originalMouseX, float originalMouseY, MouseInputCode key)
    {
        super(source, mouseX, mouseY);

        this.key = key;

        this.originalMouseX = originalMouseX;
        this.originalMouseY = originalMouseY;
    }

    public MouseInputCode getKey()
    {
        return key;
    }

    public float getOriginalMouseX()
    {
        return originalMouseX;
    }

    public float getOriginalMouseY()
    {
        return originalMouseY;
    }

    @Override
    public ClickDragEvent copy(IEventEmitter source)
    {
        return new ClickDragEvent((GuiElement) source, getMouseX(), getMouseY(), getOriginalMouseX(), getOriginalMouseY(), getKey());
    }

    public static class Left extends ClickDragEvent
    {
        public static EventType<Left> TYPE = new EventType<>(ClickDragEvent.TYPE,
                "MOUSE_LEFT_CLICK_DRAG_EVENT");

        public Left(GuiElement source, float mouseX, float mouseY, float originalMouseX, float originalMouseY)
        {
            super(source, mouseX, mouseY, originalMouseX, originalMouseY, MouseInputCode.MOUSE_LEFT);
        }

        @Override
        public Left copy(IEventEmitter source)
        {
            return new Left((GuiElement) source, getMouseX(), getMouseY(), getOriginalMouseX(), getOriginalMouseY());
        }
    }

    public static class Right extends ClickDragEvent
    {
        public static EventType<Right> TYPE = new EventType<>(ClickDragEvent.TYPE,
                "MOUSE_RIGHT_CLICK_DRAG_EVENT");

        public Right(GuiElement source, float mouseX, float mouseY, float originalMouseX, float originalMouseY)
        {
            super(source, mouseX, mouseY, originalMouseX, originalMouseY, MouseInputCode.MOUSE_RIGHT);
        }

        @Override
        public Right copy(IEventEmitter source)
        {
            return new Right((GuiElement) source, getMouseX(), getMouseY(), getOriginalMouseX(), getOriginalMouseY());
        }
    }

    public static class Middle extends ClickDragEvent
    {
        public static EventType<Middle> TYPE = new EventType<>(ClickDragEvent.TYPE,
                "MOUSE_MIDDLE_CLICK_DRAG_EVENT");

        public Middle(GuiElement source, float mouseX, float mouseY, float originalMouseX, float originalMouseY)
        {
            super(source, mouseX, mouseY, originalMouseX, originalMouseY, MouseInputCode.MOUSE_BUTTON_MIDDLE);
        }

        @Override
        public Middle copy(IEventEmitter source)
        {
            return new Middle((GuiElement) source, getMouseX(), getMouseY(), getOriginalMouseX(), getOriginalMouseY());
        }
    }
}