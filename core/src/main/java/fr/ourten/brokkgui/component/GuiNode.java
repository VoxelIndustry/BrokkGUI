package fr.ourten.brokkgui.component;

import fr.ourten.brokkgui.BrokkGuiPlatform;
import fr.ourten.brokkgui.GuiFocusManager;
import fr.ourten.brokkgui.control.GuiFather;
import fr.ourten.brokkgui.data.RelativeBindingHelper;
import fr.ourten.brokkgui.event.ClickEvent;
import fr.ourten.brokkgui.event.DisableEvent;
import fr.ourten.brokkgui.event.EventDispatcher;
import fr.ourten.brokkgui.event.EventHandler;
import fr.ourten.brokkgui.event.FocusEvent;
import fr.ourten.brokkgui.event.GuiMouseEvent;
import fr.ourten.brokkgui.event.HoverEvent;
import fr.ourten.brokkgui.event.KeyEvent;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiNode
{
    private final BaseProperty<GuiFather> fatherProperty;
    private final BaseProperty<Float>     xPosProperty, yPosProperty, xTranslateProperty, yTranslateProperty,
            widthProperty, heightProperty, widthRatioProperty, heightRatioProperty, zLevelProperty;

    private EventDispatcher               eventDispatcher;
    private EventHandler<FocusEvent>      onFocusEvent;
    private EventHandler<DisableEvent>    onDisableEvent;
    private EventHandler<HoverEvent>      onHoverEvent;
    private EventHandler<ClickEvent>      onClickEvent;
    private final BaseProperty<Boolean>   focusedProperty, disabledProperty, hoveredProperty, focusableProperty;

    public GuiNode()
    {
        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");

        this.xTranslateProperty = new BaseProperty<>(0f, "xTranslateProperty");
        this.yTranslateProperty = new BaseProperty<>(0f, "yTranslateProperty");

        this.widthProperty = new BaseProperty<>(0f, "widthProperty");
        this.heightProperty = new BaseProperty<>(0f, "heightProperty");

        this.widthRatioProperty = new BaseProperty<>(-1f, "widthRatioProperty");
        this.heightRatioProperty = new BaseProperty<>(-1f, "heightRatioProperty");

        this.zLevelProperty = new BaseProperty<>(0f, "zLevelProperty");

        this.fatherProperty = new BaseProperty<>(null, "fatherProperty");

        this.focusedProperty = new BaseProperty<>(false, "focusedProperty");
        this.disabledProperty = new BaseProperty<>(false, "disabledProperty");
        this.hoveredProperty = new BaseProperty<>(false, "hoveredProperty");

        this.focusableProperty = new BaseProperty<>(false, "focusableProperty");
    }

    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (this.isPointInside(mouseX, mouseY))
        {
            if (!this.isHovered())
                this.setHovered(true);
        }
        else if (this.isHovered())
            this.setHovered(false);
    }

    public void handleMouseInput()
    {
        if (BrokkGuiPlatform.getInstance().getMouseUtil().getEventDWheel() != 0)
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.WHEEL,
                    new GuiMouseEvent.Wheel(this, BrokkGuiPlatform.getInstance().getMouseUtil().getEventDWheel()));
    }

    public void handleClick(final int mouseX, final int mouseY, final int key)
    {
        if (!this.isDisabled())
        {
            switch (key)
            {
                case 0:
                    this.getEventDispatcher().dispatchEvent(ClickEvent.Left.TYPE,
                            new ClickEvent.Left(this, mouseX, mouseY));
                    break;
                case 1:
                    this.getEventDispatcher().dispatchEvent(ClickEvent.Right.TYPE,
                            new ClickEvent.Right(this, mouseX, mouseY));
                    break;
                case 2:
                    this.getEventDispatcher().dispatchEvent(ClickEvent.Middle.TYPE,
                            new ClickEvent.Middle(this, mouseX, mouseY));
                    break;
                default:
                    this.getEventDispatcher().dispatchEvent(ClickEvent.TYPE, new ClickEvent(this, mouseX, mouseY, key));
                    break;
            }
            this.setFocused(!this.isFocused());
        }
    }

    public void handleKeyInput(final char c, final int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.TYPE, new KeyEvent(this, c, key));
    }

    public boolean isPointInside(final int pointX, final int pointY)
    {
        return this.getxPos() + this.getxTranslate() < pointX
                && pointX < this.getxPos() + this.getxTranslate() + this.getWidth()
                && this.getyPos() + this.getyTranslate() < pointY
                && pointY < this.getyPos() + this.getyTranslate() + this.getHeight();
    }

    public BaseProperty<Float> getzLevelProperty()
    {
        return this.zLevelProperty;
    }

    public float getzLevel()
    {
        return this.zLevelProperty.getValue();
    }

    public void setzLevel(final float zLevel)
    {
        this.zLevelProperty.setValue(zLevel);
    }

    public BaseProperty<Float> getxPosProperty()
    {
        return this.xPosProperty;
    }

    public BaseProperty<Float> getyPosProperty()
    {
        return this.yPosProperty;
    }

    public BaseProperty<Float> getxTranslateProperty()
    {
        return this.xTranslateProperty;
    }

    public BaseProperty<Float> getyTranslateProperty()
    {
        return this.yTranslateProperty;
    }

    public BaseProperty<Float> getWidthProperty()
    {
        return this.widthProperty;
    }

    public BaseProperty<Float> getHeightProperty()
    {
        return this.heightProperty;
    }

    public BaseProperty<Float> getWidthRatioProperty()
    {
        return this.widthRatioProperty;
    }

    public BaseProperty<Float> getHeightRatioProperty()
    {
        return this.heightRatioProperty;
    }

    /**
     * @return xPos, used for layout management, do not attempt to change the
     *         property outside the layouting scope.
     */
    public float getxPos()
    {
        return this.xPosProperty.getValue();
    }

    /**
     * @return yPos, used for layout management, do not attempt to change the
     *         property outside the layouting scope.
     */
    public float getyPos()
    {
        return this.yPosProperty.getValue();
    }

    /**
     * @return xTranslate, used for component positionning.
     */
    public float getxTranslate()
    {
        return this.xTranslateProperty.getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable
     * outside layouting scope.
     *
     * @param xTranslate
     */
    public void setxTranslate(final float xTranslate)
    {
        this.xTranslateProperty.setValue(xTranslate);
    }

    /**
     * @return yTranslate, used for component positionning.
     */
    public float getyTranslate()
    {
        return this.yTranslateProperty.getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable
     * outside layouting scope.
     *
     * @param yTranslate
     */
    public void setyTranslate(final float yTranslate)
    {
        this.yTranslateProperty.setValue(yTranslate);
    }

    public float getWidth()
    {
        return this.widthProperty.getValue();
    }

    public void setWidth(final float width)
    {
        if (this.widthProperty.isBound())
            this.widthProperty.unbind();
        this.widthProperty.setValue(width);
    }

    public float getHeight()
    {
        return this.heightProperty.getValue();
    }

    public void setHeight(final float height)
    {
        if (this.heightProperty.isBound())
            this.heightProperty.unbind();
        this.heightProperty.setValue(height);
    }

    public float getWidthRatio()
    {
        return this.widthRatioProperty.getValue();
    }

    /**
     * Allow to set the width of the node relatively to the width of his parent.
     * Use absolute setWidth to cancel the binding or set the width ratio to -1
     *
     * @param ratio
     */
    public void setWidthRatio(final float ratio)
    {
        if (ratio == -1)
            this.widthProperty.unbind();
        else if (!this.widthProperty.isBound())
            RelativeBindingHelper.bindWidthRelative(this, this.getFather(), this.widthRatioProperty);
        this.widthRatioProperty.setValue(ratio);
    }

    public float getHeightRatio()
    {
        return this.heightRatioProperty.getValue();
    }

    /**
     * Allow to set the height of the node relatively to the height of his
     * parent. Use absolute setHeight to cancel the binding or set the height
     * ratio to -1
     *
     * @param ratio
     */
    public void setHeightRatio(final float ratio)
    {
        if (ratio == -1)
            this.heightProperty.unbind();
        else if (!this.heightProperty.isBound())
            RelativeBindingHelper.bindHeightRelative(this, this.getFather(), this.heightRatioProperty);
        this.heightRatioProperty.setValue(ratio);
    }

    public BaseProperty<GuiFather> getFatherProperty()
    {
        return this.fatherProperty;
    }

    public GuiFather getFather()
    {
        return this.fatherProperty.getValue();
    }

    /**
     * Internal method for the layouting system. A developper should never have
     * to use it.
     *
     * @param father
     */
    public void setFather(final GuiFather father)
    {
        if (this.heightProperty.isBound())
            this.heightProperty.unbind();
        if (this.widthProperty.isBound())
            this.widthProperty.unbind();
        this.fatherProperty.setValue(father);
        if (father != null && this.getWidthRatio() != -1)
            RelativeBindingHelper.bindWidthRelative(this, father, this.widthRatioProperty);
        if (father != null && this.getHeightRatio() != -1)
            RelativeBindingHelper.bindHeightRelative(this, father, this.heightRatioProperty);
    }

    public BaseProperty<Boolean> getFocusedProperty()
    {
        return this.focusedProperty;
    }

    public BaseProperty<Boolean> getDisabledProperty()
    {
        return this.disabledProperty;
    }

    public BaseProperty<Boolean> getHoveredProperty()
    {
        return this.hoveredProperty;
    }

    public BaseProperty<Boolean> getFocusableProperty()
    {
        return this.focusableProperty;
    }

    public boolean isFocused()
    {
        return this.focusedProperty.getValue();
    }

    public void setFocused(final boolean focused)
    {
        if (!this.isDisabled() && this.isFocusable())
            if (focused)
                GuiFocusManager.getInstance().requestFocus(this);
            else
                GuiFocusManager.getInstance().requestFocus(null);
    }

    public void internalSetFocused(final boolean focused)
    {
        this.focusedProperty.setValue(focused);
        this.getEventDispatcher().dispatchEvent(FocusEvent.TYPE, new FocusEvent(this, focused));
    }

    public boolean isFocusable()
    {
        return this.focusableProperty.getValue();
    }

    public void setFocusable(final boolean focusable)
    {
        this.focusableProperty.setValue(focusable);
    }

    public boolean isDisabled()
    {
        return this.disabledProperty.getValue();
    }

    public void setDisabled(final boolean disable)
    {
        this.disabledProperty.setValue(disable);
        this.getEventDispatcher().dispatchEvent(DisableEvent.TYPE, new DisableEvent(this, disable));
    }

    public boolean isHovered()
    {
        return this.hoveredProperty.getValue();
    }

    public void setHovered(final boolean hovered)
    {
        this.hoveredProperty.setValue(hovered);
        this.getEventDispatcher().dispatchEvent(HoverEvent.TYPE, new HoverEvent(this, hovered));
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnFocusEvent(final EventHandler<FocusEvent> onFocusEvent)
    {
        this.getEventDispatcher().removeHandler(FocusEvent.TYPE, this.onFocusEvent);
        this.onFocusEvent = onFocusEvent;
        this.getEventDispatcher().addHandler(FocusEvent.TYPE, this.onFocusEvent);
    }

    public void setOnDisableEvent(final EventHandler<DisableEvent> onDisableEvent)
    {
        this.getEventDispatcher().removeHandler(DisableEvent.TYPE, this.onDisableEvent);
        this.onDisableEvent = onDisableEvent;
        this.getEventDispatcher().addHandler(DisableEvent.TYPE, this.onDisableEvent);
    }

    public void setOnHoverEvent(final EventHandler<HoverEvent> onHoverEvent)
    {
        this.getEventDispatcher().removeHandler(HoverEvent.TYPE, this.onHoverEvent);
        this.onHoverEvent = onHoverEvent;
        this.getEventDispatcher().addHandler(HoverEvent.TYPE, this.onHoverEvent);
    }

    public void setOnClickEvent(final EventHandler<ClickEvent> onClickEvent)
    {
        this.getEventDispatcher().removeHandler(ClickEvent.TYPE, this.onClickEvent);
        this.onClickEvent = onClickEvent;
        this.getEventDispatcher().addHandler(ClickEvent.TYPE, this.onClickEvent);
    }

    public EventHandler<ClickEvent> getOnClickEvent()
    {
        return this.onClickEvent;
    }

    public EventHandler<FocusEvent> getOnFocusEvent()
    {
        return this.onFocusEvent;
    }

    public EventHandler<DisableEvent> getOnDisableEvent()
    {
        return this.onDisableEvent;
    }

    public EventHandler<HoverEvent> getOnHoverEvent()
    {
        return this.onHoverEvent;
    }

    public EventDispatcher getEventDispatcher()
    {
        if (this.eventDispatcher == null)
            this.initEventDispatcher();
        return this.eventDispatcher;
    }

    private void initEventDispatcher()
    {
        this.eventDispatcher = new EventDispatcher();
    }
}