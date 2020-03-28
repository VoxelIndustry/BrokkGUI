package net.voxelindustry.brokkgui.sprite;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SpriteAnimationInstanceTest
{
    @Test
    public void getCurrentTexture_givenFourFramesWithLinearTime_thenShouldReturnCorrectFrameAtGivenTime()
    {
        Map<Integer, Long> frames = new HashMap<>();
        frames.put(0, 250L);
        frames.put(1, 500L);
        frames.put(2, 750L);
        frames.put(3, 1000L);

        Texture texture = new Texture("blabla", 0, 0, 1, 1);

        SpriteAnimation spriteAnimation = new SpriteAnimation(false, 4, frames);
        SpriteAnimationInstance spriteAnimationInstance = spriteAnimation.instantiate();

        spriteAnimationInstance.computeTextures(texture);
        spriteAnimationInstance.start(0);

        assertThat(spriteAnimationInstance.getCurrentFrame(1))
                .isEqualTo(new Texture("blabla", 0, 0, 0.25F, 1));
        assertThat(spriteAnimationInstance.getCurrentFrame(251))
                .isEqualTo(new Texture("blabla", 0.25F, 0, 0.5F, 1));
        assertThat(spriteAnimationInstance.getCurrentFrame(501))
                .isEqualTo(new Texture("blabla", 0.5F, 0, 0.75F, 1));
        assertThat(spriteAnimationInstance.getCurrentFrame(751))
                .isEqualTo(new Texture("blabla", 0.75F, 0, 1, 1));
    }

    @Test
    public void getCurrentTexture_givenFourFramesWithLinearTime_andTimeOutsideDuration_thenShouldLoopBack()
    {
        Map<Integer, Long> frames = new HashMap<>();
        frames.put(0, 250L);
        frames.put(1, 500L);
        frames.put(2, 750L);
        frames.put(3, 1000L);

        Texture texture = new Texture("blabla", 0, 0, 1, 1);

        SpriteAnimation spriteAnimation = new SpriteAnimation(false, 4, frames);
        SpriteAnimationInstance spriteAnimationInstance = spriteAnimation.instantiate();

        spriteAnimationInstance.computeTextures(texture);
        spriteAnimationInstance.start(0);

        assertThat(spriteAnimationInstance.getCurrentFrame(1001))
                .isEqualTo(new Texture("blabla", 0, 0, 0.25F, 1));
        assertThat(spriteAnimationInstance.getCurrentFrame(1251))
                .isEqualTo(new Texture("blabla", 0.25F, 0, 0.5F, 1));
        assertThat(spriteAnimationInstance.getCurrentFrame(1501))
                .isEqualTo(new Texture("blabla", 0.5F, 0, 0.75F, 1));
        assertThat(spriteAnimationInstance.getCurrentFrame(1751))
                .isEqualTo(new Texture("blabla", 0.75F, 0, 1, 1));
    }
}