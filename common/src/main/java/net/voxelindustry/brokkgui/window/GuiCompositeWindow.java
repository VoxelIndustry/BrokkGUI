package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
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

        this.second.getScreenWidthProperty().bind(this.first.getScreenWidthProperty());
        this.second.getScreenHeightProperty().bind(this.first.getScreenHeightProperty());
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
    public void setScreenWidth(int width)
    {
        first.setScreenWidth(width);
        second.setScreenWidth(width);
    }

    @Override
    public void setScreenHeight(int height)
    {
        first.setScreenHeight(height);
        second.setScreenHeight(height);
    }

    @Override
    public IProperty<Integer> getScreenWidthProperty()
    {
        return first.getScreenWidthProperty();
    }

    @Override
    public IProperty<Integer> getScreenHeightProperty()
    {
        return first.getScreenHeightProperty();
    }

    @Override
    public void render(int mouseX, int mouseY, RenderTarget target, RenderPass... pass)
    {
        first.render(mouseX, mouseY, target, pass);
        second.render(mouseX, mouseY, target, pass);
    }

    @Override
    public void renderLast(int mouseX, int mouseY)
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
    public boolean doesOccludePoint(int mouseX, int mouseY)
    {
        return first.doesOccludePoint(mouseX, mouseY) || second.doesOccludePoint(mouseX, mouseY);
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY)
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
    public void onKeyTyped(char c, int key)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.KEY_TYPE))
                first.onKeyTyped(c, key);
            if (inputEventFilter.get().test(second, InputType.KEY_TYPE))
                second.onKeyTyped(c, key);
        }
        else
        {
            first.onKeyTyped(c, key);
            second.onKeyTyped(c, key);
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
    public void onClick(int mouseX, int mouseY, int key)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_CLICK))
                first.onClick(mouseX, mouseY, key);
            if (inputEventFilter.get().test(second, InputType.MOUSE_CLICK))
                second.onClick(mouseX, mouseY, key);
        }
        else
        {
            first.onClick(mouseX, mouseY, key);
            second.onClick(mouseX, mouseY, key);
        }
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int key, double dragX, double dragY)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_DRAG_START))
                first.onClickDrag(mouseX, mouseY, key, dragX, dragY);
            if (inputEventFilter.get().test(second, InputType.MOUSE_DRAG_START))
                second.onClickDrag(mouseX, mouseY, key, dragX, dragY);
        }
        else
        {
            first.onClickDrag(mouseX, mouseY, key, dragX, dragY);
            second.onClickDrag(mouseX, mouseY, key, dragX, dragY);
        }
    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int state)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_DRAG_STOP))
                first.onClickStop(mouseX, mouseY, state);
            if (inputEventFilter.get().test(second, InputType.MOUSE_DRAG_STOP))
                second.onClickStop(mouseX, mouseY, state);
        }
        else
        {
            first.onClickStop(mouseX, mouseY, state);
            second.onClickStop(mouseX, mouseY, state);
        }
    }

    @Override
    public void handleMouseScroll(double scrolled)
    {
        if (inputEventFilter.isPresent())
        {
            if (inputEventFilter.get().test(first, InputType.MOUSE_SCROLL))
                first.handleMouseScroll(scrolled);
            if (inputEventFilter.get().test(second, InputType.MOUSE_SCROLL))
                second.handleMouseScroll(scrolled);
        }
        else
        {
            first.handleMouseScroll(scrolled);
            second.handleMouseScroll(scrolled);
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
    public BaseProperty<Float> getxRelativePosProperty()
    {
        return first.getxRelativePosProperty();
    }

    @Override
    public BaseProperty<Float> getyRelativePosProperty()
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
}
