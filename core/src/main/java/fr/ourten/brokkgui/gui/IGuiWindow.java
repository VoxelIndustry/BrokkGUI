package fr.ourten.brokkgui.gui;

import fr.ourten.hermod.IEventEmitter;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IEventEmitter
{
    void open();

    void close();
}