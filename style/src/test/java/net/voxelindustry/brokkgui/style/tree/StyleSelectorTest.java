package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.style.DummyStyleHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StyleSelectorTest
{
    @Test
    public void matchStyleHolder()
    {
        StyleSelector selector = new StyleSelector();
        selector.add(StyleSelectorType.CLASS, "someClass");
        selector.add(StyleSelectorType.ID, "someID");
        selector.add(StyleSelectorType.PSEUDOCLASS, "hovered");
        selector.add(StyleSelectorType.TYPE, "pane");

        DummyStyleHolder paneStyle = new DummyStyleHolder("someID", "pane");
        paneStyle.styleClass().add("someClass");
        paneStyle.activePseudoClass().add("hovered");

        DummyStyleHolder notmatchingStyle = new DummyStyleHolder("someID");
        notmatchingStyle.styleClass().add("someClass");

        assertThat(selector.match(paneStyle)).isTrue();
        assertThat(selector.match(notmatchingStyle)).isFalse();
    }

    @Test
    public void matchStyleSelector()
    {
        StyleSelector selector = new StyleSelector();
        selector.add(StyleSelectorType.CLASS, "someClass");
        selector.add(StyleSelectorType.ID, "someID");
        selector.add(StyleSelectorType.PSEUDOCLASS, "hovered");

        StyleSelector identical = new StyleSelector();
        identical.add(StyleSelectorType.CLASS, "someClass");
        identical.add(StyleSelectorType.ID, "someID");
        identical.add(StyleSelectorType.PSEUDOCLASS, "hovered");

        StyleSelector errored = new StyleSelector();
        errored.add(StyleSelectorType.CLASS, "someClass");
        errored.add(StyleSelectorType.PSEUDOCLASS, "hovered");
        errored.add(StyleSelectorType.CLASS, "anotherClass");

        assertThat(selector.match(identical)).isTrue();
        assertThat(selector.match(selector)).isTrue();
        assertThat(selector.match(errored)).isFalse();
    }

    @Test
    public void computedSpecificity()
    {
        StyleSelector selector = new StyleSelector();
        selector.add(StyleSelectorType.CLASS, "someClass");
        selector.add(StyleSelectorType.ID, "someID");
        selector.add(StyleSelectorType.PSEUDOCLASS, "hovered");

        assertThat(selector.getSpecificity()).isEqualTo(120);

        selector.add(StyleSelectorType.CLASS, "anotherClass");

        assertThat(selector.getSpecificity()).isEqualTo(130);
    }
}
