package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.specific.FloatBinding;
import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import fr.ourten.teabeans.property.specific.FloatProperty;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.Position;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.KeyEvent.Press;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.event.TransformLayoutEvent;
import net.voxelindustry.brokkgui.policy.GuiScrollbarPolicy;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.util.MathUtils;
import net.voxelindustry.hermod.HermodEvent;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nullable;

import static java.lang.Integer.signum;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Scrollable extends GuiComponent
{
    public static final Texture TRACK_LEFT_TEXTURE  = new Texture("brokkgui:textures/scroll/track_button_left.png");
    public static final Texture TRACK_RIGHT_TEXTURE = new Texture("brokkgui:textures/scroll/track_button_right.png");
    public static final Texture TRACK_UP_TEXTURE    = new Texture("brokkgui:textures/scroll/track_button_up.png");
    public static final Texture TRACK_DOWN_TEXTURE  = new Texture("brokkgui:textures/scroll/track_button_down.png");

    private final FloatProperty trueWidthProperty  = new FloatProperty();
    private final FloatProperty trueHeightProperty = new FloatProperty();

    private final FloatProperty gripXWidthProperty  = createRenderPropertyFloat(0F);
    private final FloatProperty gripXHeightProperty = createRenderPropertyFloat(5F);
    private final FloatProperty gripYWidthProperty  = createRenderPropertyFloat(20F);
    private final FloatProperty gripYHeightProperty = createRenderPropertyFloat(0F);

    private final Property<GuiScrollbarPolicy> scrollXPolicyProperty = createRenderProperty(GuiScrollbarPolicy.NEEDED);
    private final Property<GuiScrollbarPolicy> scrollYPolicyProperty = createRenderProperty(GuiScrollbarPolicy.NEEDED);

    private final Property<RectBox> paddingProperty = new Property<>(RectBox.EMPTY);

    private final FloatProperty scrollXProperty = new FloatProperty();
    private final FloatProperty scrollYProperty = new FloatProperty();

    private final FloatProperty scrollSpeedProperty = new FloatProperty(5F);
    private final FloatProperty panSpeedProperty    = new FloatProperty(1F);

    private final BooleanProperty showTrackButtonsProperty = new BooleanProperty(false);

    private boolean isPannable;
    private boolean isScalable;
    private boolean isScrollable = true;

    private float dragStartX;
    private float dragStartY;

    protected GuiElement gripX;
    protected GuiElement gripY;

    protected GuiElement trackX;
    protected GuiElement trackY;

    protected GuiElement trackXButtonLeft;
    protected GuiElement trackXButtonRight;

    protected GuiElement trackYButtonUp;
    protected GuiElement trackYButtonDown;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        element().setFocusable(true);

        transform().xOffsetProperty().bindProperty(scrollXProperty().combine(paddingProperty(),
                (scroll, padding) -> scroll.floatValue() + padding.getLeft()));
        transform().yOffsetProperty().bindProperty(scrollYProperty().combine(paddingProperty(),
                (scroll, padding) -> scroll.floatValue() + padding.getTop()));

        getEventDispatcher().addHandler(TransformLayoutEvent.TYPE, this::onLayoutChange);

        getEventDispatcher().addHandler(ScrollEvent.TYPE, this::onScroll);
        getEventDispatcher().addHandler(KeyEvent.PRESS, this::onScrollKey);

        getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, this::onMouseDragStart);
        getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, this::onMouseDrag);

        createTrackX();
        createGripX();
        createTrackY();
        createGripY();

        showTrackButtonsProperty().addChangeListener(obs ->
        {
            if (showTrackButtons())
            {
                if (trackXButtonLeft == null)
                {
                    createXTrackButtons();
                    createYTrackButtons();
                }
                else
                {
                    trackXButtonLeft.setVisible(true);
                    trackXButtonRight.setVisible(true);
                    trackYButtonUp.setVisible(true);
                    trackYButtonDown.setVisible(true);
                }
                bindTracksToButtons();
            }
            else
            {
                trackX.transform().xPosProperty().bindProperty(
                        transform().xPosProperty()
                                .add(transform().xTranslateProperty()));
                trackX.transform().widthProperty().bindProperty(transform().widthProperty());

                trackY.transform().yPosProperty().bindProperty(
                        transform().yPosProperty()
                                .add(transform().yTranslateProperty()));
                trackY.transform().heightProperty().bindProperty(transform().heightProperty());
                trackXButtonLeft.setVisible(false);
                trackXButtonRight.setVisible(false);
                trackYButtonUp.setVisible(false);
                trackYButtonDown.setVisible(false);
            }
        });

        if (!showTrackButtons())
        {
            trackX.transform().xPosProperty().bindProperty(
                    transform().xPosProperty()
                            .add(transform().xTranslateProperty()));
            trackX.transform().widthProperty().bindProperty(transform().widthProperty().subtract(trackY.transform().widthProperty()));

            trackY.transform().yPosProperty().bindProperty(
                    transform().yPosProperty()
                            .add(transform().yTranslateProperty()));
            trackY.transform().heightProperty().bindProperty(transform().heightProperty());
        }
        else
        {
            createXTrackButtons();
            createYTrackButtons();
            bindTracksToButtons();
        }

        ValueInvalidationListener widthListener = obs ->
        {
            if (scrollXPolicy() == GuiScrollbarPolicy.NEEDED)
            {
                boolean visible = trueWidth() > transform().width();
                trackX.setVisible(visible);
                // Check due to concurrent evaluation of listener while track buttons are being created
                if (showTrackButtons() && trackXButtonLeft != null && trackXButtonRight != null)
                {
                    trackXButtonLeft.setVisible(visible);
                    trackXButtonRight.setVisible(visible);
                }
            }
        };
        transform().widthProperty().addChangeListener(widthListener);
        trueWidthProperty().addChangeListener(widthListener);

        ValueInvalidationListener heightListener = obs ->
        {
            if (scrollYPolicy() == GuiScrollbarPolicy.NEEDED)
            {
                boolean visible = trueHeight() > transform().height();
                trackY.setVisible(visible);
                // Check due to concurrent evaluation of listener while track buttons are being created
                if (showTrackButtons() && trackYButtonUp != null && trackYButtonDown != null)
                {
                    trackYButtonUp.setVisible(visible);
                    trackYButtonDown.setVisible(visible);
                }
            }
        };
        transform().heightProperty().addChangeListener(heightListener);
        trueHeightProperty().addChangeListener(heightListener);

        scrollXPolicyProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (newValue == GuiScrollbarPolicy.NEVER && (oldValue == GuiScrollbarPolicy.ALWAYS || trueWidth() > transform().width()))
            {
                trackX.setVisible(false);
                if (showTrackButtons())
                {
                    trackXButtonLeft.setVisible(false);
                    trackXButtonRight.setVisible(false);
                }
            }
            else if (newValue != GuiScrollbarPolicy.NEVER && oldValue == GuiScrollbarPolicy.NEVER)
            {
                trackX.setVisible(true);
                if (showTrackButtons())
                {
                    trackXButtonLeft.setVisible(true);
                    trackXButtonRight.setVisible(true);
                }
            }
        });
        scrollYPolicyProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (newValue == GuiScrollbarPolicy.NEVER && (oldValue == GuiScrollbarPolicy.ALWAYS || trueHeight() > transform().height()))
            {
                trackY.setVisible(false);
                if (showTrackButtons())
                {
                    trackYButtonUp.setVisible(false);
                    trackYButtonDown.setVisible(false);
                }
            }
            else if (newValue != GuiScrollbarPolicy.NEVER && oldValue == GuiScrollbarPolicy.NEVER)
            {
                trackY.setVisible(true);
                if (showTrackButtons())
                {
                    trackYButtonUp.setVisible(true);
                    trackYButtonDown.setVisible(true);
                }
            }
        });

        paddingProperty.addChangeListener(obs -> onLayoutChange(null));
    }

    private void onScrollKey(Press event)
    {
        if (event.getKey() == keyboard().getKeyCode("UP"))
        {
            scrollY(min(0, scrollY() + scrollSpeed()));
        }
        else if (event.getKey() == keyboard().getKeyCode("DOWN"))
        {
            scrollY(max(transform().height() - trueHeight(), scrollY() - scrollSpeed()));
        }
        else if (event.getKey() == keyboard().getKeyCode("LEFT"))
        {
            scrollX(min(0, scrollX() + scrollSpeed()));
        }
        else if (event.getKey() == keyboard().getKeyCode("RIGHT"))
        {
            scrollX(max(transform().width() - trueWidth(), scrollX() - scrollSpeed()));
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

    protected void createTrackX()
    {
        transform().addChild((trackX = new Rectangle()).transform());

        trackX.transform().visibleProperty().addChangeListener(obs -> System.out.println(trackX.isVisible()));
        trackX.paint().backgroundColor(Color.GRAY);

        trackX.transform().xPosProperty().bindProperty(transform().xPosProperty().add(transform().xTranslateProperty()));
        trackX.transform().yPosProperty().bindProperty(transform().yPosProperty().combine(transform().yTranslateProperty(),
                transform().heightProperty(),
                trackX.transform().heightProperty(),
                (yPos, yTranslate, height, trackHeight) -> yPos.floatValue() + yTranslate.floatValue() + height.floatValue() - trackHeight.floatValue()));

        trackX.height(8);

        trackX.getEventDispatcher().addHandler(ClickPressEvent.TYPE, e ->
        {
            float ratio = max(0, (e.getMouseX() - trackX.getLeftPos()) / trackX.width());
            scrollX(-(trueWidth() - transform().width()) * ratio);
        });

        if (scrollXPolicy() == GuiScrollbarPolicy.NEVER || (scrollXPolicy() == GuiScrollbarPolicy.NEEDED && trueWidth() <= transform().width()))
        {
            trackX.setVisible(false);
            if (showTrackButtons())
            {
                trackXButtonLeft.setVisible(false);
                trackXButtonRight.setVisible(false);
            }
        }
    }

    protected void createXTrackButtons()
    {
        transform().addChild((trackXButtonLeft = new Rectangle(8, 8)).transform());
        trackXButtonLeft.paint().backgroundTexture(TRACK_LEFT_TEXTURE);
        trackXButtonLeft.transform().xPosProperty().bindProperty(transform().xPosProperty().add(transform().xTranslateProperty()));
        trackXButtonLeft.transform().yPosProperty().bindProperty(transform().yPosProperty().combine(
                transform().yTranslateProperty(),
                transform().heightProperty(),
                trackXButtonLeft.transform().heightProperty(),
                (yPos, yTranslate, height, trackButtonHeight) -> yPos.floatValue() + yTranslate.floatValue() + height.floatValue() - trackButtonHeight.floatValue()));

        trackXButtonLeft.getEventDispatcher().addHandler(ClickPressEvent.TYPE, event -> scrollX(min(0, scrollX() + scrollSpeed())));

        transform().addChild((trackXButtonRight = new Rectangle(8, 8)).transform());
        trackXButtonRight.paint().backgroundTexture(TRACK_RIGHT_TEXTURE);
        trackXButtonRight.transform().xPosProperty().bindProperty(new FloatBinding()
        {
            {
                super.bind(transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        transform().widthProperty(),
                        trackXButtonRight.transform().widthProperty(),
                        trackY.transform().visibleProperty(),
                        trackY.transform().widthProperty()
                );
            }

            @Override
            protected float computeValue()
            {
                if (trackY.isVisible())
                    return transform().rightPos() - trackXButtonRight.width() - trackY.width();
                return transform().rightPos() - trackXButtonRight.width();
            }
        });

        trackXButtonRight.transform().yPosProperty().bindProperty(transform().yPosProperty().combine(
                transform().yTranslateProperty(),
                transform().heightProperty(),
                trackXButtonRight.transform().heightProperty(),
                (yPos, yTranslate, height, trackButtonHeight) -> yPos.floatValue() + yTranslate.floatValue() + height.floatValue() - trackButtonHeight.floatValue()));

        trackXButtonRight.getEventDispatcher().addHandler(ClickPressEvent.TYPE, event -> scrollX(max(transform().width() - trueWidth(), scrollX() - scrollSpeed())));
    }

    protected void createYTrackButtons()
    {
        transform().addChild((trackYButtonUp = new Rectangle(8, 8)).transform());
        trackYButtonUp.paint().backgroundTexture(TRACK_UP_TEXTURE);
        trackYButtonUp.transform().xPosProperty().bindProperty(transform().xPosProperty().combine(
                transform().xTranslateProperty(),
                transform().widthProperty(),
                trackYButtonUp.transform().widthProperty(),
                (xPos, xTranslate, width, trackButtonWidth) -> xPos.floatValue() + xTranslate.floatValue() + width.floatValue() - trackButtonWidth.floatValue()));
        trackYButtonUp.transform().yPosProperty().bindProperty(transform().yPosProperty().add(transform().yTranslateProperty()));

        trackYButtonUp.getEventDispatcher().addHandler(ClickPressEvent.TYPE, event -> scrollY(min(0, scrollY() + scrollSpeed())));

        transform().addChild((trackYButtonDown = new Rectangle(8, 8)).transform());
        trackYButtonDown.paint().backgroundTexture(TRACK_DOWN_TEXTURE);
        trackYButtonDown.transform().yPosProperty().bindProperty(transform().yPosProperty().combine(
                transform().yTranslateProperty(),
                transform().heightProperty(),
                trackYButtonDown.transform().heightProperty(),
                (yPos, yTranslate, height, trackButtonHeight) -> yPos.floatValue() + yTranslate.floatValue() + height.floatValue() - trackButtonHeight.floatValue()));
        trackYButtonDown.transform().xPosProperty().bindProperty(transform().xPosProperty().combine(
                transform().xTranslateProperty(),
                transform().widthProperty(),
                trackYButtonDown.transform().widthProperty(),
                (xPos, xTranslate, width, trackButtonWidth) -> xPos.floatValue() + xTranslate.floatValue() + width.floatValue() - trackButtonWidth.floatValue()));

        trackYButtonDown.getEventDispatcher().addHandler(ClickPressEvent.TYPE, event -> scrollY(max(transform().height() - trueHeight(), scrollY() - scrollSpeed())));
    }

    private void bindTracksToButtons()
    {
        trackX.transform().xPosProperty().bindProperty(transform().xPosProperty()
                .combine(transform().xTranslateProperty(), trackXButtonLeft.transform().widthProperty(),
                        (xPos, xTranslate, buttonWidth) -> xPos.floatValue() + xTranslate.floatValue() + buttonWidth.floatValue()));

        trackX.transform().widthProperty().bindProperty(new FloatBinding()
        {
            {
                super.bind(trackY.transform().visibleProperty(),
                        trackY.transform().widthProperty(),
                        transform().widthProperty(),
                        trackXButtonLeft.transform().widthProperty(),
                        trackXButtonRight.transform().widthProperty());
            }

            @Override
            protected float computeValue()
            {
                if (trackY.isVisible())
                    return transform().width() - trackXButtonLeft.width() - trackXButtonRight.width() - trackY.width();
                return transform().width() - trackXButtonLeft.width() - trackXButtonRight.width();
            }
        });

        trackY.transform().yPosProperty().bindProperty(transform().yPosProperty()
                .combine(transform().yTranslateProperty(), trackYButtonUp.transform().heightProperty(),
                        (yPos, yTranslate, buttonHeight) -> yPos.floatValue() + yTranslate.floatValue() + buttonHeight.floatValue()));
        trackY.transform().heightProperty().bindProperty(
                transform().heightProperty().combine(
                        trackYButtonUp.transform().heightProperty(),
                        trackYButtonDown.transform().heightProperty(),
                        (height, upButtonHeight, downButtonHeight) -> height.floatValue() - upButtonHeight.floatValue() - downButtonHeight.floatValue()));
    }

    public void createGripX()
    {
        trackX.transform().addChild((gripX = new Rectangle()).transform());

        gripX.getEventDispatcher().addHandler(ClickPressEvent.TYPE, HermodEvent::consume);

        gripX.getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, event -> dragStartX = scrollX());
        gripX.getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, event ->
        {
            float ratio = trueWidth() / transform().width();
            scrollX(-MathUtils.clamp(0, trueWidth() - transform().width(), -dragStartX + event.getDragX() * ratio));
        });

        gripX.paint().backgroundColor(Color.LIGHT_GRAY);

        gripX.transform().yPosProperty().bindProperty(transform().yPosProperty().combine(transform().yTranslateProperty(),
                transform().heightProperty(),
                gripX.transform().heightProperty(),
                (yPos, yTranslate, height, gripHeight) -> yPos.floatValue() + yTranslate.floatValue() + height.floatValue() - gripHeight.floatValue()));

        gripX.transform().xPosProperty().bindProperty(new FloatBinding()
        {
            {
                super.bind(trackX.transform().xPosProperty(),
                        trackX.transform().xTranslateProperty(),
                        scrollXProperty(),
                        trackX.transform().widthProperty(),
                        gripX.transform().widthProperty(),
                        trueWidthProperty(),
                        transform().widthProperty());
            }

            @Override
            protected float computeValue()
            {
                return trackX.transform().leftPos() - scrollX() / (trueWidth() - transform().width()) * (trackX.width() - gripX.width());
            }
        });

        gripX.height(8);
        gripX.transform().widthProperty().bindProperty(trueWidthProperty().combine(
                transform().widthProperty(),
                (trueWidth, width) -> max(16, 0.9F * width.floatValue() * (width.floatValue() / trueWidth.floatValue()))));
    }

    protected void createTrackY()
    {
        transform().addChild((trackY = new Rectangle()).transform());
        trackY.paint().backgroundColor(Color.GRAY);

        trackY.transform().xPosProperty().bindProperty(transform().xPosProperty().combine(transform().xTranslateProperty(),
                transform().widthProperty(),
                trackY.transform().widthProperty(),
                (xPos, xTranslate, width, trackWidth) -> xPos.floatValue() + xTranslate.floatValue() + width.floatValue() - trackWidth.floatValue()));
        trackY.transform().yPosProperty().bindProperty(transform().yPosProperty().add(transform().yTranslateProperty()));

        trackY.width(8);
        trackY.transform().heightProperty().bindProperty(transform().heightProperty());

        trackY.getEventDispatcher().addHandler(ClickPressEvent.TYPE, e ->
        {
            float ratio = max(0, (e.getMouseY() - trackY.getTopPos()) / trackY.height());
            scrollY(-(trueHeight() - transform().height()) * ratio);
        });

        if (scrollYPolicy() == GuiScrollbarPolicy.NEVER || (scrollYPolicy() == GuiScrollbarPolicy.NEEDED && trueHeight() <= transform().height()))
            trackY.setVisible(false);
    }

    public void createGripY()
    {
        trackY.transform().addChild((gripY = new Rectangle()).transform());

        gripY.getEventDispatcher().addHandler(ClickPressEvent.TYPE, HermodEvent::consume);

        gripY.getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, event -> dragStartY = scrollY());
        gripY.getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, event ->
        {
            float ratio = trueHeight() / transform().height();
            scrollY(-MathUtils.clamp(0, trueHeight() - transform().height(), -dragStartY + event.getDragY() * ratio));
        });

        gripY.paint().backgroundColor(Color.LIGHT_GRAY);

        gripY.transform().xPosProperty().bindProperty(transform().xPosProperty().combine(transform().xTranslateProperty(),
                transform().widthProperty(),
                gripY.transform().widthProperty(),
                (xPos, xTranslate, width, gripWidth) -> xPos.floatValue() + xTranslate.floatValue() + width.floatValue() - gripWidth.floatValue()));

        gripY.transform().yPosProperty().bindProperty(new FloatBinding()
        {
            {
                super.bind(trackY.transform().yPosProperty(),
                        trackY.transform().yTranslateProperty(),
                        scrollYProperty(),
                        trackY.transform().heightProperty(),
                        gripY.transform().heightProperty(),
                        trueHeightProperty(),
                        transform().heightProperty());
            }

            @Override
            protected float computeValue()
            {
                return trackY.transform().topPos() - scrollY() / (trueHeight() - transform().height()) * (trackY.height() - gripY.height());
            }
        });

        gripY.width(8);
        gripY.transform().heightProperty().bindProperty(trueHeightProperty().combine(
                transform().heightProperty(),
                (trueHeight, height) -> max(16, 0.9F * height.floatValue() * (height.floatValue() / trueHeight.floatValue()))));
    }

    private void onLayoutChange(@Nullable TransformLayoutEvent event)
    {
        float maxX = 0;
        float maxY = 0;
        for (Transform child : transform().children())
        {
            if (shouldIgnoreLayoutForChild(child))
                continue;

            float childRightPos = child.rightPos() - transform().leftPos() - transform().xOffsetProperty().get();
            float childBottomPos = child.bottomPos() - transform().topPos() - transform().yOffsetProperty().get();

            if (childRightPos > maxX)
                maxX = childRightPos;
            if (childBottomPos > maxY)
                maxY = childBottomPos;
        }

        if (maxX + padding().getLeft() > transform().width())
            trueWidthProperty().set(maxX + padding().getHorizontal());
        else
            trueWidthProperty().set(maxX);

        if (maxY + padding().getTop() > transform().height())
            trueHeightProperty().set(maxY + padding().getVertical());
        else
            trueHeightProperty().set(maxY);
    }

    private boolean shouldIgnoreLayoutForChild(Transform child)
    {
        GuiElement element = child.element();
        return element == trackX || element == trackY || element == trackXButtonLeft || element == trackXButtonRight || element == trackYButtonUp || element == trackYButtonDown;
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
        boolean vertical = event.scrollY() != 0 && !BrokkGuiPlatform.getInstance().getKeyboardUtil().isShiftKeyDown();

        if (vertical)
        {
            if (transform().height() >= trueHeight())
                return;

            scrolled = event.scrollY() * scrollSpeed();
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

            // Use scrollY in the case of shift holding (switch scroll axis)
            float eventScroll = event.scrollX() == 0 ? event.scrollY() : event.scrollX();

            scrolled = eventScroll * scrollSpeed();
            if (scrollX() + scrolled <= transform().width() - trueWidth()
                    && eventScroll < 0)
                scrolled = transform().width() - trueWidth() - scrollX();
            if (scrollX() + scrolled >= 0 && eventScroll > 0)
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
        return scrollXProperty;
    }

    public FloatProperty scrollYProperty()
    {
        return scrollYProperty;
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

    public BooleanProperty showTrackButtonsProperty()
    {
        return showTrackButtonsProperty;
    }

    public Property<RectBox> paddingProperty()
    {
        return paddingProperty;
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
        throw new NotImplementedException("Scalable is currently not supported by Scrollable component.");
    }

    public boolean scrollable()
    {
        return isScrollable;
    }

    public void scrollable(boolean scrollable)
    {
        isScrollable = scrollable;
    }

    public boolean showTrackButtons()
    {
        return showTrackButtonsProperty().getValue();
    }

    public void showTrackButtons(boolean showButtons)
    {
        showTrackButtonsProperty().setValue(showButtons);
    }

    public RectBox padding()
    {
        return paddingProperty().getValue();
    }

    public void padding(RectBox padding)
    {
        paddingProperty().setValue(padding);
    }
}
