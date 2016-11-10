package fr.ourten.brokkgui.gui;

import fr.ourten.brokkgui.control.GuiFather;
import fr.ourten.brokkgui.event.WindowEvent;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.hermod.EventHandler;

public class SubGuiScreen extends GuiFather implements IGuiWindow
{
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private boolean                         closeOnClick;
    private boolean                         hasWarFog;

    public SubGuiScreen()
    {

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

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    @Override
    public void open()
    {
        this.getEventDispatcher().dispatchEvent(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        this.getEventDispatcher().dispatchEvent(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        super.renderNode(renderer, pass, mouseX, mouseY);
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