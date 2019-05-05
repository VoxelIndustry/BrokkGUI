package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.event.WindowEvent;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

public class SubGuiScreen extends GuiFather implements IGuiSubWindow
{
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private final BaseProperty<Float> xRelativePosProperty, yRelativePosProperty;

    private boolean closeOnClick;
    private boolean hasWarFog;

    public SubGuiScreen(float xRelativePos, float yRelativePos)
    {
        super("subscreen");
        this.setzLevel(300);

        this.xRelativePosProperty = new BaseProperty<>(xRelativePos, "xRelativePosProperty");
        this.yRelativePosProperty = new BaseProperty<>(yRelativePos, "yRelativePosProperty");

        this.setWindow(this);
    }

    public SubGuiScreen()
    {
        this(0, 0);
    }

    /**
     * @return if the subgui can be closed with a click outside his area.
     */
    public boolean closeOnClick()
    {
        return this.closeOnClick;
    }

    public void setCloseOnClick(final boolean closeOnClick)
    {
        this.closeOnClick = closeOnClick;
    }

    /**
     * @return if the subgui shadow everything outside his area.
     */
    public boolean hasWarFog()
    {
        return this.hasWarFog;
    }

    public void setWarFog(final boolean warFog)
    {
        this.hasWarFog = warFog;
    }

    @Override
    public BaseProperty<Float> getxRelativePosProperty()
    {
        return this.xRelativePosProperty;
    }

    @Override
    public BaseProperty<Float> getyRelativePosProperty()
    {
        return this.yRelativePosProperty;
    }

    @Override
    public float getxRelativePos()
    {
        return this.getxRelativePosProperty().getValue();
    }

    public void setxRelativePos(final float xRelativePos)
    {
        this.getxRelativePosProperty().setValue(xRelativePos);
    }

    @Override
    public float getyRelativePos()
    {
        return this.getyRelativePosProperty().getValue();
    }

    public void setyRelativePos(final float yRelativePos)
    {
        this.getyRelativePosProperty().setValue(yRelativePos);
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

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

    @Override
    public void open()
    {
        this.getEventDispatcher().dispatchEvent(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        this.dispose();
        this.getEventDispatcher().dispatchEvent(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

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
}