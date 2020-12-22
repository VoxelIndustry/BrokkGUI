package net.voxelindustry.brokkgui.immediate;

import fr.ourten.teabeans.property.IProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.internal.ITextHelper;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.text.TextSettings;
import net.voxelindustry.brokkgui.window.IGuiWindow;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public abstract class BaseImmediateWindow implements IGuiWindow, IEventEmitter
{
    private static final Property<Float> RELATIVE_POS_PROPERTY = new Property<>(0.5F);

    private EventDispatcher eventDispatcher;
    private IBrokkGuiImpl   wrapper;

    private final Property<Integer> screenWidthProperty;
    private final Property<Integer> screenHeightProperty;

    private IRenderCommandReceiver renderer;

    private int mouseX;
    private int mouseY;

    private int lastClickX;
    private int lastClickY;

    private int lastHeldClickX;
    private int lastHeldClickY;

    private int    lastWheelX;
    private int    lastWheelY;
    private double lastWheelValue;

    private int lastKeyPressed;

    private boolean isScissorActive;
    private RectBox scissorBox;

    private final TextSettings textSettings = TextSettings.build().fontName("default").create();

    public BaseImmediateWindow()
    {
        screenWidthProperty = new Property<>(0);
        screenHeightProperty = new Property<>(0);
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void render(int mouseX, int mouseY, RenderTarget target, RenderPass... pass)
    {
    }

    @Override
    public void renderLast(int mouseX, int mouseY)
    {
        immediateRender();

        lastWheelX = -1;
        lastWheelY = -1;
        lastWheelValue = 0;

        lastKeyPressed = -1;

        lastClickX = -1;
        lastClickY = -1;
    }

    public abstract void immediateRender();

    public IRenderCommandReceiver getRenderer()
    {
        return renderer;
    }

    public ITextHelper getTextHelper()
    {
        return BrokkGuiPlatform.getInstance().getTextHelper();
    }

    public float getStringWidth(String text)
    {
        return getTextHelper().getStringWidth(text, textSettings());
    }

    public float getStringWidthMultiLine(String multilineText)
    {
        return getTextHelper().getStringWidthMultiLine(multilineText, textSettings());
    }

    public float getStringHeight()
    {
        return getTextHelper().getStringHeight(textSettings());
    }

    public float getStringHeightMultiLine(String multilineText)
    {
        return getTextHelper().getStringHeightMultiLine(multilineText, textSettings());
    }

    public float getStringHeightMultiLine(String multilineText, float lineSpacingMultiplier)
    {
        float previousMultiplier = textSettings().lineSpacingMultiplier();
        textSettings().lineSpacingMultiplier(lineSpacingMultiplier);
        float stringHeight = getTextHelper().getStringHeightMultiLine(multilineText, textSettings());
        textSettings().lineSpacingMultiplier(previousMultiplier);

        return stringHeight;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public int getLastKeyPressed()
    {
        return lastKeyPressed;
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

        renderer = wrapper.getRenderer();
    }

    @Override
    public IProperty<Integer> getScreenWidthProperty()
    {
        return screenWidthProperty;
    }

    @Override
    public IProperty<Integer> getScreenHeightProperty()
    {
        return screenHeightProperty;
    }

    public int getScreenWidth()
    {
        return getScreenWidthProperty().getValue();
    }

    public int getScreenHeight()
    {
        return getScreenHeightProperty().getValue();
    }

    @Override
    public void setScreenWidth(int width)
    {
        getScreenWidthProperty().setValue(width);
    }

    @Override
    public void setScreenHeight(int height)
    {
        getScreenHeightProperty().setValue(height);
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
        return wrapper;
    }

    @Override
    public boolean doesOccludePoint(int mouseX, int mouseY)
    {
        return false;
    }

    @Override
    public void onKeyPressed(int key)
    {
        lastKeyPressed = key;
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
    public void onClick(int mouseX, int mouseY, MouseInputCode key)
    {
        lastClickX = mouseX;
        lastClickY = mouseY;

        lastHeldClickX = mouseX;
        lastHeldClickY = mouseY;
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, MouseInputCode clickedMouseButton, double dragX, double dragY)
    {

    }

    @Override
    public void onClickStop(int mouseX, int mouseY, MouseInputCode mouseInputCode)
    {
        lastHeldClickX = -1;
        lastHeldClickY = -1;
    }

    @Override
    public void handleMouseScroll(double scrolled)
    {
        lastWheelX = mouseX;
        lastWheelY = mouseY;
        lastWheelValue = scrolled;
    }

    @Override
    public void open()
    {

    }

    @Override
    public void close()
    {

    }

    public int getLastClickX()
    {
        return lastClickX;
    }

    public int getLastClickY()
    {
        return lastClickY;
    }

    public int getLastWheelX()
    {
        return lastWheelX;
    }

    public int getLastWheelY()
    {
        return lastWheelY;
    }

    public double getLastWheelValue()
    {
        return lastWheelValue;
    }

    public void scissor(float startX, float startY, float endX, float endY)
    {
        isScissorActive = true;

        scissorBox = RectBox.build().left(startX).right(endX).top(startY).bottom(endY).create();
        getRenderer().beginScissor();
        getRenderer().scissorBox(startX, startY, endX, endY);
    }

    public void stopScissor()
    {
        isScissorActive = false;
        scissorBox = RectBox.EMPTY;
        getRenderer().endScissor();
    }

    public boolean isScissorActive()
    {
        return isScissorActive;
    }

    public RectBox getScissorBox()
    {
        return scissorBox;
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
    public Property<Float> getxRelativePosProperty()
    {
        return RELATIVE_POS_PROPERTY;
    }

    @Override
    public Property<Float> getyRelativePosProperty()
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
        getEventDispatcher().dispatchEvent(type, event.copy(this));
    }

    @Override
    public void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        getEventDispatcher().dispatchEvent(type, event);
    }

    public TextSettings textSettings()
    {
        return textSettings;
    }
}
