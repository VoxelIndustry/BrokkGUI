package net.voxelindustry.brokkgui.wrapper.event;

import net.minecraft.container.Slot;
import net.voxelindustry.brokkgui.wrapper.container.BrokkGuiContainer;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

/**
 * @author Ourten 31 oct. 2016
 */
public class SlotEvent extends HermodEvent
{
    public static final EventType<SlotEvent>       ANY    = new EventType<>("SLOT_EVENT");
    public static final EventType<SlotEvent.Click> CLICK  = new EventType<>(SlotEvent.ANY, "SLOT_CLICK_EVENT");
    public static final EventType<Change>          CHANGE = new EventType<>(SlotEvent.ANY, "SLOT_CHANGE_EVENT");

    private final Slot slot;

    public SlotEvent(final BrokkGuiContainer<?> source, final Slot slot)
    {
        super(source);

        this.slot = slot;
    }

    public Slot getSlot()
    {
        return this.slot;
    }

    public static final class Click extends SlotEvent
    {
        private final int key;

        public Click(final BrokkGuiContainer<?> source, final Slot slot, final int key)
        {
            super(source, slot);

            this.key = key;
        }

        public int getKey()
        {
            return this.key;
        }
    }

    public static final class Change extends SlotEvent
    {
        public Change(final BrokkGuiContainer<?> source, final Slot slot)
        {
            super(source, slot);
        }
    }
}
