package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.BaseSetProperty;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.*;
import net.voxelindustry.brokkgui.gui.IGuiSubWindow;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.shape.ScissorBox;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.Collections;
import java.util.function.Supplier;

public abstract class GuiNode implements IEventEmitter
{
    private final BaseProperty<GuiFather> fatherProperty;
    private final BaseProperty<Float>     xPosProperty, yPosProperty, xTranslateProperty, yTranslateProperty,
            widthProperty, heightProperty, widthRatioProperty, heightRatioProperty, zLevelProperty;

    private final BaseProperty<Rotation> rotationProperty;
    private final BaseProperty<Float>    scaleXProperty, scaleYProperty, scaleZProperty;

    private       EventDispatcher            eventDispatcher;
    private       EventHandler<FocusEvent>   onFocusEvent;
    private       EventHandler<DisableEvent> onDisableEvent;
    private       EventHandler<HoverEvent>   onHoverEvent;
    private       EventHandler<ClickEvent>   onClickEvent;
    private final BaseProperty<Boolean>      focusedProperty, disabledProperty, hoveredProperty, focusableProperty;
    private final BaseProperty<Boolean> visibleProperty;
    private final BaseProperty<Boolean> draggedProperty;
    private       int                   draggedX, draggedY;

    private ScissorBox scissorBox;

    private final BaseProperty<String>    styleID;
    private final BaseSetProperty<String> styleClass;
    private final BaseSetProperty<String> activePseudoClass;
    private       String                  type;
    private       StyleHolder             styleHolder;

    private IGuiSubWindow window;

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

        this.rotationProperty = new BaseProperty<>(Rotation.NONE, "rotationProperty");
        this.scaleXProperty = new BaseProperty<>(1f, "scaleXProperty");
        this.scaleYProperty = new BaseProperty<>(1f, "scaleYProperty");
        this.scaleZProperty = new BaseProperty<>(1f, "scaleZProperty");

        this.fatherProperty = new BaseProperty<>(null, "fatherProperty");

        this.focusedProperty = new BaseProperty<>(false, "focusedProperty");
        this.disabledProperty = new BaseProperty<>(false, "disabledProperty");
        this.hoveredProperty = new BaseProperty<>(false, "hoveredProperty");

        this.focusableProperty = new BaseProperty<>(false, "focusableProperty");

        this.visibleProperty = new BaseProperty<>(true, "visibleProperty");

        this.draggedProperty = new BaseProperty<>(false, "draggedProperty");

        this.styleID = new BaseProperty<>(null, "styleIDProperty");
        this.styleClass = new BaseSetProperty<>(Collections.emptySet(), "styleClassListProperty");
        this.activePseudoClass = new BaseSetProperty<>(Collections.emptySet(), "activePseudoClassListProperty");
        this.styleHolder = new StyleHolder(this);

        this.styleID.addListener((obs, oldValue, newValue) -> this.refreshStyle());
        this.styleClass.addListener((ListValueChangeListener<String>) (obs, oldValue, newValue) -> this.refreshStyle());
        this.activePseudoClass
                .addListener((ListValueChangeListener<String>) (obs, oldValue, newValue) -> this.refreshStyle());

        this.getHoveredProperty().addListener(obs ->
        {
            if (this.isHovered())
                this.getActivePseudoClass().add("hover");
            else
                this.getActivePseudoClass().remove("hover");
        });

