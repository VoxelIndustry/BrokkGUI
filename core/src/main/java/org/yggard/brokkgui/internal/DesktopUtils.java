package org.yggard.brokkgui.internal;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DesktopUtils
{
    public static final void openURL(final String url)
    {
        if (Desktop.isDesktopSupported())
        {
            final Desktop desktop = Desktop.getDesktop();
            try
            {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            final Runtime runtime = Runtime.getRuntime();
            try
            {
                runtime.exec("xdg-open " + url);
            } catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}