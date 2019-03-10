package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.parser.StyleTranslator;

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
        StyleTranslator.getInstance().registerStyleDecoder(Color.class, new ColorStyleDecoder());
        StyleTranslator.getInstance().registerStyleDecoder(Texture.class, new TextureStyleDecoder());

        StyleTranslator.getInstance().registerStyleEncoder(Color.class, new ColorStyleEncoder());
        StyleTranslator.getInstance().registerStyleEncoder(Texture.class, new TextureStyleEncoder());
    }
}
