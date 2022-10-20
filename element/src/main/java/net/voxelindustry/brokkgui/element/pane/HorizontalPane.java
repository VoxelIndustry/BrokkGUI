package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.HorizontalLayout;
import net.voxelindustry.brokkgui.style.StyledElement;

public class HorizontalPane extends GuiElement implements StyledElement, IGuiPane<HorizontalPaneChildPlacer<HorizontalPane>>
{
    private final HorizontalPaneChildPlacer<HorizontalPane> placer = new HorizontalPaneChildPlacer<>(this);

    private HorizontalLayout layoutComponent;

    public HorizontalPane()
    {
        transform().bindChild(false);
    }

    @Override
    public String type()
    {
        return "horizontal-pane";
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        layoutComponent = provide(HorizontalLayout.class);

        layoutComponent.setChildrenTransforms(transform().childrenProperty());
        layoutComponent.elementVerticalAlignment(false);
    }

    @Override
    public HorizontalPaneChildPlacer<HorizontalPane> addChild(GuiElement child)
    {
        transform().addChild(child.transform());
        placer.currentChild(child.transform());
        return placer;
    }

    @Override
    public HorizontalPaneChildPlacer<HorizontalPane> setChildPosition(GuiElement child)
    {
        placer.currentChild(child.transform());
        return placer;
    }
}