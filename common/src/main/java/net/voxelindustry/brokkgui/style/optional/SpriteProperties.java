package net.voxelindustry.brokkgui.style.optional;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.shape.GuiShape;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationInstance;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationParser;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.StyleProperty;

import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

public class SpriteProperties implements Consumer<StyleHolder>
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
    public void accept(StyleHolder holder)
    {
        String key = isBackground ? "background" : "foreground";
        holder.registerProperty(key + "-color", Color.ALPHA, Color.class);

        holder.registerProperty(key + "-texture", Texture.EMPTY, Texture.class);
        holder.registerProperty(key + "-repeat", SpriteRepeat.NONE, SpriteRepeat.class);
        holder.registerProperty(key + "-animation", null, Resource.class);
        holder.registerProperty(key + "-position", RectBox.EMPTY, RectBox.class);

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
}