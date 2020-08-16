package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldCompleteBehavior;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.GuiTextfieldCompleteSkin;

import java.util.Collection;
import java.util.List;

public class GuiTextfieldComplete extends GuiTextfield
{
    private final ListProperty<String> suggestionsProperty;
    private final Property<Float>      cellHeightProperty;
    private final Property<Integer>    maxSuggestionProperty;
    private final Property<Integer>    charBeforeCompletionProperty;
    private final Property<Float>      completePopupWidthProperty;

    public GuiTextfieldComplete(String text)
    {
        super(text);

        suggestionsProperty = new ListProperty<>(null);
        cellHeightProperty = new Property<>(16F);
        maxSuggestionProperty = new Property<>(4);
        charBeforeCompletionProperty = new Property<>(3);
        completePopupWidthProperty = new Property<>(0F);

        completePopupWidthProperty.bindProperty(transform().widthProperty());
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

    public ListProperty<String> getSuggestionsProperty()
    {
        return suggestionsProperty;
    }

    public Property<Float> getCellHeightProperty()
    {
        return cellHeightProperty;
    }

    public Property<Integer> getMaxSuggestionProperty()
    {
        return maxSuggestionProperty;
    }

    public Property<Integer> getCharBeforeCompletionProperty()
    {
        return charBeforeCompletionProperty;
    }

    public Property<Float> getCompletePopupWidthProperty()
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
            getCompletePopupWidthProperty().bindProperty(transform().widthProperty());
            return;
        }
        getCompletePopupWidthProperty().setValue(completePopupWidth);
    }
}