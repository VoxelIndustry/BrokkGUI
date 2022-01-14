package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.IProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

import java.util.Collection;
import java.util.function.Predicate;

public class GuiCompositeWindow implements IGuiWindow
{
    private final IGuiWindow first, second;

    private WindowInputEventFilter inputEventFilter;

    public GuiCompositeWindow(IGuiWindow first, IGuiWindow second)
    {
        this.first = first;
        this.second = second;

        this.second.screenWidthProperty().bindBidirectional(this.first.screenWidthProperty());
        this.second.screenHeightProperty().bindBidirectional(this.first.screenHeightProperty());
    }

    public void setInputEventFilter(WindowInputEventFilter inputEventFilter)
    {
        this.inputEventFilter = inputEventFilter;
    }

    @Override
    public void onOpen()
    {
        first.onOpen();
        second.onOpen();
    }

    @Override
    public void onClose()
    {
        first.onClose();
        second.onClose();
    }

    @Override
    public void setWrapper(IBrokkGuiImpl wrapper)
    {
        first.setWrapper(wrapper);
        second.setWrapper(wrapper);
    }

    @Override
    public void screenWidth(int width)
    {
        first.screenWidth(width);
        second.screenWidth(width);
    }

    @Override
    public void screenHeight(int height)
    {
        first.screenHeight(height);
        second.screenHeight(height);
    }

    @Override
    public IProperty<Integer> screenWidthProperty()
    {
        return first.screenWidthProperty();
    }

    @Override
    public IProperty<Integer> screenHeightProperty()
    {
        return first.screenHeightProperty();
    }

    @Override
    public float windowWidthRatio()
    {
        return first.windowWidthRatio();
    }

    @Override
    public float windowHeightRatio()
    {
        return first.windowHeightRatio();
    }

    @Override
    public void render(float mouseX, float mouseY, RenderTarget target)
    {
        first.render(mouseX, mouseY, target);
        second.render(mouseX, mouseY, target);
    }

    @Override
    public void renderLast(float mouseX, float mouseY)
    {
        first.renderLast(mouseX, mouseY);
        second.renderLast(mouseX, mouseY);
    }

    @Override
    public void tick()
    {
        first.tick();
        second.tick();
    }

    @Override
    public void initGui()
    {
        first.initGui();
        second.initGui();
    }

    @Override
    public IBrokkGuiImpl getWrapper()
    {
        return first.getWrapper();
    }

    @Override
    public boolean doesOccludePoint(float mouseX, float mouseY)
    {
        return first.doesOccludePoint(mouseX, mouseY) || second.doesOccludePoint(mouseX, mouseY);
    }

    @Override
    public boolean onMouseMoved(float mouseX, float mouseY)
    {
        return dispatchEventToWindows(InputType.MOUSE_MOVE, window -> window.onMouseMoved(mouseX, mouseY));
    }

    @Override
    public boolean onKeyPressed(int key)
    {
        return dispatchEventToWindows(InputType.KEY_PRESS, window -> window.onKeyPressed(key));
    }

    @Override
    public boolean onTextTyped(String text)
    {
        return dispatchEventToWindows(InputType.KEY_TYPE, window -> window.onTextTyped(text));
    }

    @Override
    public boolean onKeyReleased(int key)
    {
        return dispatchEventToWindows(InputType.KEY_RELEASE, window -> window.onKeyReleased(key));
    }

    @Override
    public boolean onClick(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        return dispatchEventToWindows(InputType.MOUSE_CLICK, window -> window.onClick(mouseX, mouseY, mouseInputCode));
    }

    @Override
    public boolean onClickDrag(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        return dispatchEventToWindows(InputType.MOUSE_CLICK_DRAG, window -> window.onClickDrag(mouseX, mouseY, mouseInputCode));
    }

    @Override
    public boolean onClickStop(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        return dispatchEventToWindows(InputType.MOUSE_CLICK_STOP, window -> window.onClickStop(mouseX, mouseY, mouseInputCode));
    }

    @Override
    public boolean onScroll(float mouseX, float mouseY, double xOffset, double yOffset)
    {
        return dispatchEventToWindows(InputType.MOUSE_SCROLL, window -> window.onScroll(mouseX, mouseY, xOffset, yOffset));
    }

    private boolean dispatchEventToWindows(InputType inputType, Predicate<IGuiWindow> eventCallable)
    {
        if (inputEventFilter != null)
        {
            var filterResult = inputEventFilter.apply(first, inputType);
            var consumed = false;
            if (filterResult != WindowEventFilter.IGNORE)
                consumed = eventCallable.test(first);
            if (filterResult == WindowEventFilter.SWALLOW)
                return consumed;
            if (!consumed)
                return eventCallable.test(second);
            return true;
        }
        return eventCallable.test(first) || eventCallable.test(second);
    }

    @Override
    public void open()
    {
        first.open();
        second.open();
    }

    @Override
    public void close()
    {
        first.close();
        second.close();
    }

    @Override
    public float xRelativePos()
    {
        return first.xRelativePos();
    }

    @Override
    public float yRelativePos()
    {
        return first.yRelativePos();
    }

    @Override
    public Property<Float> xRelativePosProperty()
    {
        return first.xRelativePosProperty();
    }

    @Override
    public Property<Float> yRelativePosProperty()
    {
        return first.yRelativePosProperty();
    }

    @Override
    public float getWidth()
    {
        return first.getWidth();
    }

    @Override
    public float getHeight()
    {
        return first.getHeight();
    }

    @Override
    public <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler)
    {
        first.addEventHandler(type, handler);
        second.addEventHandler(type, handler);
    }

    @Override
    public <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler)
    {
        first.removeEventHandler(type, handler);
        second.removeEventHandler(type, handler);
    }

    @Override
    public <T extends HermodEvent> void dispatchEventRedirect(EventType<T> type, T event)
    {
        first.dispatchEventRedirect(type, event);
        second.dispatchEventRedirect(type, event);
    }

    @Override
    public <T extends HermodEvent> void dispatchEvent(EventType<T> type, T event)
    {
        first.dispatchEvent(type, event);
        second.dispatchEvent(type, event);
    }

    @Override
    public GuiElement getRootElement()
    {
        return first.getRootElement();
    }

    @Override
    public void addFloating(Transform transform)
    {
        first.addFloating(transform);
    }

    @Override
    public boolean removeFloating(Transform transform)
    {
        return first.removeFloating(transform);
    }

    @Override
    public Collection<Transform> getFloatingList()
    {
        return first.getFloatingList();
    }
}
