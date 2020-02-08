package net.voxelindustry.brokkgui.sprite;

import java.util.Map;

public class SpriteAnimationInstance extends SpriteAnimation
{
    private final Texture[] computedTextures;
    private       long      startTimeMillis;

    public SpriteAnimationInstance(boolean vertical, int frameCount, Map<Integer, Long> frameTimeMillisByFrameIndex)
    {
        super(vertical, frameCount, frameTimeMillisByFrameIndex);
        this.computedTextures = new Texture[frameCount];
    }

    public void start(long currentTimeMillis)
    {
        this.startTimeMillis = currentTimeMillis;
    }

    public Texture getCurrentFrame(long currentTimeMillis)
    {
        if (currentTimeMillis - this.startTimeMillis > this.getDurationMillis())
        {
            this.startTimeMillis = currentTimeMillis - (currentTimeMillis - this.startTimeMillis) % this.getDurationMillis();
        }

        int index;
        for (index = 0; index < this.getFrameCount(); index++)
        {
            if (this.getFrameTimeMillisByFrameIndex().get(index) >= currentTimeMillis - this.startTimeMillis)
                break;
        }

        return this.computedTextures[index];
    }

    public void computeTextures(Texture origin)
    {
        for (int frame = 0; frame < getFrameCount(); frame++)
        {
            if (this.isVertical())
            {
                this.computedTextures[frame] = new Texture(
                        origin.getResource(),
                        origin.getUMin(),
                        (origin.getVMax() - origin.getVMin()) / getFrameCount() * frame + origin.getVMin(),
                        origin.getUMax(),
                        (origin.getVMax() - origin.getVMin()) / getFrameCount() * (frame + 1) + origin.getVMin(),
                        origin.getPixelWidth(),
                        origin.getPixelHeight());
            }
            else
            {
                this.computedTextures[frame] = new Texture(
                        origin.getResource(),
                        (origin.getUMax() - origin.getUMin()) / getFrameCount() * frame + origin.getUMin(),
                        origin.getVMin(),
                        (origin.getUMax() - origin.getUMin()) / getFrameCount() * (frame + 1) + origin.getUMin(),
                        origin.getVMax(),
                        origin.getPixelWidth(),
                        origin.getPixelHeight());
            }
        }
    }
}
