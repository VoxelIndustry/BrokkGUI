package org.yggard.brokkgui.style;

import fr.ourten.teabeans.value.BaseListProperty;

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

	BaseListProperty<String> getStyleClass();

	StyleHolder getStyle();

	default void setStyle(String style)
	{
		this.setStyle(new StyleHolder(style));
	}

	void setStyle(StyleHolder style);
}
