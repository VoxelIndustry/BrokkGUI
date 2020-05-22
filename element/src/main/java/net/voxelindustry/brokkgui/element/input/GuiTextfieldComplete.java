package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldCompleteBehavior;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.GuiTextfieldCompleteSkin;

import java.util.Collection;
import java.util.List;

public class GuiTextfieldComplete extends GuiTextfield
{
    private final BaseListProperty<String> suggestionsProperty;
    private final BaseProperty<Float>      cellHeightProperty;
    private final BaseProperty<Integer>    maxSuggestionProperty;
    private final BaseProperty<Integer>    charBeforeCompletionProperty;
    private final BaseProperty<Float>      completePopupWidthProperty;

    public GuiTextfieldComplete(String text)
    {
        super(text);

        suggestionsProperty = new BaseListProperty<>(null, "suggestionsListProperty");
        cellHeightProperty = new BaseProperty<>(16f, "cellHeightProperty");
        maxSuggestionProperty = new BaseProperty<>(4, "maxSuggestionProperty");
        charBeforeCompletionProperty = new BaseProperty<>(3, "charBeforeCompletionProperty");
        completePopupWidthProperty = new BaseProperty<>(0f, "completePopupWidthProperty");

        completePopupWidthProperty.bind(transform().widthProperty());
    }

    public GuiTextfieldComplete()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiTextfieldCompleteSkin<>(this, new GuiTextfieldCompleteBehavior<>(this));
    }

    public BaseListProperty<String> getSuggestionsProperty()
    {
        return suggestionsProperty;
    }

    public BaseProperty<Float> getCellHeightProperty()
    {
        return cellHeightProperty;
    }

    public BaseProperty<Integer> getMaxSuggestionProperty()
    {
        return maxSuggestionProperty;
    }

    public BaseProperty<Integer> getCharBeforeCompletionProperty()
    {
        return charBeforeCompletionProperty;
    }

    public BaseProperty<Float> getCompletePopupWidthProperty()
    {
        return completePopupWidthProperty;
    }

    public List<String> getSuggestions()
    {
        return getSuggestionsProperty().getValue();
    }

    public void addSuggestion(String suggestion)
    {
        getSuggestionsProperty().add(suggestion);
    }

    public boolean removeSuggestion(String suggestion)
    {
        return getSuggestionsProperty().remove(suggestion);
    }

    public void setSuggestions(Collection<String> suggestions)
    {
        getSuggestionsProperty().clear();
        getSuggestionsProperty().addAll(suggestions);
    }

    public float getCellHeight()
    {
        return getCellHeightProperty().getValue();
    }

    public void setCellHeight(float cellHeight)
    {
        getCellHeightProperty().setValue(cellHeight);
    }

    public int getMaxSuggestion()
    {
        return getMaxSuggestionProperty().getValue();
    }

    public void setMaxSuggestion(int maxSuggestion)
    {
        getMaxSuggestionProperty().setValue(maxSuggestion);
    }

    public int getCharBeforeCompletion()
    {
        return getCharBeforeCompletionProperty().getValue();
    }

    public void setCharBeforeCompletion(int charBeforeCompletion)
    {
        getCharBeforeCompletionProperty().setValue(charBeforeCompletion);
    }

    public float getCompletePopupWidth()
    {
        return getCompletePopupWidthProperty().getValue();
    }

    /**
     * Set the width of the popup displayed when auto completing.
     * By default the property is bound to the Textfield size.
     * Entering a value of -1 will rebind it to its width.
     *
     * @param completePopupWidth
     */
    public void setCompletePopupWidth(float completePopupWidth)
    {
        if (getCompletePopupWidthProperty().isBound() && completePopupWidth != -1)
            getCompletePopupWidthProperty().unbind();
        else if (completePopupWidth == -1)
        {
            getCompletePopupWidthProperty().bind(transform().widthProperty());
            return;
        }
        getCompletePopupWidthProperty().setValue(completePopupWidth);
    }
}