package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;
import net.voxelindustry.brokkgui.policy.GuiScrollbarPolicy;
import net.voxelindustry.brokkgui.shape.ScissorBox;
import net.voxelindustry.hermod.EventHandler;

public abstract class GuiScrollableBase extends GuiSkinedElement
{
    private final BaseProperty<Float> scrollXProperty, scrollYProperty;
    private final BaseProperty<Float> trueWidthProperty, trueHeightProperty;
    private final BaseProperty<Float> scrollSpeedProperty;
    private final BaseProperty<Float> gripXWidthProperty, gripXHeightProperty, gripYWidthProperty, gripYHeightProperty;

    private final BaseProperty<GuiScrollbarPolicy> scrollXPolicyProperty, scrollYPolicyProperty;

    private final BaseProperty<Float> panSpeedProperty;

    private boolean isPannable;
    private boolean isScalable;
    private boolean isScrollable;

    private EventHandler<ScrollEvent> onScrollEvent;

    public GuiScrollableBase()
    {
        isScrollable = true;

        scrollXProperty = new BaseProperty<>(0f, "scrollXProperty");
        scrollYProperty = new BaseProperty<>(0f, "scrollYProperty");

        trueWidthProperty = new BaseProperty<>(0f, "trueWidthProperty");
        trueHeightProperty = new BaseProperty<>(0f, "trueHeightProperty");

        scrollSpeedProperty = new BaseProperty<>(1f, "scrollSpeedProperty");

        gripXWidthProperty = new BaseProperty<>(0f, "gripXWidthProperty");
        gripXHeightProperty = new BaseProperty<>(5f, "gripXHeightProperty");
        gripYWidthProperty = new BaseProperty<>(20f, "gripYWidthProperty");
        gripYHeightProperty = new BaseProperty<>(0f, "gripYHeightProperty");

        scrollXPolicyProperty = new BaseProperty<>(GuiScrollbarPolicy.NEEDED, "scrollXPolicyProperty");
        scrollYPolicyProperty = new BaseProperty<>(GuiScrollbarPolicy.NEEDED, "scrollYPolicyProperty");

        panSpeedProperty = new BaseProperty<>(1F, "panSpeedProperty");

        setScissorBox(ScissorBox.fitNode(transform()));
        setGuiOverflow(GuiOverflowPolicy.TRIM_ALL);

        setFocusable(true);
    }

    public BaseProperty<Float> getScrollXProperty()
    {
        return scrollXProperty;
    }

    public BaseProperty<Float> getScrollYProperty()
    {
        return scrollYProperty;
    }

    public BaseProperty<Float> getTrueWidthProperty()
    {
        return trueWidthProperty;
    }

    public BaseProperty<Float> getTrueHeightProperty()
    {
        return trueHeightProperty;
    }

