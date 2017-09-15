package org.yggard.brokkgui.style;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.paint.ColorConstants;
import org.yggard.brokkgui.panel.GuiPane;
import org.yggard.brokkgui.style.tree.StyleList;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleNodeTest
{
    @Test
    public void testSimpleBorder()
    {
        StyleList tree = null;
        try
        {
            tree = StylesheetManager.getInstance().loadStylesheet("/assets/brokkgui/css/test_simple_border.css");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        GuiPane pane = new GuiPane();
        StyleList finalTree = tree;
        pane.setStyleTree(() -> finalTree);
        pane.refreshStyle();

        assertThat(pane.getBorderColor()).isEqualTo(ColorConstants.getColor("khaki"));
        assertThat(pane.getBorderThin()).isEqualTo(2);
    }

    @Test
    public void testHierarchicBorder()
    {
        StyleList tree = null;
        try
        {
            tree = StylesheetManager.getInstance().loadStylesheet("/assets/brokkgui/css/test_hierarchic_border.css");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        StyleList finalTree = tree;

        GuiPane pane = new GuiPane();
        pane.getStyleClass().add("myPane");
        pane.setStyleTree(() -> finalTree);
        pane.refreshStyle();

        GuiPane childPane = new GuiPane();
        childPane.getStyleClass().add("myChildPane");
        pane.addChild(childPane);
        childPane.setStyleTree(() -> finalTree);
        childPane.refreshStyle();

        GuiPane fakeChildPane = new GuiPane();
        fakeChildPane.getStyleClass().add("myChildPane");
        fakeChildPane.setStyleTree(() -> finalTree);
        fakeChildPane.refreshStyle();

        assertThat(pane.getBorderColor()).isEqualTo(ColorConstants.getColor("khaki"));
        assertThat(pane.getBorderThin()).isEqualTo(2);
    }
}
