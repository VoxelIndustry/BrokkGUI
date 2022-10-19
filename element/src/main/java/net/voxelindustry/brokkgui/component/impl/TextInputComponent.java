package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import fr.ourten.teabeans.property.specific.IntProperty;
import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.animation.Animation;
import net.voxelindustry.brokkgui.animation.Interpolators;
import net.voxelindustry.brokkgui.animation.PropertyAnimation;
import net.voxelindustry.brokkgui.animation.ValueInterpolators;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.ClickPressEvent;
import net.voxelindustry.brokkgui.event.CursorMoveEvent;
import net.voxelindustry.brokkgui.event.EventQueueBuilder;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.event.ScrollEvent;
import net.voxelindustry.brokkgui.event.TextTypedEvent;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.hermod.EventHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class TextInputComponent extends GuiComponent implements RenderComponent
{
    private static final Pattern ALPHA_NUM_REGEX = Pattern.compile("[a-zA-Z0-9]");

    private final BooleanProperty editableProperty = new BooleanProperty(true);

    private final IntProperty maxTextLengthProperty = new IntProperty(Integer.MAX_VALUE);
    private final IntProperty cursorPosProperty     = createRenderPropertyInt(0);

    private final IntProperty cursorSelectedPosProperty = createRenderPropertyInt(-1);

    private EventHandler<TextTypedEvent>  onTextTyped;
    private EventHandler<CursorMoveEvent> onCursorMoveEvent;

    private TextComponent  textComponent;
    private StyleComponent styleComponent;

    private final PropertyAnimation<Float> cursorAnimation;
    private final Property<Float>          cursorOpacity = createRenderProperty(0F);

    private final EventHandler<ClickPressEvent> clickOutsideHandler = this::onClickPressOutside;

    public TextInputComponent()
    {
        cursorAnimation = PropertyAnimation.<Float>build()
                .property(cursorOpacity)
                .valueInterpolator(ValueInterpolators.IDENTITY)
                .interpolator(Interpolators.QUAD_BOTH)
                .startValue(0F)
                .endValue(1F)
                .maxCycles(Animation.INFINITE_CYCLES)
                .reverse(true)
                .duration(800, TimeUnit.MILLISECONDS)
                .create();
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(TextComponent.class))
            throw new GuiComponentException("Cannot attach TextInputComponent to an element not having TextComponent!");

        if (!element.has(StyleComponent.class))
            throw new GuiComponentException("Cannot attach TextInputComponent to an element not having StyleComponent!");

        textComponent = element.get(TextComponent.class);
        styleComponent = element().get(StyleComponent.class);

        element.getEventDispatcher().addHandler(KeyEvent.TEXT_TYPED, this::onTextTyped);
        element.getEventDispatcher().addHandler(KeyEvent.PRESS, this::onKeyPressed);

        element().windowProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
                oldValue.removeEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
            if (newValue != null)
                newValue.addEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);
        });
        if (element().windowProperty().isPresent())
            element().window().addEventHandler(ClickPressEvent.TYPE, clickOutsideHandler);

        ValueInvalidationListener onCursorMove = this::computeTextTranslateFromCursorPos;

        transform().widthProperty().addChangeListener(onCursorMove);

        textComponent.textProperty().addChangeListener(onCursorMove);
        textComponent.textAlignmentProperty().addChangeListener(onCursorMove);
        // Prevent stackoverflow by not triggering eager retrieval of binding value
        textComponent.computedTextPaddingValue().addListener(onCursorMove);

        textComponent.boldProperty().addChangeListener(onCursorMove);
        textComponent.italicProperty().addChangeListener(onCursorMove);
        textComponent.fontProperty().addChangeListener(onCursorMove);
        textComponent.fontSizeProperty().addChangeListener(onCursorMove);

        cursorPosProperty().addChangeListener(onCursorMove);

        styleComponent.registerProperty("cursor-color", Color.BLACK, Color.class);
        styleComponent.registerProperty("cursor-texture", Texture.EMPTY, Texture.class);

        element().focusedProperty().addListener(obs ->
        {
            if (element().isFocused())
                cursorAnimation.restart();
            else
                cursorAnimation.pause();
        });

        getEventDispatcher().addHandler(ClickPressEvent.TYPE, this::handleClick);
        getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, this::handleClickDragStart);
        getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, this::handleClickDrag);
        getEventDispatcher().addHandler(ScrollEvent.TYPE, this::handleScroll);
    }

    private void handleScroll(ScrollEvent event)
    {
        float scroll = keyboard().isShiftKeyDown() ? event.scrollY() : event.scrollX();

        if (scroll > 0)
        {
            if (textComponent.textTranslate() < 0)
                textComponent.textTranslateProperty().set(min(0, textComponent.textTranslate() + scroll * 10));
        }
        else if (scroll < 0)
        {
            float textLength = textHelper().getStringWidth(textComponent.text(), textComponent.textSettings());

            if (textLength + textComponent.textTranslate() > transform().width() - textComponent.computedTextPadding().getHorizontal())
            {
                float max = transform().width() - textComponent.computedTextPadding().getHorizontal() - textLength;
                textComponent.textTranslateProperty().set(max(max, textComponent.textTranslate() + scroll * 10));
            }
        }
    }

    private void handleClick(ClickPressEvent event)
    {
        if (element().setFocused())
            event.consume();

        if (!element().isFocused())
            return;

        int cursorPos = findCursorPos(event.getMouseX(), event.getMouseY());

        if (cursorPos != -1)
            cursorPos(cursorPos);
    }

    private void handleClickDragStart(GuiMouseEvent.DragStart event)
    {
        int cursorPos = findCursorPos(event.getMouseX(), event.getMouseY());
        if (cursorPos != -1)
            cursorSelectedPos(cursorPos);
    }

    private void handleClickDrag(GuiMouseEvent.Dragging event)
    {
        int cursorPos = findCursorPos(event.getMouseX(), event.getMouseY());
        if (cursorPos != -1)
            cursorPos(cursorPos);
    }

    private float findStartTextPos()
    {
        if (textComponent.textAlignment().isHorizontalCentered())
            return transform().leftPos()
                    + transform().width() / 2
                    - textHelper().getStringWidth(textComponent.text(), textComponent.textSettings()) / 2
                    + textComponent.textTranslate();
        else if (textComponent.textAlignment().isLeft())
            return transform().leftPos()
                    + textComponent.computedTextPadding().getLeft()
                    + textComponent.textTranslate();
        else
            return transform().rightPos()
                    - textComponent.computedTextPadding().getRight()
                    - textHelper().getStringWidth(textComponent.text(), textComponent.textSettings())
                    - textComponent.textTranslate();
    }

    private int findCursorPos(float x, float y)
    {
        RectBox textPadding = textComponent.computedTextPadding();
        if (x < transform().leftPos() + textPadding.getLeft() ||
                x > transform().rightPos() - textPadding.getRight())
            return -1;

        float currentPos = findStartTextPos();
        String text = textComponent.text();

        int cursorPosFirstPart = findCursorPos(currentPos, x, text, 0, text.length() / 2);
        if (cursorPosFirstPart != -1)
            return cursorPosFirstPart;
        int cursorPosLastPart = findCursorPos(
                currentPos + textHelper().getStringWidth(text.substring(0, text.length() / 2), textComponent.textSettings()),
                x,
                text,
                text.length() / 2,
                text.length());

        if (cursorPosLastPart != -1)
            return cursorPosLastPart;
        return text.length();
    }

    private int findCursorPos(float startOffset, float x, String text, int firstChar, int lastChar)
    {
        float currentPos = startOffset + textHelper().getStringWidth(text.substring(firstChar, lastChar), textComponent.textSettings());

        if (x >= startOffset && x <= currentPos && lastChar - firstChar == 1)
            return abs(x - startOffset) > abs(x - currentPos) ? lastChar : firstChar;
        if (x > currentPos || x < startOffset)
            return -1;

        int distance = lastChar - firstChar;
        int cursorPosFirstPart = findCursorPos(startOffset, x, text, firstChar, lastChar - distance / 2);
        if (cursorPosFirstPart != -1)
            return cursorPosFirstPart;

        return findCursorPos(
                startOffset + textHelper().getStringWidth(text.substring(firstChar, firstChar + distance / 2), textComponent.textSettings()),
                x,
                text,
                firstChar + distance / 2,
                lastChar);
    }

    private void computeTextTranslateFromCursorPos(Observable observable)
    {
        if (cursorPos() > textComponent.text().length())
            cursorPos(textComponent.text().length());

        float cursorPos = textComponent.textTranslate();

        if (textComponent.textAlignment().isLeft())
            cursorPos += textHelper().getStringWidth(textComponent.text().substring(0, cursorPos()), textComponent.textSettings());
        else if (textComponent.textAlignment().isRight())
            cursorPos += textHelper().getStringWidth(textComponent.text().substring(cursorPos()), textComponent.textSettings());
        else
        {
            cursorPos += -textComponent.computedTextPadding().getLeft() + transform().width() / 2
                    - textHelper().getStringWidth(textComponent.text(), textComponent.textSettings()) / 2
                    + textHelper().getStringWidth(textComponent.text().substring(0, cursorPos()), textComponent.textSettings());
        }

        RectBox textPadding = textComponent.computedTextPadding();

        if (cursorPos < 0)
            textComponent.textTranslateProperty().set(textComponent.textTranslate() - cursorPos);
        else if (cursorPos > transform().width() - textPadding.getHorizontal())
            textComponent.textTranslateProperty().set(textComponent.textTranslate() + (transform().width() - textPadding.getHorizontal() - cursorPos));
    }

    @Override
    public void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {
        if (!element().isFocused())
            return;

        // Cursor

        float x = transform().leftPos();
        float y = transform().topPos();
        RectBox textPadding = textComponent.textPadding();
        float textTranslate = textComponent.textTranslate();

        float cursorPosX = getRenderCursorPosX(textPadding, textTranslate, cursorPos());

        float textHeight = textHelper().getStringHeight(textComponent.textSettings());
        float yOffset = textPadding.getTop();

        if (textComponent.textAlignment().isVerticalCentered())
            yOffset = transform().height() / 2 - textHeight / 2;
        else if (textComponent.textAlignment().isDown())
            yOffset = transform().height() - textHeight - textPadding.getBottom();

        if (hasSelection())
        {
            float selectionPosX = getRenderCursorPosX(textPadding, textTranslate, cursorSelectedPos());
            renderSelection(renderer, x, y, min(cursorPosX, selectionPosX), max(cursorPosX, selectionPosX), textHeight, yOffset);
        }

        renderCursor(renderer, x, y, cursorPosX, textHeight, yOffset);
    }

    private void renderSelection(IRenderCommandReceiver renderer, float x, float y, float startPosX, float endPosX, float textHeight, float yOffset)
    {
        renderer.drawColoredRect(x + startPosX,
                y + yOffset,
                endPosX - startPosX,
                textHeight,
                // selection draw is under text
                transform().zLevel() - 1,
                getSelectionColor(),
                RenderPass.FOREGROUND);
    }

    private void renderCursor(IRenderCommandReceiver renderer, float x, float y, float cursorPosX, float textHeight, float yOffset)
    {
        Texture cursorTexture = getCursorTexture();

        if (cursorTexture != Texture.EMPTY)
        {
            renderer.drawTexturedRectWithColor(
                    x + cursorPosX,
                    y + yOffset,
                    cursorTexture.getUMin(),
                    cursorTexture.getVMin(),
                    cursorTexture.getUMax(),
                    cursorTexture.getVMax(),
                    1,
                    textHeight,
                    transform().zLevel() + 1,
                    RenderPass.FOREGROUND,
                    getCursorColor()
            );
        }
        else
        {
            Color cursorColor = new Color(getCursorColor().getRed(),
                    getCursorColor().getGreen(),
                    getCursorColor().getBlue(),
                    getCursorColor().getAlpha() * cursorOpacity.getValue());

            renderer.drawColoredRect(x + cursorPosX,
                    y + yOffset,
                    1,
                    textHeight,
                    transform().zLevel() + 1,
                    cursorColor,
                    RenderPass.FOREGROUND);
        }
    }

    private float getRenderCursorPosX(RectBox textPadding, float textTranslate, int cursorPos)
    {
        if (textComponent.textAlignment().isHorizontalCentered())
        {
            float cursorPosX = textHelper().getStringWidth(textComponent.text().substring(0, cursorPos), textComponent.textSettings());
            return transform().width() / 2 - textHelper().getStringWidth(textComponent.text(), textComponent.textSettings()) / 2 + cursorPosX + textTranslate;
        }
        else if (textComponent.textAlignment().isLeft())
        {
            float cursorPosX = textHelper().getStringWidth(textComponent.text().substring(0, cursorPos), textComponent.textSettings());
            return cursorPosX + textPadding.getLeft() + textTranslate;
        }
        else
            return transform().width() - textPadding.getRight() - textHelper().getStringWidth(textComponent.text().substring(cursorPos), textComponent.textSettings()) - textComponent.textTranslate();
    }

    public Color getCursorColor()
    {
        return styleComponent.getValue("cursor-color", Color.class, Color.BLACK);
    }

    public Texture getCursorTexture()
    {
        return styleComponent.getValue("cursor-texture", Texture.class, Texture.EMPTY);
    }

    public Color getSelectionColor()
    {
        return styleComponent.getValue("selection-color", Color.class, Color.AQUA.addAlpha(-0.4F, new Color()));
    }

    protected void onTextTyped(KeyEvent.TextTyped event)
    {
        String oldText = textComponent.text();

        if (editable())
        {
            appendTextToCursor(event.text());

            EventQueueBuilder.fromTarget(element()).dispatch(TextTypedEvent.TYPE, new TextTypedEvent(element(), oldText, textComponent.text()));
            event.consume();
        }
    }

    protected void onKeyPressed(KeyEvent.Press event)
    {
        event.consume();

        String oldText = textComponent.text();
        boolean contentChanged = false;

        moveCursorAndSelection(event);

        if (!editable())
            return;

        if (event.scanCode() == keyboard().getScanCode("DELETE"))
        {
            if (hasSelection())
                removeSelectedText();
            else if (keyboard().isCtrlKeyDown())
                contentChanged = deleteWordAfterCursor();
            else
                contentChanged = deleteAfterCursor();
        }
        else if (event.scanCode() == keyboard().getScanCode("BACK"))
        {
            if (hasSelection())
                removeSelectedText();
            else if (keyboard().isCtrlKeyDown())
                contentChanged = deleteWordBeforeCursor();
            else
                contentChanged = deleteFromCursor();
        }
        else if (event.scanCode() == keyboard().getScanCode("V")
                && keyboard().isCtrlKeyDown()
                && !StringUtils.isEmpty(keyboard().getClipboardString()))
        {
            if (hasSelection())
                removeSelectedText();

            appendTextToCursor(keyboard().getClipboardString());
            contentChanged = true;
        }
        else if (event.scanCode() == keyboard().getScanCode("C")
                && keyboard().isCtrlKeyDown())
        {
            if (hasSelection())
                keyboard().setClipboardString(getSelectedText());
            else
                keyboard().setClipboardString(textComponent.text());
        }

        if (contentChanged)
            EventQueueBuilder.fromTarget(element()).dispatch(TextTypedEvent.TYPE, new TextTypedEvent(element(), oldText, textComponent.text()));
    }

    private void moveCursorAndSelection(KeyEvent.Press event)
    {
        if (event.scanCode() == keyboard().getScanCode("LEFT"))
        {
            if (keyboard().isShiftKeyDown())
            {
                if (!hasSelection())
                    cursorSelectedPos(cursorPos());
            }
            else if (hasSelection())
            {
                cursorPos(min(cursorPos(), cursorSelectedPos()));
                cursorSelectedPos(-1);
                return;
            }

            if (keyboard().isCtrlKeyDown())
                cursorPos(previousWordPosition());
            else
                cursorPos(cursorPos() - 1);
        }
        else if (event.scanCode() == keyboard().getScanCode("RIGHT"))
        {
            if (keyboard().isShiftKeyDown())
            {
                if (!hasSelection())
                    cursorSelectedPos(cursorPos());
            }
            else if (hasSelection())
            {
                cursorPos(max(cursorPos(), cursorSelectedPos()));
                cursorSelectedPos(-1);
                return;
            }

            if (keyboard().isCtrlKeyDown())
                cursorPos(nextWordPosition());
            else
                cursorPos(cursorPos() + 1);
        }
        else if (event.scanCode() == keyboard().getScanCode("UP"))
        {
            if (keyboard().isShiftKeyDown())
            {
                if (!hasSelection())
                    cursorSelectedPos(cursorPos());
            }
            else if (hasSelection())
                cursorSelectedPos(-1);

            cursorPos(0);
        }
        else if (event.scanCode() == keyboard().getScanCode("DOWN"))
        {
            if (keyboard().isShiftKeyDown())
            {
                if (!hasSelection())
                    cursorSelectedPos(cursorPos());
            }
            else if (hasSelection())
                cursorSelectedPos(-1);

            cursorPos(textComponent.text().length());
        }
    }

    private void onClickPressOutside(ClickPressEvent event)
    {
        if (!transform().isPointInside(event.getMouseX(), event.getMouseY()) && element().isFocused())
            element().removeFocus();
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
        cursorPos(cursorPos() - 1);
        textComponent.text(result);
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
        if (hasSelection())
        {
            removeSelectedText();
            cursorSelectedPos(-1);
        }

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
        int previousCursorPos = cursorPos();
        String currentText = textComponent.text();

        cursorPos(pos);
        textComponent.text(currentText.substring(0, pos) + currentText.substring(previousCursorPos));
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

    protected String getSelectedText()
    {
        int from = min(cursorPos(), cursorSelectedPos());
        int to = max(cursorPos(), cursorSelectedPos());
        return textComponent.text().substring(from, to);
    }

    protected void removeSelectedText()
    {
        int from = min(cursorPos(), cursorSelectedPos());
        int to = max(cursorPos(), cursorSelectedPos());

        cursorPos(from);

        String currentText = textComponent.text();
        textComponent.text(currentText.substring(0, from) + currentText.substring(to));
    }

    public BooleanProperty editableProperty()
    {
        return editableProperty;
    }

    public IntProperty maxTextLengthProperty()
    {
        return maxTextLengthProperty;
    }

    public IntProperty cursorPosProperty()
    {
        return cursorPosProperty;
    }

    public IntProperty cursorSelectedPosProperty()
    {
        return cursorSelectedPosProperty;
    }

    public boolean editable()
    {
        return editableProperty().get();
    }

    public void editable(boolean editable)
    {
        editableProperty().set(editable);
    }

    public int maxTextLength()
    {
        return maxTextLengthProperty().get();
    }

    public void maxTextLength(int length)
    {
        maxTextLengthProperty().set(length);
    }

    public int cursorPos()
    {
        return cursorPosProperty().get();
    }

    public void cursorPos(int cursorPos)
    {
        if (cursorPos >= 0 && cursorPos <= textComponent.text().length())
        {
            if (getOnCursorMoveEvent() != null)
                EventQueueBuilder.fromTarget(element()).dispatch(CursorMoveEvent.TYPE, new CursorMoveEvent(element(), cursorPos(), cursorPos));
            cursorPosProperty().set(cursorPos);
        }
    }

    public int cursorSelectedPos()
    {
        return cursorSelectedPosProperty().get();
    }

    public void cursorSelectedPos(int cursorPos)
    {
        if (cursorPos >= 0 && cursorPos <= textComponent.text().length())
        {
            cursorSelectedPosProperty().set(cursorPos);
        }
        else if (cursorPos == -1)
            cursorSelectedPosProperty().set(-1);
    }

    public boolean hasSelection()
    {
        var cursorSelectedPos = cursorSelectedPos();
        return cursorSelectedPos >= 0 && cursorSelectedPos <= textComponent.text().length();
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
