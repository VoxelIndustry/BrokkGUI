package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.data.StyleValue;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EnumStyleTranslator<T extends Enum<T>> implements IStyleTranslator<T>
{
    private final Map<T, String> styleValueByConstant;
    private final Map<String, T> constantByStyleValue = new HashMap<>();
    private final Class<T>       enumClass;

    public EnumStyleTranslator(Class<T> enumClass)
    {
        this.enumClass = enumClass;
        this.styleValueByConstant = new EnumMap<>(enumClass);

        var enumConstants = enumClass.getEnumConstants();
        for (T constant : enumConstants)
        {
            var value = constant instanceof StyleValue styleValue ? styleValue.value() : constant.name().toLowerCase();
            styleValueByConstant.put(constant, value);
            constantByStyleValue.put(value, constant);
        }
    }

    @Override
    public T decode(String style, @Nullable AtomicInteger consumedLength)
    {
        var result = constantByStyleValue.entrySet()
                .stream()
                .filter(entry -> style.startsWith(entry.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot decode " + enumClass.getName() +
                        " value of [" + style + "]. Allowed values are [" +
                        String.join(", ", constantByStyleValue.keySet()) + "]")
                );

        if (consumedLength != null)
            consumedLength.set(result.getKey().length());
        return result.getValue();
    }

    @Override
    public String encode(T value, boolean prettyPrint)
    {
        return styleValueByConstant.get(value);
    }
}
