package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.animation.ITickSender;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import net.voxelindustry.brokkgui.internal.IMouseUtil;

/**
 * @author Ourten 5 oct. 2016
 */
public class BrokkGuiPlatform
{
    private static volatile BrokkGuiPlatform INSTANCE;

    public static BrokkGuiPlatform instance()
    {
        if (BrokkGuiPlatform.INSTANCE == null)
            BrokkGuiPlatform.INSTANCE = new BrokkGuiPlatform();
        return BrokkGuiPlatform.INSTANCE;
    }

    private IGuiHelper    guiHelper;
    private IKeyboardUtil keyboardUtil;
    private IMouseUtil    mouseUtil;
    private ITickSender   tickSender;
    private String        platformName;

    private boolean enableRenderDebug;

    private BrokkGuiPlatform()
    {
    }

    public IKeyboardUtil keyboardUtil()
    {
        return this.keyboardUtil;
    }

    public void keyboardUtil(IKeyboardUtil util)
    {
        this.keyboardUtil = util;
    }

    public IMouseUtil mouseUtil()
    {
        return this.mouseUtil;
    }

    public void mouseUtil(IMouseUtil mouseUtil)
    {
        this.mouseUtil = mouseUtil;
    }

    public String platformName()
    {
        return this.platformName;
    }

    public void platformName(String platformName)
    {
        this.platformName = platformName;
    }

    public IGuiHelper guiHelper()
    {
        return this.guiHelper;
    }

    public void guiHelper(IGuiHelper guiHelper)
    {
        this.guiHelper = guiHelper;
    }

    public boolean isRenderDebugEnabled()
    {
        return enableRenderDebug;
    }

    public void enableRenderDebug(boolean enableRenderDebug)
    {
        this.enableRenderDebug = enableRenderDebug;
    }

    public ITickSender tickSender()
    {
        return tickSender;
    }

    public void tickSender(ITickSender sender)
    {
        this.tickSender = sender;
    }
}