package org.yggard.brokkgui.style;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.style.tree.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StylesheetManagerTest
{
    @Test
    public void parseSimpleCSS()
    {
        StyleTree tree = null;
        try
        {
            tree = StylesheetManager.getInstance().loadStylesheet("/assets/brokkgui/css/test.css");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        assertThat(tree).isNotNull();
        assertThat(tree.getInternalTree().getChildren(tree.getWildcard())).hasSize(2);
    }
}
