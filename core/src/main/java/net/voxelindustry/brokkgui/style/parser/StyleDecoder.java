package net.voxelindustry.brokkgui.style.parser;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.adapter.ColorStyleAdapter;
import net.voxelindustry.brokkgui.style.adapter.IStyleAdapter;
import net.voxelindustry.brokkgui.style.adapter.TextureStyleAdapter;

import java.util.HashMap;

public class StyleDecoder
{
    private static StyleDecoder INSTANCE;

    public static StyleDecoder getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StyleDecoder();
        return INSTANCE;
    }

    private HashMap<Class<?>, IStyleAdapter<?>> styleAdapters;

    private StyleDecoder()
    {
        this.styleAdapters = new HashMap<>();

        this.registerBuiltins();
    }

    public <T> void registerStyleAdapter(Class<T> valueClass, IStyleAdapter<T> adapter)
    {
        this.styleAdapters.put(valueClass, adapter);
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(String value, Class<?> valueClass)
    {
        return (T) this.styleAdapters.get(valueClass).decode(value);
    }

    private void registerBuiltins()
    {
        this.registerStyleAdapter(Integer.class, Integer::parseInt);
        this.registerStyleAdapter(Float.class, Float::parseFloat);
        this.registerStyleAdapter(String.class, String::valueOf);
        this.registerStyleAdapter(Boolean.class, Boolean::parseBoolean);
        this.registerStyleAdapter(Double.class, Double::parseDouble);
        this.registerStyleAdapter(Long.class, Long::parseLong);

        this.registerStyleAdapter(Color.class, new ColorStyleAdapter());
        this.registerStyleAdapter(Texture.class, new TextureStyleAdapter());
    }
}
