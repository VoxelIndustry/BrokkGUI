package net.voxelindustry.brokkgui.panel;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;
import net.voxelindustry.brokkgui.style.StyleSource;

public class GuiPane extends GuiFather
{
    private final GuiOverflowPolicy guiOverflowPolicy;

    public GuiPane()
    {
        super("pane");

        this.guiOverflowPolicy = GuiOverflowPolicy.NONE;

        this.getStyle().registerProperty("opacity", 1D, Double.class);
    }

    @Override
    public void addChild(final GuiNode node)
    {
        super.addChild(node);

        RelativeBindingHelper.bindToCenter(node, this);
    }

    @Override
    public void removeChild(final GuiNode node)
    {
        super.removeChild(node);

        node.getxPosProperty().unbind();
        node.getyPosProperty().unbind();
    }

    @Override
    public void clearChilds()
    {
        this.getChildrensProperty().getValue().forEach(node ->
        {
            node.setFather(null);
            node.getxPosProperty().unbind();
            node.getyPosProperty().unbind();
        });
        this.getChildrensProperty().clear();
    }

    @Override
    public void renderContent(final IGuiRenderer renderer, final RenderPass pass, final int mouseX, final int
            mouseY)
    {
        if (this.getOpacity() != 1)
            renderer.getHelper().startAlphaMask(this.getOpacity());

        super.renderContent(renderer, pass, mouseX, mouseY);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();
    }

    @Override
    public GuiOverflowPolicy getGuiOverflowPolicy()
    {
        return this.guiOverflowPolicy;
    }

    public BaseProperty<Double> getOpacityProperty()
    {
        return this.getStyle().getStyleProperty("opacity", Double.class);
    }

    public double getOpacity()
    {
        return this.getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        this.getStyle().getStyleProperty("opacity", Double.class).setStyle(StyleSource.CODE, 0, opacity);
    }
}