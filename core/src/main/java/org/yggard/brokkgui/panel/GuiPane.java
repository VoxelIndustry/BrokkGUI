package org.yggard.brokkgui.panel;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.data.ZLevelComparator;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.policy.EOverflowPolicy;

import java.util.Arrays;

public class GuiPane extends GuiFather
{
    private static final ZLevelComparator  ZLEVEL_COMPARATOR = new ZLevelComparator();
    private final EOverflowPolicy          overflowPolicy;

    private final BaseProperty<Background> backgroundProperty;

    public GuiPane()
    {
        super("pane");

        this.overflowPolicy = EOverflowPolicy.NONE;

        this.getStyle().registerProperty("-border-thin", 0, Integer.class);
        this.getStyle().registerProperty("-border-color", Color.BLACK, Color.class);

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

    public boolean hasChild(final GuiNode node)
    {
        return this.getChildrensProperty().contains(node);
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.SPECIAL && this.getBorderThin() > 0 && this.getBorderColor() != Color.ALPHA)
            renderer.getHelper().drawColoredEmptyRect(renderer, this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                    this.getBorderColor(), this.getBorderThin());
        this.getBackground().renderNode(renderer, pass, mouseX, mouseY);
        super.renderNode(renderer, pass, mouseX, mouseY);
    }

    public BaseProperty<Background> getBackgroundProperty()
    {
        return this.backgroundProperty;
    }

    public int getBorderThin()
    {
        return this.getStyle().getStyleProperty("-border-thin", Integer.class).getValue();
    }

    public Color getBorderColor()
    {
        return this.getStyle().getStyleProperty("-border-color", Color.class).getValue();
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