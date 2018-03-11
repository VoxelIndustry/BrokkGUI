package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.control.GuiScrollableBase;
import org.yggard.brokkgui.event.GuiMouseEvent;
import org.yggard.brokkgui.event.ScrollEvent;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollableBehavior<C extends GuiScrollableBase> extends GuiBehaviorBase<C>
{
    public GuiScrollableBehavior(final C model)
    {
        super(model);

        this.getModel().getEventDispatcher().addHandler(GuiMouseEvent.WHEEL, this::onMouseWheel);
    }

    private void onMouseWheel(final GuiMouseEvent.Wheel event)
    {
        float scrolled;
        final boolean vertical = !BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown();

        if (vertical)
        {
            if (this.getModel().getHeight() >= this.getModel().getTrueHeight())
                return;

            scrolled = event.getDwheel() / 10 * this.getModel().getScrollSpeed();
            if (this.getModel().getScrollY() + scrolled <= this.getModel().getHeight() - this.getModel().getTrueHeight()
                    && event.getDwheel() < 0)
                scrolled = this.getModel().getHeight() - this.getModel().getTrueHeight() - this.getModel().getScrollY();
            if (this.getModel().getScrollY() + scrolled >= 0 && event.getDwheel() > 0)
                scrolled = 0 - this.getModel().getScrollY();
            this.getModel().setScrollY(this.getModel().getScrollY() + scrolled);
        }
        else
        {
            if (this.getModel().getWidth() >= this.getModel().getTrueWidth())
                return;

            scrolled = event.getDwheel() / 10 * this.getModel().getScrollSpeed();
            if (this.getModel().getScrollX() + scrolled <= this.getModel().getWidth() - this.getModel().getTrueWidth()
                    && event.getDwheel() < 0)
                scrolled = this.getModel().getWidth() - this.getModel().getTrueWidth() - this.getModel().getScrollX();
            if (this.getModel().getScrollX() + scrolled >= 0 && event.getDwheel() > 0)
                scrolled = 0 - this.getModel().getScrollX();
            this.getModel().setScrollX(this.getModel().getScrollX() + scrolled);
        }

        this.getModel().getEventDispatcher().dispatchEvent(ScrollEvent.TYPE,
                new ScrollEvent(this.getModel(), vertical ? 0 : scrolled, vertical ? scrolled : 0));
    }
}