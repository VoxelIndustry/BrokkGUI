package org.yggard.brokkgui.style;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.style.tree.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StylesheetManagerTest
{
    @Test
    public void parseSimpleCSS()
    {
        StyleList list = null;
        try
        {
            list = StylesheetManager.getInstance().loadStylesheet("/assets/brokkgui/css/test.css");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        assertThat(list).isNotNull();
        assertThat(list.getInternalStyleList()).hasSize(3);
    }
}
