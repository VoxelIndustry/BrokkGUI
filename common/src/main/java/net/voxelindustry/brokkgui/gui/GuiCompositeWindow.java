package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.panel.GuiPane;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

public class GuiCompositeWindow implements IGuiWindow
{
    private final IGuiWindow first, second;

    public GuiCompositeWindow(IGuiWindow first, IGuiWindow second)
    {
        this.first = first;
        this.second = second;
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
    public GuiPane getMainPanel()
    {
        return first.getMainPanel();
    }

    @Override
    public boolean doesOccludePoint(int mouseX, int mouseY)
    {
        return first.doesOccludePoint(mouseX, mouseY) || second.doesOccludePoint(mouseX, mouseY);
    }

    @Override
    public void onKeyPressed(int key)
    {
        first.onKeyPressed(key);
        second.onKeyPressed(key);
    }

    @Override
    public void onKeyTyped(char c, int key)
    {
        first.onKeyTyped(c, key);
        second.onKeyTyped(c, key);
    }

    @Override
    public void onKeyReleased(int key)
    {
        first.onKeyPressed(key);
        second.onKeyPressed(key);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int key)
    {
        first.onClick(mouseX, mouseY, key);
        second.onClick(mouseX, mouseY, key);
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int key, double dragX, double dragY)
    {
        first.onClickDrag(mouseX, mouseY, key, dragX, dragY);
        second.onClickDrag(mouseX, mouseY, key, dragX, dragY);
    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int state)
    {
        first.onClickStop(mouseX, mouseY, state);
        second.onClickStop(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseScroll(double scrolled)
    {
        first.handleMouseScroll(scrolled);
        second.handleMouseScroll(scrolled);
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
