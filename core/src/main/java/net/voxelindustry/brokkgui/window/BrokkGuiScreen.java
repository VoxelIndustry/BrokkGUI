package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.property.IProperty;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.debug.DebugRenderer;
import net.voxelindustry.brokkgui.element.pane.GuiPane;
import net.voxelindustry.brokkgui.event.ClickDragEvent;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.ClickReleaseEvent;
import net.voxelindustry.brokkgui.event.DisposeEvent;
import net.voxelindustry.brokkgui.event.EventQueueBuilder;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.internal.PopupHandler;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    private       GuiPane                 mainPanel;
    private final ArrayList<SubGuiScreen> windows;
    private       IRenderCommandReceiver  renderer;

    private final Property<Float> widthProperty, heightProperty, xPosProperty, yPosProperty;

    private final Property<Float> xRelativePosProperty, yRelativePosProperty;
    private final Property<Float> xOffsetProperty, yOffsetProperty;

    private final Property<Integer> screenWidthProperty, screenHeightProperty;

    private final ListProperty<String> stylesheetsProperty;
    private final Property<StyleList>  styleListProperty;

    private final ListenerPool listenerPool;

    private IBrokkGuiImpl wrapper;

    private float lastClickX;
    private float lastClickY;

    private final PriorityQueue<Pair<Runnable, Long>> tasksQueue;

    protected boolean isDebugged;

    public BrokkGuiScreen(float xRelativePos, float yRelativePos, float width, float height)
    {
        widthProperty = new Property<>(width);
        heightProperty = new Property<>(height);

        xRelativePosProperty = new Property<>(xRelativePos);
        yRelativePosProperty = new Property<>(yRelativePos);

        xPosProperty = new Property<>(0F);
        yPosProperty = new Property<>(0F);

        xOffsetProperty = new Property<>(0F);
        yOffsetProperty = new Property<>(0F);

        windows = new ArrayList<>();

        screenWidthProperty = new Property<>(0);
        screenHeightProperty = new Property<>(0);

        listenerPool = new ListenerPool();

        stylesheetsProperty = new ListProperty<>(emptyList());
        styleListProperty = new Property<>(null);

        lastClickX = -1;
        lastClickY = -1;

        Comparator<Pair<Runnable, Long>> comparator = Comparator.comparingLong(Pair::getValue);
        tasksQueue = new PriorityQueue<>(comparator.reversed());

        setMainPanel(new GuiPane());
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

        xPosProperty.bindProperty(new Expression<>(() -> wrapper.getGuiRelativePosX(getxRelativePos(), getWidth()) + getxOffset(),
                screenWidthProperty(), getxRelativePosProperty(), getWidthProperty(),
                getxOffsetProperty()));

        yPosProperty.bindProperty(new Expression<>(() -> wrapper.getGuiRelativePosY(getyRelativePos(), getHeight()) + getyOffset(),
                getyRelativePosProperty(), screenHeightProperty(), getHeightProperty(),
                getyOffsetProperty()));

        stylesheetsProperty.addListener(obs ->
        {
            StylesheetManager.getInstance().refreshStylesheets(this);
            if (getMainPanel() != null)
                StyleEngine.refreshHierarchy(getMainPanel().transform());
        });

        StylesheetManager.getInstance().refreshStylesheets(this);
        if (getMainPanel() != null)
            StyleEngine.refreshHierarchy(getMainPanel().transform());
    }

    @Override
    public void onMouseMoved(float mouseX, float mouseY)
    {
        if (!windows.isEmpty() && windows.stream().anyMatch(gui -> gui.isPointInside(mouseX, mouseY)))
        {
            windows.forEach(gui -> gui.handleHover(mouseX, mouseY, gui.isPointInside(mouseX, mouseY)));
            mainPanel.handleHover(mouseX, mouseY, false);
        }
        else
        {
            windows.forEach(gui -> gui.handleHover(mouseX, mouseY, false));
            mainPanel.handleHover(mouseX, mouseY, mainPanel.isPointInside(mouseX, mouseY));
        }

        PopupHandler.getInstance(this).handleHover(mouseX, mouseY);
    }

    @Override
    public void render(float mouseX, float mouseY, RenderTarget target)
    {
        switch (target)
        {
            case MAIN:
                mainPanel.renderNode(renderer, mouseX, mouseY);
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
            case POPUP:
                PopupHandler.getInstance(this).renderPopup(renderer, mouseX, mouseY);
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

        PopupHandler.getInstance(this).handleClick(mouseX, mouseY, key);

        GuiElement source = null;

        if (!windows.isEmpty())
        {
            if (windows.get(0).isPointInside(mouseX, mouseY))
                source = windows.get(0);
            else if (windows.get(0).closeOnClick())
                removeSubGui(windows.get(0));
        }
        else
        {
            source = mainPanel;
            if (!mainPanel.isPointInside(mouseX, mouseY))
                GuiFocusManager.instance.removeWindowFocus(this);
        }

        lastClickX = mouseX;
        lastClickY = mouseY;

        if (source == null)
            return;

        if (source.isPointInside(mouseX, mouseY) && !source.isDisabled() && source.isVisible())
        {
            EventQueue eventQueue = EventQueueBuilder.allChildrenMatching(source,
                    EventQueueBuilder.isPointInside(mouseX, mouseY)
                            .and(EventQueueBuilder.isEnabled)
                            .and(EventQueueBuilder.isVisible));

            switch (key)
            {
                case MOUSE_LEFT:
                    eventQueue.dispatch(ClickPressEvent.Left.TYPE, new ClickPressEvent.Left(source, mouseX, mouseY));
                    break;
                case MOUSE_RIGHT:
                    eventQueue.dispatch(ClickPressEvent.Right.TYPE, new ClickPressEvent.Right(source, mouseX, mouseY));
                    break;
                case MOUSE_BUTTON_MIDDLE:
                    eventQueue.dispatch(ClickPressEvent.Middle.TYPE, new ClickPressEvent.Middle(source, mouseX, mouseY));
                    break;
                default:
                    eventQueue.dispatch(ClickPressEvent.TYPE, new ClickPressEvent(source, mouseX, mouseY, key));
                    break;
            }
        }
    }

    @Override
    public void onClickDrag(float mouseX, float mouseY, MouseInputCode key)
    {
        if (mainPanel.isPointInside(mouseX, mouseY) && !mainPanel.isDisabled() && mainPanel.isVisible())
        {
            EventQueue eventQueue = EventQueueBuilder.allChildrenMatching(mainPanel,
                    EventQueueBuilder.isPointInside(mouseX, mouseY)
                            .and(EventQueueBuilder.isEnabled)
                            .and(EventQueueBuilder.isVisible));

            switch (key)
            {
                case MOUSE_LEFT:
                    eventQueue.dispatch(ClickDragEvent.Left.TYPE, new ClickDragEvent.Left(mainPanel, mouseX, mouseY, lastClickX, lastClickY));
                    break;
                case MOUSE_RIGHT:
                    eventQueue.dispatch(ClickDragEvent.Right.TYPE, new ClickDragEvent.Right(mainPanel, mouseX, mouseY, lastClickX, lastClickY));
                    break;
                case MOUSE_BUTTON_MIDDLE:
                    eventQueue.dispatch(ClickDragEvent.Middle.TYPE, new ClickDragEvent.Middle(mainPanel, mouseX, mouseY, lastClickX, lastClickY));
                    break;
                default:
                    eventQueue.dispatch(ClickDragEvent.TYPE, new ClickDragEvent(mainPanel, mouseX, mouseY, lastClickX, lastClickY, key));
                    break;
            }
        }
    }

    @Override
    public void onClickStop(float mouseX, float mouseY, MouseInputCode key)
    {
        if (mainPanel.isPointInside(mouseX, mouseY) && !mainPanel.isDisabled() && mainPanel.isVisible())
        {
            EventQueue eventQueue = EventQueueBuilder.allChildrenMatching(mainPanel,
                    EventQueueBuilder.isPointInside(mouseX, mouseY)
                            .and(EventQueueBuilder.isEnabled)
                            .and(EventQueueBuilder.isVisible));

            switch (key)
            {
                case MOUSE_LEFT:
                    eventQueue.dispatch(ClickReleaseEvent.Left.TYPE, new ClickReleaseEvent.Left(mainPanel, mouseX, mouseY));
                    break;
                case MOUSE_RIGHT:
                    eventQueue.dispatch(ClickReleaseEvent.Right.TYPE, new ClickReleaseEvent.Right(mainPanel, mouseX, mouseY));
                    break;
                case MOUSE_BUTTON_MIDDLE:
                    eventQueue.dispatch(ClickReleaseEvent.Middle.TYPE, new ClickReleaseEvent.Middle(mainPanel, mouseX, mouseY));
                    break;
                default:
                    eventQueue.dispatch(ClickReleaseEvent.TYPE, new ClickReleaseEvent(mainPanel, mouseX, mouseY, key));
                    break;
            }
        }
        lastClickX = -1;
        lastClickY = -1;
    }

    @Override
    public void onScroll(float mouseX, float mouseY, double xOffset, double yOffset)
    {
        GuiFather hovered = getNodeUnderMouse(mouseX, mouseY);
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

        GuiFather hovered = getNodeUnderMouse(mouseX, mouseY);
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

        GuiFather hovered = getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
        {
            EventQueueBuilder.fromTarget(hovered).dispatch(KeyEvent.RELEASE, new KeyEvent.Release(hovered, key));
        }
    }

    public void addSubGui(SubGuiScreen subGui)
    {
        windows.add(0, subGui);

        subGui.transform().xPosProperty().bindProperty(new Expression<>(() -> getScreenWidth() / (1 / subGui.getxRelativePos())
                - subGui.getWidth() / 2, subGui.getxRelativePosProperty(), screenWidthProperty(),
                subGui.transform().widthProperty()));

        subGui.transform().yPosProperty().bindProperty(new Expression<>(() -> getScreenHeight() / (1 / subGui.getyRelativePos())
                - subGui.getHeight() / 2, subGui.getyRelativePosProperty(), screenHeightProperty(),
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
        PopupHandler.getInstance(this).delete(this);

        if (mainPanel != null)
            EventQueueBuilder.allChildren(mainPanel).dispatch(DisposeEvent.TYPE, new DisposeEvent(mainPanel));
        getSubGuis().forEach(SubGuiScreen::close);

        new EventQueue().addDispatcher(getEventDispatcher()).dispatch(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

    private GuiFather getNodeUnderMouse(float mouseX, float mouseY)
    {
        if (!windows.isEmpty())
        {
            Optional<SubGuiScreen> match =
                    windows.stream().filter(gui -> gui.isPointInside(mouseX, mouseY)).findFirst();

            if (match.isPresent())
                return match.get();
        }
        if (getMainPanel().isPointInside(mouseX, mouseY))
            return getMainPanel();
        return null;
    }

    public GuiPane getMainPanel()
    {
        return mainPanel;
    }

    public <T extends GuiPane> T setMainPanel(T mainPanel)
    {
        if (this.mainPanel != null)
        {
            this.mainPanel.transform().widthProperty().unbind();
            this.mainPanel.transform().heightProperty().unbind();
            this.mainPanel.transform().xPosProperty().unbind();
            this.mainPanel.transform().yPosProperty().unbind();

            if (this.mainPanel.getWindow() == this)
                this.mainPanel.setWindow(null);
        }

        this.mainPanel = mainPanel;

        this.mainPanel.transform().widthProperty().bindProperty(widthProperty);
        this.mainPanel.transform().heightProperty().bindProperty(heightProperty);

        this.mainPanel.transform().xPosProperty().bindProperty(xPosProperty);
        this.mainPanel.transform().yPosProperty().bindProperty(yPosProperty);

        StyleEngine.setStyleSupplierHierarchy(mainPanel.transform(), getStyleListProperty()::getValue);
        if (wrapper != null)
            StyleEngine.refreshHierarchy(mainPanel.transform());
        this.mainPanel.setWindow(this);

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
    public Property<Float> getxRelativePosProperty()
    {
        return xRelativePosProperty;
    }

    @Override
    public Property<Float> getyRelativePosProperty()
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
    public float getxRelativePos()
    {
        return getxRelativePosProperty().getValue();
    }

    public void setxRelativePos(float xRelativePos)
    {
        getxRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float getyRelativePos()
    {
        return getyRelativePosProperty().getValue();
    }

    public void setyRelativePos(float yRelativePos)
    {
        getyRelativePosProperty().setValue(yRelativePos);
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
        eventDispatcher = new EventDispatcher();
    }

    @Override
    public <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler)
    {
        getEventDispatcher().addHandler(type, handler);
    }

    @Override
    public <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<T> handler)
    {
        getEventDispatcher().removeHandler(type, handler);
    }

    @Override
    public void dispatchEventRedirect(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        EventQueueBuilder.singleton(this).dispatch(type, event.copy(this));
    }

    @Override
    public void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        EventQueueBuilder.singleton(this).dispatch(type, event);
    }

    @Override
    public GuiElement getRootElement()
    {
        return getMainPanel();
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
