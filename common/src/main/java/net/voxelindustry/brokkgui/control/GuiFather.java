package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;
import net.voxelindustry.brokkgui.shape.GuiShape;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.ICascadeStyleable;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GuiFather extends GuiShape
{
    private final BaseListProperty<GuiNode> childrensProperty;
    private final List<ICascadeStyleable>   styleChilds;

    private BaseProperty<GuiOverflowPolicy> guiOverflowProperty;

    public GuiFather(String type)
    {
        super(type, Rectangle.SHAPE);

        this.childrensProperty = new BaseListProperty<>(null, "childrensProperty");

        this.guiOverflowProperty = new BaseProperty<>(GuiOverflowPolicy.NONE, "overflowProperty");
        this.guiOverflowProperty.addListener(obs ->
        {
            if (getScissorBox() == null)
                return;

            if (guiOverflowProperty.getValue() == GuiOverflowPolicy.TRIM)
                getScissorBox().setRenderPassPredicate(pass -> pass.getPriority() <= RenderPass.FOREGROUND.getPriority());
            else if (guiOverflowProperty.getValue() == GuiOverflowPolicy.NONE)
                getScissorBox().setRenderPassPredicate(pass -> false);
            else
                getScissorBox().setRenderPassPredicate(pass -> true);
        });

        this.childrensProperty.addListener((ListValueChangeListener<GuiNode>) (obs, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                this.addStyleChild(newValue);
                newValue.setFather(this);
            }
            if (oldValue != null)
            {
                this.removeStyleChild(oldValue);
                oldValue.setFather(null);
            }
        });

        this.styleChilds = new ArrayList<>();
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

    public void addChild(final GuiNode node)
    {
        this.getChildrensProperty().add(node);
    }

    public void addChilds(final GuiNode... nodes)
    {
        for (final GuiNode node : nodes)
            this.addChild(node);
    }

    public void removeChild(final GuiNode node)
    {
        this.getChildrensProperty().remove(node);
        node.getxPosProperty().unbind();
        node.getyPosProperty().unbind();
    }

    public void clearChilds()
    {
        this.getChildrensProperty().getValue().forEach(node ->
        {
            node.getxPosProperty().unbind();
            node.getyPosProperty().unbind();
        });
        this.getChildrensProperty().clear();
    }

    public boolean hasChild(final GuiNode node)
    {
        return this.getChildrensProperty().contains(node);
    }

    public void addStyleChild(ICascadeStyleable styleable)
    {
        if (this.styleChilds.contains(styleable))
            return;
        this.styleChilds.add(styleable);
        styleable.setStyleTree(this.getStyle().getStyleSupplier());
        styleable.setParent(this);
        styleable.refreshStyle();
    }

    public boolean removeStyleChild(ICascadeStyleable styleable)
    {
        return this.styleChilds.remove(styleable);
    }

    public BaseProperty<GuiOverflowPolicy> getGuiOverflowProperty()
    {
        return this.guiOverflowProperty;
    }

    public void setGuiOverflow(GuiOverflowPolicy guiOverflow)
    {
        this.getGuiOverflowProperty().setValue(guiOverflow);
    }

    public GuiOverflowPolicy getGuiOverflow()
    {
        return this.getGuiOverflowProperty().getValue();
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        super.renderContent(renderer, pass, mouseX, mouseY);
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
    public void handleClickDrag(int mouseX, int mouseY, int key, int originalMouseX, int originalMouseY)
    {
        super.handleClickDrag(mouseX, mouseY, key, originalMouseX, originalMouseY);

        this.getChildrens().stream().filter(child -> child.isPointInside(originalMouseX, originalMouseY))
                .forEach(child -> child.handleClickDrag(mouseX, mouseY, key, originalMouseX, originalMouseY));
    }

    @Override
    public void handleClickStop(int mouseX, int mouseY, int key, int originalMouseX, int originalMouseY)
    {
        super.handleClickStop(mouseX, mouseY, key, originalMouseX, originalMouseY);

        this.getChildrens().stream().filter(child -> child.isPointInside(originalMouseX, originalMouseY))
                .forEach(child -> child.handleClickStop(mouseX, mouseY, key, originalMouseX, originalMouseY));
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
    public void handleMouseScroll(int mouseX, int mouseY, double scrolled)
    {
        super.handleMouseScroll(mouseX, mouseY, scrolled);

        this.getChildrens().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.handleMouseScroll(mouseX, mouseY, scrolled));
    }

    @Override
    public void handleKeyPress(int mouseX, int mouseY, int key)
    {
        super.handleKeyPress(mouseX, mouseY, key);

        this.getChildrens().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.handleKeyPress(mouseX, mouseY, key));
    }

    @Override
    public void handleKeyRelease(int mouseX, int mouseY, int key)
    {
        super.handleKeyRelease(mouseX, mouseY, key);

        this.getChildrens().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.handleKeyRelease(mouseX, mouseY, key));
    }

    @Override
    public void dispose()
    {
        super.dispose();

        this.getChildrens().forEach(GuiNode::dispose);
    }

    /////////////////////
    //     STYLING     //
    /////////////////////

    @Override
    public void setStyleTree(Supplier<StyleList> treeSupplier)
    {
        super.setStyleTree(treeSupplier);

        this.styleChilds.forEach(node -> node.setStyleTree(treeSupplier));
    }

    @Override
    public void refreshStyle()
    {
        this.styleChilds.forEach(ICascadeStyleable::refreshStyle);
        super.refreshStyle();
    }
}
