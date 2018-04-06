package org.yggard.brokkgui.wrapper.elements;

import fr.ourten.teabeans.value.BaseListProperty;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiTooltip;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;

import java.util.ArrayList;
import java.util.List;

public class MCTooltip extends GuiNode
{
    public static Builder build()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private List<String> lines;

        public Builder()
        {
            lines = new ArrayList<>();
        }

        public Builder line(String line)
        {
            this.lines.add(line);
            return this;
        }

        public GuiTooltip create()
        {
            GuiTooltip tooltip = new GuiTooltip(get());
            tooltip.setxTranslate(0);
            tooltip.setyTranslate(0);
            
            return tooltip;
        }

        public MCTooltip get()
        {
            return new MCTooltip(lines);
        }
    }

    private BaseListProperty<String> lines;

    public MCTooltip(List<String> lines)
    {
        super("mctooltip");

        this.lines = new BaseListProperty<>(lines, "linesProperty");
    }

    @Override
    protected void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        GuiUtils.drawHoveringText(lines.getValue(), (int) this.getxPos(), (int) this.getyPos(),
                Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight,
                Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().fontRenderer);
    }
}
