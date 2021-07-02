package net.voxelindustry.brokkgui.markup;

public class MarkupUtils
{
    public static String extractText(String text)
    {
        if (text.charAt(0) == '\n')
            text = text.substring(1);

        return text.stripTrailing().stripIndent();
    }
}
