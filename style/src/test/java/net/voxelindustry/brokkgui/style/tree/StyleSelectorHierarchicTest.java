package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.selector.StyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelectorHierarchic;
import net.voxelindustry.brokkgui.style.selector.StyleSelectorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StyleSelectorHierarchicTest
{
    @Test
    public void directChild()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        Rectangle parent = new Rectangle();
        parent.get(StyleComponent.class).styleClass().add("test");

        Rectangle child = new Rectangle();
        child.id("myID");

        parent.transform().addChild(child.transform());

        assertThat(selector.match(child.get(StyleComponent.class))).isTrue();
        assertThat(selector.match(parent.get(StyleComponent.class))).isFalse();
    }

    @Test
    public void distantChild()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), false);
        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        Rectangle parent = new Rectangle();
        parent.get(StyleComponent.class).styleClass().add("test");

        Rectangle child = new Rectangle();
        child.id("myID");

        Rectangle subChild = new Rectangle();
        subChild.id("myID");

        parent.transform().addChild(child.transform());
        child.transform().addChild(subChild.transform());

        assertThat(selector.match(subChild.get(StyleComponent.class))).isTrue();
        assertThat(selector.match(child.get(StyleComponent.class))).isTrue();
        assertThat(selector.match(parent.get(StyleComponent.class))).isFalse();
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

        assertThat(selector.getSpecificity()).isEqualTo(1_002_000);

        selector.addParent(StyleSelectorType.CLASS, "anotherClass");

        assertThat(selector.getSpecificity()).isEqualTo(1_003_000);
    }

    @Test
    public void matchWildcard()
    {
        StyleSelectorHierarchic direct = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        direct.addParent(StyleSelectorType.WILDCARD, "");
        direct.add(StyleSelectorType.ID, "someID");

        Rectangle element = new Rectangle();

        Rectangle child = new Rectangle();
        child.id("someID");
        element.transform().addChild(child.transform());

        assertThat(direct.match(child.get(StyleComponent.class))).isTrue();

        StyleSelectorHierarchic indirect = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), false);
        indirect.addParent(StyleSelectorType.WILDCARD, "");
        indirect.add(StyleSelectorType.ID, "someID");

        Rectangle subChild = new Rectangle();
        subChild.id("someID");
        child.transform().addChild(subChild.transform());

        assertThat(indirect.match(subChild.get(StyleComponent.class))).isTrue();
        assertThat(indirect.match(child.get(StyleComponent.class))).isTrue();
        assertThat(indirect.match(element.get(StyleComponent.class))).isFalse();
    }
}
