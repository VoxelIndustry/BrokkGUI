package net.voxelindustry.brokkgui.exp.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.*;
import net.voxelindustry.brokkgui.gui.IGuiSubWindow;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.util.MouseInBoundsChecker;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GuiElement implements IEventEmitter
{
    private Map<Class<? extends GuiComponent>, GuiComponent> componentMap;

    private List<RenderComponent> renderComponents;

    private BaseProperty<String> idProperty;

    private final Transform transform;

    private EventDispatcher            eventDispatcher;
    private EventHandler<FocusEvent>   onFocusEvent;
    private EventHandler<DisableEvent> onDisableEvent;
    private EventHandler<HoverEvent>   onHoverEvent;
    private EventHandler<ClickEvent>   onClickEvent;

    private final BaseProperty<Boolean> focusedProperty;
    private final BaseProperty<Boolean> disabledProperty;
    private final BaseProperty<Boolean> hoveredProperty;
    private final BaseProperty<Boolean> focusableProperty;
    private final BaseProperty<Boolean> visibleProperty;
    private final BaseProperty<Boolean> draggedProperty;

    private BaseProperty<Double> opacityProperty;

    private MouseInBoundsChecker mouseInBoundsChecker;

    private int draggedX;
    private int draggedY;

    private IGuiSubWindow window;

    public GuiElement()
    {
        this.componentMap = new IdentityHashMap<>();
        this.renderComponents = new ArrayList<>(1);

        this.transform = this.add(new Transform());

        this.idProperty = new BaseProperty<>(null, "idProperty");

        this.focusedProperty = new BaseProperty<>(false, "focusedProperty");
        this.disabledProperty = new BaseProperty<>(false, "disabledProperty");
        this.hoveredProperty = new BaseProperty<>(false, "hoveredProperty");

        this.focusableProperty = new BaseProperty<>(false, "focusableProperty");
        this.visibleProperty = new BaseProperty<>(true, "visibleProperty");
        this.draggedProperty = new BaseProperty<>(false, "draggedProperty");

        this.opacityProperty = new BaseProperty<>(1D, "opacityProperty");

        this.mouseInBoundsChecker = MouseInBoundsChecker.DEFAULT;
    }

    ////////////
    // INPUTS //
    ////////////

    public final void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (!this.isVisible())
            return;

        boolean createdMatrix = false;
        if (this.transform.getRotation() != Rotation.NONE && this.transform.getRotation().getAngle() % 360 != 0)
        {
            createdMatrix = true;
            renderer.beginMatrix();

            float translateX, translateY;

            if (this.transform.getRotation().getOrigin().isRelativePos())
            {
                translateX = this.transform.getRotation().getOrigin().getOriginX() * this.transform.getWidth();
                translateY = this.transform.getRotation().getOrigin().getOriginY() * this.transform.getHeight();
            }
            else
            {
                translateX = this.transform.getRotation().getOrigin().getOriginX();
                translateY = this.transform.getRotation().getOrigin().getOriginY();
            }

            translateX += this.transform.getxPos() + this.transform.getxTranslate();
            translateY += this.transform.getyPos() + this.transform.getyTranslate();

            renderer.translateMatrix(translateX, translateY, 0);
            renderer.rotateMatrix(this.transform.getRotation().getAngle(), 0, 0, 1);
            renderer.translateMatrix(-translateX, -translateY, 0);
        }

        if (this.transform.getScaleX() != 1 || this.transform.getScaleY() != 1 || this.transform.getScaleZ() != 1)
        {
            if (!createdMatrix)
            {
                createdMatrix = true;
                renderer.beginMatrix();
            }
            renderer.translateMatrix(this.transform.getxPos() + this.transform.getxTranslate() + this.transform.getWidth() / 2,
                    this.transform.getyPos() + this.transform.getyTranslate() + this.transform.getHeight() / 2, 0);
            renderer.scaleMatrix(this.transform.getScaleX(), this.transform.getScaleY(), this.transform.getScaleZ());
            renderer.translateMatrix(-(this.transform.getxPos() + this.transform.getxTranslate() + this.transform.getWidth() / 2),
                    -(this.transform.getyPos() + this.transform.getyTranslate() + this.transform.getHeight() / 2), 0);
        }

        if (this.getOpacity() != 1)
            renderer.getHelper().startAlphaMask(this.getOpacity());

        boolean appliedScissor =
                this.transform.getScissorBox() != null && this.transform.getScissorBox().setupAndApply(renderer, pass);

        this.renderContent(renderer, pass, mouseX, mouseY);

        if (appliedScissor)
            this.transform.getScissorBox().end(renderer);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();

        if (createdMatrix)
            renderer.endMatrix();
    }

    protected void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        for (RenderComponent component : this.renderComponents)
            component.renderContent(renderer, pass, mouseX, mouseY);
    }

    public void handleHover(int mouseX, int mouseY, boolean hovered)
    {
        if (this.isVisible())
        {
            if (hovered && !this.isHovered())
                this.setHovered(true);
            else if (!hovered && this.isHovered())
                this.setHovered(false);
        }

        if (hovered)
            this.transform.childrenProperty().getValue().forEach(child ->
                    child.getElement().handleHover(mouseX, mouseY, child.isPointInside(mouseX, mouseY)));
        else
            this.transform.childrenProperty().getValue().forEach(child ->
                    child.getElement().handleHover(mouseX, mouseY, false));
    }

    public void handleMouseScroll(int mouseX, int mouseY, double scroll)
    {
        if (scroll != 0)
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.WHEEL,
                    new GuiMouseEvent.Wheel(this, mouseX, mouseY, (int) scroll));

        this.transform.childrenProperty().getValue().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.getElement().handleMouseScroll(mouseX, mouseY, scroll));
    }

    public void handleClick(int mouseX, int mouseY, int key)
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

        this.transform.childrenProperty().getValue().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.getElement().handleClick(mouseX, mouseY, key));
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

        this.transform.childrenProperty().getValue().stream()
                .filter(child -> child.isPointInside(originalMouseX, originalMouseY))
                .forEach(child -> child.getElement().handleClickDrag(mouseX, mouseY, key, originalMouseX,
                        originalMouseY));
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

        this.transform.childrenProperty().getValue().stream()
                .filter(child -> child.isPointInside(originalMouseX, originalMouseY))
                .forEach(child -> child.getElement().handleClickStop(mouseX, mouseY, key, originalMouseX,
                        originalMouseY));
    }

    public void handleKeyInput(char c, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.INPUT, new KeyEvent.Input(this, c, key));
    }

    public void handleKeyPress(int mouseX, int mouseY, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.PRESS, new KeyEvent.Press(this, key));

        this.transform.childrenProperty().getValue().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.getElement().handleKeyPress(mouseX, mouseY, key));
    }

    public void handleKeyRelease(int mouseX, int mouseY, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.RELEASE, new KeyEvent.Release(this, key));

        this.transform.childrenProperty().getValue().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.getElement().handleKeyRelease(mouseX, mouseY, key));
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

    public void internalSetFocused(boolean focused)
    {
        this.getFocusedProperty().setValue(focused);
        this.getEventDispatcher().dispatchEvent(FocusEvent.TYPE, new FocusEvent(this, focused));
    }

    public boolean isFocusable()
    {
        return this.getFocusableProperty().getValue();
    }

    public void setFocusable(boolean focusable)
    {
        this.getFocusableProperty().setValue(focusable);
    }

    public boolean isDisabled()
    {
        return this.getDisabledProperty().getValue();
    }

    public void setDisabled(boolean disable)
    {
        this.getDisabledProperty().setValue(disable);
        this.getEventDispatcher().dispatchEvent(DisableEvent.TYPE, new DisableEvent(this, disable));
    }

    public boolean isHovered()
    {
        return this.getHoveredProperty().getValue();
    }

    public void setHovered(boolean hovered)
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

    /////////////
    // DISPLAY //
    /////////////

    public BaseProperty<Double> getOpacityProperty()
    {
        return this.opacityProperty;
    }

    public void replaceOpacityProperty(BaseProperty<Double> opacityProperty)
    {
        this.opacityProperty = opacityProperty;
    }

    public double getOpacity()
    {
        return this.getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        this.getOpacityProperty().setValue(opacity);
    }

    public MouseInBoundsChecker mouseInBoundsChecker()
    {
        return this.mouseInBoundsChecker;
    }

    public void mouseInBoundsChecker(MouseInBoundsChecker checker)
    {
        this.mouseInBoundsChecker = checker;
    }

    public boolean isPointInside(int pointX, int pointY)
    {
        return this.mouseInBoundsChecker().test(this, pointX, pointY);
    }

    /////////////
    // WINDOWS //
    /////////////

    /**
     * Clear all resources related to this component
     * <p>
     * Only called on UI closing.
     */
    public void dispose()
    {
        this.getEventDispatcher().dispatchEvent(DisposeEvent.TYPE, new DisposeEvent(this));

        if (this.transform.getScissorBox() != null)
            this.transform.getScissorBox().dispose();
    }

    public IGuiSubWindow getWindow()
    {
        return window;
    }

    public void setWindow(IGuiSubWindow window)
    {
        this.window = window;
    }

    ////////////////
    // COMPONENTS //
    ////////////////

    public Transform transform()
    {
        return this.transform;
    }

    public <T extends GuiComponent> T add(T component)
    {
        this.componentMap.put(component.getClass(), component);
        component.attach(this);

        if (component instanceof RenderComponent)
            this.renderComponents.add((RenderComponent) component);
        return component;
    }

    public <T extends GuiComponent> T add(Class<T> componentClass)
    {
        T instance = null;
        try
        {
            instance = componentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        this.componentMap.put(componentClass, instance);
        instance.attach(this);

        if (instance instanceof RenderComponent)
            this.renderComponents.add((RenderComponent) instance);
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T extends GuiComponent> T remove(Class<T> componentClass)
    {
        GuiComponent component = this.componentMap.remove(componentClass);

        if (component instanceof RenderComponent)
            this.renderComponents.remove(component);
        return (T) component;
    }

    @SuppressWarnings("unchecked")
    public <T extends GuiComponent> T get(Class<T> componentClass)
    {
        return (T) this.componentMap.get(componentClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends GuiComponent> Collection<T> getAll(Class<T> componentClass)
    {
        return this.componentMap.keySet().stream().filter(componentClass::isAssignableFrom)
                .map(key -> (T) componentMap.get(key)).collect(Collectors.toSet());
    }

    public boolean has(Class<? extends GuiComponent> componentClass)
    {
        return this.componentMap.containsKey(componentClass);
    }

    public boolean has(GuiComponent component)
    {
        return this.componentMap.containsValue(component);
    }

    protected abstract String getType();

    public BaseProperty<String> getIdProperty()
    {
        return this.idProperty;
    }

    public String getId()
    {
        return this.getIdProperty().getValue();
    }

    public void setId(String id)
    {
        this.getIdProperty().setValue(id);
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
