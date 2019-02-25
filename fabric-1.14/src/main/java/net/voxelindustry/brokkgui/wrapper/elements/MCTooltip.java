package net.voxelindustry.brokkgui.wrapper.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import fr.ourten.teabeans.value.BaseListProperty;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.element.GuiTooltip;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;

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
        GlStateManager.pushLightingAttributes();
        ((GuiHelper) renderer.getHelper()).drawTooltip(renderer, (int) this.getxPos(), (int) this.getyPos(),
                lines.getValue());
        GlStateManager.popAttributes();
    }
}
