package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.style.StyleableProperty;
import net.voxelindustry.brokkgui.style.parser.StyleTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StyleDebugPanel
{
    private GuiNode node;

    public StyleDebugPanel(GuiNode node)
    {
        this.node = node;
    }

    public void render(IGuiRenderer renderer, float windowWidth, float windowHeight, int mouseX, int mouseY)
    {
        List<String> styleText = new ArrayList<>();

        for (Map.Entry<String, StyleableProperty<?>> entry : node.getStyle().getProperties().entrySet())
        {
            styleText.add(entry.getKey() + ": " + (entry.getValue().getValue() != null ?
                    StyleTranslator.getInstance().encode(entry.getValue().getValue(),
                            entry.getValue().getValueClass(), true) : "null"));
        }

        float width = (float) styleText.stream().mapToDouble(renderer.getHelper()::getStringWidth).max().orElse(0);
        float height = styleText.size() * renderer.getHelper().getStringHeight();
        float currentY = windowHeight - height;

      //  renderer.getHelper().drawColoredRect(renderer, 0, currentY, width, height, 300, DebugRenderer.BOX_COLOR);

        for (String line : styleText)
        {
           // renderer.getHelper().drawString(line, 0, currentY, 300, DebugRenderer.TEXT_COLOR);
            currentY += renderer.getHelper().getStringHeight();
        }
    }
}
