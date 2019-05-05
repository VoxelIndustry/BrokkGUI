package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class LayoutEvent extends HermodEvent
{
    public static final EventType<LayoutEvent> ANY    = new EventType<>("LAYOUT_EVENT");
    public static final EventType<Add>         ADD    = new EventType<>(ANY, "LAYOUT_ADD_EVENT");
    public static final EventType<Remove>      REMOVE = new EventType<>(ANY, "LAYOUT_REMOVE_EVENT");

    public LayoutEvent(GuiElement source)
    {
        super(source);
    }

    @Override
    public LayoutEvent copy(IEventEmitter source)
    {
        return new LayoutEvent((GuiElement) source);
    }

    public static class Add extends LayoutEvent
    {
        public Add(GuiElement source)
        {
            super(source);
        }

        @Override
        public Add copy(IEventEmitter source)
        {
            return new Add((GuiElement) source);
        }
    }

    public static class Remove extends LayoutEvent
    {
        public Remove(GuiElement source)
        {
            super(source);
        }

        @Override
        public Remove copy(IEventEmitter source)
        {
            return new Remove((GuiElement) source);
        }
    }
}
