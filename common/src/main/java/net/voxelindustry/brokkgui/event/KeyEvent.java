package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class KeyEvent extends GuiInputEvent
{
    public static final EventType<KeyEvent>  ANY        = new EventType<>(GuiInputEvent.ANY, "INPUT_KEY_EVENT");
    public static final EventType<TextTyped> TEXT_TYPED = new EventType<>(GuiInputEvent.ANY, "TEXT_TYPED_EVENT");
    public static final EventType<Press>     PRESS      = new EventType<>(ANY, "KEY_PRESS_EVENT");
    public static final EventType<Release>   RELEASE    = new EventType<>(ANY, "KEY_RELEASE_EVENT");

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
        return key;
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
        return new KeyEvent((GuiElement) source, getKey());
    }

    public static class TextTyped extends GuiInputEvent
    {
        private final String text;

        public TextTyped(GuiElement source, String text)
        {
            super(source);
            this.text = text;
        }

        public String text()
        {
            return text;
        }

        @Override
        public TextTyped copy(IEventEmitter source)
        {
            return new TextTyped((GuiElement) source, text());
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