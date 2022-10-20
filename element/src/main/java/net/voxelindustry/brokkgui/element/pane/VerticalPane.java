package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.VerticalLayout;
import net.voxelindustry.brokkgui.style.StyledElement;

public class VerticalPane extends GuiElement implements StyledElement, IGuiPane<VerticalPaneChildPlacer<VerticalPane>>
{
    private final VerticalPaneChildPlacer<VerticalPane> placer = new VerticalPaneChildPlacer<>(this);

    private VerticalLayout layoutComponent;

    public VerticalPane()
    {
        transform().bindChild(false);
    }

    @Override
    public String type()
    {
        return "vertical-pane";
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        layoutComponent = provide(VerticalLayout.class);

        layoutComponent.setChildrenTransforms(transform().childrenProperty());
        layoutComponent.elementHorizontalAlignment(false);
    }

    @Override
    public VerticalPaneChildPlacer<VerticalPane> addChild(GuiElement child)
    {
        transform().addChild(child.transform());
        placer.currentChild(child.transform());
        return placer;
    }

    @Override
    public VerticalPaneChildPlacer<VerticalPane> setChildPosition(GuiElement child)
    {
        placer.currentChild(child.transform());
        return placer;
    }
}