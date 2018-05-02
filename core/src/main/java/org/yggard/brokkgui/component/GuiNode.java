package org.yggard.brokkgui.component;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.BaseSetProperty;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.GuiFocusManager;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.event.*;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.style.ICascadeStyleable;
import org.yggard.brokkgui.style.StyleHolder;
import org.yggard.brokkgui.style.StyleSource;
import org.yggard.brokkgui.style.tree.StyleList;
import org.yggard.hermod.EventDispatcher;
import org.yggard.hermod.EventHandler;
import org.yggard.hermod.IEventEmitter;

import java.util.Collections;
import java.util.function.Supplier;

public abstract class GuiNode implements IEventEmitter, ICascadeStyleable
{
    private final BaseProperty<GuiFather> fatherProperty;
    private final BaseProperty<Float>     xPosProperty, yPosProperty, xTranslateProperty, yTranslateProperty,
            widthProperty, heightProperty, widthRatioProperty, heightRatioProperty, zLevelProperty;

    private       EventDispatcher            eventDispatcher;
    private       EventHandler<FocusEvent>   onFocusEvent;
    private       EventHandler<DisableEvent> onDisableEvent;
    private       EventHandler<HoverEvent>   onHoverEvent;
    private       EventHandler<ClickEvent>   onClickEvent;
    private final BaseProperty<Boolean>      focusedProperty, disabledProperty, hoveredProperty, focusableProperty;
    private final BaseProperty<Boolean> visibleProperty;

    private final BaseProperty<String>    styleID;
    private final BaseSetProperty<String> styleClass;
    private final BaseSetProperty<String> activePseudoClass;
    private       String                  type;
    private       StyleHolder             styleHolder;

    public GuiNode(String type)
    {
        this.type = type;

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

        this.visibleProperty = new BaseProperty<>(true, "visibleProperty");

        this.styleID = new BaseProperty<>(null, "styleIDProperty");
        this.styleClass = new BaseSetProperty<>(Collections.emptySet(), "styleClassListProperty");
        this.activePseudoClass = new BaseSetProperty<>(Collections.emptySet(), "activePseudoClassListProperty");
        this.styleHolder = new StyleHolder(this);

        this.styleID.addListener((obs, oldValue, newValue) -> this.refreshStyle());
        this.styleClass.addListener((ListValueChangeListener<String>) (obs, oldValue, newValue) -> this.refreshStyle());
        this.activePseudoClass
                .addListener((ListValueChangeListener<String>) (obs, oldValue, newValue) -> this.refreshStyle());

        this.getEventDispatcher().addHandler(HoverEvent.TYPE, e ->
        {
            if (e.isEntering())
                this.getActivePseudoClass().add("hover");
            else
                this.getActivePseudoClass().remove("hover");
        });

        this.getEventDispatcher().addHandler(DisableEvent.TYPE, e ->
        {
            if (e.isDisabled())
            {
                if (!this.getActivePseudoClass().contains("disabled") &&
                        this.getActivePseudoClass().contains("enabled"))
                    this.getActivePseudoClass().replace("enabled", "disabled");
                else if (!this.getActivePseudoClass().contains("enabled") &&
                        !this.getActivePseudoClass().contains("disabled"))
                    this.getActivePseudoClass().add("disabled");
            }
            else
            {
                if (!this.getActivePseudoClass().contains("enabled") &&
                        this.getActivePseudoClass().contains("disabled"))
                    this.getActivePseudoClass().replace("disabled", "enabled");
                else if (!this.getActivePseudoClass().contains("disabled") &&
                        !this.getActivePseudoClass().contains("enabled"))
                    this.getActivePseudoClass().add("enabled");
            }
        });

        this.getEventDispatcher().addHandler(FocusEvent.TYPE, e ->
        {
            if (e.isFocused())
                this.getActivePseudoClass().add("focus");
            else
                this.getActivePseudoClass().remove("focus");
        });

        this.getStyle().registerProperty("-background-color", Color.ALPHA, Color.class);
        this.getStyle().registerProperty("-foreground-color", Color.ALPHA, Color.class);

        this.getStyle().registerProperty("-background-texture", Texture.EMPTY, Texture.class);
        this.getStyle().registerProperty("-foreground-texture", Texture.EMPTY, Texture.class);
    }

