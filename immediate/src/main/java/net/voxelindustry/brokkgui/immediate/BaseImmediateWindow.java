package net.voxelindustry.brokkgui.immediate;

import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public abstract class BaseImmediateWindow implements IGuiWindow, IEventEmitter
{
    private static final BaseProperty<Float> RELATIVE_POS_PROPERTY = new BaseProperty<>(0.5F, "fixedRelativePosProperty");

    private EventDispatcher eventDispatcher;
    private IBrokkGuiImpl   wrapper;

    private final BaseProperty<Integer> screenWidthProperty, screenHeightProperty;
    private IGuiRenderer renderer;

    private int mouseX, mouseY;

    public BaseImmediateWindow()
    {
        this.screenWidthProperty = new BaseProperty<>(0, "screenWidthProperty");
        this.screenHeightProperty = new BaseProperty<>(0, "screenHeightProperty");
    }

    @Override
    public void render(int mouseX, int mouseY, RenderTarget target, RenderPass... pass)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.immediateRender();
    }

    @Override
    public void renderLast(int mouseX, int mouseY)
    {

    }

    public abstract void immediateRender();

    public IGuiRenderer getRenderer()
    {
        return renderer;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    @Override
    public void onOpen()
    {

    }

    @Override
    public void onClose()
    {

    }

    @Override
    public void setWrapper(IBrokkGuiImpl wrapper)
    {
        this.wrapper = wrapper;

        this.renderer = wrapper.getRenderer();
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

    @Override
    public void tick()
    {

    }

    @Override
    public void initGui()
    {

    }

    @Override
    public IBrokkGuiImpl getWrapper()
    {
        return this.wrapper;
    }

    @Override
    public boolean doesOccludePoint(int mouseX, int mouseY)
    {
        return false;
    }

    @Override
    public void onKeyPressed(int key)
    {

    }

    @Override
    public void onKeyTyped(char c, int key)
    {

    }

    @Override
    public void onKeyReleased(int key)
    {

    }

    @Override
    public void onClick(int mouseX, int mouseY, int key)
    {

    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int clickedMouseButton, double dragX, double dragY)
    {

    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int state)
    {

    }

    @Override
    public void handleMouseScroll(double scrolled)
    {

    }

    @Override
    public void open()
    {

    }

    @Override
    public void close()
    {

    }

    @Override
    public float getxRelativePos()
    {
        return RELATIVE_POS_PROPERTY.getValue();
    }

    @Override
    public float getyRelativePos()
    {
        return RELATIVE_POS_PROPERTY.getValue();
    }

    @Override
    public BaseProperty<Float> getxRelativePosProperty()
    {
        return RELATIVE_POS_PROPERTY;
    }

    @Override
    public BaseProperty<Float> getyRelativePosProperty()
    {
        return RELATIVE_POS_PROPERTY;
    }

    @Override
    public float getWidth()
    {
        return getScreenWidth();
    }

    @Override
    public float getHeight()
    {
        return getScreenHeight();
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
}
