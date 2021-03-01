package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.element.input.GuiTextfieldComplete;

public class GuiTextfieldCompleteBehavior<T extends GuiTextfieldComplete> extends GuiTextfieldBehavior<T>
{
    private String tempText;

    public GuiTextfieldCompleteBehavior(T model)
    {
        super(model);

/*
        model.getEventDispatcher().addHandler(FocusEvent.TYPE, this::onFocusEvent);
*/
    }

/*    private void onFocusEvent(FocusEvent e)
    {
        GuiTextfieldCompleteSkin skin = (GuiTextfieldCompleteSkin) getModel().getSkin();

        if (!e.isFocused() && skin.isCompletePopupShown())
            skin.hideCompletePopup();
        else if (e.isFocused() && !skin.isCompletePopupShown() &&
                getModel().getText().length() >= getModel().getCharBeforeCompletion())
            skin.showCompletePopup();

    }

    protected void onKeyTyped(KeyEvent.Input event)
    {
        GuiTextfieldCompleteSkin skin = (GuiTextfieldCompleteSkin) getModel().getSkin();

        if (getModel().getText().length() < getModel().getCharBeforeCompletion() &&
                skin.isCompletePopupShown())
            skin.hideCompletePopup();
        else if (getModel().getText().length() >= getModel().getCharBeforeCompletion() &&
                !skin.isCompletePopupShown())
            skin.showCompletePopup();
    }

    protected void onKeyPressed(KeyEvent.Press event)
    {
        GuiTextfieldCompleteSkin skin = (GuiTextfieldCompleteSkin) getModel().getSkin();

        if (!skin.isCompletePopupShown())
            return;

        if (event.getKey() == this.keyboard.getKeyCode("UP"))
        {
            int current = skin.getSelectedIndex();

            if (current == -1)
            {
                skin.selectSuggestion(skin.getActualSuggestionSize() - 1);
                tempText = getModel().getText();
                getModel().setText(skin.getSelectedValue());
                moveCursorToMax();
            }
            else if (current > 0)
            {
                skin.selectSuggestion(current - 1);
                getModel().setText(skin.getSelectedValue());
                moveCursorToMax();
            }
            else
            {
                skin.deselect();
                getModel().setText(tempText);
                moveCursorToMax();
            }
        }
        else if (event.getKey() == this.keyboard.getKeyCode("DOWN"))
        {
            int current = skin.getSelectedIndex();

            if (current == -1)
                tempText = getModel().getText();
            if (current < skin.getActualSuggestionSize() - 1)
            {
                skin.selectSuggestion(current + 1);
                getModel().setText(skin.getSelectedValue());
                moveCursorToMax();
            }
            else
            {
                skin.deselect();
                getModel().setText(tempText);
                moveCursorToMax();
            }
        }
        else if (event.getKey() == this.keyboard.getKeyCode("RETURN") ||
                event.getKey() == this.keyboard.getKeyCode("NUMPADENTER"))
        {
            skin.deselect();
            skin.hideCompletePopup();
        }
        else if (skin.hasSelected())
            skin.deselect();
    }

    private void moveCursorToMax()
    {
        getModel().setCursorPos(getModel().getText().length());
    }*/
}
