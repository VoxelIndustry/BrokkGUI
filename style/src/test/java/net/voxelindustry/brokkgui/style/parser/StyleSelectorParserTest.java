package net.voxelindustry.brokkgui.style.parser;

import net.voxelindustry.brokkgui.style.tree.IStyleSelector;
import net.voxelindustry.brokkgui.style.tree.StyleSelector;
import net.voxelindustry.brokkgui.style.tree.StyleSelectorHierarchic;
import net.voxelindustry.brokkgui.style.tree.StyleSelectorType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StyleSelectorParserTest
{
    private StyleSelectorParser parser;

    @BeforeAll
    public void setup()
    {
        this.parser = new StyleSelectorParser();
    }

    @Test
    public void testSelectorClean()
    {
        String simpleTest = "button .class";

        assertThat(parser.cleanSelector(simpleTest)).isEqualTo("button>>.class");

        String trimTest = "button > .class";

        assertThat(parser.cleanSelector(trimTest)).isEqualTo("button>.class");

        String replaceAndTrim = "button >>  .class text";

        assertThat(parser.cleanSelector(replaceAndTrim)).isEqualTo("button>>.class>>text");
    }

    @Test
    public void testSelectorOr()
    {
        String simpleOr = "button , text";

        IStyleSelector[] selectors = parser.readSelectors(simpleOr);

        assertThat(selectors.length).isEqualTo(2);

        assertThat(selectors[0].match(new StyleSelector().add(StyleSelectorType.TYPE, "button"))).isTrue();
        assertThat(selectors[1].match(new StyleSelector().add(StyleSelectorType.TYPE, "text"))).isTrue();
    }

    @Test
    public void testSimpleHierarchic()
    {
        String simpleHierarchic = "button > text";
        StyleSelector parent = new StyleSelector().add(StyleSelectorType.TYPE, "button");
        StyleSelector child = new StyleSelector().add(StyleSelectorType.TYPE, "text");
        IStyleSelector[] selectors = parser.readSelectors(simpleHierarchic);

        assertThat(selectors.length).isEqualTo(1);

        assertThat(selectors[0]).isInstanceOf(StyleSelectorHierarchic.class);

        assertThat(((StyleSelectorHierarchic) selectors[0]).isDirectChild()).isTrue();
        assertThat(((StyleSelectorHierarchic) selectors[0]).getParentSelector().match(parent)).isTrue();
        assertThat(((StyleSelectorHierarchic) selectors[0]).getChildSelector().match(child)).isTrue();
    }

    @Test
    public void testTwoGenerationHierarchic()
    {
        String twoGenerationHierarchic = "pane button > text";
        StyleSelector grandparent = new StyleSelector().add(StyleSelectorType.TYPE, "pane");
        StyleSelector parent = new StyleSelector().add(StyleSelectorType.TYPE, "button");
        StyleSelector child = new StyleSelector().add(StyleSelectorType.TYPE, "text");

        IStyleSelector[] twoGenerationSelectors = parser.readSelectors(twoGenerationHierarchic);

        assertThat(twoGenerationSelectors[0]).isInstanceOf(StyleSelectorHierarchic.class);

        StyleSelectorHierarchic selector = (StyleSelectorHierarchic) twoGenerationSelectors[0];

        assertThat(selector.getParentSelector().match(new StyleSelectorHierarchic(grandparent, parent, false))).isTrue();
        assertThat(selector.isDirectChild()).isTrue();

        assertThat(selector.getChildSelector()).isInstanceOf(StyleSelector.class);
        assertThat(selector.getParentSelector()).isInstanceOf(StyleSelectorHierarchic.class);

        assertThat(((StyleSelectorHierarchic) selector.getParentSelector()).getParentSelector().match(grandparent)).isTrue();
    }
}
