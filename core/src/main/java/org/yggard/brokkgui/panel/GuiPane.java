package org.yggard.brokkgui.panel;

import java.util.Arrays;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.data.ZLevelComparator;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.policy.EOverflowPolicy;

import fr.ourten.teabeans.value.BaseProperty;

public class GuiPane extends GuiFather
{
    private static final ZLevelComparator  ZLEVEL_COMPARATOR = new ZLevelComparator();
    private final EOverflowPolicy          overflowPolicy;

    private final BaseProperty<Integer>    borderThinProperty;
    private final BaseProperty<Color>      borderColorProperty;

    private final BaseProperty<Background> backgroundProperty;

    public GuiPane()
    {
        super();
        this.overflowPolicy = EOverflowPolicy.NONE;

        this.borderThinProperty = new BaseProperty<>(0, "borderThinProperty");
        this.borderColorProperty = new BaseProperty<>(Color.BLACK, "borderColorProperty");

        final Background background = new Background();
        background.attach(this);
        this.backgroundProperty = new BaseProperty<>(background, "backgroundProperty");

        this.backgroundProperty.addListener((property, oldValue, newValue) ->
        {
            oldValue.detach();
            newValue.attach(this);
        });
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
        this.getBackground().renderNode(renderer, pass, mouseX, mouseY);
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

    public BaseProperty<Background> getBackgroundProperty()
    {
        return this.backgroundProperty;
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

    @Override
    public EOverflowPolicy getOverflowPolicy()
    {
        return this.overflowPolicy;
    }

    public Background getBackground()
    {
        return this.getBackgroundProperty().getValue();
    }

    public void setBackground(final Background background)
    {
        this.getBackgroundProperty().setValue(background);
    }
}