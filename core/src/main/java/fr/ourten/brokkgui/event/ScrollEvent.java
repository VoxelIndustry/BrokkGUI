package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.hermod.EventType;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollEvent extends GuiInputEvent
{
    public static final EventType<ScrollEvent> TYPE = new EventType<>(GuiInputEvent.ANY, "INPUT_SCROLL_EVENT");

    private final float                        scrollX, scrollY;

    public ScrollEvent(final GuiNode source, final float scrollX, final float scrollY)
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
}