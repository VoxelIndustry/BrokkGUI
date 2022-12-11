package net.voxelindustry.brokkgui.style.adapter;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
public interface IStyleDecoder<T>
{
    /**
     * Decode a css string and return the value.
     *
     * @param style          css string
     * @param consumedLength count of chars consumed from the input string
     * @return the decoded value
     */
    T decode(String style, @Nullable AtomicInteger consumedLength);

    default T decode(String style)
    {
        return decode(style, null);
    }
}
