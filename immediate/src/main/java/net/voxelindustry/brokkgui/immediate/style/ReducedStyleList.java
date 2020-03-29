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

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ReducedStyleList
{
    private final LoadingCache<Triple<String, StyleType, String>, StyleSelector> selectorCache = CacheBuilder.newBuilder().maximumSize(256)
            .build(new CacheLoader<Triple<String, StyleType, String>, StyleSelector>()
            {
                @Override
                public StyleSelector load(Triple<String, StyleType, String> key)
                {
                    StyleSelector selector = new StyleSelector();

                    selector.add(StyleSelectorType.TYPE, key.getLeft());

                    if (!StringUtils.isBlank(key.getRight()))
                        selector.add(StyleSelectorType.PSEUDOCLASS, key.getRight());

                    if (key.getMiddle() != null)
                    {
                        key.getMiddle().getNames().forEach(clazz ->
                        {
                            if ("normal".equalsIgnoreCase(clazz))
                                return;
                            selector.add(StyleSelectorType.CLASS, clazz);
                        });
                    }
                    return selector;
                }
            });

    private final Table<StyleSelector, String, Object> styleValues = HashBasedTable.create();

    public ReducedStyleList(StyleList styleList, Map<String, IStyleDecoder<?>> styleDecodersByRules)
    {
        styleList.getInternalStyleList()
                .forEach(entry ->
                {
                    // Only simple selectors are supported
                    // Hierarchic are always evicted since this is intended for use in a ImmediateGui context
                    if (!(entry.getSelector() instanceof StyleSelector))
                        return;

                    entry.getRules().forEach(rule ->
                            {
                                IStyleDecoder<?> decoder = styleDecodersByRules.get(rule.getRuleIdentifier());

                                if (decoder == null)
                                {
                                    BrokkGuiPlatform.getInstance().getLogger().warning("Unknown rule identifier given to ImmediateWindow CSS. An ImmediateElement did not register its rules or a invalid css file was provided. rule=" + rule);
                                    return;
                                }

                                styleValues.put(
                                        (StyleSelector) entry.getSelector(),
                                        rule.getRuleIdentifier(),
                                        decoder.decode(rule.getRuleValue()));
                            }
                    );
                });
    }

    @SuppressWarnings("unchecked")
    public <T> T getStyleValue(String element, StyleType type, String pseudoClass, String rule)
    {
        try
        {
            StyleSelector selector = selectorCache.get(Triple.of(element, type, pseudoClass));

            Optional<StyleSelector> mostSpecificSelector = styleValues.column(rule).keySet().stream()
                    .filter(selector::isSupersetOf).max(Comparator.comparingInt(IStyleSelector::getSpecificity));

            return mostSpecificSelector.map(styleSelector -> (T) styleValues.get(styleSelector, rule)).orElse(null);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
