package net.voxelindustry.brokkgui.sprite;

import java.util.Map;

public class SpriteAnimation
{
    public static SpriteAnimation EMPTY = new SpriteAnimation(null);

    private boolean vertical;

    private int  frameCount;
    private long defaultFrameTimeMillis;

    private Map<Integer, Long> frameTimeMillisByFrameIndex;

    private String  resourcePath;
    private boolean isParsed = false;

    public SpriteAnimation(String resourcePath)
    {
        this.resourcePath = resourcePath;
    }

    public SpriteAnimation(boolean vertical, int frameCount, long defaultFrameTimeMillis, Map<Integer, Long> frameTimeMillisByFrameIndex)
    {
        this.vertical = vertical;
        this.frameCount = frameCount;
        this.defaultFrameTimeMillis = defaultFrameTimeMillis;
        this.frameTimeMillisByFrameIndex = frameTimeMillisByFrameIndex;

        this.isParsed = true;
    }

    public void parse()
    {
        if (this.isParsed)
            return;
        // TODO: Sprite animation parser
    }

    public String getResourcePath()
    {
        return resourcePath;
    }

    public boolean isParsed()
    {
        return isParsed;
    }

    public boolean isVertical()
    {
        return vertical;
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public long getDefaultFrameTimeMillis()
    {
        return defaultFrameTimeMillis;
    }

    public Map<Integer, Long> getFrameTimeMillisByFrameIndex()
    {
        return frameTimeMillisByFrameIndex;
    }
}
