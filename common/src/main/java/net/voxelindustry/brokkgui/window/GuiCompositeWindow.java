package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.IProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

import java.util.Optional;
import java.util.function.BiPredicate;

public class GuiCompositeWindow implements IGuiWindow
{
    private final IGuiWindow first, second;

    private Optional<BiPredicate<IGuiWindow, InputType>> inputEventFilter = Optional.empty();

    public GuiCompositeWindow(IGuiWindow first, IGuiWindow second)
    {
        this.first = first;
        this.second = second;

        this.second.screenWidthProperty().bindBidirectional(this.first.screenWidthProperty());
        this.second.screenHeightProperty().bindBidirectional(this.first.screenHeightProperty());
    }

    public void setInputEventFilter(BiPredicate<IGuiWindow, InputType> inputEventFilter)
    {
        this.inputEventFilter = Optional.ofNullable(inputEventFilter);
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
    public void onMouseMoved(float mouseX, float mouseY)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_MOVE))
                first.onMouseMoved(mouseX, mouseY);
            if (inputEventFilter.get().test(second, InputType.MOUSE_MOVE))
                second.onMouseMoved(mouseX, mouseY);
        }
        else
        {
            first.onMouseMoved(mouseX, mouseY);
            second.onMouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public void onKeyPressed(int key)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.KEY_PRESS))
                first.onKeyPressed(key);
            if (inputEventFilter.get().test(second, InputType.KEY_PRESS))
                second.onKeyPressed(key);
        }
        else
        {
            first.onKeyPressed(key);
            second.onKeyPressed(key);
        }
    }

    @Override
    public void onTextTyped(String text)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.KEY_TYPE))
                first.onTextTyped(text);
            if (inputEventFilter.get().test(second, InputType.KEY_TYPE))
                second.onTextTyped(text);
        }
        else
        {
            first.onTextTyped(text);
            second.onTextTyped(text);
        }
    }

    @Override
    public void onKeyReleased(int key)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.KEY_RELEASE))
                first.onKeyReleased(key);
            if (inputEventFilter.get().test(second, InputType.KEY_RELEASE))
                second.onKeyReleased(key);
        }
        else
        {
            first.onKeyReleased(key);
            second.onKeyReleased(key);
        }
    }

    @Override
    public void onClick(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_CLICK))
                first.onClick(mouseX, mouseY, mouseInputCode);
            if (inputEventFilter.get().test(second, InputType.MOUSE_CLICK))
                second.onClick(mouseX, mouseY, mouseInputCode);
        }
        else
        {
            first.onClick(mouseX, mouseY, mouseInputCode);
            second.onClick(mouseX, mouseY, mouseInputCode);
        }
    }

    @Override
    public void onClickDrag(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_DRAG_START))
                first.onClickDrag(mouseX, mouseY, mouseInputCode);
            if (inputEventFilter.get().test(second, InputType.MOUSE_DRAG_START))
                second.onClickDrag(mouseX, mouseY, mouseInputCode);
        }
        else
        {
            first.onClickDrag(mouseX, mouseY, mouseInputCode);
            second.onClickDrag(mouseX, mouseY, mouseInputCode);
        }
    }

    @Override
    public void onClickStop(float mouseX, float mouseY, MouseInputCode mouseInputCode)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_DRAG_STOP))
                first.onClickStop(mouseX, mouseY, mouseInputCode);
            if (inputEventFilter.get().test(second, InputType.MOUSE_DRAG_STOP))
                second.onClickStop(mouseX, mouseY, mouseInputCode);
        }
        else
        {
            first.onClickStop(mouseX, mouseY, mouseInputCode);
            second.onClickStop(mouseX, mouseY, mouseInputCode);
        }
    }

    @Override
    public void onScroll(float mouseX, float mouseY, double xOffset, double yOffset)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_SCROLL))
                first.onScroll(mouseX, mouseY, xOffset, yOffset);
            if (inputEventFilter.get().test(second, InputType.MOUSE_SCROLL))
                second.onScroll(mouseX, mouseY, xOffset, yOffset);
        }
        else
        {
            first.onScroll(mouseX, mouseY, xOffset, yOffset);
            second.onScroll(mouseX, mouseY, xOffset, yOffset);
        }
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
    public float getxRelativePos()
    {
        return first.getxRelativePos();
    }

    @Override
    public float getyRelativePos()
    {
        return first.getyRelativePos();
    }

    @Override
    public Property<Float> getxRelativePosProperty()
    {
        return first.getxRelativePosProperty();
    }

    @Override
    public Property<Float> getyRelativePosProperty()
    {
        return first.getyRelativePosProperty();
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
    public <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<T> handler)
    {
        first.removeEventHandler(type, handler);
        second.removeEventHandler(type, handler);
    }

    @Override
    public void dispatchEventRedirect(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        first.dispatchEventRedirect(type, event);
        second.dispatchEventRedirect(type, event);
    }

    @Override
    public void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event)
    {
        first.dispatchEvent(type, event);
        second.dispatchEvent(type, event);
    }

    @Override
    public GuiElement getRootElement()
    {
        return first.getRootElement();
    }
}
