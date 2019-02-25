package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;
import net.voxelindustry.brokkgui.panel.GuiPane;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;

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
            tree = StylesheetManager.getInstance().loadStylesheets("/assets/brokkgui/css/test_simple_border.css");
        } catch (ExecutionException e)
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
            tree = StylesheetManager.getInstance().loadStylesheets("/assets/brokkgui/css/test_hierarchic_border.css");
        } catch (ExecutionException e)
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

    @Test
    public void testBackgroundAlias()
    {
        StyleList tree = null;
        try
        {
            tree = StylesheetManager.getInstance().loadStylesheets("/assets/brokkgui/css/test_background_alias.css");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        StyleList finalTree = tree;

        GuiPane pane = new GuiPane();
        pane.setStyleTree(() -> finalTree);
        pane.refreshStyle();

        assertThat(pane.getBackgroundColor()).isEqualTo(ColorConstants.getColor("limegreen"));

        pane.setStyle("background-color: red;");

        assertThat(pane.getBackgroundColor()).isEqualTo(Color.RED);
    }
}
