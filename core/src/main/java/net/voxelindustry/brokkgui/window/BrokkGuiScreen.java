package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.property.IProperty;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.debug.DebugRenderer;
import net.voxelindustry.brokkgui.element.pane.GuiPane;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.ClickReleaseEvent;
import net.voxelindustry.brokkgui.event.DisposeEvent;
import net.voxelindustry.brokkgui.event.EventQueueBuilder;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.style.IStyleRoot;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.StyleEngine;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import net.voxelindustry.brokkgui.text.TextSettings;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventQueue;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;

public class BrokkGuiScreen implements IGuiWindow, IStyleRoot, IEventEmitter
{
    private static final TextSettings DEBUG_TEXT_SETTINGS = TextSettings.build()
            .fontName("default")
            .textColor(Color.WHITE)
            .create();

    private EventDispatcher                 eventDispatcher;
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private       GuiElement              root;
    private final ArrayList<SubGuiScreen> windows = new ArrayList<>();
    private       IRenderCommandReceiver  renderer;

    private final Property<Float> widthProperty;
    private final Property<Float> heightProperty;
    private final Property<Float> xPosProperty = new Property<>(0F);
    private final Property<Float> yPosProperty = new Property<>(0F);

    private final Property<Float> xRelativePosProperty;
    private final Property<Float> yRelativePosProperty;
    private final Property<Float> xOffsetProperty = new Property<>(0F);
    private final Property<Float> yOffsetProperty = new Property<>(0F);

    private final Property<Integer> screenWidthProperty  = new Property<>(0);
    private final Property<Integer> screenHeightProperty = new Property<>(0);

    private final ListProperty<String> stylesheetsProperty = new ListProperty<>(emptyList());
    private final Property<StyleList>  styleListProperty   = new Property<>(null);

    private final List<Transform> floatingTransforms = new ArrayList<>();

    private final ListenerPool listenerPool = new ListenerPool();

    private IBrokkGuiImpl wrapper;

    private float lastClickX = -1;
    private float lastClickY = -1;

    private boolean firstDragSinceClick;

    private final PriorityQueue<Pair<Runnable, Long>> tasksQueue;

    protected boolean isDebugged;

    public BrokkGuiScreen(float xRelativePos, float yRelativePos, float width, float height)
    {
        widthProperty = new Property<>(width);
        heightProperty = new Property<>(height);

        xRelativePosProperty = new Property<>(xRelativePos);
        yRelativePosProperty = new Property<>(yRelativePos);

        Comparator<Pair<Runnable, Long>> comparator = Comparator.comparingLong(Pair::getValue);
        tasksQueue = new PriorityQueue<>(comparator.reversed());

        GuiPane root = new GuiPane();
        root.id("main-panel");
        setRoot(root);
    }

    public BrokkGuiScreen(float width, float height)
    {
        this(0.5F, 0.5F, width, height);
    }

    public BrokkGuiScreen()
    {
        this(0, 0);
    }

    @Override
    public void setWrapper(IBrokkGuiImpl wrapper)
    {
        this.wrapper = wrapper;

        renderer = wrapper.getRenderer();

        xPosProperty.bindProperty(new Expression<>(() -> wrapper.getGuiRelativePosX(xRelativePos(), getWidth()) + getxOffset(),
                screenWidthProperty(), xRelativePosProperty(), getWidthProperty(),
                getxOffsetProperty()));

        yPosProperty.bindProperty(new Expression<>(() -> wrapper.getGuiRelativePosY(yRelativePos(), getHeight()) + getyOffset(),
                yRelativePosProperty(), screenHeightProperty(), getHeightProperty(),
                getyOffsetProperty()));

        stylesheetsProperty.addListener(obs ->
        {
            StylesheetManager.getInstance().refreshStylesheets(this);
            if (root() != null)
                StyleEngine.refreshHierarchy(root().transform());
        });

        StylesheetManager.getInstance().refreshStylesheets(this);
        if (root() != null)
            StyleEngine.refreshHierarchy(root().transform());
    }

