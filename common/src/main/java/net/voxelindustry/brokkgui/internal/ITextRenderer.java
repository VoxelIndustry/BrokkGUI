package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.text.TextSettings;

public interface ITextRenderer
{
    void drawString(String text, float x, float y, float zLevel, RenderPass pass, TextSettings settings);

    void drawStringMultiline(String text, float x, float y, float zLevel, RenderPass pass, TextSettings settings);
}
