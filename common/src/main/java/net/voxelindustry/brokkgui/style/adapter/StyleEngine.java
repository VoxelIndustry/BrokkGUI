package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.paint.BackgroundRepeat;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.SpriteAnimation;
import net.voxelindustry.brokkgui.paint.Texture;

public class StyleEngine
{
    private static StyleEngine instance;

    public static StyleEngine getInstance()
    {
        if (instance == null)
            instance = new StyleEngine();
        return instance;
    }

    private StyleEngine()
    {

    }

    public void start()
    {
        ColorStyleTranslator colorTranslator = new ColorStyleTranslator();

        StyleTranslator.getInstance().registerTranslator(Color.class,
                colorTranslator,
                colorTranslator,
                colorTranslator);

        TextureStyleTranslator textureTranslator = new TextureStyleTranslator();

        StyleTranslator.getInstance().registerTranslator(Texture.class,
                textureTranslator,
                textureTranslator,
                textureTranslator);

        RectBoxTranslator rectBoxTranslator = new RectBoxTranslator();

        StyleTranslator.getInstance().registerTranslator(RectBox.class,
                rectBoxTranslator,
                rectBoxTranslator,
                rectBoxTranslator);

        BackgroundRepeatStyleTranslator backgroundRepeatTranslator = new BackgroundRepeatStyleTranslator();

        StyleTranslator.getInstance().registerTranslator(BackgroundRepeat.class,
                backgroundRepeatTranslator,
                backgroundRepeatTranslator,
                backgroundRepeatTranslator);

        SpriteAnimationStyleTranslator spriteAnimationTranslator = new SpriteAnimationStyleTranslator();

        StyleTranslator.getInstance().registerTranslator(SpriteAnimation.class,
                spriteAnimationTranslator,
                spriteAnimationTranslator,
                spriteAnimationTranslator);
    }
}
