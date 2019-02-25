package net.voxelindustry.brokkgui.wrapper.elements;

import fr.ourten.teabeans.value.BaseListProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.element.GuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

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
        GlStateManager.pushAttrib();
        GuiUtils.drawHoveringText(lines.getValue(), (int) this.getxPos(), (int) this.getyPos(),
                Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight,
                Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().fontRenderer);
        GlStateManager.popAttrib();
    }
}
