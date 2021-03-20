package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.specific.FloatBinding;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.FloatProperty;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.data.Position;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.KeyEvent.Press;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.event.TransformLayoutEvent;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.policy.GuiScrollbarPolicy;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.util.MathUtils;

import static java.lang.Integer.signum;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Scrollable extends GuiComponent implements RenderComponent
{
    private final FloatProperty trueWidthProperty  = new FloatProperty();
    private final FloatProperty trueHeightProperty = new FloatProperty();

    private final FloatProperty gripXWidthProperty  = createRenderPropertyFloat(0F);
    private final FloatProperty gripXHeightProperty = createRenderPropertyFloat(5F);
    private final FloatProperty gripYWidthProperty  = createRenderPropertyFloat(20F);
    private final FloatProperty gripYHeightProperty = createRenderPropertyFloat(0F);

    private final Property<GuiScrollbarPolicy> scrollXPolicyProperty = createRenderProperty(GuiScrollbarPolicy.NEEDED);
    private final Property<GuiScrollbarPolicy> scrollYPolicyProperty = createRenderProperty(GuiScrollbarPolicy.NEEDED);

    private final FloatProperty scrollSpeedProperty = new FloatProperty(1F);
    private final FloatProperty panSpeedProperty    = new FloatProperty(1F);

    private boolean isPannable;
    private boolean isScalable;
    private boolean isScrollable = true;

    private float dragStartX;
    private float dragStartY;

    private GuiElement gripXComponent;
    private GuiElement gripYComponent;

    private GuiElement trackXComponent;
    private GuiElement trackYComponent;


    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        getEventDispatcher().addHandler(TransformLayoutEvent.TYPE, this::onLayoutChange);

        getEventDispatcher().addHandler(ScrollEvent.TYPE, this::onScroll);
        getEventDispatcher().addHandler(ClickPressEvent.TYPE, this::onClick);
        getEventDispatcher().addHandler(KeyEvent.PRESS, this::onScrollKey);

        getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, this::onMouseDragStart);
        getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, this::onMouseDrag);

        createTrackY();
        createGripY();
    }

    private void onScrollKey(Press event)
    {
        if (event.getKey() == keyboard().getKeyCode("UP"))
        {
            scrollY(min(0, scrollY() + 10 * scrollSpeed()));
        }
        else if (event.getKey() == keyboard().getKeyCode("DOWN"))
        {
            scrollY(max(transform().height() - trueHeight(), scrollY() - 10 * scrollSpeed()));
        }
        else if (event.getKey() == keyboard().getKeyCode("PAGE_UP"))
        {
            scrollY(min(0, scrollY() + transform().height()));
        }
        else if (event.getKey() == keyboard().getKeyCode("PAGE_DOWN"))
        {
            scrollY(max(transform().height() - trueHeight(), scrollY() - transform().height()));
        }
        else if (event.getKey() == keyboard().getKeyCode("HOME"))
        {
            scrollY(0);
        }
        else if (event.getKey() == keyboard().getKeyCode("END"))
        {
            scrollY(transform().height() - trueHeight());
        }
    }

    private void createTrackY()
    {
        transform().addChild((trackYComponent = new Rectangle()).transform());
        trackYComponent.paint().backgroundColor(Color.GRAY);

        trackYComponent.transform().xPosProperty().bindProperty(transform().xPosProperty().combine(transform().xTranslateProperty(),
                transform().widthProperty(),
                trackYComponent.transform().widthProperty(),
                (xPos, xTranslate, width, trackWidth) -> xPos + xTranslate + width - trackWidth));
        trackYComponent.transform().yPosProperty().bindProperty(transform().yPosProperty().combine(transform().yTranslateProperty(), Float::sum));

        trackYComponent.width(8);
        trackYComponent.transform().heightProperty().bindProperty(transform().heightProperty());

        trackYComponent.getEventDispatcher().addHandler(ClickPressEvent.TYPE, e ->
        {
            float ratio = max(0, (e.getMouseY() - trackYComponent.getTopPos()) / trackYComponent.height());
            scrollY(-(trueHeight() - transform().height()) * ratio);
        });
    }

    public void createGripY()
    {
        transform().addChild((gripYComponent = new Rectangle()).transform());

        gripYComponent.getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, event ->
        {
            dragStartY = scrollY();
        });

        gripYComponent.getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, event ->
        {
            System.out.println(-dragStartY + event.getDragY());
            System.out.println(scrollY());
/*            scrollY(MathUtils.clamp(0,
                    transform().height() - trueHeight(),
                    -dragStartY + event.getDragY()));*/
            scrollY(-MathUtils.clamp(0, trueHeight() - transform().height(), -dragStartY + event.getDragY()));
        });

        gripYComponent.getEventDispatcher().addHandler(GuiMouseEvent.DRAG_STOP, event ->
        {
            System.out.println("STOPPED");
        });

        gripYComponent.paint().backgroundColor(Color.LIGHT_GRAY);

        gripYComponent.transform().xPosProperty().bindProperty(transform().xPosProperty().combine(transform().xTranslateProperty(),
                transform().widthProperty(),
                gripYComponent.transform().widthProperty(),
                (xPos, xTranslate, width, gripWidth) -> xPos + xTranslate + width - gripWidth));

        gripYComponent.transform().yPosProperty().bindProperty(new FloatBinding()
        {
            {
                super.bind(trackYComponent.transform().yPosProperty(),
                        trackYComponent.transform().yTranslateProperty(),
                        scrollYProperty(),
                        trackYComponent.transform().heightProperty(),
                        gripYComponent.transform().heightProperty(),
                        trueHeightProperty(),
                        transform().heightProperty());
            }

            @Override
            protected float computeValue()
            {
                return trackYComponent.transform().topPos() - scrollY() / (trueHeight() - transform().height()) * (trackYComponent.height() - gripYComponent.height());
            }
        });

        gripYComponent.width(8);
        gripYComponent.transform().heightProperty().bindProperty(trueHeightProperty().combine(
                transform().heightProperty(),
                (trueHeight, height) -> max(16, 0.9F * height * (height / trueHeight))));
    }

    private void onLayoutChange(TransformLayoutEvent event)
    {
        float maxX = 0;
        float maxY = 0;
        for (Transform child : transform().children())
        {
            if (child.rightPos() > maxX)
                maxX = child.rightPos() - transform().leftPos();
            if (child.bottomPos() > maxY)
                maxY = child.bottomPos() - transform().topPos();
        }

        trueWidthProperty().set(maxX);
        trueHeightProperty().set(maxY);
    }

    @Override
    public void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {

    }

    private void onMouseDragStart(GuiMouseEvent.DragStart event)
    {
        if (!pannable())
            return;

        dragStartX = scrollX();
        dragStartY = scrollY();
    }

    private void handlePanning(GuiMouseEvent.Dragging event)
    {
        if (trueWidth() > transform().width())
        {
            scrollX(MathUtils.clamp(
                    transform().width() - trueWidth(),
                    0,
                    dragStartX + event.getDragX() * panSpeed()));
        }
        if (trueHeight() > transform().height())
        {
            scrollY(MathUtils.clamp(transform().height() - trueHeight(),
                    0,
                    dragStartY + event.getDragY() * panSpeed()));
        }
    }

    private void onMouseDrag(GuiMouseEvent.Dragging event)
    {
        if (pannable())
            handlePanning(event);

/*        // Min X to select the vertical grip
        float gripYMinX = transform().rightPos() - gripYWidth();

        if (scrollYPolicy() != GuiScrollbarPolicy.NEVER &&
                trueHeight() > transform().height() && event.getMouseX() - event.getDragX() > gripYMinX)
        {
            float ratio =
                    (event.getMouseY() - transform().yPos() - transform().yTranslate()) / transform().height();
            float minY = transform().topPos();
            float maxY = minY + transform().height();

            if (event.getMouseY() > minY && event.getMouseY() < maxY)
                scrollY((trueHeight() - transform().height()) * -ratio);
            else if (event.getMouseY() <= minY)
                scrollY(0);
            else
                scrollY(-(trueHeight() - transform().height()));
        }

        // Min Y to select the horizontal grip
        float gripXMinY = transform().bottomPos() - gripXHeight();

        if (scrollXPolicy() != GuiScrollbarPolicy.NEVER &&
                trueWidth() > transform().width() && event.getMouseY() - event.getDragY() > gripXMinY)
        {
            float ratio =
                    (event.getMouseX() - transform().xPos() - transform().xTranslate()) / transform().width();
            float minX = transform().leftPos();
            float maxX = minX + transform().width();

            if (event.getMouseX() > minX && event.getMouseX() < maxX)
                scrollX((trueWidth() - transform().width()) * -ratio);
            else if (event.getMouseX() <= minX)
                scrollX(0);
            else
                scrollX(-(trueWidth() - transform().width()));
        }*/
    }

    private void onClick(ClickPressEvent event)
    {
        // Min X to select the vertical grip
        float gripYMinX = transform().rightPos() - gripYWidth();

        if (scrollYPolicy() != GuiScrollbarPolicy.NEVER &&
                trueHeight() > transform().height() && event.getMouseX() > gripYMinX)
        {
            float ratio =
                    (event.getMouseY() - transform().yPos() - transform().yTranslate()) / transform().height();
            scrollY((trueHeight() - transform().height()) * -ratio);
        }

        // Min Y to select the horizontal grip
        float gripXMinY = transform().bottomPos() - gripXHeight();
        if (scrollXPolicy() != GuiScrollbarPolicy.NEVER &&
                trueWidth() > transform().width() && event.getMouseY() > gripXMinY)
        {
            float ratio =
                    (event.getMouseX() - transform().xPos() - transform().xTranslate()) / transform().width();
            scrollX((trueWidth() - transform().width()) * -ratio);
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
            if (transform().height() >= trueHeight())
                return;

            scrolled = event.scrollY() * 5 * scrollSpeed();
            if (scrollY() + scrolled <= transform().height() - trueHeight()
                    && event.scrollY() < 0)
                scrolled = transform().height() - trueHeight() - scrollY();
            if (scrollY() + scrolled >= 0 && event.scrollY() > 0)
                scrolled = 0 - scrollY();

            scrollY(scrollY() + scrolled);
        }
        else
        {
            if (transform().width() >= trueWidth())
                return;

            scrolled = event.scrollX() * 10F * scrollSpeed();
            if (scrollX() + scrolled <= transform().width() - trueWidth()
                    && event.scrollX() < 0)
                scrolled = transform().width() - trueWidth() - scrollX();
            if (scrollX() + scrolled >= 0 && event.scrollX() > 0)
                scrolled = 0 - scrollX();

            scrollX(scrollX() + scrolled);
        }
    }

    private void onScroll(ScrollEvent event)
    {
        if (scalable())
            handleScale(event);
        else if (scrollable())
            handleScroll(event);
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public FloatProperty scrollXProperty()
    {
        return transform().xOffsetProperty();
    }

    public FloatProperty scrollYProperty()
    {
        return transform().yOffsetProperty();
    }

    public FloatProperty trueWidthProperty()
    {
        return trueWidthProperty;
    }

    public FloatProperty trueHeightProperty()
    {
        return trueHeightProperty;
    }

    public FloatProperty scrollSpeedProperty()
    {
        return scrollSpeedProperty;
    }

    public FloatProperty gripXWidthProperty()
    {
        return gripXWidthProperty;
    }

    public FloatProperty gripXHeightProperty()
    {
        return gripXHeightProperty;
    }

    public FloatProperty gripYWidthProperty()
    {
        return gripYWidthProperty;
    }

    public FloatProperty gripYHeightProperty()
    {
        return gripYHeightProperty;
    }

    public FloatProperty panSpeedProperty()
    {
        return panSpeedProperty;
    }

    public Property<GuiScrollbarPolicy> scrollXPolicyProperty()
    {
        return scrollXPolicyProperty;
    }

    public Property<GuiScrollbarPolicy> scrollYPolicyProperty()
    {
        return scrollYPolicyProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public float scrollX()
    {
        return scrollXProperty().getValue();
    }

    public void scrollX(float scrollX)
    {
        scrollXProperty().setValue(scrollX);
    }

    public float scrollY()
    {
        return scrollYProperty().getValue();
    }

    public void scrollY(float scrollY)
    {
        scrollYProperty().setValue(scrollY);
    }

    public float trueWidth()
    {
        return trueWidthProperty().getValue();
    }

    public float trueHeight()
    {
        return trueHeightProperty().getValue();
    }


    public GuiScrollbarPolicy scrollXPolicy()
    {
        return scrollXPolicyProperty().getValue();
    }

    public void scrollXPolicy(GuiScrollbarPolicy policy)
    {
        scrollXPolicyProperty().setValue(policy);
    }

    public GuiScrollbarPolicy scrollYPolicy()
    {
        return scrollYPolicyProperty().getValue();
    }

    public void scrollYPolicy(GuiScrollbarPolicy policy)
    {
        scrollYPolicyProperty().setValue(policy);
    }

    /**
     * @return the ScrollSpeed value of the ScrollPane. It represent the multiplier
     * used on the mouse delta when scrolling. Default is 1.
     */
    public float scrollSpeed()
    {
        return scrollSpeedProperty().getValue();
    }

    public void scrollSpeed(float scrollSpeed)
    {
        scrollSpeedProperty().setValue(scrollSpeed);
    }

    public float gripXWidth()
    {
        return gripXWidthProperty.getValue();
    }

    /**
     * Forcefully set the width of the horizontal grip used to scroll
     *
     * @param gripXWidth value of the fixed width. Setting it to 0 will reset it to automatic.
     */
    public void gripXWidth(float gripXWidth)
    {
        gripXWidthProperty.setValue(gripXWidth);
    }

    public float gripXHeight()
    {
        return gripXHeightProperty.getValue();
    }

    public void gripXHeight(float gripXHeight)
    {
        gripXHeightProperty.setValue(gripXHeight);
    }

    public float gripYWidth()
    {
        return gripYWidthProperty.getValue();
    }

    public void gripYWidth(float gripYWidth)
    {
        gripYWidthProperty.setValue(gripYWidth);
    }

    public float gripYHeight()
    {
        return gripYHeightProperty.getValue();
    }

    /**
     * Forcefully set the height of the vertical grip used to scroll
     *
     * @param gripYHeight value of the fixed height. Setting it to 0 will reset it to automatic.
     */
    public void gripYHeight(float gripYHeight)
    {
        gripYHeightProperty.setValue(gripYHeight);
    }

    public float panSpeed()
    {
        return panSpeedProperty().getValue();
    }

    public void panSpeed(float panSpeed)
    {
        panSpeedProperty().setValue(panSpeed);
    }

    public boolean pannable()
    {
        return isPannable;
    }

    public void pannable(boolean pannable)
    {
        isPannable = pannable;
    }

    /**
     * Allow scroll controls to alter this element scale.
     * This allow a zooming behavior on mousewheel.
     * <p>
     * When a Scrollable element is Scalable it will no longer be possible to scroll with the mousewheel.
     * If translating the Scrollable is still desired it is recommended to set the element as pannable @see #setPannable(boolean)
     */
    public boolean scalable()
    {
        return isScalable;
    }

    /**
     * Allow scroll controls to alter this element scale.
     * This allow a zooming behavior on mousewheel.
     * <p>
     * When a Scrollable element is Scalable it will no longer be possible to scroll with the mousewheel.
     * If translating the Scrollable is still desired it is recommended to set the element as pannable @see #setPannable(boolean)
     *
     * @param scalable
     */
    public void scalable(boolean scalable)
    {
        isScalable = scalable;
    }

    public boolean scrollable()
    {
        return isScrollable;
    }

    public void scrollable(boolean scrollable)
    {
        isScrollable = scrollable;
    }
}
