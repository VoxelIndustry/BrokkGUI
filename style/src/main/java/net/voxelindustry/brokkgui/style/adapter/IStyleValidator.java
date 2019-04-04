package net.voxelindustry.brokkgui.style.adapter;

@FunctionalInterface
public interface IStyleValidator<T>
{
    /**
     * Validate a css string and return the consumed length.
     * return 0 for an invalid css String.
     *
     * @param style css string
     * @return the consumed chars count
     */
    int validate(String style);
}
