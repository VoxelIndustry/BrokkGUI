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

        if (isBackground)
        {
            holder.getStyleProperty("background-animation", Resource.class).addListener((obs, oldValue, newValue) ->
            {
                if (newValue != null)
                {
                    SpriteAnimationInstance spriteAnimationInstance = SpriteAnimationParser.getAnimation(newValue).instantiate();
                    spriteAnimationInstance.computeTextures(((GuiShape) holder.getOwner()).getBackgroundTexture());
                    ((GuiShape) holder.getOwner()).setBackgroundAnimation(spriteAnimationInstance);
                }
            });

            holder.getStyleProperty("background-texture", Texture.class).addListener((obs, oldValue, newValue) ->
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