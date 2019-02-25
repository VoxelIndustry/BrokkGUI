package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.panel.GuiPane;
import net.voxelindustry.brokkgui.style.tree.StyleSelector;
import net.voxelindustry.brokkgui.style.tree.StyleSelectorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
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

        GuiPane pane = new GuiPane();
        pane.getStyleClass().add("someClass");
        pane.setID("someID");
        pane.getActivePseudoClass().add("hovered");

        GuiPane notmatching = new GuiPane();
        notmatching.getStyleClass().add("someClass");
        notmatching.setID("someID");

        assertThat(selector.match(pane.getStyle())).isTrue();
        assertThat(selector.match(notmatching.getStyle())).isFalse();
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
