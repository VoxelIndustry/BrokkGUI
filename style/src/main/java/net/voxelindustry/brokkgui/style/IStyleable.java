package net.voxelindustry.brokkgui.style;

import fr.ourten.teabeans.property.SetProperty;

public interface IStyleable
{
    void setID(String id);

    String getID();

    default void addStyleClass(String styleClass)
    {
        getStyleClass().add(styleClass);
    }

    default void removeStyleClass(String styleClass)
    {
        getStyleClass().remove(styleClass);
    }

    default void clearStyleClass()
    {
        getStyleClass().clear();
    }

    SetProperty<String> getStyleClass();

    SetProperty<String> getActivePseudoClass();

    StyleComponent getStyle();

    default void setStyle(String style)
    {
        getStyle().parseInlineCSS(style);
    }

    String type();

    void refreshStyle();
}
