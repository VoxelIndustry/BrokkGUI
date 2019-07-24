package net.voxelindustry.brokkgui.element.input;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.TextRendererStyle;
import net.voxelindustry.brokkgui.component.delegate.IconDelegate;
import net.voxelindustry.brokkgui.component.delegate.TextDelegate;
import net.voxelindustry.brokkgui.component.delegate.ToggleableDelegate;
import net.voxelindustry.brokkgui.component.impl.Icon;
import net.voxelindustry.brokkgui.component.impl.Text;
import net.voxelindustry.brokkgui.component.impl.TextRenderer;
import net.voxelindustry.brokkgui.component.impl.Toggleable;
import net.voxelindustry.brokkgui.element.shape.GuiNode;
import net.voxelindustry.brokkgui.element.shape.Rectangle;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;
import net.voxelindustry.hermod.EventHandler;

public class ToggleButton extends GuiNode implements TextDelegate, IconDelegate, ToggleableDelegate
{
    private Text       text;
    private Icon       icon;
    private Toggleable toggleable;

    private TextRenderer textRenderer;

    private EventHandler<ActionEvent> onActionEvent;

    public ToggleButton(String value, GuiElement icon)
    {
        text(value);

        // Setting the property will trigger invalidated listeners even with null
        if (icon != null)
            icon(icon);

        getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::onClick);
    }

    public ToggleButton(String value)
    {
        this(value, null);
    }

    public ToggleButton()
    {
        this("");
    }

    private void onClick(final ClickEvent.Left event)
    {
        if (!isDisabled())
        {
            activate();
            select(!selected());
        }
    }

    public void activate()
    {
        getEventDispatcher().dispatchEvent(ActionEvent.TYPE, new ActionEvent(this));
    }

    public void onActionEvent(EventHandler<ActionEvent> onActionEvent)
    {
        getEventDispatcher().removeHandler(ActionEvent.TYPE, this.onActionEvent);
        this.onActionEvent = onActionEvent;
        getEventDispatcher().addHandler(ActionEvent.TYPE, this.onActionEvent);
    }

    @Override
    public void postConstruct()
    {
        text = add(Text.class);
        icon = add(Icon.class);
        toggleable = add(Toggleable.class);
    }

    @Override
    public String type()
    {
        return "button";
    }

    @Override
    public ShapeDefinition shape()
    {
        return Rectangle.SHAPE;
    }

    @Override
    protected void refreshStyle(boolean useStyle)
    {
        super.refreshStyle(useStyle);

        TextRenderer textRenderer;
        if (useStyle)
        {
            textRenderer = new TextRendererStyle();
            remove(TextRenderer.class);
        }
        else
        {
            textRenderer = new TextRenderer();
            remove(TextRendererStyle.class);
        }

        this.textRenderer = add(textRenderer);
    }

    @Override
    public Text textComponent()
    {
        return text;
    }

    @Override
    public TextRenderer textRendererComponent()
    {
        return textRenderer;
    }

    @Override
    public Icon iconComponent()
    {
        return icon;
    }

    @Override
    public Toggleable toggleableComponent()
    {
        return toggleable;
    }
}