    public final void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (!this.isVisible())
            return;

        if (pass == RenderPass.BACKGROUND)
        {
            if (this.getBackgroundTexture() != Texture.EMPTY)
            {
                Texture background = this.getBackgroundTexture();

                renderer.getHelper().bindTexture(background);
                renderer.getHelper().drawTexturedRect(renderer,
                        this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(),
                        background.getUMin(), background.getVMin(), background.getUMax(), background.getVMax(),
                        this.getWidth(), this.getHeight(), this.getzLevel());
            }
            if (this.getBackgroundColor().getAlpha() != 0)
            {
                Color background = this.getBackgroundColor();

                renderer.getHelper().drawColoredRect(renderer,
                        this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(),
                        this.getWidth(), this.getHeight(), this.getzLevel(),
                        background);
            }
        }
        if (pass == RenderPass.FOREGROUND)
        {
            if (this.getForegroundTexture() != Texture.EMPTY)
            {
                Texture foreground = this.getForegroundTexture();

                renderer.getHelper().bindTexture(foreground);
                renderer.getHelper().drawTexturedRect(renderer,
                        this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(),
                        foreground.getUMin(), foreground.getVMin(), foreground.getUMax(), foreground.getVMax(),
                        this.getWidth(), this.getHeight(), this.getzLevel());
            }
            if (this.getForegroundColor().getAlpha() != 0)
            {
                Color foreground = this.getForegroundColor();

                renderer.getHelper().drawColoredRect(renderer,
                        this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(),
                        this.getWidth(), this.getHeight(), this.getzLevel(),
                        foreground);
            }
        }
        this.renderContent(renderer, pass, mouseX, mouseY);
    }

    protected abstract void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY);

    public void handleHover(int mouseX, int mouseY, boolean hovered)
    {
        if (this.isVisible())
        {
            if (hovered && !this.isHovered())
                this.setHovered(true);
            else if (!hovered && this.isHovered())
                this.setHovered(false);
        }
    }

    public void handleMouseInput(int mouseX, int mouseY)
    {
        if (BrokkGuiPlatform.getInstance().getMouseUtil().getEventDWheel() != 0)
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.WHEEL,
                    new GuiMouseEvent.Wheel(this, BrokkGuiPlatform.getInstance().getMouseUtil().getEventDWheel()));
    }

    public void handleClick(final int mouseX, final int mouseY, final int key)
    {
        if (!this.isDisabled() && this.isVisible())
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
     * property outside the layouting scope.
     */
    public float getxPos()
    {
        return this.getxPosProperty().getValue();
    }

    /**
     * @return yPos, used for layout management, do not attempt to change the
     * property outside the layouting scope.
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
     * Set the translation of this component to this specified value, usable outside
     * layouting scope.
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
     * Set the translation of this component to this specified value, usable outside
     * layouting scope.
     *
     * @param yTranslate
     */
    public void setyTranslate(final float yTranslate)
    {
        this.getyTranslateProperty().setValue(yTranslate);
    }

    /**
     * Helper method to set the translation in one call.
     * <p>
     * Set the translation of this component to this specified value, usable outside
     * layouting scope.
     *
     * @param xTranslate
     * @param yTranslate
     */
    public void setTranslate(float xTranslate, float yTranslate)
    {
        this.setxTranslate(xTranslate);
        this.setyTranslate(yTranslate);
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

    /**
     * Helper method to set the width and height of the Node in one call.
     *
     * @param width
     * @param height
     */
    public void setSize(float width, float height)
    {
        this.setWidth(width);
        this.setHeight(height);
    }

    public float getWidthRatio()
    {
        return this.getWidthRatioProperty().getValue();
    }

    /**
     * Allow to set the width of the node relatively to the width of his parent. Use
     * absolute setWidth to cancel the binding or set the width ratio to -1
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
     * Allow to set the height of the node relatively to the height of his parent.
     * Use absolute setHeight to cancel the binding or set the height ratio to -1
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

    /**
     * Helper method to set the width ratio and height ratio in one call.
     *
     * @param widthRatio
     * @param heightRatio
     */
    public void setSizeRatio(float widthRatio, float heightRatio)
    {
        this.setWidthRatio(widthRatio);
        this.setHeightRatio(heightRatio);
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
     * Internal method for the layouting system. A developer should never have to
     * use it.
     *
     * @param father
     */
    public void setFather(final GuiFather father)
    {
        this.getFatherProperty().setValue(father);
        if (father != null && this.getWidthRatio() != -1)
            RelativeBindingHelper.bindWidthRelative(this, father, this.getWidthRatioProperty());
        if (father != null && this.getHeightRatio() != -1)
            RelativeBindingHelper.bindHeightRelative(this, father, this.getHeightRatioProperty());

        this.getStyle().getParent().setValue(father);
        if (father != null)
            this.setStyleTree(father.getStyle().getStyleSupplier());
        this.refreshStyle();
    }

    public Texture getBackgroundTexture()
    {
        return this.getStyle().getStyleProperty("-background-texture", Texture.class).getValue();
    }

    public void setBackgroundTexture(Texture texture)
    {
        this.getStyle().getStyleProperty("-background-texture", Texture.class)
                .setStyle(StyleSource.CODE, 10_000, texture);
    }

    public Color getBackgroundColor()
    {
        return this.getStyle().getStyleProperty("-background-color", Color.class).getValue();
    }

    public void setBackgroundColor(Color color)
    {
        this.getStyle().getStyleProperty("-background-color", Color.class)
                .setStyle(StyleSource.CODE, 10_000, color);
    }

    public Texture getForegroundTexture()
    {
        return this.getStyle().getStyleProperty("-foreground-texture", Texture.class).getValue();
    }

    public void setForegroundTexture(Texture texture)
    {
        this.getStyle().getStyleProperty("-foreground-texture", Texture.class)
                .setStyle(StyleSource.CODE, 10_000, texture);
    }

    public Color getForegroundColor()
    {
        return this.getStyle().getStyleProperty("-foreground-color", Color.class).getValue();
    }

    public void setForegroundColor(Color color)
    {
        this.getStyle().getStyleProperty("-foreground-color", Color.class)
                .setStyle(StyleSource.CODE, 10_000, color);
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

    public BaseProperty<Boolean> getVisibleProperty()
    {
        return visibleProperty;
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
        if (this.isHovered())
            this.setHovered(false);
        if (this.isFocused())
            this.setFocused(false);
        this.getDisabledProperty().setValue(disable);
        this.getEventDispatcher().dispatchEvent(DisableEvent.TYPE, new DisableEvent(this, disable));
    }

    public boolean isHovered()
    {
        return this.getHoveredProperty().getValue();
    }

    public void setHovered(final boolean hovered)
    {
        if (this.isDisabled())
            return;
        this.getHoveredProperty().setValue(hovered);
        this.getEventDispatcher().dispatchEvent(HoverEvent.TYPE, new HoverEvent(this, hovered));
    }

    public boolean isVisible()
    {
        return this.getVisibleProperty().getValue();
    }

    public void setVisible(boolean visible)
    {
        this.getVisibleProperty().setValue(visible);
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

    /////////////////////
    // STYLING //
    /////////////////////

    @Override
    public void setID(String id)
    {
        this.getIDProperty().setValue(id);
    }

    @Override
    public String getID()
    {
        return this.getIDProperty().getValue();
    }

    public BaseProperty<String> getIDProperty()
    {
        return this.styleID;
    }

    @Override
    public BaseSetProperty<String> getStyleClass()
    {
        return this.styleClass;
    }

    @Override
    public StyleHolder getStyle()
    {
        return this.styleHolder;
    }

    @Override
    public ICascadeStyleable getParent()
    {
        return this.getFather();
    }

    @Override
    public void setStyleTree(Supplier<StyleList> treeSupplier)
    {
        this.getStyle().setStyleSupplier(treeSupplier);
    }

    @Override
    public void refreshStyle()
    {
        this.getStyle().refresh();
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public BaseSetProperty<String> getActivePseudoClass()
    {
        return activePseudoClass;
    }

    protected void setType(String type)
    {
        this.type = type;
    }
}