package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.CursorMoveEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.TextTypedEvent;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.hermod.EventHandler;

import java.util.regex.Pattern;

import static java.lang.Math.min;

public class TextInputComponent extends GuiComponent implements RenderComponent
{
    private static final Pattern ALPHA_NUM_REGEX = Pattern.compile("[a-zA-Z0-9]");

    protected final IKeyboardUtil keyboard = BrokkGuiPlatform.getInstance().getKeyboardUtil();

    private final Property<Boolean> editableProperty = new Property<>(true);

    private final Property<Integer> maxTextLengthProperty = new Property<>(Integer.MAX_VALUE);
    private final Property<Integer> cursorPosProperty     = createRenderProperty(0);

    private EventHandler<TextTypedEvent>  onTextTyped;
    private EventHandler<CursorMoveEvent> onCursorMoveEvent;

    private TextComponent textComponent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        textComponent = element.get(TextComponent.class);

        element.getEventDispatcher().addHandler(KeyEvent.TEXT_TYPED, this::onKeyTyped);
        element.getEventDispatcher().addHandler(KeyEvent.PRESS, this::onKeyPressed);

        ValueInvalidationListener onCursorMove = this::computeTextTranslateFromCursorPos;

        transform().widthProperty().addListener(onCursorMove);

        textComponent.textProperty().addListener(onCursorMove);
        textComponent.textAlignmentProperty().addListener(onCursorMove);
        textComponent.computedTextPaddingValue().addListener(onCursorMove);

        textComponent.boldProperty().addListener(onCursorMove);
        textComponent.italicProperty().addListener(onCursorMove);
        textComponent.fontProperty().addListener(onCursorMove);
        textComponent.fontSizeProperty().addListener(onCursorMove);

