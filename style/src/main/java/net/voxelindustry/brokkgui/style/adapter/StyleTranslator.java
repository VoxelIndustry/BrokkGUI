package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.util.StringCountUtils;

import java.util.IdentityHashMap;
import java.util.Map;

public class StyleTranslator
{
    private static StyleTranslator INSTANCE;

    public static StyleTranslator getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StyleTranslator();
        return INSTANCE;
    }

    private Map<Class<?>, IStyleDecoder<?>>   styleDecoders;
    private Map<Class<?>, IStyleEncoder<?>>   styleEncoders;
    private Map<Class<?>, IStyleValidator<?>> styleValidators;

    private StyleTranslator()
    {
        this.styleDecoders = new IdentityHashMap<>();
        this.styleEncoders = new IdentityHashMap<>();
        this.styleValidators = new IdentityHashMap<>();

        this.registerBuiltins();
    }

    public <T> void registerTranslator(Class<T> valueClass, IStyleDecoder<T> decoder, IStyleEncoder<T> encoder,
                                       IStyleValidator<T> validator)
    {
        this.styleDecoders.put(valueClass, decoder);
        this.styleEncoders.put(valueClass, encoder);
        this.styleValidators.put(valueClass, validator);
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(String cssString, Class<?> valueClass)
    {
        return (T) this.styleDecoders.get(valueClass).decode(cssString);
    }

    @SuppressWarnings("unchecked")
    public <T> String encode(Object value, Class<T> valueClass, boolean prettyPrint)
    {
        return ((IStyleEncoder<T>) this.styleEncoders.get(valueClass)).encode((T) value, prettyPrint);
    }

    public int validate(String cssString, Class<?> valueClass)
    {
        return this.styleValidators.get(valueClass).validate(cssString);
    }

    private void registerBuiltins()
    {
        this.registerTranslator(Integer.class, Integer::parseInt,
                (cssString, pretty) -> String.valueOf(cssString),
                StringCountUtils::integerAtStart);

        this.registerTranslator(Long.class, Long::parseLong,
                (cssString, pretty) -> String.valueOf(cssString),
                StringCountUtils::integerAtStart);

        FloatTranslator floatTranslator = new FloatTranslator();
        this.registerTranslator(Float.class, floatTranslator, floatTranslator, floatTranslator);

        DoubleTranslator doubleTranslator = new DoubleTranslator();
        this.registerTranslator(Double.class, doubleTranslator, doubleTranslator, doubleTranslator);

        this.registerTranslator(String.class, String::valueOf,
                (cssString, pretty) -> cssString,
                cssString -> cssString.indexOf(" "));

        this.registerTranslator(Boolean.class, Boolean::parseBoolean,
                (cssString, pretty) -> String.valueOf(cssString),
                StringCountUtils::boolAtStart);
    }
}
