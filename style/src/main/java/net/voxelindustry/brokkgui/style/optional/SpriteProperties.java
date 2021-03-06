package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationInstance;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationParser;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.StyleProperty;

import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

public class SpriteProperties implements Consumer<StyleComponent>
{
    private static SpriteProperties backgroundInstance;
    private static SpriteProperties foregroundInstance;

    public static SpriteProperties getBackgroundInstance()
    {
        if (backgroundInstance == null)
            backgroundInstance = new SpriteProperties(true);
        return backgroundInstance;
    }

    public static SpriteProperties getForegroundInstance()
    {
        if (foregroundInstance == null)
            foregroundInstance = new SpriteProperties(false);
        return foregroundInstance;
    }

    private boolean isBackground;

    private SpriteProperties(boolean isBackground)
    {
        this.isBackground = isBackground;
    }

    @Override
    public void accept(StyleComponent holder)
    {
        String key = isBackground ? "background" : "foreground";
        holder.registerProperty(key + "-color", Color.ALPHA, Color.class);

        holder.registerProperty(key + "-texture", Texture.EMPTY, Texture.class);
        holder.registerProperty(key + "-repeat", SpriteRepeat.NONE, SpriteRepeat.class);
        holder.registerProperty(key + "-animation", null, Resource.class);
        holder.registerProperty(key + "-position", RectBox.EMPTY, RectBox.class);

        if (isBackground)
        {
            StyleProperty<Resource> animationProperty = holder.getProperty("background-animation", Resource.class);

            animationProperty.addChangeListener((obs, oldValue, newValue) ->
            {
                if (newValue != null)
                {
                    SpriteAnimationInstance spriteAnimationInstance = SpriteAnimationParser.getAnimation(newValue).instantiate();
                    spriteAnimationInstance.computeTextures(holder.element().paint().backgroundTexture());
                    holder.element().paint().backgroundAnimation(spriteAnimationInstance);
                }
            });

            StyleProperty<Texture> textureProperty = holder.getProperty("background-texture", Texture.class);

            textureProperty.addChangeListener((obs, oldValue, newValue) ->
            {
                ofNullable(holder.element().paint().backgroundAnimation()).ifPresent(sai -> sai.computeTextures(newValue));
            });
        }
        else
        {
            holder.getProperty("foreground-animation", Resource.class).addChangeListener((obs, oldValue, newValue) ->
            {
                if (newValue != null)
                {
                    SpriteAnimationInstance spriteAnimationInstance = SpriteAnimationParser.getAnimation(newValue).instantiate();
                    spriteAnimationInstance.computeTextures(holder.element().paint().foregroundTexture());
                    holder.element().paint().foregroundAnimation(spriteAnimationInstance);
                }
            });

            holder.getProperty("foreground-texture", Texture.class).addChangeListener((obs, oldValue, newValue) ->
            {
                ofNullable(holder.element().paint().foregroundAnimation()).ifPresent(sai -> sai.computeTextures(newValue));
            });
        }
    }
}