package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.markup.definitions.MarkupElementDefinition;

import java.util.Map;

/**
 * This Reader cannot be safely used after the completion of the method passing it.
 * Implementation may recycle it for other elements to reduce allocation overhead during markup deserialization.
 * <p>
 * It is not cacheable.
 */
public interface MarkupElementReader
{
    GuiElement getParentElement();

    String getName();

    String getText();

    Map<String, String> getRawAttributesMap();

    GuiElement parseElement();

    GuiElement parseElementAs(String typeAlias);

    <T extends GuiElement> T parseElementFrom(MarkupElementDefinition<T> elementDefinition);
}
