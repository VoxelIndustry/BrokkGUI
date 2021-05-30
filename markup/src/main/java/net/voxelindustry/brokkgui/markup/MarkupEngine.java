package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.component.GuiElement;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MarkupEngine
{
    private static final Map<String, MarkupElementDefinition<?>> elementDefinitions = new HashMap<>();

    public static <T extends GuiElement> void registerElementDefinition(String name, MarkupElementDefinition<T> definition)
    {
        elementDefinitions.put(name, definition);
    }

    public static <T extends GuiElement> MarkupElementDefinition<T> getElementDefinition(String name)
    {
        return (MarkupElementDefinition<T>) elementDefinitions.get(name);
    }

    public static GuiElement fromMarkup(String markup)
    {
        return fromMarkup(markup, null, StandardCharsets.UTF_8);
    }

    public static GuiElement fromMarkup(String markup, Charset charset)
    {
        return fromMarkup(markup, null, charset);
    }

    public static GuiElement fromMarkup(String markup, GuiElement root)
    {
        return fromMarkup(markup, root, StandardCharsets.UTF_8);
    }

    public static GuiElement fromMarkup(String markup, GuiElement root, Charset charset)
    {
        Document document = null;
        try
        {
            document = parseXML(IOUtils.toInputStream("""
                    <pane>
                        <label x="10" y="50" translate-x="50">Ceci est un label</label>
                    </pane>
                    """, StandardCharsets.UTF_8));
        } catch (DocumentException e)
        {
            e.printStackTrace();
        }

        return walkXMLTree(document.getRootElement(), root, getElementDefinition(root.type()));
    }

    private static Document parseXML(InputStream is) throws DocumentException
    {
        SAXReader reader = new SAXReader();
        reader.setMergeAdjacentText(true);
        reader.setStripWhitespaceText(true);
        return reader.read(is);
    }

    private static GuiElement walkXMLTree(Node node, GuiElement parentElement, MarkupElementDefinition<?> parentDefinition)
    {
        if (node instanceof Element)
        {
            var currentDefinition = MarkupEngine.getElementDefinition(node.getName());
            var createdElement = currentDefinition.elementCreator().get();

            if (parentElement != null)
                parentElement.transform().addChild(createdElement.transform());

            for (var attribute : ((Element) node).attributes())
            {
                currentDefinition.attributeEvents().entrySet().stream()
                        .filter(event -> event.getKey().contains(attribute.getName()))
                        .findAny()
                        .ifPresent(event -> event.getValue().accept(createdElement));

                boolean resolved = false;
                for (var dynamicResolver : currentDefinition.dynamicAttributeResolvers())
                {
                    if (dynamicResolver.resolve(attribute.getName(), attribute.getValue(), createdElement))
                    {
                        resolved = true;
                        break;
                    }
                }
                if (resolved)
                    continue;

                var markupAttribute = currentDefinition.attributeMap().get(attribute.getName());
                if (markupAttribute == null && parentDefinition != null)
                    markupAttribute = parentDefinition.childrenAttributeMap().get(attribute.getName());

                if (markupAttribute != null)
                    markupAttribute.decoder().decode(attribute.getValue(), createdElement);
            }

            for (int i = 0, size = ((Element) node).nodeCount(); i < size; i++)
            {
                var childNode = ((Element) node).node(i);
                walkXMLTree(childNode, createdElement, currentDefinition);
            }
            return createdElement;
        }
        else if (node instanceof Text)
            parentDefinition.textChildReceiver().decode(node.getText(), parentElement);

        return parentElement;
    }
}
