package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.debug.DebugRenderer;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.panel.GuiPane;
import net.voxelindustry.brokkgui.style.IStyleRoot;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import net.voxelindustry.hermod.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BrokkGuiScreen implements IGuiWindow, IStyleRoot, IEventEmitter
{
    private EventDispatcher                 eventDispatcher;
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private       GuiPane                 mainPanel;
    private final ArrayList<SubGuiScreen> windows;
    private       IGuiRenderer            renderer;

    private final BaseProperty<Float> widthProperty, heightProperty, xPosProperty, yPosProperty;

    private final BaseProperty<Float> xRelativePosProperty, yRelativePosProperty;

    private final BaseProperty<Integer> screenWidthProperty, screenHeightProperty;

    private final BaseListProperty<String> stylesheetsProperty;
    private final BaseProperty<StyleList>  styleTreeProperty;

    private final ListenerPool listenerPool;

    private IBrokkGuiImpl wrapper;

    private int cachedMouseX, cachedMouseY;
    private int lastClickX, lastClickY;

    private PriorityQueue<Pair<Runnable, Long>> tasksQueue;

    protected boolean isDebugged;

    public BrokkGuiScreen(final float xRelativePos, final float yRelativePos, final float width, final float height)
    {
        this.widthProperty = new BaseProperty<>(width, "widthProperty");
        this.heightProperty = new BaseProperty<>(height, "heightProperty");

        this.xRelativePosProperty = new BaseProperty<>(xRelativePos, "xRelativePosProperty");
        this.yRelativePosProperty = new BaseProperty<>(yRelativePos, "yRelativePosProperty");

        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");

        this.windows = new ArrayList<>();

        this.screenWidthProperty = new BaseProperty<>(0, "screenWidthProperty");
        this.screenHeightProperty = new BaseProperty<>(0, "screenHeightProperty");

        this.listenerPool = new ListenerPool();

        this.stylesheetsProperty = new BaseListProperty<>(Collections.emptyList(), "styleSheetsListProperty");
        this.styleTreeProperty = new BaseProperty<>(null, "styleTreeProperty");

        this.cachedMouseX = -1;
        this.cachedMouseY = -1;

        this.lastClickX = -1;
        this.lastClickY = -1;

        Comparator<Pair<Runnable, Long>> comparator = Comparator.comparingLong(Pair::getValue);
        this.tasksQueue = new PriorityQueue<>(comparator.reversed());

        this.setMainPanel(new GuiPane());
    }

    public BrokkGuiScreen(final float width, final float height)
    {
        this(0, 0, width, height);
    }

    public BrokkGuiScreen()
    {
        this(0, 0);
    }

    @Override
    public void setWrapper(final IBrokkGuiImpl wrapper)
    {
        this.wrapper = wrapper;

        this.renderer = wrapper.getRenderer();

        this.xPosProperty.bind(new BaseExpression<>(() -> wrapper.getGuiRelativePosX(getxRelativePos(), getWidth()),
                this.getScreenWidthProperty(), this.getxRelativePosProperty(), this.getWidthProperty()));

        this.yPosProperty.bind(new BaseExpression<>(() -> wrapper.getGuiRelativePosY(getyRelativePos(), getHeight()),
                this.getyRelativePosProperty(), this.getScreenHeightProperty(), this.getHeightProperty()));

        this.stylesheetsProperty.addListener(obs ->
        {
            StylesheetManager.getInstance().refreshStylesheets(this);
            if (this.getMainPanel() != null)
                this.getMainPanel().refreshStyle();
            PopupHandler.getInstance().refreshStyle();

            this.windows.forEach(GuiFather::refreshStyle);
        });

        StylesheetManager.getInstance().refreshStylesheets(this);
        if (this.getMainPanel() != null)
            this.getMainPanel().refreshStyle();
        this.windows.forEach(GuiFather::refreshStyle);
        PopupHandler.getInstance().setStyleSupplier(this.getStyleTreeProperty()::getValue);
    }

    @Override
    public void render(int mouseX, int mouseY, RenderTarget target, RenderPass... passes)
    {
        switch (target)
        {
            case MAIN:
                if (this.cachedMouseX != mouseX || this.cachedMouseY != mouseY)
                {
                    if (!this.windows.isEmpty() && this.windows.stream().anyMatch(gui ->
                            gui.isPointInside(mouseX, mouseY)))
                    {
                        this.windows.forEach(gui -> gui.handleHover(mouseX, mouseY, gui.isPointInside(mouseX, mouseY)));
                        this.mainPanel.handleHover(mouseX, mouseY, false);
                    }
                    else
                    {
                        this.windows.forEach(gui -> gui.handleHover(mouseX, mouseY, false));
                        this.mainPanel.handleHover(mouseX, mouseY, this.mainPanel.isPointInside(mouseX, mouseY));
                    }

                    PopupHandler.getInstance().handleHover(mouseX, mouseY);
                    this.cachedMouseX = mouseX;
                    this.cachedMouseY = mouseY;
                }

                for (RenderPass pass : passes)
                {
                    this.renderer.beginPass(pass);
                    this.mainPanel.renderNode(this.renderer, pass, mouseX, mouseY);
                    this.renderer.endPass(pass);
                }
                break;
            case WINDOW:
                if (!this.windows.isEmpty())
                    for (int i = this.windows.size() - 1; i >= 0; i--)
                    {
                        if (this.windows.get(i).hasWarFog())
                            this.renderer.getHelper().drawColoredRect(this.renderer, 0, 0, this.getWidth(),
                                    this.getHeight(), 5 + i, Color.BLACK.addAlpha(-0.5f));

                        for (RenderPass pass : passes)
                        {
                            this.renderer.beginPass(pass);
                            this.windows.get(i).renderNode(this.renderer, pass, mouseX, mouseY);
                            this.renderer.endPass(pass);
                        }
                    }
                break;
            case POPUP:
                for (RenderPass pass : passes)
                    PopupHandler.getInstance().renderPopupInPass(renderer, pass, mouseX, mouseY);
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
    public void renderLast(int mouseX, int mouseY)
    {
        if (BrokkGuiPlatform.getInstance().isRenderDebugEnabled() && !this.isDebugged)
        {
            this.renderer.getHelper().drawColoredEmptyRect(this.renderer, 1, 1,
                    this.renderer.getHelper().getStringWidth("DEBUG") + 2,
                    this.renderer.getHelper().getStringHeight() + 2,
                    400, Color.RED, 1f);
            this.renderer.getHelper().drawString("DEBUG", 2, 2.5f, 400, Color.WHITE, Color.ALPHA);
        }
    }

    @Override
    public boolean doesOccludePoint(int mouseX, int mouseY)
    {
        return !this.windows.isEmpty() && this.windows.stream().anyMatch(gui -> gui.isPointInside(mouseX, mouseY));
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
    public void onClick(final int mouseX, final int mouseY, final int key)
    {
        if (BrokkGuiPlatform.getInstance().isRenderDebugEnabled() && !this.isDebugged)
        {
            if (mouseX > 0 && mouseY > 0 && mouseX < this.renderer.getHelper().getStringWidth("DEBUG") && mouseY < this.renderer.getHelper().getStringHeight())
            {
                DebugRenderer.wrap(this);
                this.isDebugged = true;
            }
        }

        PopupHandler.getInstance().handleClick(mouseX, mouseY, key);

        if (!this.windows.isEmpty())
        {
            if (this.windows.get(0).isPointInside(mouseX, mouseY))
                this.windows.get(0).handleClick(mouseX, mouseY, key);
            else if (this.windows.get(0).closeOnClick())
                this.removeSubGui(this.windows.get(0));
        }
        else
        {
            if (this.mainPanel.isPointInside(mouseX, mouseY))
                this.mainPanel.handleClick(mouseX, mouseY, key);
            else
                GuiFocusManager.getInstance().requestFocus(null);
        }

        this.lastClickX = mouseX;
        this.lastClickY = mouseY;
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int key, long timeSinceDrag)
    {
        if (this.mainPanel.isPointInside(lastClickX, lastClickY))
            this.mainPanel.handleClickDrag(mouseX, mouseY, key, lastClickX, lastClickY);
    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int key)
    {
        if (this.mainPanel.isPointInside(lastClickX, lastClickY))
            this.mainPanel.handleClickStop(mouseX, mouseY, key, lastClickX, lastClickY);
        this.lastClickX = -1;
        this.lastClickY = -1;
    }

    @Override
    public void handleMouseScroll(double scrolled)
    {
        int mouseX = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseX();
        int mouseY = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseY();

        GuiFather hovered = this.getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
            hovered.handleMouseScroll(mouseX, mouseY, scrolled);
    }

    @Override
    public void onKeyTyped(final char c, final int key)
    {
        if (GuiFocusManager.getInstance().getFocusedNode() != null)
            GuiFocusManager.getInstance().getFocusedNode().handleKeyInput(c, key);
    }

    @Override
    public void onKeyPressed(int key)
    {
        int mouseX = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseX();
        int mouseY = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseY();

        GuiFather hovered = this.getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
            hovered.handleKeyPress(mouseX, mouseY, key);
    }

    @Override
    public void onKeyReleased(int key)
    {
        int mouseX = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseX();
        int mouseY = BrokkGuiPlatform.getInstance().getMouseUtil().getMouseY();

        GuiFather hovered = this.getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
            hovered.handleKeyRelease(mouseX, mouseY, key);
    }

    public void addSubGui(final SubGuiScreen subGui)
    {
        this.windows.add(0, subGui);

        subGui.getxPosProperty().bind(new BaseExpression<>(() -> getScreenWidth() / (1 / subGui.getxRelativePos())
                - subGui.getWidth() / 2, subGui.getxRelativePosProperty(), this.getScreenWidthProperty(),
                subGui.getWidthProperty()));

        subGui.getyPosProperty().bind(new BaseExpression<>(() -> getScreenHeight() / (1 / subGui.getyRelativePos())
                - subGui.getHeight() / 2, subGui.getyRelativePosProperty(), this.getScreenHeightProperty(),
                subGui.getHeightProperty()));

        subGui.open();

        subGui.setStyleTree(this.getStyleTreeProperty()::getValue);
        if (this.wrapper != null)
            subGui.refreshStyle();
    }

    public void removeSubGui(final SubGuiScreen subGui)
    {
        subGui.close();

        subGui.getxPosProperty().unbind();
        subGui.getyPosProperty().unbind();
        this.windows.remove(subGui);

        subGui.setStyleTree(null);
    }

    public boolean hasSubGui(final SubGuiScreen subGui)
    {
        return this.windows.contains(subGui);
    }

    public List<SubGuiScreen> getSubGuis()
    {
        return Collections.unmodifiableList(this.windows);
    }

    @Override
    public void open()
    {
        this.wrapper.askOpen();
        this.onOpen();
    }

    @Override
    public void onOpen()
    {
        this.getEventDispatcher().dispatchEvent(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        this.wrapper.askClose();
        this.onClose();
    }

    @Override
    public void onClose()
    {
        GuiFocusManager.getInstance().requestFocus(null);
        this.listenerPool.clear();
        PopupHandler.getInstance().clearPopups();

        if (this.mainPanel != null)
            this.mainPanel.dispose();
        this.getSubGuis().forEach(SubGuiScreen::close);

        this.getEventDispatcher().dispatchEvent(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

    private GuiFather getNodeUnderMouse(int mouseX, int mouseY)
    {
        if (!this.windows.isEmpty())
        {
            Optional<SubGuiScreen> match =
                    this.windows.stream().filter(gui -> gui.isPointInside(mouseX, mouseY)).findFirst();

            if (match.isPresent())
                return match.get();
        }
        if (this.getMainPanel().isPointInside(mouseX, mouseY))
            return this.getMainPanel();
        return null;
    }

    public GuiPane getMainPanel()
    {
        return this.mainPanel;
    }

    public void setMainPanel(final GuiPane mainPanel)
    {
        if (this.mainPanel != null)
        {
            this.mainPanel.getWidthProperty().unbind();
            this.mainPanel.getHeightProperty().unbind();
            this.mainPanel.getxPosProperty().unbind();
            this.mainPanel.getyPosProperty().unbind();

            if (this.mainPanel.getWindow() == this)
                this.mainPanel.setWindow(null);
        }

        this.mainPanel = mainPanel;

        this.mainPanel.getWidthProperty().bind(this.widthProperty);
        this.mainPanel.getHeightProperty().bind(this.heightProperty);

        this.mainPanel.getxPosProperty().bind(this.xPosProperty);
        this.mainPanel.getyPosProperty().bind(this.yPosProperty);

        this.mainPanel.setStyleTree(this.getStyleTreeProperty()::getValue);
        if (this.wrapper != null)
            this.mainPanel.refreshStyle();
        this.mainPanel.setWindow(this);
    }

    public void runLater(Runnable runnable, long time, TimeUnit unit)
    {
        this.tasksQueue.add(Pair.of(runnable, System.currentTimeMillis() + unit.toMillis(time)));
    }

    public void runLater(Runnable runnable, int ticks)
    {
        this.runLater(runnable, ticks * 50, TimeUnit.MILLISECONDS);
    }

    public BaseProperty<Float> getWidthProperty()
    {
        return this.widthProperty;
    }

    public BaseProperty<Float> getHeightProperty()
    {
        return this.heightProperty;
    }

    public BaseProperty<Float> getxPosProperty()
    {
        return this.xPosProperty;
    }

    public BaseProperty<Float> getyPosProperty()
    {
        return this.yPosProperty;
    }

    @Override
    public BaseProperty<Float> getxRelativePosProperty()
    {
        return this.xRelativePosProperty;
    }

    @Override
    public BaseProperty<Float> getyRelativePosProperty()
    {
        return this.yRelativePosProperty;
    }

    @Override
    public float getWidth()
    {
        return this.getWidthProperty().getValue();
    }

    @Override
    public float getHeight()
    {
        return this.getHeightProperty().getValue();
    }

    public void setWidth(final float width)
    {
        this.getWidthProperty().setValue(width);
    }

    public void setHeight(final float height)
    {
        this.getHeightProperty().setValue(height);
    }

    public float getxPos()
    {
        return this.getxPosProperty().getValue();
    }

    public void setxPos(final float xPos)
    {
        this.getxPosProperty().setValue(xPos);
    }

    public float getyPos()
    {
        return this.getyPosProperty().getValue();
    }

    public void setyPos(final float yPos)
    {
        this.getyPosProperty().setValue(yPos);
    }

    @Override
    public float getxRelativePos()
    {
        return this.getxRelativePosProperty().getValue();
    }

    public void setxRelativePos(final float xRelativePos)
    {
        this.getxRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float getyRelativePos()
    {
        return this.getyRelativePosProperty().getValue();
    }

    public void setyRelativePos(final float yRelativePos)
    {
        this.getyRelativePosProperty().setValue(yRelativePos);
    }

    @Override
    public IProperty<Integer> getScreenWidthProperty()
    {
        return this.screenWidthProperty;
    }

    @Override
    public IProperty<Integer> getScreenHeightProperty()
    {
        return this.screenHeightProperty;
    }

    public int getScreenWidth()
    {
        return this.getScreenWidthProperty().getValue();
    }

    public int getScreenHeight()
    {
        return this.getScreenHeightProperty().getValue();
    }

    @Override
    public void setScreenWidth(int width)
    {
        this.getScreenWidthProperty().setValue(width);
    }

    @Override
    public void setScreenHeight(int height)
    {
        this.getScreenHeightProperty().setValue(height);
    }

    public ListenerPool getListeners()
    {
        return this.listenerPool;
    }

    @Override
    public IBrokkGuiImpl getWrapper()
    {
        return this.wrapper;
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnOpenEvent(final EventHandler<WindowEvent.Open> onOpenEvent)
    {
        this.getEventDispatcher().removeHandler(WindowEvent.OPEN, this.onOpenEvent);
        this.onOpenEvent = onOpenEvent;
        this.getEventDispatcher().addHandler(WindowEvent.OPEN, this.onOpenEvent);
    }

    public void setOnCloseEvent(final EventHandler<WindowEvent.Close> onCloseEvent)
    {
        this.getEventDispatcher().removeHandler(WindowEvent.CLOSE, this.onCloseEvent);
        this.onCloseEvent = onCloseEvent;
        this.getEventDispatcher().addHandler(WindowEvent.CLOSE, this.onCloseEvent);
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

    @Override
    public <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler)
    {
        this.getEventDispatcher().addHandler(type, handler);
    }

    @Override
    public <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<T> handler)
    {
        this.getEventDispatcher().removeHandler(type, handler);
    }

    @Override
    public void dispatchEventRedirect(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        this.getEventDispatcher().dispatchEvent(type, event.copy(this));
    }

    @Override
    public void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        this.getEventDispatcher().dispatchEvent(type, event);
    }

    /////////////////////
    //     STYLING     //
    /////////////////////

    @Override
    public BaseListProperty<String> getStylesheetsProperty()
    {
        return stylesheetsProperty;
    }

    private BaseProperty<StyleList> getStyleTreeProperty()
    {
        return this.styleTreeProperty;
    }

    @Override
    public void setStyleTree(StyleList tree)
    {
        this.getStyleTreeProperty().setValue(tree);
    }

    public void addStylesheet(String stylesheetLocation)
    {
        this.getStylesheetsProperty().add(stylesheetLocation);
    }

    public void removeStylesheet(String stylesheetLocation)
    {
        this.getStylesheetsProperty().remove(stylesheetLocation);
    }

    @Override
    public String getThemeID()
    {
        return this.wrapper.getThemeID();
    }
}
