package org.yggard.brokkgui.wrapper.container;

import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.wrapper.event.SlotEvent;

import fr.ourten.hermod.EventHandler;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class BrokkGuiContainer<T extends Container> extends BrokkGuiScreen
{
    private final T                       container;

    private EventHandler<SlotEvent.Click> onSlotClickEvent;

    public BrokkGuiContainer(final T container)
    {
        super();
        this.container = container;
    }

    public T getContainer()
    {
        return this.container;
    }

    public void slotClick(final Slot slot, final int key)
    {
        this.getEventDispatcher().dispatchEvent(SlotEvent.CLICK, new SlotEvent.Click(this, slot, key));
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnSlotClickEvent(final EventHandler<SlotEvent.Click> onSlotClickEvent)
    {
        this.getEventDispatcher().removeHandler(SlotEvent.CLICK, this.onSlotClickEvent);
        this.onSlotClickEvent = onSlotClickEvent;
        this.getEventDispatcher().addHandler(SlotEvent.CLICK, this.onSlotClickEvent);
    }
}