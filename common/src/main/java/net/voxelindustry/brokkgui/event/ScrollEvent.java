package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollEvent extends GuiInputEvent
{
    public static final EventType<ScrollEvent> TYPE = new EventType<>(ANY, "INPUT_SCROLL_EVENT");

    private final float scrollX, scrollY;

    public ScrollEvent(GuiNode source, float scrollX, float scrollY)
    {
        super(source);

        this.scrollX = scrollX;
        this.scrollY = scrollY;
    }

    public float getScrollX()
    {
        return this.scrollX;
    }

    public float getScrollY()
    {
        return this.scrollY;
    }

    @Override
    public ScrollEvent copy(IEventEmitter source)
    {
        return new ScrollEvent((GuiNode) source, getScrollX(), getScrollY());
    }
}