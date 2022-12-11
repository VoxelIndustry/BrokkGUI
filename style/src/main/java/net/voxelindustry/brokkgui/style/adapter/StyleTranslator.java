package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.style.adapter.translator.DoubleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.FloatTranslator;
import net.voxelindustry.brokkgui.util.PrimitivesParser;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StyleTranslator
{
    private static StyleTranslator INSTANCE;

    public static StyleTranslator getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StyleTranslator();
        return INSTANCE;
    }

    private final Map<Class<?>, IStyleDecoder<?>> styleDecoders = new IdentityHashMap<>();
    private final Map<Class<?>, IStyleEncoder<?>> styleEncoders = new IdentityHashMap<>();

    private StyleTranslator()
    {
        this.registerBuiltins();
    }

    public <T> void registerTranslator(Class<? extends T> valueClass, IStyleDecoder<T> decoder, IStyleEncoder<T> encoder)
    {
        this.styleDecoders.put(valueClass, decoder);
        this.styleEncoders.put(valueClass, encoder);
    }

    public <T, V extends IStyleDecoder<T> & IStyleEncoder<T>> void registerTranslator(Class<? extends T> valueClass, V translator)
    {
        this.registerTranslator(valueClass, translator, translator);
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
    public <T> T decode(String cssString, Class<T> valueClass)
    {
        return (T) this.styleDecoders.get(valueClass).decode(cssString);
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(String cssString, Class<T> valueClass, AtomicInteger consumedLength)
    {
        return (T) this.styleDecoders.get(valueClass).decode(cssString, consumedLength);
    }

    @SuppressWarnings("unchecked")
    public <T> String encode(Object value, Class<T> valueClass, boolean prettyPrint)
    {
        return ((IStyleEncoder<T>) this.styleEncoders.get(valueClass)).encode((T) value, prettyPrint);
    }

    private void registerBuiltins()
    {
        this.registerTranslator(Integer.class, (style, consumedLength) ->
                {
                    var length = PrimitivesParser.intLength(style);
                    if (consumedLength != null)
                        consumedLength.set(length);
                    return Integer.parseInt(style, 0, length, 10);
                },
                (cssString, pretty) -> String.valueOf(cssString));

        this.registerTranslator(Long.class, (style, consumedLength) ->
                {
                    var length = PrimitivesParser.intLength(style);
                    if (consumedLength != null)
                        consumedLength.set(length);
                    return Long.parseLong(style, 0, length, 10);
                },
                (cssString, pretty) -> String.valueOf(cssString));

        var floatTranslator = new FloatTranslator();
        this.registerTranslator(Float.class, floatTranslator, floatTranslator);

        var doubleTranslator = new DoubleTranslator();
        this.registerTranslator(Double.class, doubleTranslator, doubleTranslator);

        this.registerTranslator(String.class, (style, consumedLength) ->
                {
                    var firstSpace = style.indexOf(" ");
                    if (firstSpace == -1)
                        firstSpace = style.length();

                    if (consumedLength != null)
                        consumedLength.set(firstSpace);
                    return style.substring(0, firstSpace);
                },
                (cssString, pretty) -> cssString);

        this.registerTranslator(Boolean.class, (style, consumedLength) ->
                {
                    var value = Boolean.parseBoolean(style);

                    if (consumedLength != null)
                        consumedLength.set(value ? 4 : 5);
                    return value;
                },
                (cssString, pretty) -> String.valueOf(cssString));
    }
}
