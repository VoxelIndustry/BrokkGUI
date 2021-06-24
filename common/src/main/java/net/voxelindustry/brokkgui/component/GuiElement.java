package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.ComponentEvent;
import net.voxelindustry.brokkgui.event.DisableEvent;
import net.voxelindustry.brokkgui.event.EventQueueBuilder;
import net.voxelindustry.brokkgui.event.FocusEvent;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.GuiMouseEvent.DragStart;
import net.voxelindustry.brokkgui.event.GuiMouseEvent.DragStop;
import net.voxelindustry.brokkgui.event.HoverEvent;
import net.voxelindustry.brokkgui.event.LayoutEvent;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.window.IGuiSubWindow;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static net.voxelindustry.brokkgui.shape.RectangleShape.RECTANGLE_SHAPE;

public abstract class GuiElement implements IEventEmitter, ComponentHolder
{
    private final Map<Class<? extends GuiComponent>, GuiComponent> componentMap;

    private final List<RenderComponent> renderComponents;
    private final List<UpdateComponent> updateComponents;

    private final Transform transform;

    private final Paint paint;

    private final Property<String> idProperty;

    private EventDispatcher               eventDispatcher;
    private EventHandler<FocusEvent>      onFocusEvent;
    private EventHandler<DisableEvent>    onDisableEvent;
    private EventHandler<HoverEvent>      onHoverEvent;
    private EventHandler<ClickPressEvent> onClickEvent;
    private EventHandler<ScrollEvent>     onScrollEvent;

    private Property<Double> opacityProperty;

    private final Property<Boolean> focusedProperty;
    private final Property<Boolean> disabledProperty;
    private final Property<Boolean> hoveredProperty;
    private final Property<Boolean> focusableProperty;
    private final Property<Boolean> draggedProperty;

    private Property<IGuiSubWindow> windowProperty = new Property<>();

    private boolean isRenderDirty      = true;
    private boolean isChildRenderDirty = true;

    public GuiElement()
    {
        componentMap = new IdentityHashMap<>();
        renderComponents = new ArrayList<>(1);
        updateComponents = new ArrayList<>(1);
        idProperty = new Property<>(null);

        focusedProperty = new Property<>(false);
        disabledProperty = new Property<>(false);
        hoveredProperty = new Property<>(false);

        focusableProperty = new Property<>(false);
        draggedProperty = new Property<>(false);

        opacityProperty = new Property<>(1D);

        transform = provide(Transform.class);
        ComponentEngine.instance().inject(this);

        paint = provide(Paint.class);
        paint.shape(RECTANGLE_SHAPE);

        postConstruct();

        ValueInvalidationListener disableListener = this::disableListener;
        disabledProperty.addListener(disableListener);

        getEventDispatcher().addHandler(ClickPressEvent.TYPE, this::handleClickStart);

        getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, this::handleDragStart);
        getEventDispatcher().addHandler(GuiMouseEvent.DRAG_STOP, this::handleDragStop);
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

