package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.event.ScrollEvent;
import org.yggard.brokkgui.policy.GuiOverflowPolicy;
import org.yggard.brokkgui.policy.GuiScrollbarPolicy;
import org.yggard.hermod.EventHandler;

public abstract class GuiScrollableBase extends GuiControl
{
    private final BaseProperty<Float> scrollXProperty, scrollYProperty;
    private final BaseProperty<Float> trueWidthProperty, trueHeightProperty;
    private final BaseProperty<Float> scrollSpeedProperty;
    private final BaseProperty<Float> gripXWidthProperty, gripXHeightProperty, gripYWidthProperty, gripYHeightProperty;

    private final BaseProperty<GuiScrollbarPolicy> scrollXPolicyProperty, scrollYPolicyProperty;

    private EventHandler<ScrollEvent> onScrollEvent;

    public GuiScrollableBase(String type)
    {
        super(type);

        this.scrollXProperty = new BaseProperty<>(0f, "scrollXProperty");
        this.scrollYProperty = new BaseProperty<>(0f, "scrollYProperty");

        this.trueWidthProperty = new BaseProperty<>(0f, "trueWidthProperty");
        this.trueHeightProperty = new BaseProperty<>(0f, "trueHeightProperty");

        this.scrollSpeedProperty = new BaseProperty<>(1f, "scrollSpeedProperty");

        this.gripXWidthProperty = new BaseProperty<>(0f, "gripXWidthProperty");
        this.gripXHeightProperty = new BaseProperty<>(5f, "gripXHeightProperty");
        this.gripYWidthProperty = new BaseProperty<>(20f, "gripYWidthProperty");
        this.gripYHeightProperty = new BaseProperty<>(0f, "gripYHeightProperty");

        this.scrollXPolicyProperty = new BaseProperty<>(GuiScrollbarPolicy.NEEDED, "scrollXPolicyProperty");
        this.scrollYPolicyProperty = new BaseProperty<>(GuiScrollbarPolicy.NEEDED, "scrollYPolicyProperty");
        this.setGuiOverflowPolicy(GuiOverflowPolicy.TRIM_ALL);

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

    public BaseProperty<Float> getGripXWidthProperty()
    {
        return gripXWidthProperty;
    }

    public BaseProperty<Float> getGripXHeightProperty()
    {
        return gripXHeightProperty;
    }

    public BaseProperty<Float> getGripYWidthProperty()
    {
        return gripYWidthProperty;
    }

    public BaseProperty<Float> getGripYHeightProperty()
    {
        return gripYHeightProperty;
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

    public BaseProperty<GuiScrollbarPolicy> getScrollXPolicyProperty()
    {
        return this.scrollXPolicyProperty;
    }

    public BaseProperty<GuiScrollbarPolicy> getScrollYPolicyProperty()
    {
        return this.scrollYPolicyProperty;
    }

    public GuiScrollbarPolicy getScrollXPolicy()
    {
        return this.getScrollXPolicyProperty().getValue();
    }

    public void setScrollXPolicy(GuiScrollbarPolicy policy)
    {
        this.getScrollXPolicyProperty().setValue(policy);
    }

    public GuiScrollbarPolicy getScrollYPolicy()
    {
        return this.getScrollYPolicyProperty().getValue();
    }

    public void setScrollYPolicy(GuiScrollbarPolicy policy)
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

    public float getGripXWidth()
    {
        return this.gripXWidthProperty.getValue();
    }

    /**
     * Forcefully set the width of the horizontal grip used to scroll
     *
     * @param gripXWidth value of the fixed width. Setting it to 0 will reset it to automatic.
     */
    public void setGripXWidth(float gripXWidth)
    {
        this.gripXWidthProperty.setValue(gripXWidth);
    }

    public float getGripXHeight()
    {
        return this.gripXHeightProperty.getValue();
    }

    public void setGripXHeight(float gripXHeight)
    {
        this.gripXHeightProperty.setValue(gripXHeight);
    }

    public float getGripYWidth()
    {
        return this.gripYWidthProperty.getValue();
    }

    public void setGripYWidth(float gripYWidth)
    {
        this.gripYWidthProperty.setValue(gripYWidth);
    }

    public float getGripYHeight()
    {
        return this.gripYHeightProperty.getValue();
    }

    /**
     * Forcefully set the height of the vertical grip used to scroll
     *
     * @param gripYHeight value of the fixed height. Setting it to 0 will reset it to automatic.
     */
    public void setGripYHeight(float gripYHeight)
    {
        this.gripYHeightProperty.setValue(gripYHeight);
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
        this.getEventDispatcher().removeHandler(ScrollEvent.TYPE, this.onScrollEvent);
        this.onScrollEvent = onScrollEvent;
        this.getEventDispatcher().addHandler(ScrollEvent.TYPE, this.onScrollEvent);
    }
}