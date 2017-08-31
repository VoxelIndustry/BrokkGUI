package org.yggard.brokkgui.style;

import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleTreeTest
{
    @Test
    public void addEntrySimple()
    {
        StyleTree tree = new StyleTree();

        StyleSelector selector = new StyleSelector().addSelector(StyleSelector.StyleSelectorType.TYPE, "button");

        String[] rules = new String[]{"-text-color: red", "-border-color: white"};
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

        String[] rules = new String[]{"-text-color: red", "-border-color: white"};
        tree.addEntry(selector, Sets.newLinkedHashSet(rules));

        StyleEntry styleEntry = tree.getInternalTree().getChildren(tree.getInternalTree().getChildren(tree.getWildcard()).get(0)).get(0);

        assertThat(styleEntry.getRules()).contains(rules);
        assertThat(styleEntry.getSelector().getSpecificity()).isEqualTo(selector.getSpecificity());
    }

    @Test
    public void addEntryMerge()
    {
        StyleTree tree = new StyleTree();

        StyleSelector selector = new StyleSelector().addSelector(StyleSelector.StyleSelectorType.TYPE, "button")
                .addSelector(StyleSelector.StyleSelectorType.ID, "myButton");

        String[] rules1 = new String[]{"-text-color: red", "-border-color: white"};
        tree.addEntry(selector, Sets.newLinkedHashSet(rules1));

        String[] rules2 = new String[]{"-text-font: Open Sans", "-border-width: 2"};
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

        tree.addEntry(selector, Sets.newLinkedHashSet("-text-color: red", "-border-color: white"));

        assertThat(tree.isEmpty()).isFalse();

        tree.clear();

        assertThat(tree.isEmpty()).isTrue();
    }
}
