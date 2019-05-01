package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.exp.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class KeyEvent extends GuiInputEvent
{
    public static final EventType<KeyEvent> ANY     = new EventType<>(GuiInputEvent.ANY, "INPUT_KEY_EVENT");
    public static final EventType<Input>    INPUT   = new EventType<>(ANY, "KEY_INPUT_EVENT");
    public static final EventType<Press>    PRESS   = new EventType<>(ANY, "KEY_PRESS_EVENT");
    public static final EventType<Release>  RELEASE = new EventType<>(ANY, "KEY_RELEASE_EVENT");

    private final int key;

    public KeyEvent(GuiElement source)
    {
        this(source, 0);
    }

    public KeyEvent(GuiElement source, int key)
    {
        super(source);
        this.key = key;
    }

    public int getKey()
    {
        return this.key;
    }

    @Override
    public KeyEvent copy(IEventEmitter source)
    {
        return new KeyEvent((GuiElement) source, getKey());
    }

    public static class Input extends KeyEvent
    {
        private final char character;

        public Input(GuiElement source, char character, int key)
        {
            super(source, key);
            this.character = character;
        }

        public char getCharacter()
        {
            return this.character;
        }

        @Override
        public KeyEvent.Input copy(IEventEmitter source)
        {
            return new KeyEvent.Input((GuiElement) source, getCharacter(), getKey());
        }
    }

    public static class Press extends KeyEvent
    {
        public Press(GuiElement source, int key)
        {
            super(source, key);
        }

        @Override
        public KeyEvent.Press copy(IEventEmitter source)
        {
            return new KeyEvent.Press((GuiElement) source, getKey());
        }
    }

    public static class Release extends KeyEvent
    {
        public Release(GuiElement source, int key)
        {
            super(source, key);
        }

        @Override
        public KeyEvent.Release copy(IEventEmitter source)
        {
            return new KeyEvent.Release((GuiElement) source, getKey());
        }
    }
}