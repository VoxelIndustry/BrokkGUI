package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;

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
        StyleTranslator.getInstance().registerTranslator(Color.class, new ColorStyleTranslator());
        StyleTranslator.getInstance().registerTranslator(Texture.class, new TextureStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(RectBox.class, new RectBoxTranslator());

        StyleTranslator.getInstance().registerTranslator(SpriteRepeat.class, new BackgroundRepeatStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(Resource.class, new ResourceStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(RandomSpriteRotation.class, new RandomSpriteRotationStyleTranslator());
    }
}
