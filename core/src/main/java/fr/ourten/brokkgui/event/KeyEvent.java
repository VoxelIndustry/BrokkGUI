package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;

public class KeyEvent extends GuiInputEvent
{
    public static final EventType<KeyEvent> TYPE = new EventType<>(GuiInputEvent.ANY, "KEY_INPUT_EVENT");

    private final int                       key;
    private final char                      character;

    public KeyEvent(final GuiNode source)
    {
        this(source, '\0', 0);
    }

    public KeyEvent(final GuiNode source, final char character, final int key)
    {
        super(source);
        this.key = key;
        this.character = character;
    }

    public int getKey()
    {
        return this.key;
    }

    public char getCharacter()
    {
        return this.character;
    }
}