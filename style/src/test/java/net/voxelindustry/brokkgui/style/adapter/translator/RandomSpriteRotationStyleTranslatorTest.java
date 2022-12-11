package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.voxelindustry.brokkgui.sprite.SpriteRotation.*;

public class RandomSpriteRotationStyleTranslatorTest
{
    private RandomSpriteRotationStyleTranslator translator;

    @BeforeEach
    public void setup()
    {
        translator = new RandomSpriteRotationStyleTranslator();
    }

    @Test
    public void decode_givenOneRotationWithoutChance_thenShouldGiveOneRotationWithFullChance()
    {
        String simpleRotation = "clockwise";

        RandomSpriteRotation randomSpriteRotation = translator.decode(simpleRotation);

        Assertions.assertThat(randomSpriteRotation.getChanceByRotation()).containsExactly(MapEntry.entry(CLOCKWISE, 1F));
    }

    @Test
    public void decode_givenOneRotationWithChance_thenShouldGiveOneRotationWithChanceAndNoRotationWithRemainingChance()
    {
        String rotationWithChance = "clockwise 20%";

        RandomSpriteRotation randomSpriteRotation = translator.decode(rotationWithChance);

        Assertions.assertThat(randomSpriteRotation.getChanceByRotation()).containsOnly(
                MapEntry.entry(CLOCKWISE, 0.2F),
                MapEntry.entry(NONE, 0.8F));
    }

    @Test
    public void decode_givenTwoRotation_OneWithChanceOneWithout_thenShouldGiveRemainingChanceToRotationWithout()
    {
        String rotations = "clockwise 20% counterclockwise";

        RandomSpriteRotation randomSpriteRotation = translator.decode(rotations);

        Assertions.assertThat(randomSpriteRotation.getChanceByRotation()).containsOnly(
                MapEntry.entry(CLOCKWISE, 0.2F),
                MapEntry.entry(COUNTERCLOCKWISE, 0.8F));
    }
}