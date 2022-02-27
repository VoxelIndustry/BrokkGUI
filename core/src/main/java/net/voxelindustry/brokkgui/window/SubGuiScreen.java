package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.element.pane.GuiPane;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

import java.util.Collection;
import java.util.Collections;

public class SubGuiScreen extends GuiPane implements StyledElement, IGuiSubWindow
{
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private final Property<Float> xRelativePosProperty, yRelativePosProperty;

    private boolean closeOnClick;
    private boolean hasWarFog;

    public SubGuiScreen(float xRelativePos, float yRelativePos)
    {
        transform().zTranslate(300);

        xRelativePosProperty = new Property<>(xRelativePos);
        yRelativePosProperty = new Property<>(yRelativePos);

        window(this);
    }

    public SubGuiScreen()
    {
        this(0, 0);
    }

    @Override
    public String type()
    {
        return "subwindow";
    }

    /**
     * @return if the subgui can be closed with a click outside his area.
     */
    public boolean closeOnClick()
    {
        return closeOnClick;
    }

    public void setCloseOnClick(boolean closeOnClick)
    {
        this.closeOnClick = closeOnClick;
    }

    /**
     * @return if the subgui shadow everything outside his area.
     */
    public boolean hasWarFog()
    {
        return hasWarFog;
    }

    public void setWarFog(boolean warFog)
    {
        hasWarFog = warFog;
    }

    @Override
    public Property<Float> xRelativePosProperty()
    {
        return xRelativePosProperty;
    }

    @Override
    public Property<Float> yRelativePosProperty()
    {
        return yRelativePosProperty;
    }

    @Override
    public float xRelativePos()
    {
        return xRelativePosProperty().getValue();
    }

    public void setxRelativePos(float xRelativePos)
    {
        xRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float yRelativePos()
    {
        return yRelativePosProperty().getValue();
    }

    public void setyRelativePos(float yRelativePos)
    {
        yRelativePosProperty().setValue(yRelativePos);
    }

    @Override
    public float getWidth()
    {
        return transform().width();
    }

    @Override
    public float getHeight()
    {
        return transform().height();
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

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

    @Override
    public GuiElement getRootElement()
    {
        return this;
    }

    @Override
    public void addFloating(Transform transform)
    {
        throw new UnsupportedOperationException("Cannot use floating transforms in a SubGuiScreen");
    }

    @Override
    public boolean removeFloating(Transform transform)
    {
        throw new UnsupportedOperationException("Cannot use floating transforms in a SubGuiScreen");
    }

    @Override
    public Collection<Transform> getFloatingList()
    {
        return Collections.emptyList();
    }

    @Override
    public void open()
    {
        getEventDispatcher().singletonQueue().dispatch(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        getEventDispatcher().singletonQueue().dispatch(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

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
}