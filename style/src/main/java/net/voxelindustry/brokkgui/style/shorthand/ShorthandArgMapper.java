package net.voxelindustry.brokkgui.style.shorthand;

@FunctionalInterface
public interface ShorthandArgMapper
{
    /**
     * Map a shorthand syntax to child properties indexes
     *
     * @param index of the current argument (0 ... n)
     * @param count numbers of specified arguments (1 ... n)
     * @return an array containing the indexes of the child properties to which apply the current argument
     */
    int[] map(int index, int count);
}
