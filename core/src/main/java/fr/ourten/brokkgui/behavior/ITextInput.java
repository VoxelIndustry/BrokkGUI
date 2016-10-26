package fr.ourten.brokkgui.behavior;

public interface ITextInput
{
    String getText();

    void setText(String text);

    boolean isEditable();

    void setEditable(boolean editable);

    /**
     * Validate the content of the ITextInput if it has validators registered.
     */
    void validate();
}