package fr.ourten.brokkgui.behavior;

import fr.ourten.brokkgui.BrokkGuiPlatform;
import fr.ourten.brokkgui.element.GuiTextfield;
import fr.ourten.brokkgui.event.CursorMoveEvent;
import fr.ourten.brokkgui.event.KeyEvent;
import fr.ourten.brokkgui.event.TextTypedEvent;
import fr.ourten.brokkgui.internal.IKeyboardUtil;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfieldBehavior<T extends GuiTextfield> extends GuiBehaviorBase<T>
{
    private final IKeyboardUtil keyboard = BrokkGuiPlatform.getInstance().getKeyboardUtil();

    public GuiTextfieldBehavior(final T model)
    {
        super(model);

        this.getModel().getEventDispatcher().addHandler(KeyEvent.TYPE, this::onKeyTyped);
    }

    public void onKeyTyped(final KeyEvent event)
    {
        final String oldText = this.getModel().getText();
        boolean contentChanged = false;
        if (event.getKey() == this.keyboard.getKeyCode("DELETE"))
        {
            if (this.getModel().isEditable())
                contentChanged = this.deleteAfterCursor();
        }
        else if (event.getKey() == this.keyboard.getKeyCode("BACK"))
        {
            if (this.getModel().isEditable())
                contentChanged = this.deleteFromCursor();
        }
        else if (event.getKey() == this.keyboard.getKeyCode("LEFT"))
            this.setCursorPosition(this.getModel().getCursorPosition() - 1);
        else if (event.getKey() == this.keyboard.getKeyCode("RIGHT"))
            this.setCursorPosition(this.getModel().getCursorPosition() + 1);
        else if (event.getKey() == this.keyboard.getKeyCode("V")
                && BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown())
        {
            if (this.getModel().isEditable())
            {
                this.appendTextToCursor(BrokkGuiPlatform.getInstance().getKeyboardUtil().getClipboardString());
                contentChanged = true;
            }
        }
        else if (this.getModel().isEditable()
                && BrokkGuiPlatform.getInstance().getKeyboardUtil().isKeyValidChar(event.getKey()))
        {
            this.appendTextToCursor(String.valueOf(event.getCharacter()));
            contentChanged = true;
        }
        if (this.getModel().getOnTextTyped() != null && contentChanged)
            this.getModel().getEventDispatcher().dispatchEvent(TextTypedEvent.TYPE,
                    new TextTypedEvent(this.getModel(), oldText, this.getModel().getText()));
    }

    /**
     * Delete a character from the current cursor position
     *
     * @return if a change to the contained text happened.
     */
    protected boolean deleteFromCursor()
    {
        if (this.getModel().getCursorPosition() == 0 || this.getModel().getText().length() == 0)
            return false;
        String result = this.getModel().getText();
        final String temp = this.getModel().getText().length() > this.getModel().getCursorPosition() ? this.getModel()
                .getText().substring(this.getModel().getCursorPosition(), this.getModel().getText().length()) : "";

        result = result.substring(0, this.getModel().getCursorPosition() - 1);
        result += temp;
        this.getModel().setText(result);
        this.setCursorPosition(this.getModel().getCursorPosition() - 1);
        return true;
    }

    /**
     * Delete a character after the current cursor position
     *
     * @return if a change to the contained text happened.
     */
    protected boolean deleteAfterCursor()
    {
        if (this.getModel().getCursorPosition() == this.getModel().getText().length()
                || this.getModel().getText().length() == 0)
            return false;
        String result = this.getModel().getText();
        final String temp = this.getModel().getText().substring(this.getModel().getCursorPosition() + 1,
                this.getModel().getText().length());

        result = result.substring(0, this.getModel().getCursorPosition());
        result += temp;
        this.getModel().setText(result);
        return true;
    }

    protected void appendTextToCursor(final String toAppend)
    {
        if (this.getModel().getMaxTextLength() >= 0
                && this.getModel().getText().length() + toAppend.length() <= this.getModel().getMaxTextLength())
            toAppend.substring(0, this.getModel().getText().length() - this.getModel().getMaxTextLength() < 0 ? 0
                    : this.getModel().getText().length() - this.getModel().getMaxTextLength());
        if (toAppend != "")
        {
            String result = this.getModel().getText();
            final String temp = this.getModel().getText().substring(this.getModel().getCursorPosition(),
                    this.getModel().getText().length());

            result = result.substring(0, this.getModel().getCursorPosition());
            result += toAppend;
            result += temp;
            this.getModel().setText(result);
            this.setCursorPosition(this.getModel().getCursorPosition() + toAppend.length());
        }
    }

    public void setCursorPosition(final int cursorPosition)
    {
        if (cursorPosition >= 0 && cursorPosition <= this.getModel().getText().length())
        {
            if (this.getModel().getOnCursorMoveEvent() != null && this.getModel().getCursorPosition() != cursorPosition)
                this.getModel().getEventDispatcher().dispatchEvent(CursorMoveEvent.TYPE,
                        new CursorMoveEvent(this.getModel(), this.getModel().getCursorPosition(), cursorPosition));
            this.getModel().setCursorPosition(cursorPosition);
        }
    }
}