    @Override
    public void onMouseMoved(float mouseX, float mouseY)
    {
        if (!windows.isEmpty() && windows.stream().anyMatch(gui -> gui.isPointInside(mouseX, mouseY)))
        {
            windows.forEach(gui -> gui.handleHover(mouseX, mouseY, gui.isPointInside(mouseX, mouseY)));
            root.handleHover(mouseX, mouseY, false);
        }
        else
        {
            windows.forEach(gui -> gui.handleHover(mouseX, mouseY, false));
            root.handleHover(mouseX, mouseY, root.isPointInside(mouseX, mouseY));
        }

        floatingTransforms.forEach(transform ->
                transform.element().handleHover(mouseX, mouseY, transform.isPointInside(mouseX, mouseY)));
    }

    @Override
    public void render(float mouseX, float mouseY, RenderTarget target)
    {
        switch (target)
        {
            case MAIN:
                root.renderNode(renderer, mouseX, mouseY);
                break;
            case WINDOW:
                if (!windows.isEmpty())
                    for (int i = windows.size() - 1; i >= 0; i--)
                    {
                        if (windows.get(i).hasWarFog())
                            renderer.drawColoredRect(0, 0, getWidth(),
                                    getHeight(), 5 + i, Color.BLACK.addAlpha(-0.5F), RenderPass.BACKGROUND);

                        windows.get(i).renderNode(renderer, mouseX, mouseY);
                    }
                break;
        }
    }

    /**
     * Called after every render passes and render targets have been made. Used for debug rendering.
     *
     * @param mouseX current x position of the mouse
     * @param mouseY current y position of the mouse
     */
    @Override
    public void renderLast(float mouseX, float mouseY)
    {
        if (BrokkGuiPlatform.getInstance().isRenderDebugEnabled() && !isDebugged)
        {
            renderer.drawColoredEmptyRect(1, 1,
                    BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth("DEBUG", DEBUG_TEXT_SETTINGS) + 2,
                    BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(DEBUG_TEXT_SETTINGS) + 2,
                    400, Color.RED, 1F, RenderPass.BACKGROUND);
            renderer.drawString("DEBUG", 2, 2.5F, 400, RenderPass.BACKGROUND, DEBUG_TEXT_SETTINGS);
        }
    }

    @Override
    public boolean doesOccludePoint(float mouseX, float mouseY)
    {
        return !windows.isEmpty() && windows.stream().anyMatch(gui -> gui.isPointInside(mouseX, mouseY));
    }

    @Override
    public void initGui()
    {

    }

    @Override
    public void tick()
    {
        if (tasksQueue.isEmpty())
            return;

        long currentMillis = System.currentTimeMillis();

        while (tasksQueue.peek().getValue() <= currentMillis)
            tasksQueue.poll().getLeft().run();
    }

    @Override
    public void onClick(float mouseX, float mouseY, MouseInputCode key)
    {
        if (BrokkGuiPlatform.getInstance().isRenderDebugEnabled() && !isDebugged)
        {
            if (mouseX > 0 && mouseY > 0 && mouseX < BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth("DEBUG", DEBUG_TEXT_SETTINGS) && mouseY < BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(DEBUG_TEXT_SETTINGS))
            {
                DebugRenderer.wrap(this);
                isDebugged = true;
            }
        }

        GuiElement source = null;

        boolean clicked = false;

        if (!windows.isEmpty())
        {
            for (var window : windows)
            {
                if (window.transform().isPointInside(mouseX, mouseY))
                {
                    source = window;
                    clicked = true;
                    break;
                }
                else if (window.closeOnClick())
                    removeSubGui(window);
            }
        }

        if (!clicked && !floatingTransforms.isEmpty())
        {
            for (var transform : floatingTransforms)
            {
                if (transform.isPointInside(mouseX, mouseY) && transform.isVisible() && !transform.element().isDisabled())
                {
                    source = transform.element();
                    clicked = true;
                    break;
                }
            }
        }

        if (!clicked)
        {
            if (root.transform().isPointInside(mouseX, mouseY))
            {
                if (!root.isDisabled() && root.isVisible())
                    source = root;
            }
            else
                GuiFocusManager.instance.removeWindowFocus(this);
        }

        lastClickX = mouseX;
        lastClickY = mouseY;
        firstDragSinceClick = true;

        if (source == null)
            return;

        EventQueue eventQueue = EventQueueBuilder.allChildrenMatching(source,
                EventQueueBuilder.isPointInside(mouseX, mouseY)
                        .and(EventQueueBuilder.isEnabled)
                        .and(EventQueueBuilder.isVisible));

        switch (key)
        {
            case MOUSE_LEFT -> eventQueue.dispatch(ClickPressEvent.Left.TYPE, new ClickPressEvent.Left(source, mouseX, mouseY));
            case MOUSE_RIGHT -> eventQueue.dispatch(ClickPressEvent.Right.TYPE, new ClickPressEvent.Right(source, mouseX, mouseY));
            case MOUSE_BUTTON_MIDDLE -> eventQueue.dispatch(ClickPressEvent.Middle.TYPE, new ClickPressEvent.Middle(source, mouseX, mouseY));
            default -> eventQueue.dispatch(ClickPressEvent.TYPE, new ClickPressEvent(source, mouseX, mouseY, key));
        }
    }

