package net.voxelindustry.brokkgui.style.optional;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.shape.GuiShape;
import net.voxelindustry.brokkgui.shape.ShapeSpriteRotationCache;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.event.StyleRefreshEvent;

import java.util.Map;
import java.util.function.Consumer;

public class SpriteRandomRotationProperties implements Consumer<StyleHolder>
{
    private static SpriteRandomRotationProperties backgroundInstance;
    private static SpriteRandomRotationProperties foregroundInstance;

    public static SpriteRandomRotationProperties getBackgroundInstance()
    {
        if (backgroundInstance == null)
            backgroundInstance = new SpriteRandomRotationProperties(true);
        return backgroundInstance;
    }

    public static SpriteRandomRotationProperties getForegroundInstance()
    {
        if (foregroundInstance == null)
            foregroundInstance = new SpriteRandomRotationProperties(false);
        return foregroundInstance;
    }

    private boolean isBackground;

    private SpriteRandomRotationProperties(boolean isBackground)
    {
        this.isBackground = isBackground;
    }

    @Override
    public void accept(StyleHolder holder)
    {
        String key = isBackground ? "background" : "foreground";

        GuiShape shape = (GuiShape) holder.getOwner();

        IProperty<RandomSpriteRotation> randomRotationProperty = holder.registerProperty(key + "-rotation", null, RandomSpriteRotation.class);
        IProperty<Texture> textureProperty = holder.getStyleProperty(key + "-texture", Texture.class);
        IProperty<SpriteRepeat> repeatProperty = holder.getStyleProperty(key + "-repeat", SpriteRepeat.class);

        ShapeSpriteRotationCache cache = new ShapeSpriteRotationCache();
        BaseProperty<Boolean> styleRefreshProperty = new BaseProperty<>(Boolean.FALSE, "styleRefreshProperty");

        holder.getOwner().getEventDispatcher().addHandler(StyleRefreshEvent.BEFORE, event -> styleRefreshProperty.setValue(true));
        holder.getOwner().getEventDispatcher().addHandler(StyleRefreshEvent.AFTER, event ->
        {
            styleRefreshProperty.setValue(false);
            updateRotationCache(
                    shape,
                    cache,
                    randomRotationProperty,
                    textureProperty,
                    repeatProperty,
                    false);
        });

        ValueInvalidationListener listener = obs -> updateRotationCache(
                shape,
                cache,
                randomRotationProperty,
                textureProperty,
                repeatProperty,
                styleRefreshProperty.getValue());

        shape.getWidthProperty().addListener(listener);
        shape.getHeightProperty().addListener(listener);
        repeatProperty.addListener(listener);
        textureProperty.addListener(listener);
        randomRotationProperty.addListener(listener);
    }

    private void updateRotationCache(GuiShape shape,
                                     ShapeSpriteRotationCache cache,
                                     IProperty<RandomSpriteRotation> randomRotationProperty,
                                     IProperty<Texture> textureProperty,
                                     IProperty<SpriteRepeat> repeatProperty,
                                     boolean isStyleRefreshing)
    {
        if (isStyleRefreshing ||
                !randomRotationProperty.isPresent() ||
                cache.matches(randomRotationProperty.getValue(), textureProperty.getValue(), repeatProperty.getValue(), shape.getWidth(), shape.getHeight()))
            return;

        cache.setLastRandomSpriteRotation(randomRotationProperty.getValue());
        cache.setLastRepeat(repeatProperty.getValue());
        cache.setLastTexture(textureProperty.getValue());
        cache.setLastWidth(shape.getWidth());
        cache.setLastHeight(shape.getHeight());

        SpriteRotation[] rotations = new SpriteRotation[repeatProperty.getValue().getRepeatCount(
                textureProperty.getValue(),
                shape.getWidth(),
                shape.getHeight())];

        boolean anythingSelected = false;

        for (int index = 0; index < rotations.length; index++)
        {
            SpriteRotation selected = SpriteRotation.NONE;
            for (Map.Entry<SpriteRotation, Float> chanceByRotation : randomRotationProperty.getValue().getChanceByRotation().entrySet())
            {
                if (BrokkGuiPlatform.getInstance().getRandom().nextFloat() <= chanceByRotation.getValue())
                {
                    selected = chanceByRotation.getKey();
                    anythingSelected = true;
                    break;
                }
            }

            rotations[index] = selected;
        }

        if (this.isBackground)
            shape.setBackgroundRotationArray(anythingSelected ? rotations : null);
        else
            shape.setForegroundRotationArray(anythingSelected ? rotations : null);
    }
}
