package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.selector.StyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelectorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StyleSelectorTest
{
    @Test
    public void matchStyleHolder()
    {
        StyleSelector selector = new StyleSelector();
        selector.add(StyleSelectorType.CLASS, "someClass");
        selector.add(StyleSelectorType.ID, "someID");
        selector.add(StyleSelectorType.PSEUDOCLASS, "hovered");
        selector.add(StyleSelectorType.TYPE, "rectangle");

        Rectangle pane = new Rectangle();
        pane.get(StyleComponent.class).styleClass().add("someClass");
        pane.id("someID");
        pane.get(StyleComponent.class).activePseudoClass().add("hovered");

        Rectangle notMatching = new Rectangle();
        notMatching.get(StyleComponent.class).styleClass().add("someClass");
        notMatching.id("someID");

        assertThat(selector.match(pane.get(StyleComponent.class))).isTrue();
        assertThat(selector.match(notMatching.get(StyleComponent.class))).isFalse();
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

        assertThat(selector.getSpecificity()).isEqualTo(1_002_000);

        selector.add(StyleSelectorType.CLASS, "anotherClass");

        assertThat(selector.getSpecificity()).isEqualTo(1_003_000);
    }
}
