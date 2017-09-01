package org.yggard.brokkgui.style.tree;

import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleTreeTest
{
    @Test
    public void addEntrySimple()
    {
        StyleTree tree = new StyleTree();

        StyleSelector selector = new StyleSelector().addSelector(StyleSelector.StyleSelectorType.TYPE, "button");

        StyleRule[] rules = new StyleRule[]{new StyleRule("-text-color", "red"), new StyleRule("-border-color",
                "white")};
        tree.addEntry(selector, Sets.newLinkedHashSet(rules));

        assertThat(tree.getInternalTree().getChildren(tree.getWildcard())).hasSize(1);
        assertThat(tree.getInternalTree().getChildren(tree.getWildcard()).get(0).getRules()).contains(rules);
    }

    @Test
    public void addEntryComposite()
    {
        StyleTree tree = new StyleTree();

        StyleSelector selector = new StyleSelector().addSelector(StyleSelector.StyleSelectorType.TYPE, "button")
                .addSelector(StyleSelector.StyleSelectorType.ID, "myButton");

        StyleRule[] rules = new StyleRule[]{new StyleRule("-text-color", "red"), new StyleRule("-border-color",
                "white")};
        tree.addEntry(selector, Sets.newLinkedHashSet(rules));

        StyleEntry styleEntry = tree.getInternalTree().getChildren(tree.getInternalTree().getChildren(tree
                .getWildcard()).get(0)).get(0);

        assertThat(styleEntry.getRules()).contains(rules);
        assertThat(styleEntry.getSelector().getSpecificity()).isEqualTo(selector.getSpecificity());
    }

    @Test
    public void addEntryMerge()
    {
        StyleTree tree = new StyleTree();

        StyleSelector selector = new StyleSelector().addSelector(StyleSelector.StyleSelectorType.TYPE, "button")
                .addSelector(StyleSelector.StyleSelectorType.ID, "myButton");

        StyleRule[] rules1 = new StyleRule[]{new StyleRule("-text-color", "red"), new StyleRule("-border-color",
                "white")};
        tree.addEntry(selector, Sets.newLinkedHashSet(rules1));

        StyleRule[] rules2 = new StyleRule[]{new StyleRule("-text-font", "Open Sans"), new StyleRule("-border-width",
                "2")};
        tree.addEntry(selector, Sets.newLinkedHashSet(rules2));

        assertThat(tree.getInternalTree().getChildren(tree.getInternalTree().getChildren(tree.getWildcard()).get(0))
                .get(0).getRules()).contains(rules1).contains(rules2);
    }

    @Test
    public void clear()
    {
        StyleTree tree = new StyleTree();

        StyleSelector selector = new StyleSelector().addSelector(StyleSelector.StyleSelectorType.TYPE, "button")
                .addSelector(StyleSelector.StyleSelectorType.ID, "myButton");

        tree.addEntry(selector, Sets.newLinkedHashSet(new StyleRule("-text-color", "red"), new StyleRule
                ("-border-color", "white")));

        assertThat(tree.isEmpty()).isFalse();

        tree.clear();

        assertThat(tree.isEmpty()).isTrue();
    }
}
