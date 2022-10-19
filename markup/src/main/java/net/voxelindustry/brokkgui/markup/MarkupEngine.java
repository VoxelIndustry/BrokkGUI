package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.markup.definitions.MarkupElementDefinition;
import net.voxelindustry.brokkgui.markup.definitions.MarkupMetaElementDefinition;
import net.voxelindustry.brokkgui.markup.definitions.ParentDefinition;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MarkupEngine
{
    private static SAXReader saxReader;

    private static SAXReader getSaxReader()
    {
        if (saxReader == null)
        {
            saxReader = SAXReader.createDefault();
            saxReader.setMergeAdjacentText(true);
            saxReader.setStripWhitespaceText(true);
        }
        return saxReader;
    }

    public static GuiElement fromMarkupFile(Resource resource)
    {
        try
        {
            return fromMarkup(BrokkGuiPlatform.getInstance().getResourceHandler().readToString(resource));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
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
            document = parseXML(IOUtils.toInputStream(markup, charset));
        } catch (DocumentException e)
        {
            e.printStackTrace();
        }

        return walkXMLTree(document.getRootElement(), root, root == null ? null : MarkupElementRegistry.getElementDefinition(root.type()), true);
    }

    private static Document parseXML(InputStream is) throws DocumentException
    {
        return getSaxReader().read(is);
    }

    private static GuiElement walkXMLTree(Node node, GuiElement parentElement, ParentDefinition parentDefinition, boolean rootOfDocument)
    {
        if (node instanceof Element element)
        {
            MarkupElementReaderImpl elementReader = null;
            boolean consumed = false;

            if (parentDefinition != null && !parentDefinition.childElementReceivers().isEmpty())
            {
                for (var receiver : parentDefinition.childElementReceivers())
                {
                    if (receiver.canReceive(element.getName()))
                    {
                        if (elementReader == null)
                            elementReader = new MarkupElementReaderImpl();
                        elementReader.setElement(element);
                        elementReader.setParentElement(parentElement);
                        elementReader.setParentDefinition(parentDefinition);

                        receiver.receive(element.getName(), elementReader);

                        consumed = true;
                    }
                }
            }

            if (consumed)
                return parentElement;

            if (rootOfDocument && MarkupElementRegistry.getMetaElementDefinition(element.getName()) != null)
                throw new UnsupportedOperationException("Meta-Element cannot be used at root-level of document");

            var metaElement = MarkupElementRegistry.getMetaElementDefinition(element.getName());
            if (metaElement != null)
            {
                getMetaElement(parentElement, element, parentDefinition, metaElement);
                return null;
            }
            return getGuiElement(parentElement, element, parentDefinition, MarkupElementRegistry.getElementDefinition(element.getName()));
        }
        else if (node instanceof Text)
        {
            parentDefinition.textChildReceiver().decode(MarkupUtils.extractText(node.getText()), parentElement);
        }

        return parentElement;
    }

    static <T extends GuiElement> T getGuiElement(GuiElement parentElement, Element element, ParentDefinition parentDefinition, MarkupElementDefinition<T> currentDefinition)
    {
        if (currentDefinition == null)
            throw new UnsupportedOperationException("Missing element definition for tag: " + element.getName());

        var createdElement = currentDefinition.elementCreator().get();

        if (parentElement != null)
            parentElement.transform().addChild(createdElement.transform());

        for (var attribute : element.attributes())
        {
            currentDefinition.attributeEvents().entrySet().stream()
                    .filter(event -> event.getKey().contains(attribute.getName()))
                    .findAny()
                    .ifPresent(event -> event.getValue().accept(createdElement));

            var markupAttribute = currentDefinition.attributeMap().get(attribute.getName());
            if (markupAttribute == null && parentDefinition != null)
                markupAttribute = parentDefinition.childrenAttributeMap().get(attribute.getName());

            if (markupAttribute != null)
                markupAttribute.decoder().decode(attribute.getValue(), createdElement);
            else
            {
                for (var dynamicResolver : currentDefinition.dynamicAttributeResolvers())
                {
                    if (dynamicResolver.resolve(attribute.getName(), attribute.getValue(), createdElement))
                        break;
                }
            }
        }

        for (int i = 0, size = element.nodeCount(); i < size; i++)
        {
            var childNode = element.node(i);
            walkXMLTree(childNode, createdElement, currentDefinition, false);
        }
        return createdElement;
    }

    static <T> T getMetaElement(GuiElement parentElement, Element element, ParentDefinition parentDefinition, MarkupMetaElementDefinition<T> currentDefinition)
    {
        if (currentDefinition == null)
            throw new UnsupportedOperationException("Missing element definition for tag: " + element.getName());

        var createdElement = currentDefinition.elementCreator().get();

        for (var attribute : element.attributes())
        {
            var markupAttribute = currentDefinition.attributeMap().get(attribute.getName());

            if (markupAttribute != null)
                markupAttribute.decoder().decode(attribute.getValue(), createdElement);
        }

        for (int i = 0, size = element.nodeCount(); i < size; i++)
        {
            var childNode = element.node(i);
            walkXMLTree(childNode, parentElement, currentDefinition, false);
        }
        return createdElement;
    }
}
