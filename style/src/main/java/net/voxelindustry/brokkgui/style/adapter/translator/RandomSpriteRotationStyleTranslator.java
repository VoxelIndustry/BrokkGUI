package net.voxelindustry.brokkgui.style.adapter.translator;

import com.google.common.base.Enums;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import java.util.EnumMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomSpriteRotationStyleTranslator implements IStyleTranslator<RandomSpriteRotation>
{
    @Override
    public RandomSpriteRotation decode(String style, AtomicInteger consumedLength)
    {
        String[] parts = style.split(" ");

        EnumMap<SpriteRotation, Float> rotations = new EnumMap<>(SpriteRotation.class);
        SpriteRotation rotation = null;

        for (String part : parts)
        {
            String trimmedPart = part.trim();
            if (trimmedPart.endsWith("%"))
            {
                if (rotation != null)
                    rotations.put(rotation, Float.parseFloat(trimmedPart.replace("%", "")) / 100);
                continue;
            }

            rotation = Enums.getIfPresent(SpriteRotation.class, trimmedPart.toUpperCase()).toJavaUtil()
                    .orElseThrow(() -> new RuntimeException("Cannot parse SpriteRotation argument. value=" + trimmedPart));
            rotations.put(rotation, null);
        }

        double total = rotations.values().stream().filter(Objects::nonNull).mapToDouble(Float::doubleValue).sum();
        long nullCount = rotations.values().stream().filter(Objects::isNull).count();

        if (nullCount != 0)
        {
            rotations.forEach((rotationKey, chanceValue) ->
            {
                if (chanceValue == null)
                    rotations.put(rotationKey, (float) ((1 - total) / nullCount));
            });
        }
        else
            rotations.put(SpriteRotation.NONE, (float) (rotations.getOrDefault(SpriteRotation.NONE, 0F) + (1 - total)));

        return new RandomSpriteRotation(rotations);
    }

    @Override
    public String encode(RandomSpriteRotation value, boolean prettyPrint)
    {
        String result = "";

        for (var entry : value.getChanceByRotation().entrySet())
        {
            SpriteRotation rotation = entry.getKey();
            Float chance = entry.getValue();
            result = result.concat(rotation.name()).concat(" ").concat(String.valueOf(chance)).concat("% ");
        }
        return result.trim();
    }
}