    @Override
    public void onClickDrag(float mouseX, float mouseY, MouseInputCode key)
    {
        if (firstDragSinceClick)
        {
            GuiElement source = getNodeUnderMouse(mouseX, mouseY);

            if (source != null)
            {
                EventQueue eventQueue = EventQueueBuilder.allChildrenMatching(source,
                        EventQueueBuilder.isPointInside(mouseX, mouseY)
                                .and(EventQueueBuilder.isEnabled)
                                .and(EventQueueBuilder.isVisible));

                eventQueue.dispatch(GuiMouseEvent.DRAG_START, new GuiMouseEvent.DragStart(source, mouseX, mouseY, key));
            }
            firstDragSinceClick = false;
        }

        for (GuiElement element : GuiFocusManager.instance.draggedNodes())
        {
            element.getEventDispatcher().singletonQueue()
                    .dispatch(GuiMouseEvent.DRAGGING, new GuiMouseEvent.Dragging(element, mouseX, mouseY, key, mouseX - lastClickX, mouseY - lastClickY));
        }
    }

    @Override
    public void onClickStop(float mouseX, float mouseY, MouseInputCode key)
    {
        GuiElement source = getNodeUnderMouse(mouseX, mouseY);

        if (source != null)
        {
            EventQueue eventQueue = EventQueueBuilder.allChildrenMatching(source,
                    EventQueueBuilder.isPointInside(mouseX, mouseY)
                            .and(EventQueueBuilder.isEnabled)
                            .and(EventQueueBuilder.isVisible));

            switch (key)
            {
                case MOUSE_LEFT -> eventQueue.dispatch(ClickReleaseEvent.Left.TYPE, new ClickReleaseEvent.Left(source, mouseX, mouseY));
                case MOUSE_RIGHT -> eventQueue.dispatch(ClickReleaseEvent.Right.TYPE, new ClickReleaseEvent.Right(source, mouseX, mouseY));
                case MOUSE_BUTTON_MIDDLE -> eventQueue.dispatch(ClickReleaseEvent.Middle.TYPE, new ClickReleaseEvent.Middle(source, mouseX, mouseY));
                default -> eventQueue.dispatch(ClickReleaseEvent.TYPE, new ClickReleaseEvent(source, mouseX, mouseY, key));
            }
        }

        GuiElement[] draggedNodesArray = GuiFocusManager.instance.draggedNodes().toArray(new GuiElement[0]);
        for (GuiElement element : draggedNodesArray)
        {
            element.getEventDispatcher().singletonQueue()
                    .dispatch(GuiMouseEvent.DRAG_STOP, new GuiMouseEvent.DragStop(element, mouseX, mouseY, key, mouseX - lastClickX, mouseY - lastClickY));
        }

        lastClickX = -1;
        lastClickY = -1;
    }

