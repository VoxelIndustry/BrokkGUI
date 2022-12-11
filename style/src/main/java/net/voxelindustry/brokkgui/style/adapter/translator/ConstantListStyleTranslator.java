package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConstantListStyleTranslator<T> implements IStyleTranslator<T>
{
    private final List<T>      constantList;
    private final List<String> styleValueList;
    private final Class<T>     typeClass;

    private ConstantListStyleTranslator(List<T> constantList, List<String> styleValueList, Class<T> typeClass)
    {
        this.constantList = constantList;
        this.styleValueList = styleValueList;
        this.typeClass = typeClass;
    }

    @Override
    public T decode(String style, @Nullable AtomicInteger consumedLength)
    {
        var result = styleValueList
                .stream()
                .filter(style::startsWith)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot decode " + typeClass.getName() +
                        " value of [" + style + "]. Allowed values are [" +
                        String.join(", ", styleValueList) + "]")
                );

        if (consumedLength != null)
            consumedLength.set(result.length());
        return constantList.get(styleValueList.indexOf(result));
    }

    @Override
    public String encode(T value, boolean prettyPrint)
    {
        return styleValueList.get(constantList.indexOf(value));
    }

    public static <T> Builder<T> builder(Class<T> typeClass)
    {
        return new Builder<>(typeClass);
    }

    public static class Builder<T>
    {
        private final List<T>      constantList   = new ArrayList<>();
        private final List<String> styleValueList = new ArrayList<>();
        private final Class<T>     typeClass;

        public Builder(Class<T> typeClass)
        {
            this.typeClass = typeClass;
        }

        public Builder<T> entry(T constant, String... values)
        {
            for (var value : values)
            {
                constantList.add(constant);
                styleValueList.add(value);
            }
            return this;
        }

        public ConstantListStyleTranslator<T> create()
        {
            return new ConstantListStyleTranslator<>(constantList, styleValueList, typeClass);
        }
    }
}
