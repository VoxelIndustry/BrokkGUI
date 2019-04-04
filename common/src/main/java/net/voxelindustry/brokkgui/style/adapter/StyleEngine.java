package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Color;
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

        StyleTranslator.getInstance().registerTranslator(Color.class, colorTranslator, colorTranslator,
                colorTranslator);

        TextureStyleTranslator textureTranslator = new TextureStyleTranslator();

        StyleTranslator.getInstance().registerTranslator(Texture.class, textureTranslator, textureTranslator,
                textureTranslator);
    }
}
