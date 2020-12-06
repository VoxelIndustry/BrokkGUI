package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.event.ComponentEvent;
import net.voxelindustry.brokkgui.event.DisableEvent;
import net.voxelindustry.brokkgui.event.DisposeEvent;
import net.voxelindustry.brokkgui.event.FocusEvent;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.HoverEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.LayoutEvent;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.shape.ScissorBox;
import net.voxelindustry.brokkgui.window.IGuiSubWindow;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.voxelindustry.brokkgui.shape.RectangleShape.RECTANGLE_SHAPE;

public abstract class GuiElement implements IEventEmitter
{
    private final Map<Class<? extends GuiComponent>, GuiComponent> componentMap;

    private final List<RenderComponent> renderComponents;
    private final List<UpdateComponent> updateComponents;

    private final Transform transform;

    private final Paint paint;

    private final Property<String> idProperty;

    private EventDispatcher            eventDispatcher;
    private EventHandler<FocusEvent>   onFocusEvent;
    private EventHandler<DisableEvent> onDisableEvent;
    private EventHandler<HoverEvent>   onHoverEvent;
    private EventHandler<ClickEvent>   onClickEvent;

    private final ValueInvalidationListener disableListener = this::disableListener;

    private Property<Double> opacityProperty;

    private final Property<Boolean> focusedProperty;
    private final Property<Boolean> disabledProperty;
    private final Property<Boolean> hoveredProperty;
    private final Property<Boolean> focusableProperty;
    private final Property<Boolean> visibleProperty;
    private final Property<Boolean> draggedProperty;

    private int draggedX;
    private int draggedY;

    private ScissorBox scissorBox;

    private IGuiSubWindow window;

    private boolean isRenderDirty      = true;
    private boolean isChildRenderDirty = true;

    public GuiElement()
    {
        componentMap = new IdentityHashMap<>();
        renderComponents = new ArrayList<>(1);
        updateComponents = new ArrayList<>(1);

        transform = add(new Transform());
        idProperty = new Property<>(null);

        focusedProperty = new Property<>(false);
        disabledProperty = new Property<>(false);
        hoveredProperty = new Property<>(false);

        focusableProperty = new Property<>(false);
        visibleProperty = new Property<>(true);
        draggedProperty = new Property<>(false);

        opacityProperty = new Property<>(1D);

        ComponentEngine.instance().inject(this);

        paint = provide(Paint.class);
        paint.shape(RECTANGLE_SHAPE);

        postConstruct();

        disabledProperty.addListener(disableListener);
    }

    /**
     * Clear all resources related to this component
     * <p>
     * Only called on UI closing.
     */
    public void dispose()
    {
        getEventDispatcher().dispatchEvent(DisposeEvent.TYPE, new DisposeEvent(this));

        if (getScissorBox() != null)
            getScissorBox().dispose();

        transform().children().forEach(childTransform -> childTransform.element().dispose());
    }

    public boolean isRenderDirty()
    {
        return isRenderDirty;
    }

    public void markRenderDirty()
    {
        isRenderDirty = true;
    }

    /**
     * Invoked when a parent GuiElement alter the render of its child.
     * For example a ScrollPane moving its children will alter them
     * They would not be notified by the standard events since their absolute position does not change.
     *
     * @return are one or all of the children of this parent dirty
     */
    public boolean isChildRenderDirty()
    {
        return isChildRenderDirty;
    }

    public void markChildRenderDirty()
    {
        isChildRenderDirty = true;
    }

