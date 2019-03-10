package net.voxelindustry.brokkgui.style.parser;

import net.voxelindustry.brokkgui.style.adapter.IStyleDecoder;
import net.voxelindustry.brokkgui.style.adapter.IStyleEncoder;

import java.util.HashMap;

public class StyleTranslator
{
    private static StyleTranslator INSTANCE;

    public static StyleTranslator getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StyleTranslator();
        return INSTANCE;
    }

    private HashMap<Class<?>, IStyleDecoder<?>> styleDecoders;
    private HashMap<Class<?>, IStyleEncoder<?>> styleEncoders;

    private StyleTranslator()
    {
        this.styleDecoders = new HashMap<>();
        this.styleEncoders = new HashMap<>();

        this.registerBuiltins();
    }

    public <T> void registerStyleDecoder(Class<T> valueClass, IStyleDecoder<T> decoder)
    {
        this.styleDecoders.put(valueClass, decoder);
    }

    public <T> void registerStyleEncoder(Class<T> valueClass, IStyleEncoder<T> encoder)
    {
        this.styleEncoders.put(valueClass, encoder);
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(String value, Class<?> valueClass)
    {
        return (T) this.styleDecoders.get(valueClass).decode(value);
    }

    @SuppressWarnings("unchecked")
    public <T> String encode(Object value, Class<T> valueClass, boolean prettyPrint)
    {
        return ((IStyleEncoder<T>) this.styleEncoders.get(valueClass)).encode((T) value, prettyPrint);
    }

    private void registerBuiltins()
    {
        this.registerStyleDecoder(Integer.class, Integer::parseInt);
        this.registerStyleDecoder(Float.class, Float::parseFloat);
        this.registerStyleDecoder(String.class, String::valueOf);
        this.registerStyleDecoder(Boolean.class, Boolean::parseBoolean);
        this.registerStyleDecoder(Double.class, Double::parseDouble);
        this.registerStyleDecoder(Long.class, Long::parseLong);

        this.registerStyleEncoder(Integer.class, (value, pretty) -> String.valueOf(value));
        this.registerStyleEncoder(Float.class, (value, pretty) -> String.valueOf(value));
        this.registerStyleEncoder(String.class, (value, pretty) -> value);
        this.registerStyleEncoder(Boolean.class, (value, pretty) -> String.valueOf(value));
        this.registerStyleEncoder(Double.class, (value, pretty) -> String.valueOf(value));
        this.registerStyleEncoder(Long.class, (value, pretty) -> String.valueOf(value));
    }
}
