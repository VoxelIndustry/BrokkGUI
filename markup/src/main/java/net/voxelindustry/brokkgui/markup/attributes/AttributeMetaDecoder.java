package net.voxelindustry.brokkgui.markup.attributes;

@FunctionalInterface
public interface AttributeMetaDecoder<T>
{
    void decode(String attributeValue, T element);
}
