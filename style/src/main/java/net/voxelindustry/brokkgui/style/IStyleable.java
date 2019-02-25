package net.voxelindustry.brokkgui.style;

import fr.ourten.teabeans.value.BaseSetProperty;

public interface IStyleable
{
	void setID(String id);

	String getID();

	default void addStyleClass(String styleClass)
	{
		this.getStyleClass().add(styleClass);
	}

	default void removeStyleClass(String styleClass)
	{
		this.getStyleClass().remove(styleClass);
	}

	default void clearStyleClass()
	{
		this.getStyleClass().clear();
	}

	BaseSetProperty<String> getStyleClass();

	BaseSetProperty<String> getActivePseudoClass();

	StyleHolder getStyle();

	default void setStyle(String style)
	{
		this.getStyle().parseInlineCSS(style);
	}

    String getType();

	void refreshStyle();
}
