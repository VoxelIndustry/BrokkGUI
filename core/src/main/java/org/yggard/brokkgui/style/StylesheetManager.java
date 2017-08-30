package org.yggard.brokkgui.style;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.gui.BrokkGuiScreen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class StylesheetManager
{
    private static StylesheetManager INSTANCE;

    public static StylesheetManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StylesheetManager();
        return INSTANCE;
    }

    private Logger logger;

    private StylesheetManager()
    {
        logger = Logger.getLogger("BrokkGui CSS Loader");
    }

    public void refreshStylesheets(BrokkGuiScreen screen)
    {

    }

    private StyleTree loadStylesheet(String styleSheet) throws IOException
    {
        InputStream input = StylesheetManager.class.getResourceAsStream(styleSheet);

        List<String> content = IOUtils.readLines(input, StandardCharsets.UTF_8);
        input.close();

        int currentLine = 1;
        StyleTree tree = new StyleTree();

        while (StringUtils.isEmpty(content.get(0)))
            currentLine++;

        while (currentLine < content.size())
        {
            if (content.get(currentLine).contains("{"))
                readBlock(readSelector(content, currentLine), tree, content, currentLine);
            else
                logger.severe("Expected { at line " + currentLine);
        }
        return tree;
    }

    private StyleSelector readSelector(List<String> content, int currentLine)
    {
        return new StyleSelector();
    }

    private void readBlock(StyleSelector selector, StyleTree tree, List<String> content, int currentLine)
    {

    }
}
