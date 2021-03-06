package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollEvent extends GuiInputEvent
{
    public static final EventType<ScrollEvent> TYPE = new EventType<>(ANY, "INPUT_SCROLL_EVENT");

    private final float mouseX;
    private final float mouseY;
    private final float scrollX;
    private final float scrollY;

    public ScrollEvent(GuiElement source, float mouseX, float mouseY, float scrollX, float scrollY)
    {
        super(source);

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.scrollX = scrollX;
        this.scrollY = scrollY;
    }

    public float scrollX()
    {
        return scrollX;
    }

    public float scrollY()
    {
        return scrollY;
    }

    public float mouseX()
    {
        return mouseX;
    }

    public float mouseY()
    {
        return mouseY;
    }

    @Override
    public ScrollEvent copy(IEventEmitter source)
    {
        return new ScrollEvent((GuiElement) source, mouseX(), mouseY(), scrollX(), scrollY());
    }
}