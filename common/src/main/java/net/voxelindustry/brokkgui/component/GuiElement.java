package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.*;
import net.voxelindustry.brokkgui.gui.IGuiSubWindow;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class GuiElement implements IEventEmitter
{
    private static final boolean FALSE = false;
    private static final boolean TRUE = true;

    private final Map<Class<? extends GuiComponent>, GuiComponent> componentMap;

    private final List<RenderComponent> renderComponents;

    private final BaseProperty<String> idProperty;

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

    private int draggedX;
    private int draggedY;

    private IGuiSubWindow window;

    public GuiElement()
    {
        this.componentMap = new IdentityHashMap<>();
        this.renderComponents = new ArrayList<>(1);

        this.transform = this.add(new Transform());

        this.idProperty = new BaseProperty<>(null, "idProperty");

        this.focusedProperty = new BaseProperty<>(FALSE, "focusedProperty");
        this.disabledProperty = new BaseProperty<>(FALSE, "disabledProperty");
        this.hoveredProperty = new BaseProperty<>(FALSE, "hoveredProperty");

        this.focusableProperty = new BaseProperty<>(FALSE, "focusableProperty");
        this.visibleProperty = new BaseProperty<>(TRUE, "visibleProperty");
        this.draggedProperty = new BaseProperty<>(FALSE, "draggedProperty");

        this.opacityProperty = new BaseProperty<>(1D, "opacityProperty");

        this.postConstruct();
    }

    ////////////
    // INPUTS //
    ////////////

    public final void renderNode(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (!this.isVisible())
            return;

        boolean createdMatrix = false;
        if (this.transform.rotation() != Rotation.NONE && this.transform.rotation().getAngle() % 360 != 0)
        {
            createdMatrix = true;
            renderer.beginMatrix();

            float translateX, translateY;

            if (this.transform.rotation().getOrigin().isRelativePos())
            {
                translateX = this.transform.rotation().getOrigin().getOriginX() * this.transform.width();
                translateY = this.transform.rotation().getOrigin().getOriginY() * this.transform.height();
            }
            else
            {
                translateX = this.transform.rotation().getOrigin().getOriginX();
                translateY = this.transform.rotation().getOrigin().getOriginY();
            }

            translateX += this.transform.xPos() + this.transform.xTranslate();
            translateY += this.transform.yPos() + this.transform.yTranslate();

            renderer.translateMatrix(translateX, translateY, 0);
            renderer.rotateMatrix(this.transform.rotation().getAngle(), 0, 0, 1);
            renderer.translateMatrix(-translateX, -translateY, 0);
        }

        if (this.transform.scaleX() != 1 || this.transform.scaleY() != 1 || this.transform.scaleZ() != 1)
        {
            if (!createdMatrix)
            {
                createdMatrix = true;
                renderer.beginMatrix();
            }
            renderer.translateMatrix(this.transform.xPos() + this.transform.xTranslate() + this.transform.width() / 2,
                    this.transform.yPos() + this.transform.yTranslate() + this.transform.height() / 2, 0);
            renderer.scaleMatrix(this.transform.scaleX(), this.transform.scaleY(), this.transform.scaleZ());
            renderer.translateMatrix(-(this.transform.xPos() + this.transform.xTranslate() + this.transform.width() / 2),
                    -(this.transform.yPos() + this.transform.yTranslate() + this.transform.height() / 2), 0);
        }

        if (this.getOpacity() != 1)
            renderer.getHelper().startAlphaMask(this.getOpacity());

        boolean appliedScissor =
                this.transform.scissorBox() != null && this.transform.scissorBox().setupAndApply(renderer, pass);

        this.renderContent(renderer, pass, mouseX, mouseY);

        if (appliedScissor)
            this.transform.scissorBox().end(renderer);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();

        if (createdMatrix)
            renderer.endMatrix();
    }

    protected void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        for (RenderComponent component : this.renderComponents)
            component.renderContent(renderer, pass, mouseX, mouseY);

        transform().children().forEach(child -> child.element().renderNode(renderer, pass, mouseX, mouseY));
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
                    child.element().handleHover(mouseX, mouseY, child.isPointInside(mouseX, mouseY)));
        else
            this.transform.childrenProperty().getValue().forEach(child ->
                    child.element().handleHover(mouseX, mouseY, false));
    }

    public void handleMouseScroll(int mouseX, int mouseY, double scroll)
    {
        if (scroll != 0)
            this.getEventDispatcher().dispatchEvent(GuiMouseEvent.WHEEL,
                    new GuiMouseEvent.Wheel(this, mouseX, mouseY, (int) scroll));

        this.transform.childrenProperty().getValue().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.element().handleMouseScroll(mouseX, mouseY, scroll));
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
                .forEach(child -> child.element().handleClick(mouseX, mouseY, key));
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
                .forEach(child -> child.element().handleClickDrag(mouseX, mouseY, key, originalMouseX,
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
                .forEach(child -> child.element().handleClickStop(mouseX, mouseY, key, originalMouseX,
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
                .forEach(child -> child.element().handleKeyPress(mouseX, mouseY, key));
    }

    public void handleKeyRelease(int mouseX, int mouseY, int key)
    {
        this.getEventDispatcher().dispatchEvent(KeyEvent.RELEASE, new KeyEvent.Release(this, key));

        this.transform.childrenProperty().getValue().stream().filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.element().handleKeyRelease(mouseX, mouseY, key));
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
            GuiFocusManager.instance().requestFocus(this);
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

        if (this.transform.scissorBox() != null)
            this.transform.scissorBox().dispose();
    }

    public IGuiSubWindow getWindow()
    {
        return window;
    }

    public void setWindow(IGuiSubWindow window)
    {
        this.window = window;
    }

    //////////////
    //  LAYOUT  //
    //////////////

    public GuiElement parent()
    {
        return this.transform().parent().element();
    }

    public float xPos()
    {
        return transform.xPos();
    }

    public float yPos()
    {
        return transform.yPos();
    }

    public float xTranslate()
    {
        return transform.xTranslate();
    }

    public void xTranslate(float xTranslate)
    {
        transform.xTranslate(xTranslate);
    }

    public float yTranslate()
    {
        return transform.yTranslate();
    }

    public void yTranslate(float yTranslate)
    {
        transform.yTranslate(yTranslate);
    }

    public void translate(float xTranslate, float yTranslate)
    {
        transform.translate(xTranslate, yTranslate);
    }

    public float width()
    {
        return transform.width();
    }

    public void width(float width)
    {
        transform.width(width);
    }

    public float height()
    {
        return transform.height();
    }

    public void height(float height)
    {
        transform.height(height);
    }

    public void size(float width, float height)
    {
        transform.size(width, height);
    }

    ////////////////
    // COMPONENTS //
    ////////////////

    /**
     * Called right after the GuiElement has reached the end of its constructor
     */
    public void postConstruct()
    {
        // do nothing
    }

    public Transform transform()
    {
        return this.transform;
    }

    public final <T extends GuiComponent> T add(T component)
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
            this.componentMap.put(componentClass, instance);
            instance.attach(this);
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

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

    public <T extends GuiComponent> boolean has(Class<T> componentClass)
    {
        return this.componentMap.containsKey(componentClass);
    }

    public boolean has(GuiComponent component)
    {
        return this.componentMap.containsValue(component);
    }

    public <T extends GuiComponent> boolean ifHas(Class<T> componentClass, Consumer<T> action)
    {
        if (!this.has(componentClass))
            return false;
        action.accept(this.get(componentClass));
        return true;
    }

    public abstract String type();

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
