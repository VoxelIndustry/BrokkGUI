package org.yggard.brokkgui.wrapper;

import org.lwjgl.input.Mouse;
import org.yggard.brokkgui.internal.IMouseUtil;

/**
 * @author Ourten 9 oct. 2016
 */
public class MouseUtil implements IMouseUtil
{
    @Override
    public int getEventDWheel()
    {
        return Mouse.getEventDWheel();
    }

    @Override
    public int getEventButton()
    {
        return Mouse.getEventButton();
    }
}