    @Override
    public void onScroll(float mouseX, float mouseY, double xOffset, double yOffset)
    {
        GuiElement hovered = getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null && (xOffset != 0 || yOffset != 0))
        {
            EventQueueBuilder.allChildrenMatching(hovered, EventQueueBuilder.isPointInside(mouseX, mouseY))
                    .dispatch(ScrollEvent.TYPE, new ScrollEvent(hovered, mouseX, mouseY, (float) xOffset, (float) yOffset));
        }
    }

    @Override
    public void onTextTyped(String text)
    {
        GuiElement focusedNode = GuiFocusManager.instance.focusedNode();
        if (focusedNode != null && GuiFocusManager.instance.focusedWindow() == this)
            EventQueueBuilder.fromTarget(focusedNode).dispatch(KeyEvent.TEXT_TYPED, new KeyEvent.TextTyped(focusedNode, text));
    }

    @Override
    public void onKeyPressed(int key)
    {
        float mouseX = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseX(this);
        float mouseY = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseY(this);

        GuiElement focusedNode = GuiFocusManager.instance.focusedNode();
        if (focusedNode != null && GuiFocusManager.instance.focusedWindow() == this)
        {
            EventQueueBuilder.fromTarget(focusedNode).dispatch(KeyEvent.PRESS, new KeyEvent.Press(focusedNode, key));
            return;
        }

        GuiElement hovered = getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
        {
            EventQueueBuilder.fromTarget(hovered).dispatch(KeyEvent.PRESS, new KeyEvent.Press(hovered, key));
        }
    }

    @Override
    public void onKeyReleased(int key)
    {
        float mouseX = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseX(this);
        float mouseY = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseY(this);

        GuiElement focusedNode = GuiFocusManager.instance.focusedNode();
        if (focusedNode != null && GuiFocusManager.instance.focusedWindow() == this)
        {
            EventQueueBuilder.fromTarget(focusedNode).dispatch(KeyEvent.RELEASE, new KeyEvent.Release(focusedNode, key));
            return;
        }

        GuiElement hovered = getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
        {
            EventQueueBuilder.fromTarget(hovered).dispatch(KeyEvent.RELEASE, new KeyEvent.Release(hovered, key));
        }
    }


    public void addSubGui(SubGuiScreen subGui)
    {
        windows.add(0, subGui);

        subGui.transform().xPosProperty().bindProperty(new Expression<>(() -> getScreenWidth() / (1 / subGui.xRelativePos())
                - subGui.getWidth() / 2, subGui.xRelativePosProperty(), screenWidthProperty(),
                subGui.transform().widthProperty()));

        subGui.transform().yPosProperty().bindProperty(new Expression<>(() -> getScreenHeight() / (1 / subGui.yRelativePos())
                - subGui.getHeight() / 2, subGui.yRelativePosProperty(), screenHeightProperty(),
                subGui.transform().heightProperty()));

        subGui.open();

        StyleEngine.setStyleSupplierHierarchy(subGui.transform(), getStyleListProperty()::getValue);
        if (wrapper != null)
            StyleEngine.refreshHierarchy(subGui.transform());
    }

    public void removeSubGui(SubGuiScreen subGui)
    {
        subGui.close();

        subGui.transform().xPosProperty().unbind();
        subGui.transform().yPosProperty().unbind();
        windows.remove(subGui);

        subGui.ifHas(StyleComponent.class, style -> style.setStyleSupplier(null));
    }

    public boolean hasSubGui(SubGuiScreen subGui)
    {
        return windows.contains(subGui);
    }

    public List<SubGuiScreen> getSubGuis()
    {
        return Collections.unmodifiableList(windows);
    }

    @Override
    public void open()
    {
        wrapper.askOpen();
        onOpen();
    }

    @Override
    public void onOpen()
    {
        new EventQueue().addDispatcher(getEventDispatcher()).dispatch(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        wrapper.askClose();
        onClose();
    }

    @Override
    public void onClose()
    {
        if (GuiFocusManager.instance.focusedWindow() == this)
            GuiFocusManager.instance.requestFocus(null, this);
        listenerPool.clear();

        if (root != null)
            EventQueueBuilder.allChildren(root).dispatch(DisposeEvent.TYPE, new DisposeEvent(root));
        getSubGuis().forEach(SubGuiScreen::close);

        new EventQueue().addDispatcher(getEventDispatcher()).dispatch(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

    private GuiElement getNodeUnderMouse(float mouseX, float mouseY)
    {
        if (!windows.isEmpty())
        {
            var match = windows.stream()
                    .filter(gui -> gui.transform().isPointInside(mouseX, mouseY) && !gui.isDisabled() && gui.isVisible())
                    .findFirst();

            if (match.isPresent())
                return match.get();
        }
        for (Transform transform : floatingTransforms)
        {
            if (transform.isPointInside(mouseX, mouseY) && !transform.element().isDisabled() && transform.isVisible())
                return transform.element();
        }
        if (root().transform().isPointInside(mouseX, mouseY) && !root().isDisabled() && root().isVisible())
            return root();
        return null;
    }

    public GuiElement root()
    {
        return root;
    }

    public <T extends GuiElement> T setRoot(T mainPanel)
    {
        if (root != null)
        {
            root.transform().widthProperty().unbind();
            root.transform().heightProperty().unbind();
            root.transform().xPosProperty().unbind();
            root.transform().yPosProperty().unbind();

            if (root.window() == this)
                root.window(null);
        }

        root = mainPanel;

        root.transform().widthProperty().bindProperty(widthProperty);
        root.transform().heightProperty().bindProperty(heightProperty);

        root.transform().xPosProperty().bindProperty(xPosProperty);
        root.transform().yPosProperty().bindProperty(yPosProperty);

        StyleEngine.setStyleSupplierHierarchy(mainPanel.transform(), getStyleListProperty()::getValue);
        if (wrapper != null)
            StyleEngine.refreshHierarchy(mainPanel.transform());
        root.window(this);

        return mainPanel;
    }

    public void runLater(Runnable runnable, long time, TimeUnit unit)
    {
        tasksQueue.add(Pair.of(runnable, System.currentTimeMillis() + unit.toMillis(time)));
    }

    public void runLater(Runnable runnable, int ticks)
    {
        runLater(runnable, ticks * 50L, TimeUnit.MILLISECONDS);
    }

    public Property<Float> getWidthProperty()
    {
        return widthProperty;
    }

    public Property<Float> getHeightProperty()
    {
        return heightProperty;
    }

    public Property<Float> getxPosProperty()
    {
        return xPosProperty;
    }

    public Property<Float> getyPosProperty()
    {
        return yPosProperty;
    }

    @Override
    public Property<Float> xRelativePosProperty()
    {
        return xRelativePosProperty;
    }

    @Override
    public Property<Float> yRelativePosProperty()
    {
        return yRelativePosProperty;
    }

    public Property<Float> getxOffsetProperty()
    {
        return xOffsetProperty;
    }

    public Property<Float> getyOffsetProperty()
    {
        return yOffsetProperty;
    }

    @Override
    public float getWidth()
    {
        return getWidthProperty().getValue();
    }

    @Override
    public float getHeight()
    {
        return getHeightProperty().getValue();
    }

    public void setWidth(float width)
    {
        getWidthProperty().setValue(width);
    }

    public void setHeight(float height)
    {
        getHeightProperty().setValue(height);
    }

    public void setSize(float width, float height)
    {
        setWidth(width);
        setHeight(height);
    }

    public float getxPos()
    {
        return getxPosProperty().getValue();
    }

    public void setxPos(float xPos)
    {
        getxPosProperty().setValue(xPos);
    }

    public float getyPos()
    {
        return getyPosProperty().getValue();
    }

    public void setyPos(float yPos)
    {
        getyPosProperty().setValue(yPos);
    }

    @Override
    public float xRelativePos()
    {
        return xRelativePosProperty().getValue();
    }

    public void setxRelativePos(float xRelativePos)
    {
        xRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float yRelativePos()
    {
        return yRelativePosProperty().getValue();
    }

    public void setyRelativePos(float yRelativePos)
    {
        yRelativePosProperty().setValue(yRelativePos);
    }

    public void setRelativePos(float xRelativePos, float yRelativePos)
    {
        setxRelativePos(xRelativePos);
        setyRelativePos(yRelativePos);
    }

    public float getxOffset()
    {
        return getxOffsetProperty().getValue();
    }

    public void setxOffset(float xOffset)
    {
        getxOffsetProperty().setValue(xOffset);
    }

    public float getyOffset()
    {
        return getyOffsetProperty().getValue();
    }

    public void setyOffset(float yOffset)
    {
        getyOffsetProperty().setValue(yOffset);
    }

    public void setOffset(float xOffset, float yOffset)
    {
        setxOffset(xOffset);
        setyOffset(yOffset);
    }

    @Override
    public IProperty<Integer> screenWidthProperty()
    {
        return screenWidthProperty;
    }

    @Override
    public IProperty<Integer> screenHeightProperty()
    {
        return screenHeightProperty;
    }

    @Override
    public float windowWidthRatio()
    {
        return wrapper.windowWidthRatio();
    }

    @Override
    public float windowHeightRatio()
    {
        return wrapper.windowHeightRatio();
    }

    public int getScreenWidth()
    {
        return screenWidthProperty().getValue();
    }

    public int getScreenHeight()
    {
        return screenHeightProperty().getValue();
    }

    @Override
    public void screenWidth(int width)
    {
        screenWidthProperty().setValue(width);
    }

    @Override
    public void screenHeight(int height)
    {
        screenHeightProperty().setValue(height);
    }

    public ListenerPool getListeners()
    {
        return listenerPool;
    }

    @Override
    public IBrokkGuiImpl getWrapper()
    {
        return wrapper;
    }

    @Override
    public GuiElement getRootElement()
    {
        return root();
    }

    @Override
    public void addFloating(Transform transform)
    {
        if (floatingTransforms.contains(transform))
            return;
        floatingTransforms.add(transform);
    }

    @Override
    public boolean removeFloating(Transform transform)
    {
        return floatingTransforms.remove(transform);
    }

    @Override
    public Collection<Transform> getFloatingList()
    {
        return floatingTransforms;
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnOpenEvent(EventHandler<WindowEvent.Open> onOpenEvent)
    {
        getEventDispatcher().removeHandler(WindowEvent.OPEN, this.onOpenEvent);
        this.onOpenEvent = onOpenEvent;
        getEventDispatcher().addHandler(WindowEvent.OPEN, this.onOpenEvent);
    }

    public void setOnCloseEvent(EventHandler<WindowEvent.Close> onCloseEvent)
    {
        getEventDispatcher().removeHandler(WindowEvent.CLOSE, this.onCloseEvent);
        this.onCloseEvent = onCloseEvent;
        getEventDispatcher().addHandler(WindowEvent.CLOSE, this.onCloseEvent);
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

    @Override
    public <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler)
    {
        getEventDispatcher().addHandler(type, handler);
    }

    @Override
    public <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler)
    {
        getEventDispatcher().removeHandler(type, handler);
    }

    @Override
    public <T extends HermodEvent> void dispatchEventRedirect(EventType<T> type, T event)
    {
        getEventDispatcher().singletonQueue().dispatch(type, (T) event.copy(this));
    }

    @Override
    public <T extends HermodEvent> void dispatchEvent(EventType<T> type, T event)
    {
        getEventDispatcher().singletonQueue().dispatch(type, event);
    }

    /////////////////////
    //     STYLING     //
    /////////////////////

    @Override
    public List<String> getStylesheets()
    {
        return stylesheetsProperty.getModifiableValue();
    }

    public ListProperty<String> getStylesheetsProperty()
    {
        return stylesheetsProperty;
    }

    private Property<StyleList> getStyleListProperty()
    {
        return styleListProperty;
    }

    @Override
    public void setStyleList(StyleList list)
    {
        getStyleListProperty().setValue(list);
    }

    public void addStylesheet(String stylesheetLocation)
    {
        getStylesheetsProperty().add(stylesheetLocation);
    }

    public void removeStylesheet(String stylesheetLocation)
    {
        getStylesheetsProperty().remove(stylesheetLocation);
    }

    @Override
    public String getThemeID()
    {
        return wrapper.getThemeID();
    }
}
