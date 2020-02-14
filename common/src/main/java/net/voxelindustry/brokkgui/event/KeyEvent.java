package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class KeyEvent extends GuiInputEvent
{
    public static final EventType<KeyEvent> ANY     = new EventType<>(GuiInputEvent.ANY, "INPUT_KEY_EVENT");
    public static final EventType<Input>    INPUT   = new EventType<>(ANY, "KEY_INPUT_EVENT");
    public static final EventType<Press>    PRESS   = new EventType<>(ANY, "KEY_PRESS_EVENT");
    public static final EventType<Release>  RELEASE = new EventType<>(ANY, "KEY_RELEASE_EVENT");

    private final int key;

    public KeyEvent(GuiNode source)
    {
        this(source, 0);
    }

    public KeyEvent(GuiNode source, int key)
    {
        super(source);
        this.key = key;
    }

    public int getKey()
    {
        return this.key;
    }

    public boolean isCtrlDown()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown();
    }

    public boolean isShiftDown()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil().isShiftKeyDown();
    }

    @Override
    public KeyEvent copy(IEventEmitter source)
    {
        return new KeyEvent((GuiNode) source, getKey());
    }

    public static class Input extends KeyEvent
    {
        private final char character;

        public Input(GuiNode source, char character, int key)
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
            return new KeyEvent.Input((GuiNode) source,getCharacter(), getKey());
        }
    }

    public static class Press extends KeyEvent
    {
        public Press(GuiNode source, int key)
        {
            super(source, key);
        }

        @Override
        public KeyEvent.Press copy(IEventEmitter source)
        {
            return new KeyEvent.Press((GuiNode) source, getKey());
        }
    }

    public static class Release extends KeyEvent
    {
        public Release(GuiNode source, int key)
        {
            super(source, key);
        }

        @Override
        public KeyEvent.Release copy(IEventEmitter source)
        {
            return new KeyEvent.Release((GuiNode) source, getKey());
        }
    }
}