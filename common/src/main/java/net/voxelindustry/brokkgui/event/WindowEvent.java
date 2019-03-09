package net.voxelindustry.brokkgui.event;

import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class WindowEvent extends HermodEvent
{
    public static final EventType<WindowEvent>       ANY   = new EventType<>("WINDOW_EVENT");
    public static final EventType<WindowEvent.Close> CLOSE = new EventType<>(WindowEvent.ANY, "WINDOW_CLOSE_EVENT");
    public static final EventType<WindowEvent.Open>  OPEN  = new EventType<>(WindowEvent.ANY, "WINDOW_OPEN_EVENT");

    public WindowEvent(IEventEmitter source)
    {
        super(source);
    }

    @Override
    public WindowEvent copy(IEventEmitter source)
    {
        return new WindowEvent(source);
    }

    public static class Close extends WindowEvent
    {
        public Close(IEventEmitter source)
        {
            super(source);
        }

        @Override
        public Close copy(IEventEmitter source)
        {
            return new Close(source);
        }
    }

    public static class Open extends WindowEvent
    {
        public Open(IEventEmitter source)
        {
            super(source);
        }

        @Override
        public Open copy(IEventEmitter source)
        {
            return new Open(source);
        }
    }
}