        this.getDisabledProperty().addListener(obs ->
        {
            if (this.isDisabled())
            {
                if (this.isHovered())
                    this.setHovered(false);
                if (this.isFocused())
                    GuiFocusManager.getInstance().requestFocus(null);

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

        this.getFocusedProperty().addListener(obs ->
        {
            if (this.isFocused())
                this.getActivePseudoClass().add("focus");
            else
                this.getActivePseudoClass().remove("focus");
        });

        this.getStyle().registerProperty("opacity", 1D, Double.class);
    }

    /**
     * Clear all resources related to this component
     * <p>
     * Only called on UI closing.
     */
    public void dispose()
    {
        this.getEventDispatcher().dispatchEvent(DisposeEvent.TYPE, new DisposeEvent(this));

        if (this.getScissorBox() != null)
            this.getScissorBox().dispose();
    }

    public final void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (!this.isVisible())
            return;

        boolean createdMatrix = false;
        if (this.getRotation() != Rotation.NONE && this.getRotation().getAngle() % 360 != 0)
        {
            createdMatrix = true;
            renderer.beginMatrix();

            float translateX, translateY;

            if (this.getRotation().getOrigin().isRelativePos())
            {
                translateX = this.getRotation().getOrigin().getOriginX() * this.getWidth();
                translateY = this.getRotation().getOrigin().getOriginY() * this.getHeight();
            }
            else
            {
                translateX = this.getRotation().getOrigin().getOriginX();
                translateY = this.getRotation().getOrigin().getOriginY();
            }

            translateX += this.getxPos() + this.getxTranslate();
            translateY += this.getyPos() + this.getyTranslate();

            renderer.translateMatrix(translateX, translateY, 0);
            renderer.rotateMatrix(this.getRotation().getAngle(), 0, 0, 1);
            renderer.translateMatrix(-translateX, -translateY, 0);
        }

        if (this.getScaleX() != 1 || this.getScaleY() != 1 || this.getScaleZ() != 1)
        {
            if (!createdMatrix)
            {
                createdMatrix = true;
                renderer.beginMatrix();
            }
            renderer.translateMatrix(this.getxPos() + this.getxTranslate() + this.getWidth() / 2,
                    this.getyPos() + this.getyTranslate() + this.getHeight() / 2, 0);
            renderer.scaleMatrix(this.getScaleX(), this.getScaleY(), this.getScaleZ());
            renderer.translateMatrix(-(this.getxPos() + this.getxTranslate() + this.getWidth() / 2),
                    -(this.getyPos() + this.getyTranslate() + this.getHeight() / 2), 0);
        }

        if (this.getOpacity() != 1)
            renderer.getHelper().startAlphaMask(this.getOpacity());

        boolean appliedScissor = this.getScissorBox() != null && this.getScissorBox().setupAndApply(renderer, pass);

        this.renderContent(renderer, pass, mouseX, mouseY);

        if (appliedScissor)
            this.getScissorBox().end(renderer);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();

        if (createdMatrix)
            renderer.endMatrix();
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

    public void handleMouseScroll(int mouseX, int mouseY, double scroll)
    {
        if (scroll != 0)
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.WHEEL,
                    new GuiMouseEvent.Wheel(this, mouseX, mouseY, (int) scroll));
    }

    public void handleClick(final int mouseX, final int mouseY, final int key)
    {
        if (this.isDisabled() || !this.isVisible())
            return;
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
        this.setFocused();
    }

    public void handleClickDrag(int mouseX, int mouseY, int key, int originalMouseX, int originalMouseY)
    {
        if (this.isDisabled() || !this.isVisible())
            return;

        if (!this.isDragged())
        {
            this.getDraggedProperty().setValue(true);
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.DRAG_START,
                    new GuiMouseEvent.DragStart(this, mouseX, mouseY, key));
        }
        this.draggedX = mouseX - originalMouseX;
        this.draggedY = mouseY - originalMouseY;


        this.getEventDispatcher().dispatchEvent(GuiMouseEvent.DRAGGING,
                new GuiMouseEvent.Dragging(this, mouseX, mouseY, key, draggedX, draggedY));
    }

