package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.specific.FloatProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.TransformLayoutEvent;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.hermod.EventHandler;

import javax.annotation.Nullable;

public class MenuDisplayListComponent extends GuiComponent
{
    private final EventHandler<ClickPressEvent> clickOutsideHandler = this::onClickPressOutside;

    private MenuDisplayListElement displayList;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        element().windowProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
                oldValue.removeEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
            if (newValue != null)
                newValue.addEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
        });
        if (element().windowProperty().isPresent())
            element().window().addEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);

        displayList = new MenuDisplayListElement();
        transform().addChild(displayList.transform());
        RelativeBindingHelper.bindToPos(displayList.transform(), transform(), null, transform().heightProperty());

        element().get(ButtonComponent.class).setOnActionEvent(e -> displayList.setFocused());
    }

    private void onClickPressOutside(ClickPressEvent event)
    {
        if (!transform().isPointInside(event.getMouseX(), event.getMouseY()) &&
                !displayList.transform().isPointInside(event.getMouseX(), event.getMouseY()) &&
                displayList.isFocused())
            displayList.removeFocus();
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
            for (Transform child : transform().children())
            {
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
