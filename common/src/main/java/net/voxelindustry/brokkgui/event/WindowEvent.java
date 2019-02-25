package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

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