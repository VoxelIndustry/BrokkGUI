package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import net.voxelindustry.brokkgui.style.tree.StyleSelector;
import net.voxelindustry.brokkgui.style.tree.StyleSelectorHierarchic;
import net.voxelindustry.brokkgui.style.tree.StyleSelectorType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class StylesheetManagerTest
{
    @BeforeAll
    public void init()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void parseSimpleCSS()
    {
        StyleList list = null;
        try
        {
            list = StylesheetManager.getInstance().loadStylesheets("/assets/brokkgui/css/test.css");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        assertTestCSS(list);
    }

    @Test
    public void parseCSSWithImport()
    {
        StyleList list = null;
        try
        {
            list = StylesheetManager.getInstance().loadStylesheets("/assets/brokkgui/css/import.css");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        assertTestCSS(list);
    }

    private void assertTestCSS(StyleList list)
    {
        assertThat(list).isNotNull();
        assertThat(list.getInternalStyleList()).hasSize(5);
        assertThat(list.getInternalStyleList().get(0).getSelector()).isInstanceOf(StyleSelector.class);

        StyleSelector wildcard = (StyleSelector) list.getInternalStyleList().get(0).getSelector();
        assertThat(wildcard.getSelectors().get(0).getKey()).isEqualTo(StyleSelectorType.WILDCARD);

        assertThat(list.getInternalStyleList().get(2).getSelector()).isInstanceOf(StyleSelectorHierarchic.class);
        StyleSelectorHierarchic hierarchic = (StyleSelectorHierarchic) list.getInternalStyleList().get(2).getSelector();

        assertThat(hierarchic.isDirectChild()).isTrue();
        assertThat(hierarchic.getChildSelector()).isInstanceOf(StyleSelector.class);
        assertThat(hierarchic.getParentSelector()).isInstanceOf(StyleSelector.class);

        StyleSelector childSelector = (StyleSelector) hierarchic.getChildSelector();
        StyleSelector parentSelector = (StyleSelector) hierarchic.getParentSelector();

        assertThat(childSelector.getSelectors().get(0).getKey()).isEqualTo(StyleSelectorType.CLASS);
        assertThat(childSelector.getSelectors().get(0).getValue()).isEqualTo("snowflakes");

        assertThat(parentSelector.getSelectors().get(0).getKey()).isEqualTo(StyleSelectorType.TYPE);
        assertThat(parentSelector.getSelectors().get(0).getValue()).isEqualTo("pane");
    }
}