        cursorPosProperty().addListener(onCursorMove);
    }

    private void computeTextTranslateFromCursorPos(Observable observable)
    {
        float cursorPos = textComponent.textTranslate();

        if (textComponent.textAlignment().isLeft())
            cursorPos += textHelper().getStringWidth(textComponent.text().substring(0, cursorPos()), textComponent.textSettings());
        else if (textComponent.textAlignment().isRight())
            cursorPos += textHelper().getStringWidth(textComponent.text().substring(cursorPos()), textComponent.textSettings());
        else
        {
            cursorPos += transform().width() / 2
                    - textHelper().getStringWidth(textComponent.text(), textComponent.textSettings()) / 2
                    + textHelper().getStringWidth(textComponent.text().substring(0, cursorPos()), textComponent.textSettings());
        }

        RectBox textPadding = textComponent.computedTextPadding();

        if (cursorPos < textPadding.getLeft())
            textComponent.textTranslateProperty().set(textComponent.textTranslate() - (cursorPos - textPadding.getLeft()));
        else if (cursorPos > transform().width() - textPadding.getHorizontal())
            textComponent.textTranslateProperty().set(textComponent.textTranslate() + (transform().width() - textPadding.getHorizontal() - cursorPos));
    }

    @Override
    public void renderContent(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.FOREGROUND)
        {
            // Cursor
            if (element().isFocused())
            {
                float x = transform().leftPos();
                float y = transform().topPos();
                RectBox textPadding = textComponent.textPadding();
                float textTranslate = textComponent.textTranslate();

                float xOffset;

                if (textComponent.textAlignment().isHorizontalCentered())
                {
                    float cursorPosX = textHelper().getStringWidth(textComponent.text().substring(0, cursorPos()), textComponent.textSettings());
                    xOffset = transform().width() / 2 - textHelper().getStringWidth(textComponent.text(), textComponent.textSettings()) / 2 + cursorPosX + textTranslate;
                }
                else if (textComponent.textAlignment().isLeft())
                {
                    float cursorPosX = textHelper().getStringWidth(textComponent.text().substring(0, cursorPos()), textComponent.textSettings());
                    xOffset = cursorPosX + textPadding.getLeft() + textTranslate;
                }
                else
                    xOffset = transform().width() - textPadding.getRight() - textHelper().getStringWidth(textComponent.text().substring(cursorPos()), textComponent.textSettings()) - textComponent.textTranslate();

                float textHeight = textHelper().getStringHeight(textComponent.textSettings());
                float yOffset = textPadding.getTop();

                if (textComponent.textAlignment().isVerticalCentered())
                    yOffset = transform().height() / 2 - textHeight / 2;
                else if (textComponent.textAlignment().isDown())
                    yOffset = transform().height() - textHeight - textPadding.getBottom();

                renderer.drawColoredRect(x + xOffset,
                        y + yOffset,
                        1,
                        textHeight,
                        transform().zLevel() + 1,
                        getCursorColor(),
                        RenderPass.FOREGROUND);
            }
        }
    }

    public Color getCursorColor()
    {
        // TODO: Cursor CSS properties
        return Color.WHITE;
    }

    protected void onKeyTyped(KeyEvent.TextTyped event)
    {
        String oldText = textComponent.text();

        if (editable())
        {
            appendTextToCursor(event.text());

            element().getEventDispatcher().dispatchEvent(TextTypedEvent.TYPE,
                    new TextTypedEvent(element(), oldText, textComponent.text()));
        }
    }

    protected void onKeyPressed(KeyEvent.Press event)
    {
        String oldText = textComponent.text();
        boolean contentChanged = false;

        if (event.getKey() == keyboard.getKeyCode("DELETE"))
        {
            if (editable())
            {
                if (keyboard.isCtrlKeyDown())
                    contentChanged = deleteWordAfterCursor();
                else
                    contentChanged = deleteAfterCursor();
            }
        }
        else if (event.getKey() == keyboard.getKeyCode("BACK"))
        {
            if (editable())
            {
                if (keyboard.isCtrlKeyDown())
                    contentChanged = deleteWordBeforeCursor();
                else
                    contentChanged = deleteFromCursor();
            }
        }
        else if (event.getKey() == keyboard.getKeyCode("LEFT"))
            if (keyboard.isCtrlKeyDown())
                cursorPos(previousWordPosition());
            else
                cursorPos(cursorPos() - 1);
        else if (event.getKey() == keyboard.getKeyCode("RIGHT"))
            if (keyboard.isCtrlKeyDown())
                cursorPos(nextWordPosition());
            else
                cursorPos(cursorPos() + 1);
        else if (event.getKey() == keyboard.getKeyCode("V")
                && BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown())
        {
            if (editable())
            {
                appendTextToCursor(BrokkGuiPlatform.getInstance().getKeyboardUtil().getClipboardString());
                contentChanged = true;
            }
        }

        if (contentChanged)
            element().getEventDispatcher().dispatchEvent(TextTypedEvent.TYPE,
                    new TextTypedEvent(element(), oldText, textComponent.text()));
    }

    /**
     * Delete a character from the current cursor position
     *
     * @return if a change to the contained text happened.
     */
    protected boolean deleteFromCursor()
    {
        if (cursorPos() == 0 || textComponent.text().length() == 0)
        {
            cursorPos(0);
            return false;
        }
        String result = textComponent.text();
        String temp = textComponent.text().length() > cursorPos() ? textComponent.text().substring(cursorPos()) : "";

        result = result.substring(0, cursorPos() - 1);
        result += temp;
        textComponent.text(result);
        cursorPos(cursorPos() - 1);
        return true;
    }

    /**
     * Delete a character after the current cursor position
     *
     * @return if a change to the contained text happened.
     */
    protected boolean deleteAfterCursor()
    {
        if (cursorPos() == textComponent.text().length()
                || textComponent.text().length() == 0)
            return false;
        String result = textComponent.text();
        String temp = textComponent.text().substring(cursorPos() + 1);

        result = result.substring(0, cursorPos());
        result += temp;
        textComponent.text(result);
        return true;
    }

    protected void appendTextToCursor(String toAppend)
    {
        if (maxTextLength() >= 0 && textComponent.text().length() + toAppend.length() <= maxTextLength())
        {
            int textSpaceLeft = maxTextLength() - textComponent.text().length();
            toAppend = toAppend.substring(0, min(textSpaceLeft, toAppend.length()));
        }

        if (toAppend.equals(""))
            return;

        String result = textComponent.text();
        String temp = textComponent.text().substring(cursorPos());

        result = result.substring(0, cursorPos());
        result += toAppend;
        result += temp;
        textComponent.text(result);
        cursorPos(cursorPos() + toAppend.length());
    }

    protected int previousWordPosition()
    {
        String textBeforeCursor = textComponent.text().substring(0, cursorPos());
        if (textBeforeCursor.isEmpty())
            return 0;
        int pos = cursorPos();

        boolean foundCharacter = false;
        while (pos > 0)
        {
            String charAtPos = textBeforeCursor.charAt(pos - 1) + "";
            if (ALPHA_NUM_REGEX.matcher(charAtPos).matches())
            {
                foundCharacter = true;
                pos--;
            }
            else if (charAtPos.equals(" "))
            {
                if (foundCharacter)
                    break;
                else
                    pos--;
            }
            else if (pos == cursorPos())
            {
                pos--;
                break;
            }
            else
            {
                pos--;
            }
        }

        return pos;
    }

    protected int nextWordPosition()
    {
        if (cursorPos() == textComponent.text().length())
            return cursorPos();
        String text = textComponent.text();
        int pos = cursorPos();
        boolean foundSpace = false;
        while (pos < textComponent.text().length())
        {
            String charAtPos = text.charAt(pos) + "";
            if (charAtPos.equals(" "))
            {
                foundSpace = true;
                pos++;
            }
            else if (foundSpace)
            {
                break;
            }
            else if (!ALPHA_NUM_REGEX.matcher(charAtPos).matches() && pos != cursorPos())
            {
                break;
            }
            else
            {
                pos++;
            }
        }
        return pos;
    }

    protected boolean deleteWordBeforeCursor()
    {
        if (cursorPos() == 0)
        {
            cursorPos(0);
            return false;
        }
        int pos = previousWordPosition();
        String currentText = textComponent.text();
        textComponent.text(currentText.substring(0, pos) + currentText.substring(cursorPos()));
        cursorPos(pos);
        return true;
    }

    protected boolean deleteWordAfterCursor()
    {
        if (cursorPos() == textComponent.text().length())
            return false;
        int pos = nextWordPosition();
        String currentText = textComponent.text();
        textComponent.text(currentText.substring(0, cursorPos()) + currentText.substring(pos));
        return true;
    }

    public Property<Boolean> editableProperty()
    {
        return editableProperty;
    }

    public Property<Integer> maxTextLengthProperty()
    {
        return maxTextLengthProperty;
    }

    public Property<Integer> cursorPosProperty()
    {
        return cursorPosProperty;
    }

    public boolean editable()
    {
        return editableProperty().getValue();
    }

    public void editable(boolean editable)
    {
        editableProperty().setValue(editable);
    }

    public int maxTextLength()
    {
        return maxTextLengthProperty().getValue();
    }

    public void maxTextLength(int length)
    {
        maxTextLengthProperty().setValue(length);
    }

    public int cursorPos()
    {
        return cursorPosProperty().getValue();
    }

    public void cursorPos(int cursorPos)
    {
        if (cursorPos >= 0 && cursorPos <= textComponent.text().length())
        {
            if (getOnCursorMoveEvent() != null)
                getEventDispatcher().dispatchEvent(CursorMoveEvent.TYPE,
                        new CursorMoveEvent(element(), cursorPos(), cursorPos));
            cursorPosProperty().setValue(cursorPos);
        }
    }

    ////////////
    // EVENTS //
    ////////////

    public EventHandler<TextTypedEvent> getOnTextTyped()
    {
        return onTextTyped;
    }

    public void setOnTextTyped(EventHandler<TextTypedEvent> onTextTyped)
    {
        getEventDispatcher().removeHandler(TextTypedEvent.TYPE, this.onTextTyped);
        this.onTextTyped = onTextTyped;
        getEventDispatcher().addHandler(TextTypedEvent.TYPE, this.onTextTyped);
    }

    public EventHandler<CursorMoveEvent> getOnCursorMoveEvent()
    {
        return onCursorMoveEvent;
    }

    public void setOnCursorMoveEvent(EventHandler<CursorMoveEvent> onCursorMoveEvent)
    {
        getEventDispatcher().removeHandler(CursorMoveEvent.TYPE, this.onCursorMoveEvent);
        this.onCursorMoveEvent = onCursorMoveEvent;
        getEventDispatcher().addHandler(CursorMoveEvent.TYPE, this.onCursorMoveEvent);
    }
}
