package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;

import static net.voxelindustry.brokkgui.sprite.SpriteRotation.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RandomSpriteRotationStyleTranslatorTest
{
    private RandomSpriteRotationStyleTranslator translator;

    @Before
    public void setup()
    {
        translator = new RandomSpriteRotationStyleTranslator();
    }

    @Test
    public void decode_givenOneRotationWithoutChance_thenShouldGiveOneRotationWithFullChance()
    {
        String simpleRotation = "clockwise";

        RandomSpriteRotation randomSpriteRotation = translator.decode(simpleRotation);

        assertThat(randomSpriteRotation.getChanceByRotation()).containsExactly(MapEntry.entry(CLOCKWISE, 1F));
    }

    @Test
    public void decode_givenOneRotationWithChance_thenShouldGiveOneRotationWithChanceAndNoRotationWithRemainingChance()
    {
        String rotationWithChance = "clockwise 20%";

        RandomSpriteRotation randomSpriteRotation = translator.decode(rotationWithChance);

        assertThat(randomSpriteRotation.getChanceByRotation()).containsOnly(
                MapEntry.entry(CLOCKWISE, 0.2F),
                MapEntry.entry(NONE, 0.8F));
    }

    @Test
    public void decode_givenTwoRotation_OneWithChanceOneWithout_thenShouldGiveRemainingChanceToRotationWithout()
    {
        String rotations = "clockwise 20% counterclockwise";

        RandomSpriteRotation randomSpriteRotation = translator.decode(rotations);

        assertThat(randomSpriteRotation.getChanceByRotation()).containsOnly(
                MapEntry.entry(CLOCKWISE, 0.2F),
                MapEntry.entry(COUNTERCLOCKWISE, 0.8F));
    }
}