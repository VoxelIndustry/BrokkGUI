package net.voxelindustry.brokkgui.markup.attributes;

public record MarkupMetaAttribute<T>(String name, AttributeMetaDecoder<T> decoder)
{
}
