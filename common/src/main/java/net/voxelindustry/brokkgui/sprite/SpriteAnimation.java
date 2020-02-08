package net.voxelindustry.brokkgui.sprite;

import java.util.Map;

public class SpriteAnimation
{
    private boolean vertical;

    private int  frameCount;
    private long durationMillis;

    private Map<Integer, Long> frameTimeMillisByFrameIndex;

    public SpriteAnimation(boolean vertical, int frameCount, Map<Integer, Long> frameTimeMillisByFrameIndex)
    {
        this.vertical = vertical;
        this.frameCount = frameCount;
        this.frameTimeMillisByFrameIndex = frameTimeMillisByFrameIndex;

        this.durationMillis = frameTimeMillisByFrameIndex.get(frameTimeMillisByFrameIndex.size() - 1);
    }

    public SpriteAnimationInstance instantiate()
    {
        return new SpriteAnimationInstance(vertical, frameCount, frameTimeMillisByFrameIndex);
    }

    public boolean isVertical()
    {
        return vertical;
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public long getDurationMillis()
    {
        return durationMillis;
    }

    public Map<Integer, Long> getFrameTimeMillisByFrameIndex()
    {
        return frameTimeMillisByFrameIndex;
    }
}
