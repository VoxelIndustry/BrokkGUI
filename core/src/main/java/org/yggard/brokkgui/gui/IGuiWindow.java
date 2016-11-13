package org.yggard.brokkgui.gui;

import org.yggard.hermod.IEventEmitter;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IEventEmitter
{
    void open();

    void close();
}