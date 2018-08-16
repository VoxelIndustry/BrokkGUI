package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.control.GuiScrollableBase;
import org.yggard.brokkgui.event.ClickEvent;
import org.yggard.brokkgui.event.GuiMouseEvent;
import org.yggard.brokkgui.event.ScrollEvent;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollableBehavior<C extends GuiScrollableBase> extends GuiBehaviorBase<C>
{
    public GuiScrollableBehavior(C model)
    {
        super(model);

        this.getModel().getEventDispatcher().addHandler(GuiMouseEvent.WHEEL, this::onMouseWheel);
        this.getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);
        this.getModel().getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, this::onMouseDrag);
    }

    private void onMouseDrag(GuiMouseEvent.Dragging event)
    {
        // Min X to select the vertical grip
        float gripYMinX =
                getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth() - getModel().getGripYWidth();

        if (getModel().getTrueHeight() > getModel().getHeight() && event.getMouseX() - event.getDragX() > gripYMinX)
        {
            float ratio =
                    (event.getMouseY() - getModel().getyPos() - getModel().getyTranslate()) / getModel().getHeight();
            float minY = getModel().getyPos() + getModel().getyTranslate();
            float maxY = minY + getModel().getHeight();

            if (event.getMouseY() > minY && event.getMouseY() < maxY)
                getModel().setScrollY((getModel().getTrueHeight() - getModel().getHeight()) * -ratio);
            else if (event.getMouseY() <= minY)
                getModel().setScrollY(0);
            else
                getModel().setScrollY(-(getModel().getTrueHeight() - getModel().getHeight()));
        }

        // Min Y to select the horizontal grip
        float gripXMinY =
                getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight() - getModel().getGripXHeight();

        if (getModel().getTrueWidth() > getModel().getWidth() && event.getMouseY() - event.getDragY() > gripXMinY)
        {
            float ratio =
                    (event.getMouseX() - getModel().getxPos() - getModel().getxTranslate()) / getModel().getWidth();
            float minX = getModel().getxPos() + getModel().getxTranslate();
            float maxX = minX + getModel().getWidth();

            if (event.getMouseX() > minX && event.getMouseX() < maxX)
                getModel().setScrollX((getModel().getTrueWidth() - getModel().getWidth()) * -ratio);
            else if (event.getMouseX() <= minX)
                getModel().setScrollX(0);
            else
                getModel().setScrollX(-(getModel().getTrueWidth() - getModel().getWidth()));
        }
    }

    private void onClick(ClickEvent event)
    {
        // Min X to select the vertical grip
        float gripYMinX =
                getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth() - getModel().getGripYWidth();

        if (getModel().getTrueHeight() > getModel().getHeight() && event.getMouseX() > gripYMinX)
        {
            float ratio =
                    (event.getMouseY() - getModel().getyPos() - getModel().getyTranslate()) / getModel().getHeight();
            getModel().setScrollY((getModel().getTrueHeight() - getModel().getHeight()) * -ratio);
        }

        // Min Y to select the horizontal grip
        float gripXMinY =
                getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight() - getModel().getGripXHeight();
        if (getModel().getTrueWidth() > getModel().getWidth() && event.getMouseY() > gripXMinY)
        {
            float ratio =
                    (event.getMouseX() - getModel().getxPos() - getModel().getxTranslate()) / getModel().getWidth();
            getModel().setScrollX((getModel().getTrueWidth() - getModel().getWidth()) * -ratio);
        }
    }

    private void onMouseWheel(GuiMouseEvent.Wheel event)
    {
        float scrolled;
        boolean vertical = !BrokkGuiPlatform.getInstance().getKeyboardUtil().isShiftKeyDown();

        if (vertical)
        {
            if (this.getModel().getHeight() >= this.getModel().getTrueHeight())
                return;

            scrolled = event.getDwheel() / 10f * this.getModel().getScrollSpeed();
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

            scrolled = event.getDwheel() / 10f * this.getModel().getScrollSpeed();
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