    public void handleClickStop(int mouseX, int mouseY, int key, int originalMouseX, int originalMouseY)
    {
        if (this.isDisabled() || !this.isVisible())
            return;

        if (this.isDragged())
        {
            this.getDraggedProperty().setValue(false);
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.DRAG_STOP,
                    new GuiMouseEvent.DragStop(this, mouseX, mouseY, key, draggedX, draggedY));
            this.draggedX = 0;
            this.draggedY = 0;
        }
    }

    public void handleKeyInput(char c, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.INPUT, new KeyEvent.Input(this, c, key));
    }

    public void handleKeyPress(int mouseX, int mouseY, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.PRESS, new KeyEvent.Press(this, key));
    }

    public void handleKeyRelease(int mouseX, int mouseY, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.RELEASE, new KeyEvent.Release(this, key));
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

    public BaseProperty<Rotation> getRotationProperty()
    {
        return rotationProperty;
    }

    public BaseProperty<Float> getScaleXProperty()
    {
        return scaleXProperty;
    }

    public BaseProperty<Float> getScaleYProperty()
    {
        return scaleYProperty;
    }

    public BaseProperty<Float> getScaleZProperty()
    {
        return scaleZProperty;
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
     * Method computing the left-pos of this Node box (pos.x + translate.x)
     *
     * @return the computed non-cached value
     */
    public float getLeftPos()
    {
        return this.getxPos() + this.getxTranslate();
    }

    /**
     * Method computing the top-pos of this Node box (pos.y + translate.y)
     *
     * @return the computed non-cached value
     */
    public float getTopPos()
    {
        return this.getyPos() + this.getyTranslate();
    }

    /**
     * Method computing the right-pos of this Node box (pos.x + translate.x + width)
     *
     * @return the computed non-cached value
     */
    public float getRightPos()
    {
        return this.getxPos() + this.getxTranslate() + this.getWidth();
    }

    /**
     * Method computing the bottom-pos of this Node box (pos.y + translate.y + height)
     *
     * @return the computed non-cached value
     */
    public float getBottomPos()
    {
        return this.getyPos() + this.getyTranslate() + this.getHeight();
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

    public Rotation getRotation()
    {
        return this.getRotationProperty().getValue();
    }

    public void setRotation(Rotation rotation)
    {
        this.getRotationProperty().setValue(rotation);
    }

    public float getScaleX()
    {
        return this.getScaleXProperty().getValue();
    }

    public void setScaleX(float scaleX)
    {
        this.getScaleXProperty().setValue(scaleX);
    }

    public float getScaleY()
    {
        return this.getScaleYProperty().getValue();
    }

    public void setScaleY(float scaleY)
    {
        this.getScaleYProperty().setValue(scaleY);
    }

    public float getScaleZ()
    {
        return this.getScaleZProperty().getValue();
    }

    public void setScaleZ(float scaleZ)
    {
        this.getScaleZProperty().setValue(scaleZ);
    }

    public void setScale(float scale)
    {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setScaleZ(scale);
    }

    public BaseProperty<Double> getOpacityProperty()
    {
        return this.getStyle().getStyleProperty("opacity", Double.class);
    }

    public double getOpacity()
    {
        return this.getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        this.getStyle().setPropertyDirect("opacity", opacity, Double.class);
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
    public void setFather(GuiFather father)
    {
        if (getFatherProperty().isPresent() && getFather().getWindow() != null)
            getFather().getWindow().dispatchEvent(LayoutEvent.REMOVE, new LayoutEvent.Remove(this));

        this.getFatherProperty().setValue(father);
        if (father != null && this.getWidthRatio() != -1)
            RelativeBindingHelper.bindWidthRelative(this, father, this.getWidthRatioProperty());
        if (father != null && this.getHeightRatio() != -1)
            RelativeBindingHelper.bindHeightRelative(this, father, this.getHeightRatioProperty());

        this.getStyle().getParent().setValue(father);
        if (father != null)
        {
            this.setStyleTree(father.getStyle().getStyleSupplier());
            this.setWindow(father.getWindow());

            if (this.getWindow() != null)
                getWindow().dispatchEvent(LayoutEvent.ADD, new LayoutEvent.Add(this));
        }
        this.refreshStyle();
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

    public BaseProperty<Boolean> getDraggedProperty()
    {
        return draggedProperty;
    }

    public boolean isFocused()
    {
        return this.getFocusedProperty().getValue();
    }

    public void setFocused()
    {
        if (!this.isDisabled() && this.isFocusable())
            GuiFocusManager.getInstance().requestFocus(this);
    }

    public void internalSetFocused(final boolean focused)
    {
        this.getFocusedProperty().setValue(focused);
        this.getEventDispatcher().dispatchEvent(FocusEvent.TYPE, new FocusEvent(this, focused));
    }

    public boolean isFocusable()
    {
        return this.getFocusableProperty().getValue();
    }

    public void setFocusable(final boolean focusable)
    {
        this.getFocusableProperty().setValue(focusable);
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
        if (this.isDisabled() && hovered)
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

    public boolean isDragged()
    {
        return this.getDraggedProperty().getValue();
    }

    public int getDraggedX()
    {
        return draggedX;
    }

    public int getDraggedY()
    {
        return draggedY;
    }

    public ScissorBox getScissorBox()
    {
        return scissorBox;
    }

    public void setScissorBox(ScissorBox scissorBox)
    {
        this.scissorBox = scissorBox;
    }

    public IGuiSubWindow getWindow()
    {
        return window;
    }

    public void setWindow(IGuiSubWindow window)
    {
        this.window = window;
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

    @Override
    public void setParent(ICascadeStyleable styleable)
    {
        this.getStyle().getParent().setValue(styleable);
    }
}
