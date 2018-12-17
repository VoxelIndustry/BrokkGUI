package net.voxelindustry.brokkgui.demo;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.gui.SubGuiScreen;
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

        FluidStackView fluidStackView = new FluidStackView(Fluids.WATER);
        fluidStackView.setWidth(18);
        fluidStackView.setHeight(64);
        fluidStackView.setFlowing(true);
        fluidStackView.setOnClickEvent(e -> this.addSubGui(new SubWindow()));

        mainPanel.addChild(fluidStackView, 4, 4);

        mainPanel.setBackgroundTexture(GuiContainerDemo.BACKGROUND);
    }

    private static class SubWindow extends SubGuiScreen
    {
        public SubWindow()
        {
            super(0.5f, 0.5f);
            this.setBackgroundColor(Color.GRAY);
            this.setSize(128, 128);

            GuiAbsolutePane mainPanel = new GuiAbsolutePane();
            mainPanel.setWidthRatio(1);
            mainPanel.setHeightRatio(1);
            this.addChild(mainPanel);
            RelativeBindingHelper.bindToPos(mainPanel, this);

            ItemStackView view = new ItemStackView(new ItemStack(Items.APPLE));
            view.setItemTooltip(true);
            view.setWidth(18);
            view.setHeight(18);

            for (int x = 0; x < 5; x++)
            {
                ItemStackView blockView = new ItemStackView(new ItemStack(Blocks.DIRT, 64));
                blockView.setItemTooltip(true);
                blockView.setWidth(18);
                blockView.setHeight(18);
                blockView.setzLevel(301);
                mainPanel.addChild(blockView, 18 * x, 18);
            }
        }
    }
}
