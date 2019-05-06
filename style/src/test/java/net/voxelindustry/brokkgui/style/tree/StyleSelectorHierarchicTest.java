package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.style.DummyGuiElement;
import net.voxelindustry.brokkgui.style.StyleHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StyleSelectorHierarchicTest
{
    @Test
    public void directChild()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        DummyGuiElement parent = new DummyGuiElement("dummy");
        parent.get(StyleHolder.class).styleClass().add("test");

        DummyGuiElement child = new DummyGuiElement("dummy");
        child.setId("myID");

        parent.transform().addChild(child.transform());

        assertThat(child.get(StyleHolder.class).parent()).isNotNull();
        assertThat(child.get(StyleHolder.class).parent()).isEqualTo(parent);
        assertThat(selector.match(child.get(StyleHolder.class))).isTrue();
        assertThat(selector.match(parent.get(StyleHolder.class))).isFalse();
    }

    @Test
    public void distantChild()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), false);
        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        DummyGuiElement parent = new DummyGuiElement("dummy");
        parent.get(StyleHolder.class).styleClass().add("test");

        DummyGuiElement child = new DummyGuiElement("dummy");
        child.setId("myID");

        DummyGuiElement grandChild = new DummyGuiElement("dummy");
        grandChild.setId("myID");

        parent.transform().addChild(child.transform());
        child.transform().addChild(grandChild.transform());

        assertThat(selector.match(grandChild.get(StyleHolder.class))).isTrue();
        assertThat(selector.match(child.get(StyleHolder.class))).isTrue();
        assertThat(selector.match(parent.get(StyleHolder.class))).isFalse();
    }

    @Test
    public void match()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        StyleSelectorHierarchic identical = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        StyleSelectorHierarchic different = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(),
                false);

        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        identical.addParent(StyleSelectorType.CLASS, "test");
        identical.add(StyleSelectorType.ID, "myID");

        different.add(StyleSelectorType.ID, "myID");
        different.addParent(StyleSelectorType.CLASS, "test");

        assertThat(selector.match(selector)).isTrue();
        assertThat(selector.match(identical)).isTrue();
        assertThat(selector.match(different)).isFalse();
    }

    @Test
    public void computedSpecificity()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        selector.add(StyleSelectorType.CLASS, "someClass");
        selector.add(StyleSelectorType.ID, "someID");
        selector.addParent(StyleSelectorType.PSEUDOCLASS, "hovered");

        assertThat(selector.getSpecificity()).isEqualTo(120);

        selector.addParent(StyleSelectorType.CLASS, "anotherClass");

        assertThat(selector.getSpecificity()).isEqualTo(130);
    }

    @Test
    public void matchWildcard()
    {
        StyleSelectorHierarchic direct = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        direct.addParent(StyleSelectorType.WILDCARD, "");
        direct.add(StyleSelectorType.ID, "someID");

        DummyGuiElement parent = new DummyGuiElement("dummy");

        DummyGuiElement child = new DummyGuiElement("dummy");
        child.setId("someID");
        parent.transform().addChild(child.transform());

        assertThat(direct.match(child.get(StyleHolder.class))).isTrue();

        StyleSelectorHierarchic indirect = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), false);
        indirect.addParent(StyleSelectorType.WILDCARD, "");
        indirect.add(StyleSelectorType.ID, "someID");

        DummyGuiElement subChild = new DummyGuiElement("dummy");
        subChild.setId("someID");
        child.transform().addChild(subChild.transform());

        assertThat(indirect.match(subChild.get(StyleHolder.class))).isTrue();
        assertThat(indirect.match(child.get(StyleHolder.class))).isTrue();
        assertThat(indirect.match(parent.get(StyleHolder.class))).isFalse();
    }
}
