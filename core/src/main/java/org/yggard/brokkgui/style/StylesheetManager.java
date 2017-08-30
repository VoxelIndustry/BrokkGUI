package org.yggard.brokkgui.style;

import org.apache.commons.io.IOUtils;
import org.yggard.brokkgui.gui.BrokkGuiScreen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StylesheetManager
{
    private static StylesheetManager INSTANCE;

    public static StylesheetManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StylesheetManager();
        return INSTANCE;
    }

    private StylesheetManager()
    {

    }

    public void refreshStylesheets(BrokkGuiScreen screen)
    {

    }

    private StyleTree loadStylesheet(String styleSheet) throws IOException
    {
        String content = IOUtils.toString(StylesheetManager.class.getResource(styleSheet), StandardCharsets.UTF_8);

        StyleTree tree = new StyleTree();

        return tree;
    }
}
