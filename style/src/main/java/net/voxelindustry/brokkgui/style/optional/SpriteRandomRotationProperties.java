package net.voxelindustry.brokkgui.style.optional;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.shape.ShapeSpriteRotationCache;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.event.StyleRefreshEvent;

import java.util.Map;
import java.util.function.Consumer;

public class SpriteRandomRotationProperties implements Consumer<StyleComponent>
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
    public void accept(StyleComponent holder)
    {
        String key = isBackground ? "background" : "foreground";

        GuiElement element = holder.element();

        IProperty<RandomSpriteRotation> randomRotationProperty = holder.registerProperty(key + "-rotation", null, RandomSpriteRotation.class);
        IProperty<Texture> textureProperty = holder.getProperty(key + "-texture", Texture.class);
        IProperty<SpriteRepeat> repeatProperty = holder.getProperty(key + "-repeat", SpriteRepeat.class);

        ShapeSpriteRotationCache cache = new ShapeSpriteRotationCache();
        BaseProperty<Boolean> styleRefreshProperty = new BaseProperty<>(Boolean.FALSE, "styleRefreshProperty");

        holder.element().getEventDispatcher().addHandler(StyleRefreshEvent.BEFORE, event -> styleRefreshProperty.setValue(true));
        holder.element().getEventDispatcher().addHandler(StyleRefreshEvent.AFTER, event ->
        {
            styleRefreshProperty.setValue(false);
            updateRotationCache(
                    element,
                    cache,
                    randomRotationProperty,
                    textureProperty,
                    repeatProperty,
                    false);
        });

        ValueInvalidationListener listener = obs -> updateRotationCache(
                element,
                cache,
                randomRotationProperty,
                textureProperty,
                repeatProperty,
                styleRefreshProperty.getValue());

        element.transform().widthProperty().addListener(listener);
        element.transform().heightProperty().addListener(listener);
        repeatProperty.addListener(listener);
        textureProperty.addListener(listener);
        randomRotationProperty.addListener(listener);
    }

    private void updateRotationCache(GuiElement shape,
                                     ShapeSpriteRotationCache cache,
                                     IProperty<RandomSpriteRotation> randomRotationProperty,
                                     IProperty<Texture> textureProperty,
                                     IProperty<SpriteRepeat> repeatProperty,
                                     boolean isStyleRefreshing)
    {
        if (isStyleRefreshing ||
                !randomRotationProperty.isPresent() ||
                cache.matches(randomRotationProperty.getValue(),
                        textureProperty.getValue(),
                        repeatProperty.getValue(),
                        shape.transform().width(),
                        shape.transform().height()))
            return;

        cache.setLastRandomSpriteRotation(randomRotationProperty.getValue());
        cache.setLastRepeat(repeatProperty.getValue());
        cache.setLastTexture(textureProperty.getValue());
        cache.setLastWidth(shape.transform().width());
        cache.setLastHeight(shape.transform().height());

        SpriteRotation[] rotations = new SpriteRotation[repeatProperty.getValue().getRepeatCount(
                textureProperty.getValue(),
                shape.transform().width(),
                shape.transform().height())];

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

        if (isBackground)
            shape.paint().backgroundRotationArray(anythingSelected ? rotations : null);
        else
            shape.paint().foregroundRotationArray(anythingSelected ? rotations : null);
    }
}