    public final void renderNode(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (!isVisible())
            return;

        BrokkGuiPlatform.getInstance().getProfiler().preElementRender(this);

        if (pass == RenderPass.BACKGROUND)
            updateComponents.forEach(UpdateComponent::update);

        boolean createdMatrix = false;
        if (transform().rotation() != Rotation.NONE && transform().rotation().getAngle() % 360 != 0)
        {
            createdMatrix = true;
            renderer.beginMatrix();

            float translateX, translateY;

            if (transform().rotation().getOrigin().isRelativePos())
            {
                translateX = transform().rotation().getOrigin().getOriginX() * transform().width();
                translateY = transform().rotation().getOrigin().getOriginY() * transform().height();
            }
            else
            {
                translateX = transform().rotation().getOrigin().getOriginX();
                translateY = transform().rotation().getOrigin().getOriginY();
            }

            translateX += getLeftPos();
            translateY += getTopPos();

            renderer.translateMatrix(translateX, translateY, 0);
            renderer.rotateMatrix(transform().rotation().getAngle(), 0, 0, 1);
            renderer.translateMatrix(-translateX, -translateY, 0);
        }

        if (transform().scaleProperty().isPresent())
        {
            createdMatrix = transform().scale().apply(renderer, this, createdMatrix);
        }

        if (getOpacity() != 1)
            renderer.startAlphaMask(getOpacity());

        boolean appliedScissor = getScissorBox() != null && getScissorBox().setupAndApply(renderer, pass);

        renderContent(renderer, pass, mouseX, mouseY);

        if (appliedScissor)
            getScissorBox().end(renderer);

        if (getOpacity() != 1)
            renderer.closeAlphaMask();

        if (createdMatrix)
            renderer.endMatrix();

        BrokkGuiPlatform.getInstance().getProfiler().postElementRender(this);

        isRenderDirty = false;
        isChildRenderDirty = false;
    }

    protected void renderContent(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        for (RenderComponent component : renderComponents)
            component.renderContent(renderer, pass, mouseX, mouseY);

        for (Transform child : transform().childrenProperty())
        {
            child.element().renderNode(renderer, pass, mouseX, mouseY);
        }
    }

    public void handleHover(int mouseX, int mouseY, boolean hovered)
    {
        if (!isVisible() || isDisabled())
            return;

        if (hovered && !isHovered())
            setHovered(true);
        else if (!hovered && isHovered())
            setHovered(false);

        if (hovered)
            transform.childrenProperty().getValue().forEach(child ->
                    child.element().handleHover(mouseX, mouseY, child.isPointInside(mouseX, mouseY)));
        else
            transform.childrenProperty().getValue().forEach(child ->
                    child.element().handleHover(mouseX, mouseY, false));
    }

