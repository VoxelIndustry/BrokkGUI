package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.event.ScrollEvent;
import org.yggard.brokkgui.policy.EOverflowPolicy;
import org.yggard.brokkgui.policy.EScrollbarPolicy;
import org.yggard.hermod.EventHandler;

public abstract class GuiScrollableBase extends GuiControl
{
    private final BaseProperty<Float> scrollXProperty, scrollYProperty;
    private final BaseProperty<Float> trueWidthProperty, trueHeightProperty;
    private final BaseProperty<Float> scrollSpeedProperty;

    private final BaseProperty<EScrollbarPolicy> scrollXPolicyProperty, scrollYPolicyProperty;

    private EventHandler<ScrollEvent> onScrollEvent;

    public GuiScrollableBase(String type)
    {
        super(type);

        this.scrollXProperty = new BaseProperty<>(0f, "scrollXProperty");
        this.scrollYProperty = new BaseProperty<>(0f, "scrollYProperty");

        this.trueWidthProperty = new BaseProperty<>(0f, "trueWidthProperty");
        this.trueHeightProperty = new BaseProperty<>(0f, "trueHeightProperty");

        this.scrollSpeedProperty = new BaseProperty<>(1f, "scrollSpeedProperty");

        this.scrollXPolicyProperty = new BaseProperty<>(EScrollbarPolicy.NEEDED, "scrollXPolicyProperty");
        this.scrollYPolicyProperty = new BaseProperty<>(EScrollbarPolicy.NEEDED, "scrollYPolicyProperty");
        this.setOverflowPolicy(EOverflowPolicy.TRIM_ALL);

        this.setFocusable(true);
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

    public BaseProperty<EScrollbarPolicy> getScrollXPolicyProperty()
    {
        return this.scrollXPolicyProperty;
    }

    public BaseProperty<EScrollbarPolicy> getScrollYPolicyProperty()
    {
        return this.scrollYPolicyProperty;
    }

    public EScrollbarPolicy getScrollXPolicy()
    {
        return this.getScrollXPolicyProperty().getValue();
    }

    public void setScrollXPolicy(EScrollbarPolicy policy)
    {
        this.getScrollXPolicyProperty().setValue(policy);
    }

    public EScrollbarPolicy getScrollYPolicy()
    {
        return this.getScrollYPolicyProperty().getValue();
    }

    public void setScrollYPolicy(EScrollbarPolicy policy)
    {
        this.getScrollYPolicyProperty().setValue(policy);
    }

    /**
     * @return the ScrollSpeed value of the ScrollPane. It represent the multiplier
     * used on the mouse delta when scrolling. Default is 1.
     */
    public float getScrollSpeed()
    {
        return this.getScrollSpeedProperty().getValue();
    }

    public void setScrollSpeed(final float scrollSpeed)
    {
        this.getScrollSpeedProperty().setValue(scrollSpeed);
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