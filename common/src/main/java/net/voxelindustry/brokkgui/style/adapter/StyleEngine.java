package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.parser.StyleDecoder;

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
        StyleDecoder.getInstance().registerStyleAdapter(Color.class, new ColorStyleAdapter());
        StyleDecoder.getInstance().registerStyleAdapter(Texture.class, new TextureStyleAdapter());
    }
}
