package org.yggard.brokkgui.wrapper.event;

import org.yggard.brokkgui.wrapper.container.BrokkGuiContainer;
import org.yggard.hermod.EventType;
import org.yggard.hermod.HermodEvent;

import net.minecraft.inventory.Slot;

/**
 * @author Ourten 31 oct. 2016
 */
public class SlotEvent extends HermodEvent
{
    public static final EventType<SlotEvent>       ANY   = new EventType<>("SLOT_EVENT");
    public static final EventType<SlotEvent.Click> CLICK = new EventType<>(SlotEvent.ANY, "SLOT_CLICK_EVENT");

    private final Slot                             slot;

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
}