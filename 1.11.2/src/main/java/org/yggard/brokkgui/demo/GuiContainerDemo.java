package org.yggard.brokkgui.demo;

import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiAbsolutePane;
import org.yggard.brokkgui.wrapper.container.BrokkGuiContainer;
import org.yggard.brokkgui.wrapper.container.ItemStackView;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author Ourten 31 oct. 2016
 */
public class GuiContainerDemo extends BrokkGuiContainer<ContainerDemo>
{
    private static final int     xSize      = 176, ySize = 166;

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
        view.setTooltip(true);
        view.setWidth(18);
        view.setHeight(18);
        mainPanel.addChild(view);

        mainPanel.setBackground(new Background(GuiContainerDemo.BACKGROUND));
    }
}