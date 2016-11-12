package org.yggard.brokkgui.event;

import org.yggard.brokkgui.gui.IGuiWindow;

import fr.ourten.hermod.EventType;
import fr.ourten.hermod.HermodEvent;

public class WindowEvent extends HermodEvent
{
    public static final EventType<WindowEvent>       ANY   = new EventType<>("WINDOW_EVENT");
    public static final EventType<WindowEvent.Close> CLOSE = new EventType<>(WindowEvent.ANY, "WINDOW_CLOSE_EVENT");
    public static final EventType<WindowEvent.Open>  OPEN  = new EventType<>(WindowEvent.ANY, "WINDOW_OPEN_EVENT");

    public WindowEvent(final IGuiWindow source)
    {
        super(source);
    }

    public static class Close extends WindowEvent
    {
        public Close(final IGuiWindow source)
        {
            super(source);
        }
    }

    public static class Open extends WindowEvent
    {
        public Open(final IGuiWindow source)
        {
            super(source);
        }
    }
}