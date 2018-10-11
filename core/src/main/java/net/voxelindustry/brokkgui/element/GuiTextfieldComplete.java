package net.voxelindustry.brokkgui.element;

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

        this.suggestionsProperty = new BaseListProperty<>(null, "suggestionsListProperty");
        this.cellHeightProperty = new BaseProperty<>(16f, "cellHeightProperty");
        this.maxSuggestionProperty = new BaseProperty<>(4, "maxSuggestionProperty");
        this.charBeforeCompletionProperty = new BaseProperty<>(3, "charBeforeCompletionProperty");
        this.completePopupWidthProperty = new BaseProperty<>(0f, "completePopupWidthProperty");

        this.completePopupWidthProperty.bind(this.getWidthProperty());
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
        return this.getSuggestionsProperty().getValue();
    }

    public void addSuggestion(String suggestion)
    {
        this.getSuggestionsProperty().add(suggestion);
    }

    public boolean removeSuggestion(String suggestion)
    {
        return this.getSuggestionsProperty().remove(suggestion);
    }

    public void setSuggestions(Collection<String> suggestions)
    {
        this.getSuggestionsProperty().clear();
        this.getSuggestionsProperty().addAll(suggestions);
    }

    public float getCellHeight()
    {
        return this.getCellHeightProperty().getValue();
    }

    public void setCellHeight(float cellHeight)
    {
        this.getCellHeightProperty().setValue(cellHeight);
    }

    public int getMaxSuggestion()
    {
        return this.getMaxSuggestionProperty().getValue();
    }

    public void setMaxSuggestion(int maxSuggestion)
    {
        this.getMaxSuggestionProperty().setValue(maxSuggestion);
    }

    public int getCharBeforeCompletion()
    {
        return this.getCharBeforeCompletionProperty().getValue();
    }

    public void setCharBeforeCompletion(int charBeforeCompletion)
    {
        this.getCharBeforeCompletionProperty().setValue(charBeforeCompletion);
    }

    public float getCompletePopupWidth()
    {
        return this.getCompletePopupWidthProperty().getValue();
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
        if (this.getCompletePopupWidthProperty().isBound() && completePopupWidth != -1)
            this.getCompletePopupWidthProperty().unbind();
        else if (completePopupWidth == -1)
        {
            this.getCompletePopupWidthProperty().bind(this.getWidthProperty());
            return;
        }
        this.getCompletePopupWidthProperty().setValue(completePopupWidth);
    }
}