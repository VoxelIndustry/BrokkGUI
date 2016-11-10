package fr.ourten.brokkgui.panel;

import fr.ourten.brokkgui.behavior.GuiScrollPaneBehavior;
import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.brokkgui.control.GuiControl;
import fr.ourten.brokkgui.data.RelativeBindingHelper;
import fr.ourten.brokkgui.event.ScrollEvent;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.policy.EOverflowPolicy;
import fr.ourten.brokkgui.policy.EScrollbarPolicy;
import fr.ourten.brokkgui.skin.GuiScrollPaneSkin;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.hermod.EventHandler;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollPane extends GuiControl
{
    private final BaseProperty<Float> scrollXProperty, scrollYProperty;
    private final BaseProperty<Float> trueWidthProperty, trueHeightProperty;
    private final BaseProperty<Float> scrollSpeedProperty;

    private EScrollbarPolicy          scrollXPolicy, scrollYPolicy;

    private final BaseProperty<Color> scrollbarColorProperty;

    private EventHandler<ScrollEvent> onScrollEvent;

    public ScrollPane(final GuiNode node)
    {
        this.scrollXProperty = new BaseProperty<>(0f, "scrollXProperty");
        this.scrollYProperty = new BaseProperty<>(0f, "scrollYProperty");

        this.trueWidthProperty = new BaseProperty<>(0f, "trueWidthProperty");
        this.trueHeightProperty = new BaseProperty<>(0f, "trueHeightProperty");

        this.scrollSpeedProperty = new BaseProperty<>(1f, "scrollSpeedProperty");

        this.scrollbarColorProperty = new BaseProperty<>(Color.LIGHT_GRAY, "scrollbarColorProperty");

        this.scrollXPolicy = this.scrollYPolicy = EScrollbarPolicy.NEEDED;
        this.setOverflowPolicy(EOverflowPolicy.TRIM_ALL);
        if (node != null)
            this.setChild(node);

        this.setFocusable(true);
    }

    public ScrollPane()
    {
        this(null);
    }

    public void setChild(final GuiNode node)
    {
        if (!this.getChildrens().isEmpty())
        {
            this.getChildrensProperty().get(0).getxPosProperty().unbind();
            this.getChildrensProperty().get(0).getyPosProperty().unbind();
            this.getChildrensProperty().get(0).setFather(null);
            this.getChildrensProperty().clear();

            this.trueWidthProperty.unbind();
            this.trueHeightProperty.unbind();
        }

        this.getChildrensProperty().add(node);

        RelativeBindingHelper.bindToPos(node, this, this.scrollXProperty, this.scrollYProperty);

        node.setFather(this);
        this.trueWidthProperty.bind(node.getWidthProperty());
        this.trueHeightProperty.bind(node.getHeightProperty());
    }

    public BaseProperty<Float> getScrollXProperty()
    {
        return this.scrollXProperty;
    }

    public BaseProperty<Float> getScrollYProperty()
    {
        return this.scrollYProperty;
    }

    public BaseProperty<Float> getTrueWidthProperty()
    {
        return this.trueWidthProperty;
    }

    public BaseProperty<Float> getTrueHeightProperty()
    {
        return this.trueHeightProperty;
    }

    public BaseProperty<Color> getScrollbarColorProperty()
    {
        return this.scrollbarColorProperty;
    }

    public BaseProperty<Float> getScrollSpeedProperty()
    {
        return this.scrollSpeedProperty;
    }

    public float getScrollX()
    {
        return this.getScrollXProperty().getValue();
    }

    public void setScrollX(final float scrollX)
    {
        this.getScrollXProperty().setValue(scrollX);
    }

    public float getScrollY()
    {
        return this.getScrollYProperty().getValue();
    }

    public void setScrollY(final float scrollY)
    {
        this.getScrollYProperty().setValue(scrollY);
    }

    public float getTrueWidth()
    {
        return this.getTrueWidthProperty().getValue();
    }

    public float getTrueHeight()
    {
        return this.getTrueHeightProperty().getValue();
    }

    public EScrollbarPolicy getScrollXPolicy()
    {
        return this.scrollXPolicy;
    }

    public void setScrollXPolicy(final EScrollbarPolicy scrollXPolicy)
    {
        this.scrollXPolicy = scrollXPolicy;
    }

    public EScrollbarPolicy getScrollYPolicy()
    {
        return this.scrollYPolicy;
    }

    public void setScrollYPolicy(final EScrollbarPolicy scrollYPolicy)
    {
        this.scrollYPolicy = scrollYPolicy;
    }

    public Color getScrollbarColor()
    {
        return this.getScrollbarColorProperty().getValue();
    }

    public void setScrollbarColor(final Color color)
    {
        this.getScrollbarColorProperty().setValue(color);
    }

    /**
     *
     * @return the ScrollSpeed value of the ScrollPane. It represent the
     *         multiplier used on the mouse delta when scrolling. Default is 1.
     */
    public float getScrollSpeed()
    {
        return this.getScrollSpeedProperty().getValue();
    }

    public void setScrollSpeed(final float scrollSpeed)
    {
        this.getScrollSpeedProperty().setValue(scrollSpeed);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiScrollPaneSkin(this, new GuiScrollPaneBehavior(this));
    }

    ////////////
    // EVENTS //
    ////////////

    public EventHandler<ScrollEvent> getOnScrollEvent()
    {
        return this.onScrollEvent;
    }

    public void setOnScrollEvent(final EventHandler<ScrollEvent> onScrollEvent)
    {
        this.onScrollEvent = onScrollEvent;
    }
}