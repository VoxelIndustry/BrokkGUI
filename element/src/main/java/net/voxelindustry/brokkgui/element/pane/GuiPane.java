package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.style.StyledElement;

public class GuiPane extends GuiElement implements StyledElement, IGuiPane<DefaultPaneChildPlacer<GuiPane>>
{
    private final DefaultPaneChildPlacer<GuiPane> placer = new DefaultPaneChildPlacer<>(this);

    public GuiPane()
    {
        transform().bindChild(false);
    }

    @Override
    public String type()
    {
        return "pane";
    }

    @Override
    public DefaultPaneChildPlacer<GuiPane> addChild(GuiElement child)
    {
        transform().addChild(child.transform());
        placer.currentChild(child.transform());
        return placer;
    }

    @Override
    public DefaultPaneChildPlacer<GuiPane> setChildPosition(GuiElement child)
    {
        placer.currentChild(child.transform());
        return placer;
    }
}