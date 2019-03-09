package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class TextTypedEvent extends GuiInputEvent
{
    public static final EventType<TextTypedEvent> TYPE = new EventType<>(GuiInputEvent.ANY, "TEXT_INPUT_EVENT");

    private final String oldText, newText;

    public TextTypedEvent(GuiNode source)
    {
        this(source, "", "");
    }

    public TextTypedEvent(GuiNode source, String oldText, String newText)
    {
        super(source);
        this.oldText = oldText;
        this.newText = newText;
    }

    public String getOldText()
    {
        return this.oldText;
    }

    public String getNewText()
    {
        return this.newText;
    }

    @Override
    public TextTypedEvent copy(IEventEmitter source)
    {
        return new TextTypedEvent((GuiNode) source, getOldText(), getNewText());
    }
}