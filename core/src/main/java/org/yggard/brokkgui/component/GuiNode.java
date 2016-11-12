package org.yggard.brokkgui.component;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.GuiFocusManager;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.event.ClickEvent;
import org.yggard.brokkgui.event.DisableEvent;
import org.yggard.brokkgui.event.FocusEvent;
import org.yggard.brokkgui.event.GuiMouseEvent;
import org.yggard.brokkgui.event.HoverEvent;
import org.yggard.brokkgui.event.KeyEvent;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;

import fr.ourten.hermod.EventDispatcher;
import fr.ourten.hermod.EventHandler;
import fr.ourten.hermod.IEventEmitter;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiNode implements IEventEmitter
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
        return this.getzLevelProperty().getValue();
    }

    public void setzLevel(final float zLevel)
    {
        this.getzLevelProperty().setValue(zLevel);
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
        return this.getxPosProperty().getValue();
    }

    /**
     * @return yPos, used for layout management, do not attempt to change the
     *         property outside the layouting scope.
     */
    public float getyPos()
    {
        return this.getyPosProperty().getValue();
    }

    /**
     * @return xTranslate, used for component positionning.
     */
    public float getxTranslate()
    {
        return this.getxTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable
     * outside layouting scope.
     *
     * @param xTranslate
     */
    public void setxTranslate(final float xTranslate)
    {
        this.getxTranslateProperty().setValue(xTranslate);
    }

    /**
     * @return yTranslate, used for component positionning.
     */
    public float getyTranslate()
    {
        return this.getyTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable
     * outside layouting scope.
     *
     * @param yTranslate
     */
    public void setyTranslate(final float yTranslate)
    {
        this.getyTranslateProperty().setValue(yTranslate);
    }

    public float getWidth()
    {
        return this.getWidthProperty().getValue();
    }

    public void setWidth(final float width)
    {
        if (this.getWidthProperty().isBound())
            this.getWidthProperty().unbind();
        this.getWidthProperty().setValue(width);
    }

    public float getHeight()
    {
        return this.getHeightProperty().getValue();
    }

    public void setHeight(final float height)
    {
        if (this.getHeightProperty().isBound())
            this.getHeightProperty().unbind();
        this.getHeightProperty().setValue(height);
    }

    public float getWidthRatio()
    {
        return this.getWidthRatioProperty().getValue();
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
            this.getWidthProperty().unbind();
        else if (!this.getWidthProperty().isBound() && this.getFather() != null)
            RelativeBindingHelper.bindWidthRelative(this, this.getFather(), this.getWidthRatioProperty());
        this.getWidthRatioProperty().setValue(ratio);
    }

    public float getHeightRatio()
    {
        return this.getHeightRatioProperty().getValue();
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
            this.getHeightProperty().unbind();
        else if (!this.getHeightProperty().isBound() && this.getFather() != null)
            RelativeBindingHelper.bindHeightRelative(this, this.getFather(), this.getHeightRatioProperty());
        this.getHeightRatioProperty().setValue(ratio);
    }

    public BaseProperty<GuiFather> getFatherProperty()
    {
        return this.fatherProperty;
    }

    public GuiFather getFather()
    {
        return this.getFatherProperty().getValue();
    }

    /**
     * Internal method for the layouting system. A developper should never have
     * to use it.
     *
     * @param father
     */
    public void setFather(final GuiFather father)
    {
        if (this.getHeightProperty().isBound())
            this.getHeightProperty().unbind();
        if (this.getWidthProperty().isBound())
            this.getWidthProperty().unbind();
        this.getFatherProperty().setValue(father);
        if (father != null && this.getWidthRatio() != -1)
            RelativeBindingHelper.bindWidthRelative(this, father, this.getWidthRatioProperty());
        if (father != null && this.getHeightRatio() != -1)
            RelativeBindingHelper.bindHeightRelative(this, father, this.getHeightRatioProperty());
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
        return this.getFocusableProperty().getValue();
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
        this.getFocusedProperty().setValue(focused);
        this.getEventDispatcher().dispatchEvent(FocusEvent.TYPE, new FocusEvent(this, focused));
    }

    public boolean isFocusable()
    {
        return this.getFocusedProperty().getValue();
    }

    public void setFocusable(final boolean focusable)
    {
        this.getFocusedProperty().setValue(focusable);
    }

    public boolean isDisabled()
    {
        return this.getDisabledProperty().getValue();
    }

    public void setDisabled(final boolean disable)
    {
        this.getDisabledProperty().setValue(disable);
        this.getEventDispatcher().dispatchEvent(DisableEvent.TYPE, new DisableEvent(this, disable));
    }

    public boolean isHovered()
    {
        return this.getHoveredProperty().getValue();
    }

    public void setHovered(final boolean hovered)
    {
        this.getHoveredProperty().setValue(hovered);
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

    @Override
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