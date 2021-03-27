package net.voxelindustry.brokkgui.immediate;

import fr.ourten.teabeans.property.IProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.internal.ITextHelper;
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

    private float mouseX;
    private float mouseY;

    private float lastClickX;
    private float lastClickY;

    private float lastHeldClickX;
    private float lastHeldClickY;

    private float  lastWheelX;
    private float  lastWheelY;
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
    public void onMouseMoved(float mouseX, float mouseY)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void render(float mouseX, float mouseY, RenderTarget target)
    {
    }

    @Override
    public void renderLast(float mouseX, float mouseY)
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

    public float getMouseX()
    {
        return mouseX;
    }

    public float getMouseY()
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
    public IProperty<Integer> screenWidthProperty()
    {
        return screenWidthProperty;
    }

    @Override
    public IProperty<Integer> screenHeightProperty()
    {
        return screenHeightProperty;
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
    public boolean doesOccludePoint(float mouseX, float mouseY)
    {
        return false;
    }

    @Override
    public void onKeyPressed(int key)
    {
        lastKeyPressed = key;
    }

    @Override
    public void onTextTyped(String text)
    {

    }

    @Override
    public void onKeyReleased(int key)
    {

    }

    @Override
    public void onClick(float mouseX, float mouseY, MouseInputCode key)
    {
        lastClickX = mouseX;
        lastClickY = mouseY;

        lastHeldClickX = mouseX;
        lastHeldClickY = mouseY;
    }

    @Override
    public void onClickDrag(float mouseX, float mouseY, MouseInputCode clickedMouseButton)
    {

    }

    @Override
    public void onClickStop(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        lastHeldClickX = -1;
        lastHeldClickY = -1;
    }

    @Override
    public void onScroll(float mouseX, float mouseY, double xOffset, double yOffset)
    {
        lastWheelX = mouseX;
        lastWheelY = mouseY;
        // FIXME: Does not handle horizontal scrolling
        lastWheelValue = yOffset;
    }

    @Override
    public void open()
    {

    }

    @Override
    public void close()
    {

    }

    public float getLastClickX()
    {
        return lastClickX;
    }

    public float getLastClickY()
    {
        return lastClickY;
    }

    public float getLastWheelX()
    {
        return lastWheelX;
    }

    public float getLastWheelY()
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

    public TextSettings textSettings()
    {
        return textSettings;
    }
}
