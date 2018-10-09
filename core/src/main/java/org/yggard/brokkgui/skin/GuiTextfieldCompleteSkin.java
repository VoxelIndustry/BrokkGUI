package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.listener.ListValueChangeListener;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.behavior.GuiTextfieldCompleteBehavior;
import org.yggard.brokkgui.component.IGuiPopup;
import org.yggard.brokkgui.data.RectAlignment;
import org.yggard.brokkgui.data.RectOffset;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.element.GuiTextfieldComplete;
import org.yggard.brokkgui.internal.PopupHandler;
import org.yggard.brokkgui.panel.GuiAbsolutePane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiTextfieldCompleteSkin<T extends GuiTextfieldComplete> extends GuiTextfieldSkin<T>
{
    private CompletePopup popup;
    private boolean       isCompletePopupShown;

    public GuiTextfieldCompleteSkin(T model, GuiTextfieldCompleteBehavior<T> behaviour)
    {
        super(model, behaviour);

        this.popup = new CompletePopup(model, this);
        model.addStyleChild(popup);
    }

    public void showCompletePopup()
    {
        if (this.getModel().getSuggestionsProperty().isEmpty())
            return;

        this.popup.mapSuggestions();
        this.isCompletePopupShown = true;
        PopupHandler.getInstance().addPopup(popup);
    }

    public void hideCompletePopup()
    {
        PopupHandler.getInstance().removePopup(popup);
        this.isCompletePopupShown = false;
    }

    public boolean isCompletePopupShown()
    {
        return isCompletePopupShown;
    }

    public int getActualSuggestionSize()
    {
        return popup.getChildrens().size();
    }

    public void selectSuggestion(int index)
    {
        popup.setSelected(index);
    }

    public void deselect()
    {
        popup.deselect();
    }

    public int getSelectedIndex()
    {
        return popup.getChildrens().indexOf(popup.selected);
    }

    public String getSelectedValue()
    {
        return popup.selected.getText();
    }

    public boolean hasSelected()
    {
        return popup.selected != null;
    }

    private static class CompletePopup extends GuiAbsolutePane implements IGuiPopup
    {
        private static RectOffset LABEL_PADDING = new RectOffset(1, 2, 1, 2);

        private GuiTextfieldComplete     model;
        private GuiTextfieldCompleteSkin skin;
        private List<GuiLabel>           labelList;
        private GuiLabel                 selected;

        private String marked;

        CompletePopup(GuiTextfieldComplete model, GuiTextfieldCompleteSkin skin)
        {
            this.addStyleClass("complete-popup");

            this.getxPosProperty().bind(BaseExpression.biCombine(model.getxPosProperty(), model.getxTranslateProperty(),
                    (xPos, xTranslate) -> xPos + xTranslate));
            this.getyPosProperty().bind(BaseExpression.triCombine(model.getyPosProperty(),
                    model.getyTranslateProperty(), model.getHeightProperty(),
                    (yPos, yTranslate, height) -> yPos + yTranslate + height));

            this.getWidthProperty().bind(model.getCompletePopupWidthProperty());
            this.getHeightProperty().bind(
                    BaseExpression.biCombine(model.getCellHeightProperty(), this.getChildrensProperty(),
                            (cellHeight, children) -> cellHeight * children.size()));

            this.model = model;
            this.skin = skin;
            this.labelList = new ArrayList<>();

            model.getTextProperty().addListener((obs, oldValue, newValue) ->
            {
                if (selected == null)
                    refreshSuggestions(newValue);
            });
        }

        void setSelected(int index)
        {
            if (selected != null)
                selected.getActivePseudoClass().remove("select");
            selected = (GuiLabel) this.getChildrensProperty().get(index);
            selected.getActivePseudoClass().add("select");
        }

        void deselect()
        {
            if (selected == null)
                return;
            selected.getActivePseudoClass().remove("select");
            selected = null;
        }

        private static int compareLabel(GuiLabel first, GuiLabel second, String search)
        {
            String firstText = first.getText().toLowerCase();
            String secondText = second.getText().toLowerCase();

            if (firstText.equals(secondText))
                return 0;
            if (search.equals(firstText))
                return -1;
            if (search.equals(secondText))
                return 1;
            if (firstText.startsWith(search))
                return -1;
            if (secondText.startsWith(search))
                return 1;
            return firstText.length() < secondText.length() ? -1 : 1;
        }

        void mapSuggestions()
        {
            if (!this.labelList.isEmpty())
                return;
            this.labelList.addAll(model.getSuggestions().stream().map(this::makeLabel)
                    .collect(Collectors.toList()));

            model.getSuggestionsProperty().addListener((ListValueChangeListener<String>) (obs, oldValue, newValue) ->
            {
                if (oldValue != null)
                    this.labelList.removeIf(label -> label.getText().equals(oldValue));
                if (newValue != null)
                    this.labelList.add(makeLabel(newValue));
            });
            this.refreshSuggestions(model.getText());
        }

        private GuiLabel makeLabel(String text)
        {
            GuiLabel label = new GuiLabel(text);
            label.getWidthProperty().bind(this.getWidthProperty());
            label.getHeightProperty().bind(this.model.getCellHeightProperty());
            label.setTextPadding(LABEL_PADDING);
            label.setTextAlignment(RectAlignment.LEFT_CENTER);
            label.setOnClickEvent(e -> marked = text);
            return label;
        }

        private void refreshSuggestions(String text)
        {
            if (StringUtils.isEmpty(text) && model.getCharBeforeCompletion() != 0)
                return;

            this.clearChilds();

            if (StringUtils.isEmpty(text))
            {
                labelList.forEach(label ->
                        this.addChild(label, 0, model.getCellHeight() * getChildrensProperty().size()));
                return;
            }

            String toSearch = text.toLowerCase().trim();
            labelList.stream().filter(label -> label.getText().toLowerCase().contains(toSearch))
                    .sorted((first, second) -> compareLabel(first, second, toSearch)).forEach(label ->
                    this.addChild(label, 0, model.getCellHeight() * getChildrensProperty().size()));
        }

        @Override
        public void handleClick(int mouseX, int mouseY, int key)
        {
            super.handleClick(mouseX, mouseY, key);

            if (marked != null)
            {
                model.setText(marked);
                marked = null;
                skin.hideCompletePopup();
            }
        }
    }
}
