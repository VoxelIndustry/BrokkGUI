package org.yggard.brokkgui.style;

import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.style.adapter.ColorStyleAdapter;
import org.yggard.brokkgui.style.adapter.IStyleAdapter;
import org.yggard.brokkgui.style.adapter.TextureStyleAdapter;

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
    <T> T decode(String value, Class<?> valueClass)
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
