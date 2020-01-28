package net.voxelindustry.brokkgui.sprite;

import java.util.Map;

public class SpriteAnimation
{
    public static SpriteAnimation EMPTY = new SpriteAnimation(null);

    private boolean vertical;

    private int frameCount;

    private Map<Integer, Long> frameTimeMillisByFrameIndex;

    private String  resourcePath;
    private boolean isParsed = false;

    public SpriteAnimation(String resourcePath)
    {
        this.resourcePath = resourcePath;
    }

    public SpriteAnimation(boolean vertical, int frameCount, Map<Integer, Long> frameTimeMillisByFrameIndex)
    {
        this.vertical = vertical;
        this.frameCount = frameCount;
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

    public Map<Integer, Long> getFrameTimeMillisByFrameIndex()
    {
        return frameTimeMillisByFrameIndex;
    }
}
