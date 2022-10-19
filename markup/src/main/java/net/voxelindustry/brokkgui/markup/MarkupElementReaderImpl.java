package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.markup.definitions.MarkupElementDefinition;
import net.voxelindustry.brokkgui.markup.definitions.ParentDefinition;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

class MarkupElementReaderImpl implements MarkupElementReader
{
    private Element          element;
    private GuiElement       parentElement;
    private ParentDefinition parentDefinition;

    public void setElement(Element element)
    {
        this.element = element;
    }

    public void setParentElement(GuiElement parentElement)
    {
        this.parentElement = parentElement;
    }

    public void setParentDefinition(ParentDefinition parentDefinition)
    {
        this.parentDefinition = parentDefinition;
    }

    @Override
    public GuiElement getParentElement()
    {
        return parentElement;
    }

    @Override
    public String getName()
    {
        return element.getName();
    }

    @Override
    public String getText()
    {
        return MarkupUtils.extractText(element.getText());
    }

    @Override
    public Map<String, String> getRawAttributesMap()
    {
        return element.attributes().stream().collect(toMap(Attribute::getName, Attribute::getValue));
    }

    @Override
    public GuiElement parseElement()
    {
        return MarkupEngine.getGuiElement(parentElement, element, parentDefinition, MarkupElementRegistry.getElementDefinition(element.getName()));
    }

    @Override
    public GuiElement parseElementAs(String typeAlias)
    {
        return MarkupEngine.getGuiElement(parentElement, element, parentDefinition, MarkupElementRegistry.getElementDefinition(typeAlias));
    }

    @Override
    public <T extends GuiElement> T parseElementFrom(MarkupElementDefinition<T> elementDefinition)
    {
        return MarkupEngine.getGuiElement(parentElement, element, parentDefinition, elementDefinition);
    }
}
