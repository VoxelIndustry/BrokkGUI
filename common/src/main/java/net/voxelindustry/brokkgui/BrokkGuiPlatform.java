package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.animation.ITickSender;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import net.voxelindustry.brokkgui.internal.IMouseUtil;
import net.voxelindustry.brokkgui.internal.profiler.IProfiler;
import net.voxelindustry.brokkgui.internal.IResourceHandler;
import net.voxelindustry.brokkgui.internal.profiler.ProfilerNoop;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Ourten 5 oct. 2016
 */
public class BrokkGuiPlatform
{
    private static volatile BrokkGuiPlatform INSTANCE;

    public static BrokkGuiPlatform getInstance()
    {
        if (BrokkGuiPlatform.INSTANCE == null)
            BrokkGuiPlatform.INSTANCE = new BrokkGuiPlatform();
        return BrokkGuiPlatform.INSTANCE;
    }

    private IGuiHelper       guiHelper;
    private IKeyboardUtil    keyboardUtil;
    private IMouseUtil       mouseUtil;
    private IResourceHandler resourceHandler;
    private ITickSender      tickSender;
    private IProfiler        profiler;

    private Logger logger;

    private String platformName;

    private boolean enableRenderDebug;

    private final Random random;

    private BrokkGuiPlatform()
    {
        StyleEngine.getInstance().start();

        this.random = new Random();
        this.profiler = new ProfilerNoop();
    }

    public IProfiler getProfiler()
    {
        return profiler;
    }

    public void setProfiler(IProfiler profiler)
    {
        this.profiler = profiler;
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
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

    public IResourceHandler getResourceHandler()
    {
        return this.resourceHandler;
    }

    public void setResourceHandler(IResourceHandler resourceHandler)
    {
        this.resourceHandler = resourceHandler;
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

    public boolean isRenderDebugEnabled()
    {
        return enableRenderDebug;
    }

    public void enableRenderDebug(boolean enableRenderDebug)
    {
        this.enableRenderDebug = enableRenderDebug;
    }

    public ITickSender getTickSender()
    {
        return tickSender;
    }

    public void setTickSender(ITickSender sender)
    {
        this.tickSender = sender;
    }

    public Random getRandom()
    {
        return this.random;
    }
}