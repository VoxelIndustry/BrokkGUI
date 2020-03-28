package net.voxelindustry.brokkgui.immediate.style;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.style.adapter.IStyleDecoder;
import net.voxelindustry.brokkgui.style.selector.IStyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelectorType;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReducedStyleList
{
    private final LoadingCache<Triple<String, StyleType, String>, IStyleSelector> selectorCache = CacheBuilder.newBuilder().maximumSize(256)
            .build(new CacheLoader<Triple<String, StyleType, String>, IStyleSelector>()
            {
                @Override
                public IStyleSelector load(Triple<String, StyleType, String> key)
                {
                    StyleSelector selector = new StyleSelector();

                    selector.add(StyleSelectorType.TYPE, key.getLeft());

                    if (!StringUtils.isBlank(key.getRight()))
                        selector.add(StyleSelectorType.PSEUDOCLASS, key.getRight());

                    key.getMiddle().getNames().forEach(clazz ->
                    {
                        if ("normal".equalsIgnoreCase(clazz))
                            return;
                        selector.add(StyleSelectorType.CLASS, clazz);
                    });
                    return selector;
                }
            });

    private final Table<IStyleSelector, String, Object> styleValues = HashBasedTable.create();

    public ReducedStyleList(StyleList styleList, Map<String, IStyleDecoder<?>> styleDecodersByRules)
    {
        styleList.getInternalStyleList()
                .forEach(entry ->
                        entry.getRules().forEach(rule ->
                                {
                                    IStyleDecoder<?> decoder = styleDecodersByRules.get(rule.getRuleIdentifier());

                                    if (decoder == null)
                                    {
                                        BrokkGuiPlatform.getInstance().getLogger().warning("Unknown rule identifier given to ImmediateWindow CSS. An ImmediateElement did not register its rules or a invalid css file was provided. rule=" + rule);
                                        return;
                                    }

                                    styleValues.put(
                                            entry.getSelector(),
                                            rule.getRuleIdentifier(),
                                            decoder.decode(rule.getRuleValue()));
                                }
                        ));
    }

    @SuppressWarnings("unchecked")
    public <T> T getStyleValue(String element, StyleType type, String pseudoClass, String rule)
    {
        try
        {
            Object value = styleValues.get(selectorCache.get(Triple.of(element, type, pseudoClass)), rule);

            if (value == null && !StringUtils.isBlank(pseudoClass))
                value = styleValues.get(selectorCache.get(Triple.of(element, type, "")), rule);

            return (T) value;
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
