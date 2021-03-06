package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.control.GuiScrollableBase;
import net.voxelindustry.brokkgui.data.Position;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.policy.GuiScrollbarPolicy;
import net.voxelindustry.brokkgui.util.MathUtils;

import static java.lang.Integer.signum;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollableBehavior<C extends GuiScrollableBase> extends GuiBehaviorBase<C>
{
    private float dragStartX;
    private float dragStartY;

    public GuiScrollableBehavior(C model)
    {
        super(model);

        getModel().getEventDispatcher().addHandler(ScrollEvent.TYPE, this::onScroll);
        getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);

        getModel().getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, this::onMouseDragStart);
        getModel().getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, this::onMouseDrag);
    }

    private void onMouseDragStart(GuiMouseEvent.DragStart event)
    {
        if (!getModel().isPannable())
            return;

        dragStartX = getModel().getScrollX();
        dragStartY = getModel().getScrollY();
    }

    private void handlePanning(GuiMouseEvent.Dragging event)
    {
        if (getModel().getTrueWidth() > transform().width())
        {
            getModel().setScrollX(MathUtils.clamp(
                    transform().width() - getModel().getTrueWidth(),
                    0,
                    dragStartX + event.getDragX() * getModel().getPanSpeed()));
        }
        if (getModel().getTrueHeight() > transform().height())
        {
            getModel().setScrollY(MathUtils.clamp(transform().height() - getModel().getTrueHeight(),
                    0,
                    dragStartY + event.getDragY() * getModel().getPanSpeed()));
        }
    }

    private void onMouseDrag(GuiMouseEvent.Dragging event)
    {
        if (getModel().isPannable())
            handlePanning(event);

        // Min X to select the vertical grip
        float gripYMinX = getModel().getRightPos() - getModel().getGripYWidth();

        if (getModel().getScrollYPolicy() != GuiScrollbarPolicy.NEVER &&
                getModel().getTrueHeight() > transform().height() && event.getMouseX() - event.getDragX() > gripYMinX)
        {
            float ratio =
                    (event.getMouseY() - transform().yPos() - transform().yTranslate()) / transform().height();
            float minY = getModel().getTopPos();
            float maxY = minY + transform().height();

            if (event.getMouseY() > minY && event.getMouseY() < maxY)
                getModel().setScrollY((getModel().getTrueHeight() - transform().height()) * -ratio);
            else if (event.getMouseY() <= minY)
                getModel().setScrollY(0);
            else
                getModel().setScrollY(-(getModel().getTrueHeight() - transform().height()));
        }

        // Min Y to select the horizontal grip
        float gripXMinY = getModel().getBottomPos() - getModel().getGripXHeight();

        if (getModel().getScrollXPolicy() != GuiScrollbarPolicy.NEVER &&
                getModel().getTrueWidth() > transform().width() && event.getMouseY() - event.getDragY() > gripXMinY)
        {
            float ratio =
                    (event.getMouseX() - transform().xPos() - transform().xTranslate()) / transform().width();
            float minX = getModel().getLeftPos();
            float maxX = minX + transform().width();

            if (event.getMouseX() > minX && event.getMouseX() < maxX)
                getModel().setScrollX((getModel().getTrueWidth() - transform().width()) * -ratio);
            else if (event.getMouseX() <= minX)
                getModel().setScrollX(0);
            else
                getModel().setScrollX(-(getModel().getTrueWidth() - transform().width()));
        }
    }

    private void onClick(ClickEvent event)
    {
        // Min X to select the vertical grip
        float gripYMinX = getModel().getRightPos() - getModel().getGripYWidth();

        if (getModel().getScrollYPolicy() != GuiScrollbarPolicy.NEVER &&
                getModel().getTrueHeight() > transform().height() && event.getMouseX() > gripYMinX)
        {
            float ratio =
                    (event.getMouseY() - transform().yPos() - transform().yTranslate()) / transform().height();
            getModel().setScrollY((getModel().getTrueHeight() - transform().height()) * -ratio);
        }

        // Min Y to select the horizontal grip
        float gripXMinY = getModel().getBottomPos() - getModel().getGripXHeight();
        if (getModel().getScrollXPolicy() != GuiScrollbarPolicy.NEVER &&
                getModel().getTrueWidth() > transform().width() && event.getMouseY() > gripXMinY)
        {
            float ratio =
                    (event.getMouseX() - transform().xPos() - transform().xTranslate()) / transform().width();
            getModel().setScrollX((getModel().getTrueWidth() - transform().width()) * -ratio);
        }
    }

    private void handleScale(ScrollEvent event)
    {
        transform().childrenProperty().getModifiableValue().forEach(child ->
        {
            float zoomValue = 0.05F * signum((int) event.scrollY());

            child.scalePivot(Position.absolute(event.mouseX() - child.leftPos(), event.mouseY() - child.topPos()));
            child.scale(child.scaleX() + zoomValue);
        });
    }

    private void handleScroll(ScrollEvent event)
    {
        float scrolled;
        boolean vertical = event.scrollY() != 0 || !BrokkGuiPlatform.getInstance().getKeyboardUtil().isShiftKeyDown();

        if (vertical)
        {
            if (transform().height() >= getModel().getTrueHeight())
                return;

            scrolled = event.scrollY() / 10F * getModel().getScrollSpeed();
            if (getModel().getScrollY() + scrolled <= transform().height() - getModel().getTrueHeight()
                    && event.scrollY() < 0)
                scrolled = transform().height() - getModel().getTrueHeight() - getModel().getScrollY();
            if (getModel().getScrollY() + scrolled >= 0 && event.scrollY() > 0)
                scrolled = 0 - getModel().getScrollY();

            getModel().setScrollY(getModel().getScrollY() + scrolled);
        }
        else
        {
            if (transform().width() >= getModel().getTrueWidth())
                return;

            scrolled = event.scrollX() / 10F * getModel().getScrollSpeed();
            if (getModel().getScrollX() + scrolled <= transform().width() - getModel().getTrueWidth()
                    && event.scrollX() < 0)
                scrolled = transform().width() - getModel().getTrueWidth() - getModel().getScrollX();
            if (getModel().getScrollX() + scrolled >= 0 && event.scrollX() > 0)
                scrolled = 0 - getModel().getScrollX();

            getModel().setScrollX(getModel().getScrollX() + scrolled);
        }
    }

    private void onScroll(ScrollEvent event)
    {
        if (getModel().isScalable())
        {
            handleScale(event);
        }
        else if (getModel().isScrollable())
        {
            handleScroll(event);
        }
    }
}