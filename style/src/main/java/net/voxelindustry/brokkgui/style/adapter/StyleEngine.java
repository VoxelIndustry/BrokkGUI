package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.data.RectBox;
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

    private ElementStyleStatus status;

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

        RectBoxTranslator rectBoxTranslator = new RectBoxTranslator();

        StyleTranslator.getInstance().registerTranslator(RectBox.class, rectBoxTranslator, rectBoxTranslator,
                rectBoxTranslator);
    }

    public void setElementsStyleStatus(ElementStyleStatus status)
    {
        this.status = status;
    }

    public ElementStyleStatus getElementStyleStatus()
    {
        return this.status;
    }

    public enum ElementStyleStatus
    {
        DEFAULT_ENABLED,
        DEFAULT_DISABLED;

        public boolean enabled()
        {
            return this == DEFAULT_ENABLED;
        }

        public boolean disabled()
        {
            return !this.enabled();
        }
    }
}
