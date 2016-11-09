package fr.ourten.brokkgui.panel;

import java.util.Arrays;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.brokkgui.control.GuiFather;
import fr.ourten.brokkgui.data.RelativeBindingHelper;
import fr.ourten.brokkgui.data.ZLevelComparator;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.policy.EOverflowPolicy;
import fr.ourten.teabeans.value.BaseProperty;

public class GuiPane extends GuiFather
{
    private static final ZLevelComparator ZLEVEL_COMPARATOR = new ZLevelComparator();
    private final EOverflowPolicy         overflowPolicy;

    private final BaseProperty<Integer>   borderThinProperty;
    private final BaseProperty<Color>     borderColorProperty;

    public GuiPane()
    {
        super();
        this.overflowPolicy = EOverflowPolicy.NONE;

        this.borderThinProperty = new BaseProperty<>(0, "borderThinProperty");
        this.borderColorProperty = new BaseProperty<>(Color.BLACK, "borderColorProperty");
    }

    public void addChild(final GuiNode node)
    {
        this.getChildrensProperty().add(node);
        node.setFather(this);

        RelativeBindingHelper.bindToCenter(node, this);
    }

    public void addChilds(final GuiNode... nodes)
    {
        this.getChildrensProperty().addAll(Arrays.asList(nodes));

        for (final GuiNode node : nodes)
        {
            node.setFather(this);
            RelativeBindingHelper.bindToCenter(node, this);
        }
    }

    public void removeChild(final GuiNode node)
    {
        this.getChildrensProperty().remove(node);

        node.setFather(null);
        node.getxPosProperty().unbind();
        node.getyPosProperty().unbind();
    }

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
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.SPECIAL)
            renderer.getHelper().drawColoredEmptyRect(renderer, this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                    this.getBorderColor(), this.getBorderThin());
        super.renderNode(renderer, pass, mouseX, mouseY);
    }

    public BaseProperty<Integer> getBorderThinProperty()
    {
        return this.borderThinProperty;
    }

    public BaseProperty<Color> getBorderColorProperty()
    {
        return this.borderColorProperty;
    }

    public int getBorderThin()
    {
        return this.getBorderThinProperty().getValue();
    }

    public void setBorderThin(final int thinness)
    {
        this.getBorderThinProperty().setValue(thinness);
    }

    public Color getBorderColor()
    {
        return this.getBorderColorProperty().getValue();
    }

    public void setBorderColor(final Color color)
    {
        this.getBorderColorProperty().setValue(color);
    }

    public EOverflowPolicy getOverflowPolicy()
    {
        return this.overflowPolicy;
    }
}