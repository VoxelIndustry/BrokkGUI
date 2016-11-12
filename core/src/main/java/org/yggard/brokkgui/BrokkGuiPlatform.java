package org.yggard.brokkgui;

import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.internal.IKeyboardUtil;
import org.yggard.brokkgui.internal.IMouseUtil;

/**
 * @author Ourten 5 oct. 2016
 */
public class BrokkGuiPlatform
{
    private static volatile BrokkGuiPlatform INSTANCE;

    public static final BrokkGuiPlatform getInstance()
    {
        if (BrokkGuiPlatform.INSTANCE == null)
            synchronized (BrokkGuiPlatform.class)
            {
                if (BrokkGuiPlatform.INSTANCE == null)
                    BrokkGuiPlatform.INSTANCE = new BrokkGuiPlatform();
            }
        return BrokkGuiPlatform.INSTANCE;
    }

    private IGuiHelper    guiHelper;
    private IKeyboardUtil keyboardUtil;
    private IMouseUtil    mouseUtil;
    private String        platformName;

    private BrokkGuiPlatform()
    {

    }

    public IKeyboardUtil getKeyboardUtil()
    {
        return this.keyboardUtil;
    }

    public void setKeyboardUtil(final IKeyboardUtil util)
    {
        this.keyboardUtil = util;
    }

    public IMouseUtil getMouseUtil()
    {
        return this.mouseUtil;
    }

    public void setMouseUtil(final IMouseUtil mouseUtil)
    {
        this.mouseUtil = mouseUtil;
    }

    public String getPlatformName()
    {
        return this.platformName;
    }

    public void setPlatformName(final String platformName)
    {
        this.platformName = platformName;
    }

    public IGuiHelper getGuiHelper()
    {
        return this.guiHelper;
    }

    public void setGuiHelper(final IGuiHelper guiHelper)
    {
        this.guiHelper = guiHelper;
    }
}