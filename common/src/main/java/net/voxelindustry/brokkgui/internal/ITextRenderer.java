package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.text.TextSettings;

public interface ITextRenderer
{
    void drawString(String text, float x, float y, float zLevel, TextSettings settings);

    void drawStringMultiline(String text, float x, float y, float zLevel, TextSettings settings);
}
