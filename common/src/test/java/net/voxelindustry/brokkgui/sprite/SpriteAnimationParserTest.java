package net.voxelindustry.brokkgui.sprite;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class SpriteAnimationParserTest
{
    @Test
    public void parse()
    {
        String json = null;
        try
        {
            json = IOUtils.toString(SpriteAnimationParserTest.class.getResourceAsStream("/assets/brokkgui/animation/sprite_animation.json"), StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        SpriteAnimation spriteAnimation = SpriteAnimationParser.parse(json);

        assertThat(spriteAnimation.getFrameCount()).isEqualTo(9);
        assertThat(spriteAnimation.isVertical()).isTrue();

        assertThat(spriteAnimation.getFrameTimeMillisByFrameIndex()).hasSize(9);
        assertThat(spriteAnimation.getFrameTimeMillisByFrameIndex()).containsEntry(0, 111L);
        assertThat(spriteAnimation.getFrameTimeMillisByFrameIndex()).containsEntry(8, 1888L);
    }
}