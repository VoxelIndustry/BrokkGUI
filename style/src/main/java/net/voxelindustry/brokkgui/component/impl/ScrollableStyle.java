package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class ScrollableStyle extends Scrollable
{
    @Override
    protected void createTrackX()
    {
        super.createTrackX();

        StyleComponent trackXStyle = trackX.get(StyleComponent.class);
        trackXStyle.addStyleClass("scrollbar-track");
        trackXStyle.addStyleClass("scrollbar-track-x");

        trackXStyle.getProperty("background-color", Color.class).setToDefault();
    }

    @Override
    protected void createTrackY()
    {
        super.createTrackY();

        StyleComponent trackYStyle = trackY.get(StyleComponent.class);
        trackYStyle.addStyleClass("scrollbar-track");
        trackYStyle.addStyleClass("scrollbar-track-y");

        trackYStyle.getProperty("background-color", Color.class).setToDefault();
    }

    @Override
    protected void createXTrackButtons()
    {
        super.createXTrackButtons();

        StyleComponent trackXButtonLeftStyle = trackXButtonLeft.get(StyleComponent.class);
        trackXButtonLeftStyle.addStyleClass("scrollbar-track-button");
        trackXButtonLeftStyle.addStyleClass("scrollbar-track-x-button-left");

        trackXButtonLeftStyle.getProperty("background-color", Color.class).setToDefault();

        StyleComponent trackXButtonRightStyle = trackXButtonRight.get(StyleComponent.class);
        trackXButtonRightStyle.addStyleClass("scrollbar-track-button");
        trackXButtonRightStyle.addStyleClass("scrollbar-track-x-button-right");

        trackXButtonRightStyle.getProperty("background-color", Color.class).setToDefault();
    }

    @Override
    protected void createYTrackButtons()
    {
        super.createYTrackButtons();

        StyleComponent trackYButtonUpStyle = trackYButtonUp.get(StyleComponent.class);
        trackYButtonUpStyle.addStyleClass("scrollbar-track-button");
        trackYButtonUpStyle.addStyleClass("scrollbar-track-y-button-up");

        trackYButtonUpStyle.getProperty("background-color", Color.class).setToDefault();

        StyleComponent trackYButtonDownStyle = trackYButtonDown.get(StyleComponent.class);
        trackYButtonDownStyle.addStyleClass("scrollbar-track-button");
        trackYButtonDownStyle.addStyleClass("scrollbar-track-y-button-down");

        trackYButtonDownStyle.getProperty("background-color", Color.class).setToDefault();
    }

    @Override
    public void createGripX()
    {
        super.createGripX();

        StyleComponent gripXStyle = gripX.get(StyleComponent.class);
        gripXStyle.addStyleClass("scrollbar-grip");
        gripXStyle.addStyleClass("scrollbar-grip-x");

        gripXStyle.getProperty("background-color", Color.class).setToDefault();
    }

    @Override
    public void createGripY()
    {
        super.createGripY();

        StyleComponent gripYStyle = gripY.get(StyleComponent.class);
        gripYStyle.addStyleClass("scrollbar-grip");
        gripYStyle.addStyleClass("scrollbar-grip-y");

        gripYStyle.getProperty("background-color", Color.class).setToDefault();
    }
}
