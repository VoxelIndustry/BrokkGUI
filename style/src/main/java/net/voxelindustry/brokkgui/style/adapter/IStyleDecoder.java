package net.voxelindustry.brokkgui.style.adapter;

@FunctionalInterface
public interface IStyleDecoder<T>
{
    /**
     * Decode a css string and return the value.
     *
     * @param style css string
     * @return the decoded value
     */
    T decode(String style);
}
