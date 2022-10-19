package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.markup.ChildElementReceiver;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public interface MarkupAttributesGroup
{
    List<MarkupAttribute> getAttributes();

    List<MarkupAttribute> getChildrenAttributes();

    default DynamicAttributeResolver getDynamicResolver()
    {
        return null;
    }

    default List<String> getAttributesNames()
    {
        return Collections.emptyList();
    }

    default Consumer<GuiElement> onAttributeAdded()
    {
        return null;
    }

    default ChildElementReceiver childElementReceiver()
    {
        return null;
    }
}
