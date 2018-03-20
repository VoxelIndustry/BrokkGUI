package org.yggard.brokkgui.panel;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.data.ZLevelComparator;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.policy.EOverflowPolicy;
import org.yggard.brokkgui.style.StyleSource;

public class GuiPane extends GuiFather
{
    private static final ZLevelComparator ZLEVEL_COMPARATOR = new ZLevelComparator();
    private final EOverflowPolicy overflowPolicy;

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
            oldValue.detach(this);
            newValue.attach(this);
        });
        this.getStyle().registerProperty("-opacity", 1D, Double.class);
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

        if (pass == RenderPass.SPECIAL && this.getBorderThin() > 0 && this.getBorderColor() != Color.ALPHA)
            renderer.getHelper().drawColoredEmptyRect(renderer, this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                    this.getBorderColor(), this.getBorderThin());
        this.getBackground().renderNode(renderer, pass, mouseX, mouseY);
        super.renderContent(renderer, pass, mouseX, mouseY);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();
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

    public BaseProperty<Double> getOpacityProperty()
    {
        return this.getStyle().getStyleProperty("-opacity", Double.class);
    }

    public double getOpacity()
    {
        return this.getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        this.getStyle().getStyleProperty("-opacity", Double.class).setStyle(StyleSource.CODE, 0, opacity);
    }
}