package fr.ourten.brokkgui.wrapper.container;

import fr.ourten.brokkgui.event.EventHandler;
import fr.ourten.brokkgui.gui.BrokkGuiScreen;
import fr.ourten.brokkgui.wrapper.event.SlotEvent;
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