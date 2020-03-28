package net.voxelindustry.brokkgui.immediate.style;

import java.util.Collection;
import java.util.Objects;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Arrays.stream;
import static java.util.Collections.singleton;

public class StyleType
{
    public static final StyleType LIGHT  = StyleType.of("light");
    public static final StyleType NORMAL = StyleType.of("normal");
    public static final StyleType DARK   = StyleType.of("dark");

    private Collection<String> names;

    private StyleType(Collection<String> names)
    {
        this.names = names;
    }

    public static StyleType of(String name)
    {
        return new StyleType(singleton(name));
    }

    public static StyleType combine(StyleType... types)
    {
        return new StyleType(stream(types)
                .flatMap(type -> type.getNames().stream())
                .collect(toImmutableSet()));
    }

    public Collection<String> getNames()
    {
        return names;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StyleType styleType = (StyleType) o;
        return Objects.equals(names, styleType.names);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(names);
    }

    @Override
    public String toString()
    {
        return "StyleType{" +
                "names=" + names +
                '}';
    }
}
