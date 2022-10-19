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

    private final int keyCode;
    private final int scanCode;

    public KeyEvent(GuiElement source)
    {
        this(source, 0, 0);
    }

    public KeyEvent(GuiElement source, int keyCode, int scanCode)
    {
        super(source);
        this.keyCode = keyCode;
        this.scanCode = scanCode;
    }

    public int scanCode()
    {
        return scanCode;
    }

    public int keyCode()
    {
        return keyCode;
    }

    public boolean isCtrlDown()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown();
    }

    public boolean isShiftDown()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil().isShiftKeyDown();
    }

    public boolean isAltDown()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil().isAltDown();
    }

    public boolean isEnterDown()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil().isEnterDown();
    }

    @Override
    public KeyEvent copy(IEventEmitter source)
    {
        return new KeyEvent((GuiElement) source, keyCode(), scanCode());
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
        public Press(GuiElement source, int keyCode, int scanCode)
        {
            super(source, keyCode, scanCode);
        }

        @Override
        public KeyEvent.Press copy(IEventEmitter source)
        {
            return new KeyEvent.Press((GuiElement) source, keyCode(), scanCode());
        }
    }

    public static class Release extends KeyEvent
    {
        public Release(GuiElement source, int keyCode, int scanCode)
        {
            super(source, keyCode, scanCode);
        }

        @Override
        public KeyEvent.Release copy(IEventEmitter source)
        {
            return new KeyEvent.Release((GuiElement) source, keyCode(), scanCode());
        }
    }
}