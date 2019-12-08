package net.voxelindustry.brokkgui.platform;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;

public class TextPlatform
{
    public static String trimTextToWidth(String text, float width)
    {
        return BrokkGuiPlatform.instance().guiHelper().trimStringToPixelWidth(text, (int) width);
    }

    public static float stringWidth(String text)
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text);
    }

    public static float stringHeight(String text)
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringHeight();
    }
}
