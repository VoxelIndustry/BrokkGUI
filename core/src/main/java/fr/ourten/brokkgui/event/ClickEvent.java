package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;

public class ClickEvent extends GuiMouseEvent
{
    public static final EventType<ClickEvent> TYPE = new EventType<>(GuiMouseEvent.ANY, "MOUSE_CLICK_EVENT");

    private final int                         key;

    public ClickEvent(final GuiNode source)
    {
        this(source, 0, 0, 0);
    }

    public ClickEvent(final GuiNode source, final int mouseX, final int mouseY, final int key)
    {
        super(source, mouseX, mouseY);

        this.key = key;
    }

    public int getKey()
    {
        return this.key;
    }

    public static class Left extends ClickEvent
    {
        public static final EventType<ClickEvent.Left> TYPE = new EventType<>(ClickEvent.TYPE,
                "MOUSE_LEFT_CLICK_EVENT");

        public Left(GuiNode source, int mouseX, int mouseY)
        {
            super(source, mouseX, mouseY, 0);
        }
    }

    public static class Right extends ClickEvent
    {
        public static final EventType<ClickEvent.Right> TYPE = new EventType<>(ClickEvent.TYPE,
                "MOUSE_RIGHT_CLICK_EVENT");

        public Right(GuiNode source, int mouseX, int mouseY)
        {
            super(source, mouseX, mouseY, 1);
        }
    }

    public static class Middle extends ClickEvent
    {
        public static final EventType<ClickEvent.Middle> TYPE = new EventType<>(ClickEvent.TYPE,
                "MOUSE_MIDDLE_CLICK_EVENT");

        public Middle(GuiNode source, int mouseX, int mouseY)
        {
            super(source, mouseX, mouseY, 2);
        }
    }
}