    public final void renderNode(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {
        if (!isVisible())
            return;

        BrokkGuiPlatform.getInstance().getProfiler().preElementRender(this);

        boolean appliedMask = false;
        // -1 is the default value for an element with overflow=visible and no parent or a complete hierarchy of parents with overflow=visible.
        if (transform().clipBoxLeft() != -1)
        {
            renderer.pushMask(
                    transform().clipBoxLeft(),
                    transform().clipBoxTop(),
                    transform().clipBoxRight(),
                    transform().clipBoxBottom());
            appliedMask = true;
        }

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

        renderContent(renderer, mouseX, mouseY);

        if (getOpacity() != 1)
            renderer.closeAlphaMask();

        if (createdMatrix)
            renderer.endMatrix();

        if (appliedMask)
            renderer.popMask();

        BrokkGuiPlatform.getInstance().getProfiler().postElementRender(this);

        isRenderDirty = false;
        isChildRenderDirty = false;
    }

    protected void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {
        for (RenderComponent component : renderComponents)
            component.renderContent(renderer, mouseX, mouseY);
    }

    public void handleHover(float mouseX, float mouseY, boolean hovered)
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

    private void handleClickStart(ClickPressEvent event)
    {
        if (setFocused())
            event.consume();
    }

    public void handleDragStart(DragStart event)
    {
        draggedProperty().setValue(true);
        GuiFocusManager.instance.draggedNode(this);
    }

    public void handleDragStop(DragStop event)
    {
        draggedProperty().setValue(false);
        GuiFocusManager.instance.removeDraggedNode(this);
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

    public Property<Boolean> draggedProperty()
    {
        return draggedProperty;
    }

    public boolean isFocused()
    {
        return focusedProperty().getValue();
    }

    public boolean setFocused()
    {
        if (!isDisabled() && isFocusable())
            GuiFocusManager.instance.requestFocus(this, window());
        return GuiFocusManager.instance.focusedNode() == this;
    }

    public void internalSetFocused(boolean focused)
    {
        focusedProperty().setValue(focused);
        EventQueueBuilder.fromTarget(this).dispatch(FocusEvent.TYPE, new FocusEvent(this, focused));
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
        EventQueueBuilder.fromTarget(this).dispatch(DisableEvent.TYPE, new DisableEvent(this, disable));
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
        EventQueueBuilder.fromTarget(this).dispatch(HoverEvent.TYPE, new HoverEvent(this, hovered));
    }

    public boolean isVisible()
    {
        return transform().isVisible();
    }

    public void setVisible(boolean visible)
    {
        transform().visible(visible);
    }

    public boolean isDragged()
    {
        return draggedProperty().getValue();
    }

    public Property<IGuiSubWindow> windowProperty()
    {
        return windowProperty;
    }

    public IGuiSubWindow window()
    {
        return windowProperty().getValue();
    }

    public void window(IGuiSubWindow window)
    {
        if (window() != window)
        {
            if (window != null)
                window.dispatchEvent(LayoutEvent.ADD, new LayoutEvent.Add(this));
            else
                window().dispatchEvent(LayoutEvent.REMOVE, new LayoutEvent.Remove(this));
        }

        windowProperty().setValue(window);

        transform().children().forEach(child -> child.element().window(window()));
    }

    protected <T> Property<T> createRenderProperty(T initialValue)
    {
        Property<T> property = new Property<>(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected <T> Property<T> createRenderPropertyPropagateChildren(T initialValue)
    {
        Property<T> property = new Property<>(initialValue);
        property.addListener(this::onRenderPropertyPropagatedChange);
        return property;
    }

    private void onRenderPropertyChange(Observable observable)
    {
        markRenderDirty();
    }

    private void onRenderPropertyPropagatedChange(Observable observable)
    {
        markRenderDirty();
        markChildRenderDirty();
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

        getEventDispatcher().singletonQueue().dispatch(ComponentEvent.ADDED, new ComponentEvent.Added(this, component));
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

        getEventDispatcher().singletonQueue().dispatch(ComponentEvent.ADDED, new ComponentEvent.Added(this, instance));
        return instance;
    }

    @Override
    public <T extends GuiComponent> T remove(Class<T> componentClass)
    {
        GuiComponent component = componentMap.remove(componentClass);
        component.detach(this);

        if (component instanceof RenderComponent)
            renderComponents.remove(component);
        if (component instanceof UpdateComponent)
            updateComponents.remove(component);

        getEventDispatcher().singletonQueue().dispatch(ComponentEvent.REMOVED, new ComponentEvent.Removed(this, component));
        return (T) component;
    }

    @Override
    public <T extends GuiComponent> T get(Class<T> componentClass)
    {
        for (Class<? extends GuiComponent> aClass : componentMap.keySet())
        {
            if (componentClass.isAssignableFrom(aClass))
            {
                return (T) componentMap.getOrDefault(componentClass,
                        Optional.<Class<? extends GuiComponent>>of(aClass).map(componentMap::get).orElse(null));
            }
        }
        return (T) componentMap.getOrDefault(componentClass,
                Optional.<Class<? extends GuiComponent>>empty().map(componentMap::get).orElse(null));
    }

    @Override
    public <T extends GuiComponent> Collection<T> getAll(Class<T> componentClass)
    {
        Set<T> set = new HashSet<>();
        for (Class<? extends GuiComponent> key : componentMap.keySet())
        {
            if (componentClass.isAssignableFrom(key))
            {
                T t = (T) componentMap.get(key);
                set.add(t);
            }
        }
        return set;
    }

    @Override
    public <T extends GuiComponent> boolean has(Class<T> componentClass)
    {
        if (componentMap.containsKey(componentClass)) return true;
        for (Class<? extends GuiComponent> aClass : componentMap.keySet())
        {
            if (componentClass.isAssignableFrom(aClass))
                return true;
        }
        return false;
    }

    @Override
    public boolean has(GuiComponent component)
    {
        return componentMap.containsValue(component);
    }

    @Override
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

    //////////////
    // CHILDREN //
    //////////////

    /**
     * @return an immutable list
     */
    public List<Transform> getChildren()
    {
        return transform().childrenProperty().getValue();
    }

    public void removeChild(GuiElement node)
    {
        transform().removeChild(node.transform());
    }

    public void clearChildren()
    {
        transform().clearChildren();
    }

    public void removeChildrenOfType(Class<?> typeClass)
    {
        transform().streamChildren()
                .filter(childTransform -> typeClass.isInstance(childTransform.element()))
                .collect(toList())
                .forEach(transform -> transform().removeChild(transform));
    }

    public boolean hasChild(GuiElement node)
    {
        return transform().hasChild(node.transform());
    }

    public List<GuiElement> getNodesAtPoint(float pointX, float pointY, boolean searchChildren)
    {
        return streamNodesAtPoint(pointX, pointY, searchChildren).collect(toList());
    }

    public Stream<GuiElement> streamNodesAtPoint(float pointX, float pointY, boolean searchChildren)
    {
        Stream<GuiElement> filteredChildren = transform().streamChildren()
                .filter(child -> child.isPointInside(pointX, pointY)).map(GuiComponent::element);

        if (searchChildren)
            filteredChildren = filteredChildren.flatMap(child ->
                    Stream.concat(Stream.of(child), streamNodesAtPoint(pointX, pointY, true)));

        return filteredChildren;
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

    public void setOnClickEvent(EventHandler<ClickPressEvent> onClickEvent)
    {
        getEventDispatcher().removeHandler(ClickPressEvent.TYPE, this.onClickEvent);
        this.onClickEvent = onClickEvent;
        getEventDispatcher().addHandler(ClickPressEvent.TYPE, this.onClickEvent);
    }

    public void setOnScrollEvent(EventHandler<ScrollEvent> onScrollEvent)
    {
        getEventDispatcher().removeHandler(ScrollEvent.TYPE, this.onScrollEvent);
        this.onScrollEvent = onScrollEvent;
        getEventDispatcher().addHandler(ScrollEvent.TYPE, this.onScrollEvent);
    }

    public EventHandler<ClickPressEvent> getOnClickEvent()
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

    public EventHandler<ScrollEvent> getOnScrollEvent()
    {
        return onScrollEvent;
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
        eventDispatcher = new EventDispatcher(this);
    }

    private void disableListener(Observable obs)
    {
        if (isHovered())
            setHovered(false);
        if (isFocused())
            GuiFocusManager.instance.removeFocusedNode(this, window());
    }

    //////////////////
    //     STATIC   //
    //////////////////

    public static GuiElement createInline(String type, GuiComponent... components)
    {
        return new GuiElement()
        {
            @Override
            public void postConstruct()
            {
                super.postConstruct();

                for (GuiComponent component : components)
                    add(component);
            }

            @Override
            public String type()
            {
                return type;
            }
        };
    }

    public static GuiElement createInline(String type, Class<? extends GuiComponent>... components)
    {
        return new GuiElement()
        {
            @Override
            public void postConstruct()
            {
                super.postConstruct();

                for (Class<? extends GuiComponent> component : components)
                    add(component);
            }

            @Override
            public String type()
            {
                return type;
            }
        };
    }
}
