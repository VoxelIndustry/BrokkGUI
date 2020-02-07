package net.voxelindustry.brokkgui.style.event;

import net.voxelindustry.brokkgui.style.ICascadeStyleable;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class StyleRefreshEvent extends HermodEvent
{
    public static final EventType<StyleRefreshEvent> ANY = new EventType<>("STYLE_REFRESH_EVENT");

    public static final EventType<BeforeEvent> BEFORE = new EventType<>("BEFORE_STYLE_REFRESH_EVENT");
    public static final EventType<AfterEvent> AFTER = new EventType<>("AFTER_STYLE_REFRESH_EVENT");

    public StyleRefreshEvent(ICascadeStyleable source)
    {
        super(source);
    }

    @Override
    public HermodEvent copy(IEventEmitter source)
    {
        return new StyleRefreshEvent((ICascadeStyleable) source);
    }

    public static class BeforeEvent extends StyleRefreshEvent
    {
        public BeforeEvent(ICascadeStyleable source)
        {
            super(source);
        }

        @Override
        public HermodEvent copy(IEventEmitter source)
        {
            return new BeforeEvent((ICascadeStyleable) source);
        }
    }

    public static class AfterEvent extends StyleRefreshEvent
    {
        public AfterEvent(ICascadeStyleable source)
        {
            super(source);
        }

        @Override
        public HermodEvent copy(IEventEmitter source)
        {
            return new AfterEvent((ICascadeStyleable) source);
        }
    }
}
