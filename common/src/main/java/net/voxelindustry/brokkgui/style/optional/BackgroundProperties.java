package net.voxelindustry.brokkgui.style.optional;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.shape.GuiShape;
import net.voxelindustry.brokkgui.shape.ShapeSpriteRotationCache;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationInstance;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationParser;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.event.StyleRefreshEvent;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

public class BackgroundProperties implements Consumer<StyleHolder>
{
    private static BackgroundProperties backgroundInstance;
    private static BackgroundProperties foregroundInstance;

    public static BackgroundProperties getBackgroundInstance()
    {
        if (backgroundInstance == null)
            backgroundInstance = new BackgroundProperties(true);
        return backgroundInstance;
    }

    public static BackgroundProperties getForegroundInstance()
    {
        if (foregroundInstance == null)
            foregroundInstance = new BackgroundProperties(false);
        return foregroundInstance;
    }

    private boolean isBackground;

    private BackgroundProperties(boolean isBackground)
    {
        this.isBackground = isBackground;
    }

    @Override
    public void accept(StyleHolder holder)
    {
        String key = isBackground ? "background" : "foreground";
        holder.registerProperty(key + "-color", Color.ALPHA, Color.class);

        holder.registerProperty(key + "-texture", Texture.EMPTY, Texture.class);
        holder.registerProperty(key + "-repeat", SpriteRepeat.NONE, SpriteRepeat.class);
        holder.registerProperty(key + "-animation", null, Resource.class);
        holder.registerProperty(key + "-position", RectBox.EMPTY, RectBox.class);

        StyleProperty<RandomSpriteRotation> randomSpriteRotationProperty = holder.registerProperty(key + "-rotation", null, RandomSpriteRotation.class);

        if (isBackground)
        {
            StyleProperty<Resource> animationProperty = holder.getStyleProperty("background-animation", Resource.class);

            animationProperty.addListener((obs, oldValue, newValue) ->
            {
                if (newValue != null)
                {
                    SpriteAnimationInstance spriteAnimationInstance = SpriteAnimationParser.getAnimation(newValue).instantiate();
                    spriteAnimationInstance.computeTextures(((GuiShape) holder.getOwner()).getBackgroundTexture());
                    ((GuiShape) holder.getOwner()).setBackgroundAnimation(spriteAnimationInstance);
                }
            });

            StyleProperty<Texture> textureProperty = holder.getStyleProperty("background-texture", Texture.class);

            textureProperty.addListener((obs, oldValue, newValue) ->
            {
                ofNullable(((GuiShape) holder.getOwner()).getBackgroundAnimation()).ifPresent(sai -> sai.computeTextures(newValue));
            });

            StyleProperty<SpriteRepeat> spriteRepeatProperty = holder.getStyleProperty("background-repeat", SpriteRepeat.class);

            setupRotationBinding((GuiShape) holder.getOwner(),
                    holder,
                    randomSpriteRotationProperty,
                    textureProperty,
                    spriteRepeatProperty);
        }
        else
        {
            holder.getStyleProperty("foreground-animation", Resource.class).addListener((obs, oldValue, newValue) ->
            {
                if (newValue != null)
                {
                    SpriteAnimationInstance spriteAnimationInstance = SpriteAnimationParser.getAnimation(newValue).instantiate();
                    spriteAnimationInstance.computeTextures(((GuiShape) holder.getOwner()).getForegroundTexture());
                    ((GuiShape) holder.getOwner()).setForegroundAnimation(spriteAnimationInstance);
                }
            });

            holder.getStyleProperty("foreground-texture", Texture.class).addListener((obs, oldValue, newValue) ->
            {
                ofNullable(((GuiShape) holder.getOwner()).getForegroundAnimation()).ifPresent(sai -> sai.computeTextures(newValue));
            });
        }
    }

    private void setupRotationBinding(GuiShape shape,
                                      StyleHolder holder,
                                      IProperty<RandomSpriteRotation> randomRotationProperty,
                                      IProperty<Texture> textureProperty,
                                      IProperty<SpriteRepeat> repeatProperty)
    {
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

        shape.setBackgroundRotationArray(anythingSelected ? rotations : null);
    }
}