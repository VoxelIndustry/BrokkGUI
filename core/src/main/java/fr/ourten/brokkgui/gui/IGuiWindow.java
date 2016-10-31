package fr.ourten.brokkgui.gui;

import fr.ourten.brokkgui.event.IGuiEventEmitter;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IGuiEventEmitter
{
    void open();

    void close();
}