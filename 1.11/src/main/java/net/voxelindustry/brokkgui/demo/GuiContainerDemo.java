package net.voxelindustry.brokkgui.demo;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;
import net.voxelindustry.brokkgui.wrapper.container.BrokkGuiContainer;
import net.voxelindustry.brokkgui.wrapper.elements.FluidStackView;
import net.voxelindustry.brokkgui.wrapper.elements.ItemStackView;

/**
 * @author Ourten 31 oct. 2016
 */
public class GuiContainerDemo extends BrokkGuiContainer<ContainerDemo>
{
    private static final int xSize = 176, ySize = 200;

    private static final Texture BACKGROUND = new Texture("brokkguidemo:textures/gui/container_background.png", 0, 0,
            GuiContainerDemo.xSize / 256.0f, GuiContainerDemo.ySize / 256.0f);

    public GuiContainerDemo(final ContainerDemo container)
    {
        super(container);

        this.setWidth(GuiContainerDemo.xSize);
        this.setHeight(GuiContainerDemo.ySize);
        this.setxRelativePos(0.5f);
        this.setyRelativePos(0.5f);

        final GuiAbsolutePane mainPanel = new GuiAbsolutePane();
        this.setMainPanel(mainPanel);

        final ItemStackView view = new ItemStackView(new ItemStack(Items.APPLE));
        view.setItemTooltip(true);
        view.setWidth(18);
        view.setHeight(18);
        view.setColor(new Color(1, 1, 1, 0.5f));
        mainPanel.addChild(view);

        FluidStackView fluidStackView = new FluidStackView(new FluidStack(FluidRegistry.WATER, 1000));
        fluidStackView.setWidth(18);
        fluidStackView.setHeight(64);
        fluidStackView.setFlowing(true);

        mainPanel.addChild(fluidStackView, 4, 4);

        mainPanel.setBackgroundTexture(GuiContainerDemo.BACKGROUND);
    }
}