    public void handleMouseScroll(int mouseX, int mouseY, double scroll)
    {
        if (scroll != 0)
            getEventDispatcher().dispatchEvent(GuiMouseEvent.WHEEL,
                    new GuiMouseEvent.Wheel(this, mouseX, mouseY, (int) scroll));

        transform.childrenProperty().getValue().stream().peek(child ->
        {
            if (child == null)
                ExceptionTranslator.createNullChildInElement(this);
        }).filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.element().handleMouseScroll(mouseX, mouseY, scroll));
    }

    public void handleClick(int mouseX, int mouseY, MouseInputCode key)
    {
        if (isDisabled() || !isVisible())
            return;
        switch (key)
        {
            case MOUSE_LEFT:
                getEventDispatcher().dispatchEvent(ClickEvent.Left.TYPE,
                        new ClickEvent.Left(this, mouseX, mouseY));
                break;
            case MOUSE_RIGHT:
                getEventDispatcher().dispatchEvent(ClickEvent.Right.TYPE,
                        new ClickEvent.Right(this, mouseX, mouseY));
                break;
            case MOUSE_BUTTON_MIDDLE:
                getEventDispatcher().dispatchEvent(ClickEvent.Middle.TYPE,
                        new ClickEvent.Middle(this, mouseX, mouseY));
                break;
            default:
                getEventDispatcher().dispatchEvent(ClickEvent.TYPE, new ClickEvent(this, mouseX, mouseY, key));
                break;
        }
        setFocused();

        transform.childrenProperty().getValue().stream().peek(child ->
        {
            if (child == null)
                ExceptionTranslator.createNullChildInElement(this);
        }).filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.element().handleClick(mouseX, mouseY, key));
    }

    public void handleClickDrag(int mouseX, int mouseY, MouseInputCode key, int originalMouseX, int originalMouseY)
    {
        if (isDisabled() || !isVisible())
            return;

        if (!isDragged())
        {
            draggedProperty().setValue(true);
            getEventDispatcher().dispatchEvent(GuiMouseEvent.DRAG_START,
                    new GuiMouseEvent.DragStart(this, mouseX, mouseY, key));
        }
        draggedX = mouseX - originalMouseX;
        draggedY = mouseY - originalMouseY;


        getEventDispatcher().dispatchEvent(GuiMouseEvent.DRAGGING,
                new GuiMouseEvent.Dragging(this, mouseX, mouseY, key, draggedX, draggedY));

        transform.childrenProperty().getValue().stream()
                .filter(child -> child.isPointInside(originalMouseX, originalMouseY))
                .forEach(child -> child.element().handleClickDrag(mouseX, mouseY, key, originalMouseX,
                        originalMouseY));
    }

    public void handleClickStop(int mouseX, int mouseY, MouseInputCode key, int originalMouseX, int originalMouseY)
    {
        if (isDisabled() || !isVisible())
            return;

        if (isDragged())
        {
            draggedProperty().setValue(false);
            getEventDispatcher().dispatchEvent(GuiMouseEvent.DRAG_STOP,
                    new GuiMouseEvent.DragStop(this, mouseX, mouseY, key, draggedX, draggedY));
            draggedX = 0;
            draggedY = 0;
        }

        transform.childrenProperty().getValue().stream()
                .filter(child -> child.isPointInside(originalMouseX, originalMouseY))
                .forEach(child -> child.element().handleClickStop(mouseX, mouseY, key, originalMouseX,
                        originalMouseY));
    }

    public void handleKeyInput(char c, int key)
    {
        getEventDispatcher().dispatchEvent(KeyEvent.INPUT, new KeyEvent.Input(this, c, key));
    }

    public void handleKeyPress(int mouseX, int mouseY, int key)
    {
        getEventDispatcher().dispatchEvent(KeyEvent.PRESS, new KeyEvent.Press(this, key));

        transform.childrenProperty().getValue().stream().peek(child ->
        {
            if (child == null)
                ExceptionTranslator.createNullChildInElement(this);
        }).filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.element().handleKeyPress(mouseX, mouseY, key));
    }

    public void handleKeyRelease(int mouseX, int mouseY, int key)
    {
        getEventDispatcher().dispatchEvent(KeyEvent.RELEASE, new KeyEvent.Release(this, key));

        transform.childrenProperty().getValue().stream().peek(child ->
        {
            if (child == null)
                ExceptionTranslator.createNullChildInElement(this);
        }).filter(child -> child.isPointInside(mouseX, mouseY))
                .forEach(child -> child.element().handleKeyRelease(mouseX, mouseY, key));
    }

    @Deprecated
    public boolean isPointInside(float pointX, float pointY)
    {
        return transform().isPointInside(pointX, pointY);
    }

    /**
     * Method computing the left-pos of this Node box (pos.x + translate.x)
     *
     * @return the computed non-cached value
     */
    @Deprecated
    public float getLeftPos()
    {
        return transform().leftPos();
    }

    /**
     * Method computing the top-pos of this Node box (pos.y + translate.y)
     *
     * @return the computed non-cached value
     */
    @Deprecated
    public float getTopPos()
    {
        return transform().topPos();
    }

    /**
     * Method computing the right-pos of this Node box (pos.x + translate.x + width)
     *
     * @return the computed non-cached value
     */
    @Deprecated
    public float getRightPos()
    {
        return transform().rightPos();
    }

    /**
     * Method computing the bottom-pos of this Node box (pos.y + translate.y + height)
     *
     * @return the computed non-cached value
     */
    @Deprecated
    public float getBottomPos()
    {
        return transform().bottomPos();
    }

    public float xPos()
    {
        return transform().xPos();
    }

    public float yPos()
    {
        return transform().yPos();
    }

    public float xTranslate()
    {
        return transform().xTranslate();
    }

    public void xTranslate(float xTranslate)
    {
        transform().xTranslate(xTranslate);
    }

    public float yTranslate()
    {
        return transform().yTranslate();
    }

    public void yTranslate(float yTranslate)
    {
        transform().yTranslate(yTranslate);
    }

    public void translate(float xTranslate, float yTranslate)
    {
        transform().translate(xTranslate, yTranslate);
    }

    public float width()
    {
        return transform().width();
    }

    public void width(float width)
    {
        transform().width(width);
    }

    public float height()
    {
        return transform().height();
    }

    public void height(float height)
    {
        transform().height(height);
    }

    public void size(float width, float height)
    {
        transform().size(width, height);
    }

    public Property<Boolean> focusedProperty()
    {
        return focusedProperty;
    }

    public Property<Boolean> disabledProperty()
    {
        return disabledProperty;
    }

    public Property<Boolean> hoveredProperty()
    {
        return hoveredProperty;
    }

    public Property<Boolean> focusableProperty()
    {
        return focusableProperty;
    }

    public Property<Boolean> visibleProperty()
    {
        return visibleProperty;
    }

    public Property<Boolean> draggedProperty()
    {
        return draggedProperty;
    }

    public boolean isFocused()
    {
        return focusedProperty().getValue();
    }

    public void setFocused()
    {
        if (!isDisabled() && isFocusable())
            GuiFocusManager.instance().requestFocus(this);
    }

    public void internalSetFocused(boolean focused)
    {
        focusedProperty().setValue(focused);
        getEventDispatcher().dispatchEvent(FocusEvent.TYPE, new FocusEvent(this, focused));
    }

    public boolean isFocusable()
    {
        return focusableProperty().getValue();
    }

    public void setFocusable(boolean focusable)
    {
        focusableProperty().setValue(focusable);
    }

    public boolean isDisabled()
    {
        return disabledProperty().getValue();
    }

    public void setDisabled(boolean disable)
    {
        disabledProperty().setValue(disable);
        getEventDispatcher().dispatchEvent(DisableEvent.TYPE, new DisableEvent(this, disable));
    }

    public boolean isHovered()
    {
        return hoveredProperty().getValue();
    }

    public void setHovered(boolean hovered)
    {
        if (isDisabled() && hovered)
            return;
        hoveredProperty().setValue(hovered);
        getEventDispatcher().dispatchEvent(HoverEvent.TYPE, new HoverEvent(this, hovered));
    }

    public boolean isVisible()
    {
        return visibleProperty().getValue();
    }

    public void setVisible(boolean visible)
    {
        visibleProperty().setValue(visible);
    }

    public boolean isDragged()
    {
        return draggedProperty().getValue();
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
        if (this.window != window)
        {
            if (window != null)
                window.dispatchEvent(LayoutEvent.ADD, new LayoutEvent.Add(this));
            else
                this.window.dispatchEvent(LayoutEvent.REMOVE, new LayoutEvent.Remove(this));
        }

        this.window = window;

        transform().children().forEach(child -> child.element().setWindow(getWindow()));
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
        return transform;
    }

    public Paint paint()
    {
        return paint;
    }

    public final <T extends GuiComponent> T provide(T component)
    {
        return (T) add(ComponentEngine.instance().override(this, component));
    }

    public final <T extends GuiComponent> T provide(Class<T> componentClass)
    {
        return (T) add(ComponentEngine.instance().override(this, componentClass));
    }

    public final <T extends GuiComponent> T add(T component)
    {
        Objects.requireNonNull(component);

        componentMap.put(component.getClass(), component);
        component.attach(this);

        if (component instanceof RenderComponent)
            renderComponents.add((RenderComponent) component);
        if (component instanceof UpdateComponent)
            updateComponents.add((UpdateComponent) component);

        getEventDispatcher().dispatchEvent(ComponentEvent.ADDED, new ComponentEvent.Added(this, component));
        return component;
    }

    public final <T extends GuiComponent> T add(Class<T> componentClass)
    {
        T instance = null;
        try
        {
            instance = componentClass.newInstance();
            componentMap.put(componentClass, instance);
            instance.attach(this);
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        if (instance instanceof RenderComponent)
            renderComponents.add((RenderComponent) instance);
        if (instance instanceof UpdateComponent)
            updateComponents.add((UpdateComponent) instance);

        getEventDispatcher().dispatchEvent(ComponentEvent.ADDED, new ComponentEvent.Added(this, instance));
        return instance;
    }

    public <T extends GuiComponent> T remove(Class<T> componentClass)
    {
        GuiComponent component = componentMap.remove(componentClass);

        if (component instanceof RenderComponent)
            renderComponents.remove(component);
        if (component instanceof UpdateComponent)
            updateComponents.remove(component);

        getEventDispatcher().dispatchEvent(ComponentEvent.REMOVED, new ComponentEvent.Removed(this, component));
        return (T) component;
    }

    public <T extends GuiComponent> T get(Class<T> componentClass)
    {
        return (T) componentMap.getOrDefault(componentClass,
                componentMap.keySet().stream()
                        .filter(componentClass::isAssignableFrom)
                        .findFirst().map(componentMap::get).orElse(null));
    }

    public <T extends GuiComponent> Collection<T> getAll(Class<T> componentClass)
    {
        return componentMap.keySet().stream().filter(componentClass::isAssignableFrom)
                .map(key -> (T) componentMap.get(key)).collect(Collectors.toSet());
    }

    public <T extends GuiComponent> boolean has(Class<T> componentClass)
    {
        return componentMap.containsKey(componentClass) ||
                componentMap.keySet().stream()
                        .anyMatch(componentClass::isAssignableFrom);
    }

    public boolean has(GuiComponent component)
    {
        return componentMap.containsValue(component);
    }

    public <T extends GuiComponent> boolean ifHas(Class<T> componentClass, Consumer<T> action)
    {
        if (!has(componentClass))
            return false;
        action.accept(get(componentClass));
        return true;
    }

    public abstract String type();

    public Property<String> idProperty()
    {
        return idProperty;
    }

    public String id()
    {
        return idProperty().getValue();
    }

    public void id(String id)
    {
        idProperty().setValue(id);
    }

    /////////////
    // DISPLAY //
    /////////////

    public Property<Double> getOpacityProperty()
    {
        return opacityProperty;
    }

    public void replaceOpacityProperty(Property<Double> opacityProperty)
    {
        this.opacityProperty = opacityProperty;
    }

    public double getOpacity()
    {
        return getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        getOpacityProperty().setValue(opacity);
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnFocusEvent(EventHandler<FocusEvent> onFocusEvent)
    {
        getEventDispatcher().removeHandler(FocusEvent.TYPE, this.onFocusEvent);
        this.onFocusEvent = onFocusEvent;
        getEventDispatcher().addHandler(FocusEvent.TYPE, this.onFocusEvent);
    }

    public void setOnDisableEvent(EventHandler<DisableEvent> onDisableEvent)
    {
        getEventDispatcher().removeHandler(DisableEvent.TYPE, this.onDisableEvent);
        this.onDisableEvent = onDisableEvent;
        getEventDispatcher().addHandler(DisableEvent.TYPE, this.onDisableEvent);
    }

    public void setOnHoverEvent(EventHandler<HoverEvent> onHoverEvent)
    {
        getEventDispatcher().removeHandler(HoverEvent.TYPE, this.onHoverEvent);
        this.onHoverEvent = onHoverEvent;
        getEventDispatcher().addHandler(HoverEvent.TYPE, this.onHoverEvent);
    }

    public void setOnClickEvent(EventHandler<ClickEvent> onClickEvent)
    {
        getEventDispatcher().removeHandler(ClickEvent.TYPE, this.onClickEvent);
        this.onClickEvent = onClickEvent;
        getEventDispatcher().addHandler(ClickEvent.TYPE, this.onClickEvent);
    }

    public EventHandler<ClickEvent> getOnClickEvent()
    {
        return onClickEvent;
    }

    public EventHandler<FocusEvent> getOnFocusEvent()
    {
        return onFocusEvent;
    }

    public EventHandler<DisableEvent> getOnDisableEvent()
    {
        return onDisableEvent;
    }

    public EventHandler<HoverEvent> getOnHoverEvent()
    {
        return onHoverEvent;
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        if (eventDispatcher == null)
            initEventDispatcher();
        return eventDispatcher;
    }

    private void initEventDispatcher()
    {
        eventDispatcher = new EventDispatcher();
    }

    private void disableListener(Observable obs)
    {
        if (isHovered())
            setHovered(false);
        if (isFocused())
            GuiFocusManager.instance().requestFocus(null);
    }
}
