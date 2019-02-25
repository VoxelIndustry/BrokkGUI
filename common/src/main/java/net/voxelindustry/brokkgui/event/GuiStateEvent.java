package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

public class GuiStateEvent extends HermodEvent
{
    public static final EventType<GuiStateEvent> ANY = new EventType<>("STATE_EVENT");

    public GuiStateEvent(final GuiNode source)
    {
        super(source);
    }
}