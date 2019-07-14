package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.element.popup.PopupHandler;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.panel.PaneBase;
import net.voxelindustry.brokkgui.style.IStyleRoot;
import net.voxelindustry.brokkgui.style.StyleHolder;
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

    private       PaneBase                mainPanel;
    private final ArrayList<SubGuiScreen> windows;
    private       IGuiRenderer            renderer;

    private final BaseProperty<Float> widthProperty, heightProperty, xPosProperty, yPosProperty;

    private final BaseProperty<Float> xRelativePosProperty, yRelativePosProperty;
    private final BaseProperty<Float> xOffsetProperty, yOffsetProperty;

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

        this.xOffsetProperty = new BaseProperty<>(0f, "xOffsetProperty");
        this.yOffsetProperty = new BaseProperty<>(0f, "yOffsetProperty");

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

        this.setMainPanel(new PaneBase());
    }

    public BrokkGuiScreen(final float width, final float height)
    {
        this(0.5f, 0.5f, width, height);
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

        this.xPosProperty.bind(new BaseExpression<>(() -> wrapper.getGuiRelativePosX(xRelativePos(), width()) + xOffset(),
                this.screenWidthProperty(), this.xRelativePosProperty(), this.widthProperty(),
                this.xOffsetProperty()));

        this.yPosProperty.bind(new BaseExpression<>(() -> wrapper.getGuiRelativePosY(yRelativePos(), height()) + yOffset(),
                this.yRelativePosProperty(), this.screenHeightProperty(), this.heightProperty(),
                this.yOffsetProperty()));

        this.stylesheetsProperty.addListener(obs ->
        {
            StylesheetManager.getInstance().refreshStylesheets(this);
            if (doesMainPanelUseStyle())
                this.getMainPanel().get(StyleHolder.class).refresh();
            PopupHandler.getInstance(this).refreshStyle();

            this.windows.forEach(SubGuiScreen::refreshStyle);
        });

        StylesheetManager.getInstance().refreshStylesheets(this);
        if (doesMainPanelUseStyle())
            this.getMainPanel().get(StyleHolder.class).refresh();
        this.windows.forEach(SubGuiScreen::refreshStyle);
        PopupHandler.getInstance(this).setStyleSupplier(this.getStyleTreeProperty()::getValue);
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
                            gui.transform().isPointInside(mouseX, mouseY)))
                    {
                        this.windows.forEach(gui -> gui.handleHover(mouseX, mouseY, gui.transform().isPointInside(mouseX, mouseY)));
                        this.mainPanel.handleHover(mouseX, mouseY, false);
                    }
                    else
                    {
                        this.windows.forEach(gui -> gui.handleHover(mouseX, mouseY, false));
                        this.mainPanel.handleHover(mouseX, mouseY, this.mainPanel.transform().isPointInside(mouseX, mouseY));
                    }

                    PopupHandler.getInstance(this).handleHover(mouseX, mouseY);
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
                            this.renderer.getHelper().drawColoredRect(this.renderer, 0, 0, this.width(),
                                    this.height(), 5 + i, Color.BLACK.addAlpha(-0.5f));

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
                    PopupHandler.getInstance(this).renderPopupInPass(renderer, pass, mouseX, mouseY);
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
        if (BrokkGuiPlatform.instance().isRenderDebugEnabled() && !this.isDebugged)
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
        return !this.windows.isEmpty() && this.windows.stream().anyMatch(gui -> gui.transform().isPointInside(mouseX, mouseY));
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
        if (BrokkGuiPlatform.instance().isRenderDebugEnabled() && !this.isDebugged)
        {
            if (mouseX > 0 && mouseY > 0 && mouseX < this.renderer.getHelper().getStringWidth("DEBUG") && mouseY < this.renderer.getHelper().getStringHeight())
            {
                DebugRenderer.wrap(this);
                this.isDebugged = true;
            }
        }

        PopupHandler.getInstance(this).handleClick(mouseX, mouseY, key);

        if (!this.windows.isEmpty())
        {
            if (this.windows.get(0).transform().isPointInside(mouseX, mouseY))
                this.windows.get(0).handleClick(mouseX, mouseY, key);
            else if (this.windows.get(0).closeOnClick())
                this.removeSubGui(this.windows.get(0));
        }
        else
        {
            if (this.mainPanel.transform().isPointInside(mouseX, mouseY))
                this.mainPanel.handleClick(mouseX, mouseY, key);
            else
                GuiFocusManager.instance().requestFocus(null);
        }

        this.lastClickX = mouseX;
        this.lastClickY = mouseY;
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int key, long timeSinceDrag)
    {
        if (this.mainPanel.transform().isPointInside(lastClickX, lastClickY))
            this.mainPanel.handleClickDrag(mouseX, mouseY, key, lastClickX, lastClickY);
    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int key)
    {
        if (this.mainPanel.transform().isPointInside(lastClickX, lastClickY))
            this.mainPanel.handleClickStop(mouseX, mouseY, key, lastClickX, lastClickY);
        this.lastClickX = -1;
        this.lastClickY = -1;
    }

    @Override
    public void handleMouseScroll(double scrolled)
    {
        int mouseX = BrokkGuiPlatform.instance().mouseUtil().getMouseX();
        int mouseY = BrokkGuiPlatform.instance().mouseUtil().getMouseY();

        GuiElement hovered = this.getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
            hovered.handleMouseScroll(mouseX, mouseY, scrolled);
    }

    @Override
    public void onKeyTyped(final char c, final int key)
    {
        if (GuiFocusManager.instance().focusedNode() != null)
            GuiFocusManager.instance().focusedNode().handleKeyInput(c, key);
    }

    @Override
    public void onKeyPressed(int key)
    {
        int mouseX = BrokkGuiPlatform.instance().mouseUtil().getMouseX();
        int mouseY = BrokkGuiPlatform.instance().mouseUtil().getMouseY();

        GuiElement hovered = this.getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
            hovered.handleKeyPress(mouseX, mouseY, key);
    }

    @Override
    public void onKeyReleased(int key)
    {
        int mouseX = BrokkGuiPlatform.instance().mouseUtil().getMouseX();
        int mouseY = BrokkGuiPlatform.instance().mouseUtil().getMouseY();

        GuiElement hovered = this.getNodeUnderMouse(mouseX, mouseY);
        if (hovered != null)
            hovered.handleKeyRelease(mouseX, mouseY, key);
    }

    public void addSubGui(final SubGuiScreen subGui)
    {
        this.windows.add(0, subGui);

        subGui.transform().xPosProperty().bind(new BaseExpression<>(() -> screenWidth() / (1 / subGui.xRelativePos())
                - subGui.width() / 2, subGui.xRelativePosProperty(), this.screenWidthProperty(),
                subGui.transform().widthProperty()));

        subGui.transform().yPosProperty().bind(new BaseExpression<>(() -> screenHeight() / (1 / subGui.yRelativePos())
                - subGui.height() / 2, subGui.yRelativePosProperty(), this.screenHeightProperty(),
                subGui.transform().heightProperty()));

        subGui.open();

        subGui.setStyleTree(this.getStyleTreeProperty()::getValue);
        if (this.wrapper != null)
            subGui.refreshStyle();
    }

    public void removeSubGui(final SubGuiScreen subGui)
    {
        subGui.close();

        subGui.transform().xPosProperty().unbind();
        subGui.transform().yPosProperty().unbind();
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
        GuiFocusManager.instance().requestFocus(null);
        this.listenerPool.clear();
        PopupHandler.getInstance(this).delete(this);

        if (this.mainPanel != null)
            this.mainPanel.dispose();
        this.getSubGuis().forEach(SubGuiScreen::close);

        this.getEventDispatcher().dispatchEvent(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

    private GuiElement getNodeUnderMouse(int mouseX, int mouseY)
    {
        if (!this.windows.isEmpty())
        {
            Optional<SubGuiScreen> match =
                    this.windows.stream().filter(gui -> gui.transform().isPointInside(mouseX, mouseY)).findFirst();

            if (match.isPresent())
                return match.get();
        }
        if (this.getMainPanel().transform().isPointInside(mouseX, mouseY))
            return this.getMainPanel();
        return null;
    }

    @Override
    public PaneBase getMainPanel()
    {
        return this.mainPanel;
    }

    public void setMainPanel(final PaneBase mainPanel)
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

        this.mainPanel.transform().widthProperty().bind(this.widthProperty);
        this.mainPanel.transform().heightProperty().bind(this.heightProperty);

        this.mainPanel.transform().xPosProperty().bind(this.xPosProperty);
        this.mainPanel.transform().yPosProperty().bind(this.yPosProperty);

        if (doesMainPanelUseStyle())
        {
            this.mainPanel.get(StyleHolder.class).setStyleSupplier(this.getStyleTreeProperty()::getValue);
            if (this.wrapper != null)
                this.mainPanel.get(StyleHolder.class).refresh();
        }
        this.mainPanel.setWindow(this);
    }

    private boolean doesMainPanelUseStyle()
    {
        return this.mainPanel != null && this.mainPanel.has(StyleHolder.class);
    }

    public void runLater(Runnable runnable, long time, TimeUnit unit)
    {
        this.tasksQueue.add(Pair.of(runnable, System.currentTimeMillis() + unit.toMillis(time)));
    }

    public void runLater(Runnable runnable, int ticks)
    {
        this.runLater(runnable, ticks * 50, TimeUnit.MILLISECONDS);
    }

    public BaseProperty<Float> widthProperty()
    {
        return this.widthProperty;
    }

    public BaseProperty<Float> heightProperty()
    {
        return this.heightProperty;
    }

    public BaseProperty<Float> xPosProperty()
    {
        return this.xPosProperty;
    }

    public BaseProperty<Float> yPosProperty()
    {
        return this.yPosProperty;
    }

    @Override
    public BaseProperty<Float> xRelativePosProperty()
    {
        return this.xRelativePosProperty;
    }

    @Override
    public BaseProperty<Float> yRelativePosProperty()
    {
        return this.yRelativePosProperty;
    }

    public BaseProperty<Float> xOffsetProperty()
    {
        return xOffsetProperty;
    }

    public BaseProperty<Float> yOffsetProperty()
    {
        return yOffsetProperty;
    }

    @Override
    public float width()
    {
        return this.widthProperty().getValue();
    }

    @Override
    public float height()
    {
        return this.heightProperty().getValue();
    }

    public void width(final float width)
    {
        this.widthProperty().setValue(width);
    }

    public void height(final float height)
    {
        this.heightProperty().setValue(height);
    }

    public void size(float width, float height)
    {
        this.width(width);
        this.height(height);
    }

    public float xPos()
    {
        return this.xPosProperty().getValue();
    }

    public void xPos(final float xPos)
    {
        this.xPosProperty().setValue(xPos);
    }

    public float yPos()
    {
        return this.yPosProperty().getValue();
    }

    public void yPos(final float yPos)
    {
        this.yPosProperty().setValue(yPos);
    }

    @Override
    public float xRelativePos()
    {
        return this.xRelativePosProperty().getValue();
    }

    public void xRelativePos(final float xRelativePos)
    {
        this.xRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float yRelativePos()
    {
        return this.yRelativePosProperty().getValue();
    }

    public void yRelativePos(final float yRelativePos)
    {
        this.yRelativePosProperty().setValue(yRelativePos);
    }

    public void setRelativePos(float xRelativePos, float yRelativePos)
    {
        this.xRelativePos(xRelativePos);
        this.yRelativePos(yRelativePos);
    }

    public float xOffset()
    {
        return this.xOffsetProperty().getValue();
    }

    public void xOffset(float xOffset)
    {
        this.xOffsetProperty().setValue(xOffset);
    }

    public float yOffset()
    {
        return this.yOffsetProperty().getValue();
    }

    public void yOffset(float yOffset)
    {
        this.yOffsetProperty().setValue(yOffset);
    }

    public void offset(float xOffset, float yOffset)
    {
        this.xOffset(xOffset);
        this.yOffset(yOffset);
    }

    @Override
    public IProperty<Integer> screenWidthProperty()
    {
        return this.screenWidthProperty;
    }

    @Override
    public IProperty<Integer> screenHeightProperty()
    {
        return this.screenHeightProperty;
    }

    public int screenWidth()
    {
        return this.screenWidthProperty().getValue();
    }

    public int screenHeight()
    {
        return this.screenHeightProperty().getValue();
    }

    @Override
    public void screenWidth(int width)
    {
        this.screenWidthProperty().setValue(width);
    }

    @Override
    public void screenHeight(int height)
    {
        this.screenHeightProperty().setValue(height);
    }

    public ListenerPool listeners()
    {
        return this.listenerPool;
    }

    @Override
    public IBrokkGuiImpl wrapper()
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
