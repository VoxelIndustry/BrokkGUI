package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

public class SubGuiScreen extends GuiFather implements IGuiSubWindow
{
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private final Property<Float> xRelativePosProperty, yRelativePosProperty;

    private boolean closeOnClick;
    private boolean hasWarFog;

    public SubGuiScreen(float xRelativePos, float yRelativePos)
    {
        transform().zLevel(300);

        xRelativePosProperty = new Property<>(xRelativePos);
        yRelativePosProperty = new Property<>(yRelativePos);

        setWindow(this);
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
    public Property<Float> getxRelativePosProperty()
    {
        return xRelativePosProperty;
    }

    @Override
    public Property<Float> getyRelativePosProperty()
    {
        return yRelativePosProperty;
    }

    @Override
    public float getxRelativePos()
    {
        return getxRelativePosProperty().getValue();
    }

    public void setxRelativePos(float xRelativePos)
    {
        getxRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float getyRelativePos()
    {
        return getyRelativePosProperty().getValue();
    }

    public void setyRelativePos(float yRelativePos)
    {
        getyRelativePosProperty().setValue(yRelativePos);
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

    @Override
    public GuiElement getRootElement()
    {
        return this;
    }

    @Override
    public void open()
    {
        getEventDispatcher().dispatchEvent(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        dispose();
        getEventDispatcher().dispatchEvent(WindowEvent.CLOSE, new WindowEvent.Close(this));
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