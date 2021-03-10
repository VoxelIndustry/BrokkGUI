package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkcolor.ColorConstants;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StyleNodeTest
{
    @BeforeEach
    public void init()
    {
        StyleEngine.getInstance().start();
    }

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

        Rectangle element = new Rectangle();

        StyleList finalTree = tree;
        element.get(StyleComponent.class).setStyleSupplier(() -> finalTree);
        element.get(StyleComponent.class).refresh();

        assertThat(element.paint().borderColor()).isEqualTo(ColorConstants.getColor("khaki"));
        assertThat(element.transform().borderWidth()).isEqualTo(2);
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

        Rectangle element = new Rectangle();
        element.get(StyleComponent.class).styleClass().add("myPane");
        element.get(StyleComponent.class).setStyleSupplier(() -> finalTree);
        element.get(StyleComponent.class).refresh();

        Rectangle childElement = new Rectangle();
        childElement.get(StyleComponent.class).styleClass().add("myChildPane");
        element.transform().addChild(childElement.transform());
        childElement.get(StyleComponent.class).setStyleSupplier(() -> finalTree);
        childElement.get(StyleComponent.class).refresh();

        Rectangle fakeChildPane = new Rectangle();
        fakeChildPane.get(StyleComponent.class).styleClass().add("myChildPane");
        fakeChildPane.get(StyleComponent.class).setStyleSupplier(() -> finalTree);
        fakeChildPane.get(StyleComponent.class).refresh();

        assertThat(element.paint().borderColor()).isEqualTo(ColorConstants.getColor("khaki"));
        assertThat(element.transform().borderWidth()).isEqualTo(2);
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

        Rectangle pane = new Rectangle();
        pane.get(StyleComponent.class).setStyleSupplier(() -> finalTree);
        pane.get(StyleComponent.class).refresh();

        assertThat(pane.paint().backgroundColor()).isEqualTo(ColorConstants.getColor("limegreen"));

        pane.get(StyleComponent.class).parseInlineCSS("background-color: red;");

        assertThat(pane.paint().backgroundColor()).isEqualTo(Color.RED);
    }
}
