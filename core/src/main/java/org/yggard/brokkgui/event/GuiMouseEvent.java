package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;

import fr.ourten.hermod.EventType;

public class GuiMouseEvent extends GuiInputEvent
{
    public static final EventType<GuiMouseEvent>       ANY   = new EventType<>(GuiInputEvent.ANY, "INPUT_MOUSE_EVENT");
    public static final EventType<GuiMouseEvent.Wheel> WHEEL = new EventType<>(GuiMouseEvent.ANY, "WHEEL_MOUSE_EVENT");

    private final int                                  mouseX, mouseY;

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
}