    public BaseProperty<Float> getScrollSpeedProperty()
    {
        return scrollSpeedProperty;
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

    public BaseProperty<Float> getPanSpeedProperty()
    {
        return panSpeedProperty;
    }

    public float getScrollX()
    {
        return getScrollXProperty().getValue();
    }

    public void setScrollX(float scrollX)
    {
        getScrollXProperty().setValue(scrollX);
    }

    public float getScrollY()
    {
        return getScrollYProperty().getValue();
    }

    public void setScrollY(float scrollY)
    {
        getScrollYProperty().setValue(scrollY);
    }

    public float getTrueWidth()
    {
        return getTrueWidthProperty().getValue();
    }

    public float getTrueHeight()
    {
        return getTrueHeightProperty().getValue();
    }

    public BaseProperty<GuiScrollbarPolicy> getScrollXPolicyProperty()
    {
        return scrollXPolicyProperty;
    }

    public BaseProperty<GuiScrollbarPolicy> getScrollYPolicyProperty()
    {
        return scrollYPolicyProperty;
    }

    public GuiScrollbarPolicy getScrollXPolicy()
    {
        return getScrollXPolicyProperty().getValue();
    }

    public void setScrollXPolicy(GuiScrollbarPolicy policy)
    {
        getScrollXPolicyProperty().setValue(policy);
    }

    public GuiScrollbarPolicy getScrollYPolicy()
    {
        return getScrollYPolicyProperty().getValue();
    }

    public void setScrollYPolicy(GuiScrollbarPolicy policy)
    {
        getScrollYPolicyProperty().setValue(policy);
    }

    /**
     * @return the ScrollSpeed value of the ScrollPane. It represent the multiplier
     * used on the mouse delta when scrolling. Default is 1.
     */
    public float getScrollSpeed()
    {
        return getScrollSpeedProperty().getValue();
    }

    public void setScrollSpeed(float scrollSpeed)
    {
        getScrollSpeedProperty().setValue(scrollSpeed);
    }

    public float getGripXWidth()
    {
        return gripXWidthProperty.getValue();
    }

    /**
     * Forcefully set the width of the horizontal grip used to scroll
     *
     * @param gripXWidth value of the fixed width. Setting it to 0 will reset it to automatic.
     */
    public void setGripXWidth(float gripXWidth)
    {
        gripXWidthProperty.setValue(gripXWidth);
    }

    public float getGripXHeight()
    {
        return gripXHeightProperty.getValue();
    }

    public void setGripXHeight(float gripXHeight)
    {
        gripXHeightProperty.setValue(gripXHeight);
    }

    public float getGripYWidth()
    {
        return gripYWidthProperty.getValue();
    }

    public void setGripYWidth(float gripYWidth)
    {
        gripYWidthProperty.setValue(gripYWidth);
    }

    public float getGripYHeight()
    {
        return gripYHeightProperty.getValue();
    }

    /**
     * Forcefully set the height of the vertical grip used to scroll
     *
     * @param gripYHeight value of the fixed height. Setting it to 0 will reset it to automatic.
     */
    public void setGripYHeight(float gripYHeight)
    {
        gripYHeightProperty.setValue(gripYHeight);
    }

    public float getPanSpeed()
    {
        return getPanSpeedProperty().getValue();
    }

    public void setPanSpeed(float panSpeed)
    {
        getPanSpeedProperty().setValue(panSpeed);
    }

    public boolean isPannable()
    {
        return isPannable;
    }

    public void setPannable(boolean pannable)
    {
        isPannable = pannable;
    }

    /**
     * Allow scroll controls to alter this element scale.
     * This allow a zooming behavior on mousewheel.
     * <p>
     * When a Scrollable element is Scalable it will no longer be possible to scroll with the mousewheel.
     * If translating the Scrollable is still desired it is recommended to set the element as pannable @see #setPannable(boolean)
     */
    public boolean isScalable()
    {
        return isScalable;
    }

    /**
     * Allow scroll controls to alter this element scale.
     * This allow a zooming behavior on mousewheel.
     * <p>
     * When a Scrollable element is Scalable it will no longer be possible to scroll with the mousewheel.
     * If translating the Scrollable is still desired it is recommended to set the element as pannable @see #setPannable(boolean)
     *
     * @param scalable
     */
    public void setScalable(boolean scalable)
    {
        isScalable = scalable;
    }

    public boolean isScrollable()
    {
        return isScrollable;
    }

    public void setScrollable(boolean scrollable)
    {
        isScrollable = scrollable;
    }

    ////////////
    // EVENTS //
    ////////////

    public EventHandler<ScrollEvent> getOnScrollEvent()
    {
        return onScrollEvent;
    }

    public void setOnScrollEvent(EventHandler<ScrollEvent> onScrollEvent)
    {
        getEventDispatcher().removeHandler(ScrollEvent.TYPE, this.onScrollEvent);
        this.onScrollEvent = onScrollEvent;
        getEventDispatcher().addHandler(ScrollEvent.TYPE, this.onScrollEvent);
    }
}