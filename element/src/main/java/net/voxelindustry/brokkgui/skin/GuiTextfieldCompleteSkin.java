package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldCompleteBehavior;
import net.voxelindustry.brokkgui.component.IGuiPopup;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.element.input.GuiTextfieldComplete;
import net.voxelindustry.brokkgui.element.pane.GuiAbsolutePane;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.style.StyleComponent;
import org.apache.commons.lang3.StringUtils;

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

        popup = new CompletePopup(model, this);
        transform().addChild(popup.transform());
    }

    public void showCompletePopup()
    {
        if (getModel().getSuggestionsProperty().isEmpty())
            return;

        popup.mapSuggestions();
        isCompletePopupShown = true;
        PopupHandler.getInstance(getModel().getWindow()).addPopup(popup);
    }

    public void hideCompletePopup()
    {
        PopupHandler.getInstance(getModel().getWindow()).removePopup(popup);
        isCompletePopupShown = false;
    }

    public boolean isCompletePopupShown()
    {
        return isCompletePopupShown;
    }

    public int getActualSuggestionSize()
    {
        return popup.getChildren().size();
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
        return popup.getChildren().indexOf(popup.selected);
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
        private static RectBox LABEL_PADDING = new RectBox(1, 2, 1, 2);

        private GuiTextfieldComplete     model;
        private GuiTextfieldCompleteSkin skin;
        private List<GuiLabel>           labelList;
        private GuiLabel                 selected;

        private String marked;

        CompletePopup(GuiTextfieldComplete model, GuiTextfieldCompleteSkin skin)
        {
            get(StyleComponent.class).styleClass().add("complete-popup");

            transform().xPosProperty().bindProperty(model.transform().xPosProperty().combine(model.transform().xTranslateProperty(), Float::sum));
            transform().yPosProperty().bindProperty(model.transform().yPosProperty().combine(
                    model.transform().yTranslateProperty(), model.transform().heightProperty(),
                    (yPos, yTranslate, height) -> yPos + yTranslate + height));

            transform().widthProperty().bindProperty(model.getCompletePopupWidthProperty());
            transform().heightProperty().bindProperty(
                    model.getCellHeightProperty().combine(transform().childrenProperty(),
                            (cellHeight, children) -> cellHeight * children.size()));

            this.model = model;
            this.skin = skin;
            labelList = new ArrayList<>();

            model.getTextProperty().addListener((obs, oldValue, newValue) ->
            {
                if (selected == null)
                    refreshSuggestions(newValue);
            });
        }

        void setSelected(int index)
        {
            if (selected != null)
                selected.get(StyleComponent.class).activePseudoClass().remove("select");
            selected = (GuiLabel) transform().childrenProperty().get(index).element();
            selected.get(StyleComponent.class).activePseudoClass().add("select");
        }

        void deselect()
        {
            if (selected == null)
                return;
            selected.get(StyleComponent.class).activePseudoClass().remove("select");
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
            if (!labelList.isEmpty())
                return;
            labelList.addAll(model.getSuggestions().stream().map(this::makeLabel)
                    .collect(Collectors.toList()));

            model.getSuggestionsProperty().addListener((ListValueChangeListener<String>) (obs, oldValue, newValue) ->
            {
                if (oldValue != null)
                    labelList.removeIf(label -> label.getText().equals(oldValue));
                if (newValue != null)
                    labelList.add(makeLabel(newValue));
            });
            refreshSuggestions(model.getText());
        }

        private GuiLabel makeLabel(String text)
        {
            GuiLabel label = new GuiLabel(text);
            label.transform().widthProperty().bindProperty(transform().widthProperty());
            label.transform().heightProperty().bindProperty(model.getCellHeightProperty());
            label.setTextPadding(LABEL_PADDING);
            label.setTextAlignment(RectAlignment.LEFT_CENTER);
            label.setOnClickEvent(e -> marked = text);
            return label;
        }

        private void refreshSuggestions(String text)
        {
            if (StringUtils.isEmpty(text) && model.getCharBeforeCompletion() != 0)
                return;

            clearChildren();

            if (StringUtils.isEmpty(text))
            {
                labelList.forEach(label ->
                        addChild(label, 0, model.getCellHeight() * transform().childrenProperty().size()));
                return;
            }

            String toSearch = text.toLowerCase().trim();
            labelList.stream().filter(label -> label.getText().toLowerCase().contains(toSearch))
                    .sorted((first, second) -> compareLabel(first, second, toSearch)).forEach(label ->
                    addChild(label, 0, model.getCellHeight() * transform().childrenProperty().size()));
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
