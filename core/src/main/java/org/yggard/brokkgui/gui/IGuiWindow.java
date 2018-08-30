package org.yggard.brokkgui.gui;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.hermod.IEventEmitter;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IEventEmitter
{
    void open();

    void close();

    float getxRelativePos();

    float getyRelativePos();

    BaseProperty<Float> getxRelativePosProperty();

    BaseProperty<Float> getyRelativePosProperty();
}