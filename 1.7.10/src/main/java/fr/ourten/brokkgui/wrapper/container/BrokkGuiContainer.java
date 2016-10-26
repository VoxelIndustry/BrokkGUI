package fr.ourten.brokkgui.wrapper.container;

import fr.ourten.brokkgui.gui.BrokkGuiScreen;
import net.minecraft.inventory.Container;

public class BrokkGuiContainer<T extends Container> extends BrokkGuiScreen
{
    private final T container;

    public BrokkGuiContainer(final T container)
    {
        super();
        this.container = container;
    }

    public T getContainer()
    {
        return this.container;
    }
}