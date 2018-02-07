package org.yggard.brokkgui.style.tree;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleListTest
{
    @Test
    public void addEntrySimple()
    {
        StyleList tree = new StyleList();

        StyleSelector selector = new StyleSelector().add(StyleSelectorType.TYPE, "button");

        StyleRule[] rules = new StyleRule[]{new StyleRule("-text-color", "red"), new StyleRule("-border-color",
                "white")};
        tree.addEntry(selector, Lists.newArrayList(rules));

        assertThat(tree.getInternalStyleList()).hasSize(2);
        assertThat(tree.getInternalStyleList().get(1).getRules()).contains(rules);
    }

    @Test
    public void addEntryComposite()
    {
        StyleList tree = new StyleList();

        StyleSelector selector = new StyleSelector().add(StyleSelectorType.TYPE, "button")
                .add(StyleSelectorType.ID, "myButton");

        StyleRule[] rules = new StyleRule[]{new StyleRule("-text-color", "red"), new StyleRule("-border-color",
                "white")};
        tree.addEntry(selector, Lists.newArrayList(rules));

        StyleEntry styleEntry = tree.getInternalStyleList().get(1);

        assertThat(styleEntry.getRules()).contains(rules);
        assertThat(styleEntry.getSelector().getSpecificity()).isEqualTo(StyleSelectorType.TYPE.getSpecificity() +
                StyleSelectorType.ID.getSpecificity());
    }

    @Test
    public void addEntryMerge()
    {
        StyleList tree = new StyleList();

        StyleSelector selector = new StyleSelector().add(StyleSelectorType.TYPE, "button")
                .add(StyleSelectorType.ID, "myButton");

        StyleRule[] rules1 = new StyleRule[]{new StyleRule("-text-color", "red"), new StyleRule("-border-color",
                "white")};
        tree.addEntry(selector, Lists.newArrayList(rules1));

        StyleRule[] rules2 = new StyleRule[]{new StyleRule("-text-font", "Open Sans"), new StyleRule("-border-width",
                "2")};
        tree.addEntry(selector, Lists.newArrayList(rules2));

        assertThat(tree.getInternalStyleList().get(1).getRules()).contains(rules1).contains(rules2);
    }

    @Test
    public void clear()
    {
        StyleList tree = new StyleList();

        StyleSelector selector = new StyleSelector().add(StyleSelectorType.TYPE, "button")
                .add(StyleSelectorType.ID, "myButton");

        tree.addEntry(selector, Lists.newArrayList(new StyleRule("-text-color", "red"), new StyleRule
                ("-border-color", "white")));

        assertThat(tree.isEmpty()).isFalse();

        tree.clear();

        assertThat(tree.isEmpty()).isTrue();
    }
}
