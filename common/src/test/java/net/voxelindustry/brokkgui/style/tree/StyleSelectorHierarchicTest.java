package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.panel.GuiPane;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleSelectorHierarchicTest
{
    @Test
    public void directChild()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), true);
        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        GuiPane parent = new GuiPane();
        parent.getStyleClass().add("test");

        GuiPane child = new GuiPane();
        child.setID("myID");

        parent.addChild(child);

        assertThat(child.getStyle().getParent().isPresent()).isTrue();
        assertThat(child.getStyle().getParent().getValue()).isEqualTo(parent);
        assertThat(selector.match(child.getStyle())).isTrue();
        assertThat(selector.match(parent.getStyle())).isFalse();
    }

    @Test
    public void distantChild()
    {
        StyleSelectorHierarchic selector = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), false);
        selector.add(StyleSelectorType.ID, "myID");
        selector.addParent(StyleSelectorType.CLASS, "test");

        GuiPane parent = new GuiPane();
        parent.getStyleClass().add("test");

        GuiPane child = new GuiPane();
        child.setID("myID");

        GuiPane subChild = new GuiPane();
        subChild.setID("myID");

        parent.addChild(child);
        child.addChild(subChild);

        assertThat(selector.match(subChild.getStyle())).isTrue();
        assertThat(selector.match(child.getStyle())).isTrue();
        assertThat(selector.match(parent.getStyle())).isFalse();
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

        GuiPane pane = new GuiPane();

        GuiPane child = new GuiPane();
        child.setID("someID");
        pane.addChild(child);

        assertThat(direct.match(child.getStyle())).isTrue();

        StyleSelectorHierarchic indirect = new StyleSelectorHierarchic(new StyleSelector(), new StyleSelector(), false);
        indirect.addParent(StyleSelectorType.WILDCARD, "");
        indirect.add(StyleSelectorType.ID, "someID");

        GuiPane subChild = new GuiPane();
        subChild.setID("someID");
        child.addChild(subChild);

        assertThat(indirect.match(subChild.getStyle())).isTrue();
        assertThat(indirect.match(child.getStyle())).isTrue();
        assertThat(indirect.match(pane.getStyle())).isFalse();
    }
}
