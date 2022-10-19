package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.specific.FloatProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.TransformLayoutEvent;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.hermod.EventHandler;

import javax.annotation.Nullable;

public class MenuDisplayListComponent extends GuiComponent
{
    private final int escapeKey    = BrokkGuiPlatform.getInstance().getKeyboardUtil().getScanCode("ESCAPE");
    private final int upArrowKey   = BrokkGuiPlatform.getInstance().getKeyboardUtil().getScanCode("UP");
    private final int downArrowKey = BrokkGuiPlatform.getInstance().getKeyboardUtil().getScanCode("DOWN");

    private final EventHandler<ClickPressEvent> clickOutsideHandler = this::onClickPressOutside;
    private final EventHandler<KeyEvent.Press>  keyHandler          = this::onKeyPress;

    private MenuDisplayListElement displayList;

    private MenuSelectComponent selectComponent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        selectComponent = element().get(MenuSelectComponent.class);

        element().windowProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                oldValue.removeEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
                oldValue.removeEventHandler(KeyEvent.PRESS, keyHandler);
            }
            if (newValue != null)
            {
                newValue.addEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
                newValue.addEventHandler(KeyEvent.PRESS, keyHandler);
            }
        });
        if (element().windowProperty().isPresent())
        {
            element().window().addEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
            element().window().addEventHandler(KeyEvent.PRESS, keyHandler);
        }

        displayList = new MenuDisplayListElement();
        transform().addChild(displayList.transform());
        RelativeBindingHelper.bindToPos(displayList.transform(), transform(), null, transform().heightProperty());

        element().get(ButtonComponent.class).setOnActionEvent(e -> displayList.toggleFocus());
    }

    private void onClickPressOutside(ClickPressEvent event)
    {
        if (!transform().isPointInside(event.getMouseX(), event.getMouseY()) &&
                !displayList.transform().isPointInside(event.getMouseX(), event.getMouseY()) &&
                displayList.isFocused())
            displayList.removeFocus();
    }

    private void onKeyPress(KeyEvent.Press e)
    {
        if (!displayList.isFocused())
            return;

        if (e.scanCode() == escapeKey)
            displayList.removeFocus();

        else if (e.scanCode() == downArrowKey)
        {
            var current = selectComponent.ordinalOfActive();
            if (current < selectComponent.getOptionsCount() - 1)
                selectComponent.activateWithOrdinal(current + 1);
        }
        else if (e.scanCode() == upArrowKey)
        {
            var current = selectComponent.ordinalOfActive();
            if (current > 0)
                selectComponent.activateWithOrdinal(current - 1);
        }
        else if (BrokkGuiPlatform.getInstance().getKeyboardUtil().isEnterKey(e.keyCode()))
        {
            selectComponent.selectWithOrdinal(selectComponent.ordinalOfActive());
            displayList.removeFocus();
        }
    }

    ////////////
    // VALUES //
    ////////////

    public MenuDisplayListElement displayList()
    {
        return displayList;
    }

    /////////////
    // ELEMENT //
    /////////////

    public static class MenuDisplayListElement extends GuiElement implements StyledElement
    {
        private VerticalLayout verticalLayoutComponent;

        private final FloatProperty maxHeightProperty = new FloatProperty(Float.MAX_VALUE);

        public MenuDisplayListElement()
        {
            maxHeightProperty().addChangeListener(obs -> onLayoutChange(null));
        }

        @Override
        public void postConstruct()
        {
            super.postConstruct();

            verticalLayoutComponent = provide(VerticalLayout.class);
            verticalLayoutComponent.addElementsToHierarchy(true);

            transform().floating(true);
            transform().widthRatio(1);
            transform().overflow(GuiOverflow.SCROLL);

            getEventDispatcher().addHandler(TransformLayoutEvent.TYPE, this::onLayoutChange);

            transform().visibleProperty().bindProperty(focusedProperty());
        }

        @Override
        public String type()
        {
            return "menu-display-list";
        }

        private void onLayoutChange(@Nullable TransformLayoutEvent event)
        {
            float maxY = 0;
            for (var child : transform().children())
            {
                if (child.element().has(StyleComponent.class))
                {
                    var style = child.element().get(StyleComponent.class);
                    if (style.hasStyleClass("scrollbar-grip") || style.hasStyleClass("scrollbar-track-button") || style.hasStyleClass("scrollbar-track"))
                        continue;
                }

                float childBottomPos = child.bottomPos() - transform().topPos() - transform().yOffsetProperty().get();

                if (childBottomPos > maxY)
                    maxY = childBottomPos;
            }

            if (maxY != transform().height())
                transform().height(Math.min(maxY, maxHeight()));
        }

        ////////////////
        // COMPONENTS //
        ////////////////

        public VerticalLayout verticalLayoutComponent()
        {
            return verticalLayoutComponent;
        }

        ////////////////
        // PROPERTIES //
        ////////////////

        public FloatProperty maxHeightProperty()
        {
            return maxHeightProperty;
        }

        ////////////
        // VALUES //
        ////////////

        public float maxHeight()
        {
            return maxHeightProperty().get();
        }

        public void maxHeight(float maxHeight)
        {
            maxHeightProperty().set(maxHeight);
        }
    }
}
