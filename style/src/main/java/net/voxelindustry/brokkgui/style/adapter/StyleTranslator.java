package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.style.adapter.translator.DoubleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.FloatTranslator;
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

    private final Map<Class<?>, IStyleDecoder<?>>   styleDecoders   = new IdentityHashMap<>();
    private final Map<Class<?>, IStyleEncoder<?>>   styleEncoders   = new IdentityHashMap<>();
    private final Map<Class<?>, IStyleValidator<?>> styleValidators = new IdentityHashMap<>();

    private StyleTranslator()
    {
        this.registerBuiltins();
    }

    public <T> void registerTranslator(Class<? extends T> valueClass, IStyleDecoder<T> decoder, IStyleEncoder<T> encoder,
                                       IStyleValidator<T> validator)
    {
        this.styleDecoders.put(valueClass, decoder);
        this.styleEncoders.put(valueClass, encoder);
        this.styleValidators.put(valueClass, validator);
    }

    public <T, V extends IStyleDecoder<T> & IStyleEncoder<T> & IStyleValidator<T>> void registerTranslator(Class<? extends T> valueClass, V translator)
    {
        this.registerTranslator(valueClass, translator, translator, translator);
    }

    @SuppressWarnings("unchecked")
    public <T> IStyleDecoder<T> getDecoder(Class<T> valueClass)
    {
        return (IStyleDecoder<T>) this.styleDecoders.get(valueClass);
    }

    @SuppressWarnings("unchecked")
    public <T> IStyleEncoder<T> getEncoder(Class<T> valueClass)
    {
        return (IStyleEncoder<T>) this.styleEncoders.get(valueClass);
    }

    @SuppressWarnings("unchecked")
    public <T> IStyleValidator<T> getValidator(Class<T> valueClass)
    {
        return (IStyleValidator<T>) this.styleValidators.get(valueClass);
    }

    public <T> T decode(String cssString, Class<T> valueClass)
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
