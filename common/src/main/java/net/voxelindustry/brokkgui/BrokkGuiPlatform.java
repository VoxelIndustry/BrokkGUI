package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.animation.ITickSender;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import net.voxelindustry.brokkgui.internal.IMouseUtil;
import net.voxelindustry.brokkgui.internal.IResourceHandler;
import net.voxelindustry.brokkgui.internal.ITextHelper;
import net.voxelindustry.brokkgui.internal.profiler.IProfiler;
import net.voxelindustry.brokkgui.internal.profiler.ProfilerNoop;
import net.voxelindustry.brokkgui.mock.MockKeyboardUtil;
import net.voxelindustry.brokkgui.mock.MockMouseUtil;
import net.voxelindustry.brokkgui.mock.MockResourceHandler;
import net.voxelindustry.brokkgui.mock.MockTextHelper;
import net.voxelindustry.brokkgui.mock.MockTickSender;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Ourten 5 oct. 2016
 */
public class BrokkGuiPlatform
{
    private static BrokkGuiPlatform INSTANCE;

    public static BrokkGuiPlatform getInstance()
    {
        if (BrokkGuiPlatform.INSTANCE == null)
            BrokkGuiPlatform.INSTANCE = new BrokkGuiPlatform();
        return BrokkGuiPlatform.INSTANCE;
    }

    private ITextHelper      textHelper;
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
        random = new Random();
        profiler = new ProfilerNoop();
    }

    public void mock()
    {
        if (logger == null)
            logger = Logger.getLogger("BrokkGui Mock");
        if (keyboardUtil == null)
            keyboardUtil = new MockKeyboardUtil();
        if (mouseUtil == null)
            mouseUtil = new MockMouseUtil();
        if (resourceHandler == null)
            resourceHandler = new MockResourceHandler();
        if (textHelper == null)
            textHelper = new MockTextHelper();
        if (tickSender == null)
            tickSender = new MockTickSender();
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
        return logger;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    public IKeyboardUtil getKeyboardUtil()
    {
        return keyboardUtil;
    }

    public void setKeyboardUtil(IKeyboardUtil util)
    {
        keyboardUtil = util;
    }

    public IMouseUtil getMouseUtil()
    {
        return mouseUtil;
    }

    public void setMouseUtil(IMouseUtil mouseUtil)
    {
        this.mouseUtil = mouseUtil;
    }

    public IResourceHandler getResourceHandler()
    {
        return resourceHandler;
    }

    public void setResourceHandler(IResourceHandler resourceHandler)
    {
        this.resourceHandler = resourceHandler;
    }

    public String getPlatformName()
    {
        return platformName;
    }

    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public ITextHelper getTextHelper()
    {
        return textHelper;
    }

    public void setTextHelper(ITextHelper textHelper)
    {
        this.textHelper = textHelper;
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
        tickSender = sender;
    }

    public Random getRandom()
    {
        return random;
    }
}