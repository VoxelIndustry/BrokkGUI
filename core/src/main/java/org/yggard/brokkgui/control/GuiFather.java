package org.yggard.brokkgui.control;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseListProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.policy.EOverflowPolicy;
import org.yggard.brokkgui.style.tree.StyleList;

import java.util.List;
import java.util.function.Supplier;

public class GuiFather extends GuiNode
{
    private final BaseListProperty<GuiNode> childrensProperty;

    private EOverflowPolicy overflowPolicy;

    public GuiFather(String type)
    {
        super(type);

        this.childrensProperty = new BaseListProperty<>(null, "childrensProperty");

        this.overflowPolicy = EOverflowPolicy.NONE;

        this.childrensProperty.addListener((ListValueChangeListener<GuiNode>) (obs, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                newValue.setStyleTree(this.getStyle().getStyleSupplier());
                newValue.refreshStyle();
            }
        });
    }

    /**
     * @return an immutable list
     */
    public List<GuiNode> getChildrens()
    {
        return this.getChildrensProperty().getValue();
    }

    protected BaseListProperty<GuiNode> getChildrensProperty()
    {
        return this.childrensProperty;
    }

    public EOverflowPolicy getOverflowPolicy()
    {
        return this.overflowPolicy;
    }

    public void setOverflowPolicy(final EOverflowPolicy overflowPolicy)
    {
        this.overflowPolicy = overflowPolicy;
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if ((this.getOverflowPolicy().ordinal() >= EOverflowPolicy.TRIM.ordinal() && pass == RenderPass.MAIN)
                || (this.getOverflowPolicy().ordinal() >= EOverflowPolicy.TRIM_ALL.ordinal()))
        {
            renderer.getHelper().beginScissor();
            renderer.getHelper().scissorBox(this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getxPos() + this.getxTranslate() + this.getWidth() - 1,
                    this.getyPos() + this.getyTranslate() + this.getHeight() - 1);
            this.getChildrens().forEach(child -> child.renderNode(renderer, pass, mouseX, mouseY));
            renderer.getHelper().endScissor();
        }
        else
            this.getChildrens().forEach(child -> child.renderNode(renderer, pass, mouseX, mouseY));
    }

    @Override
    public void handleClick(final int mouseX, final int mouseY, final int key)
    {
        super.handleClick(mouseX, mouseY, key);

        this.getChildrens().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.handleClick(mouseX, mouseY, key));
    }

    @Override
    public void handleHover(int mouseX, int mouseY, boolean hovered)
    {
        super.handleHover(mouseX, mouseY, hovered);

        if (hovered)
            this.getChildrens().forEach(child ->
                    child.handleHover(mouseX, mouseY, child.isPointInside(mouseX, mouseY)));
        else
            this.getChildrens().forEach(child -> child.handleHover(mouseX, mouseY, false));

    }

    @Override
    public void handleMouseInput(int mouseX, int mouseY)
    {
        super.handleMouseInput(mouseX, mouseY);

        this.getChildrens().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.handleMouseInput(mouseX, mouseY));
    }

    /////////////////////
    //     STYLING     //
    /////////////////////

    @Override
    public void setStyleTree(Supplier<StyleList> treeSupplier)
    {
        super.setStyleTree(treeSupplier);

        this.getChildrens().forEach(node -> node.setStyleTree(treeSupplier));
    }

    @Override
    public void refreshStyle()
    {
        super.refreshStyle();

        this.getChildrens().forEach(GuiNode::refreshStyle);
    }
}