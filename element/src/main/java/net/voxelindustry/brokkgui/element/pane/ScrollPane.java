package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Scrollable;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.GuiOverflow;

public class ScrollPane extends GuiElement implements StyledElement, IGuiPane<DefaultPaneChildPlacer<ScrollPane>>
{
    private final DefaultPaneChildPlacer<ScrollPane> placer = new DefaultPaneChildPlacer<>(this);

    private final Scrollable scrollable;

    public ScrollPane()
    {
        transform().bindChild(false);
        transform().overflow(GuiOverflow.SCROLL);
        this.scrollable = get(Scrollable.class);
    }

    public Scrollable scrollable()
    {
        return this.scrollable;
    }

    @Override
    public String type()
    {
        return "scrollpane";
    }

    @Override
    public DefaultPaneChildPlacer<ScrollPane> addChild(GuiElement child)
    {
        transform().addChild(child.transform());
        placer.currentChild(child.transform());
        return placer;
    }

    @Override
    public DefaultPaneChildPlacer<ScrollPane> setChildPosition(GuiElement child)
    {
        placer.currentChild(child.transform());
        return placer;
    }
}