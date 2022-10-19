package net.voxelindustry.brokkgui.markup.definitions;

import net.voxelindustry.brokkgui.markup.ChildElementReceiver;
import net.voxelindustry.brokkgui.markup.attributes.AttributeDecoder;
import net.voxelindustry.brokkgui.markup.attributes.MarkupAttribute;

import java.util.List;
import java.util.Map;

public interface ParentDefinition
{
    Map<String, MarkupAttribute> childrenAttributeMap();

    List<ChildElementReceiver> childElementReceivers();

    AttributeDecoder textChildReceiver();
}
