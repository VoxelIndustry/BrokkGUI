package org.yggard.brokkgui.style;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.paint.ColorConstants;
import org.yggard.brokkgui.panel.GuiPane;
import org.yggard.brokkgui.style.tree.StyleTree;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleNodeTest
{
    @Test
    public void testSimpleBorder()
    {
        StyleTree tree = null;
        try
        {
            tree = StylesheetManager.getInstance().loadStylesheet("/assets/brokkgui/css/test_simple_border.css");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        GuiPane pane = new GuiPane();
        StyleTree finalTree = tree;
        pane.setStyleTree(() -> finalTree);
        pane.refreshStyle();

        assertThat(pane.getBorderColor()).isEqualTo(ColorConstants.getColor("khaki"));
        assertThat(pane.getBorderThin()).isEqualTo(2);
    }
}
