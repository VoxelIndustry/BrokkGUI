package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.element.GuiTextfieldComplete;
import net.voxelindustry.brokkgui.event.FocusEvent;
import net.voxelindustry.brokkgui.event.KeyEvent;
import net.voxelindustry.brokkgui.skin.GuiTextfieldCompleteSkin;

public class GuiTextfieldCompleteBehavior<T extends GuiTextfieldComplete> extends GuiTextfieldBehavior<T>
{
    private String tempText;

    public GuiTextfieldCompleteBehavior(T model)
    {
        super(model);

        model.getEventDispatcher().addHandler(FocusEvent.TYPE, this::onFocusEvent);
    }

    private void onFocusEvent(FocusEvent e)
    {
        GuiTextfieldCompleteSkin skin = (GuiTextfieldCompleteSkin) this.getModel().getSkin();

        if (!e.isFocused() && skin.isCompletePopupShown())
            skin.hideCompletePopup();
        else if (e.isFocused() && !skin.isCompletePopupShown() &&
                this.getModel().getText().length() >= this.getModel().getCharBeforeCompletion())
            skin.showCompletePopup();

    }

    @Override
    protected void onKeyTyped(KeyEvent event)
    {
        super.onKeyTyped(event);

        GuiTextfieldCompleteSkin skin = (GuiTextfieldCompleteSkin) this.getModel().getSkin();

        if (this.getModel().getText().length() < this.getModel().getCharBeforeCompletion() &&
                skin.isCompletePopupShown())
            skin.hideCompletePopup();
        else if (this.getModel().getText().length() >= this.getModel().getCharBeforeCompletion() &&
                !skin.isCompletePopupShown())
            skin.showCompletePopup();

        if (!skin.isCompletePopupShown())
            return;

        if (event.getKey() == this.keyboard.getKeyCode("UP"))
        {
            int current = skin.getSelectedIndex();

            if (current == -1)
            {
                skin.selectSuggestion(skin.getActualSuggestionSize() - 1);
                tempText = this.getModel().getText();
                this.getModel().setText(skin.getSelectedValue());
                this.moveCursorToMax();
            }
            else if (current > 0)
            {
                skin.selectSuggestion(current - 1);
                this.getModel().setText(skin.getSelectedValue());
                this.moveCursorToMax();
            }
            else
            {
                skin.deselect();
                this.getModel().setText(tempText);
                this.moveCursorToMax();
            }
        }
        else if (event.getKey() == this.keyboard.getKeyCode("DOWN"))
        {
            int current = skin.getSelectedIndex();

            if (current == -1)
                tempText = this.getModel().getText();
            if (current < skin.getActualSuggestionSize() - 1)
            {
                skin.selectSuggestion(current + 1);
                this.getModel().setText(skin.getSelectedValue());
                this.moveCursorToMax();
            }
            else
            {
                skin.deselect();
                this.getModel().setText(tempText);
                this.moveCursorToMax();
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
    }
}
