package net.voxelindustry.brokkgui.util;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class ListUtil
{
    public static <T> Optional<T> tryGetIndex(List<T> list, int index)
    {
        if (list.size() > index && index >= 0)
            return ofNullable(list.get(index));
        return empty();
    }
}
