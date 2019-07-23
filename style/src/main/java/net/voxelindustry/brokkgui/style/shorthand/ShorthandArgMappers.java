package net.voxelindustry.brokkgui.style.shorthand;

public class ShorthandArgMappers
{
    public static final ShorthandArgMapper BOX_MAPPER = (index, count) ->
    {
        switch (count)
        {
            case 1:
                return new int[]{0, 1, 2, 3}; // top, right, bottom, left
            case 2:
                if (index == 0)
                    return new int[]{0, 2}; // top, bottom
                return new int[]{1, 3}; // right, left
            case 3:
                if (index == 0)
                    return new int[]{0}; // top
                if (index == 2)
                    return new int[]{2}; // bottom
                return new int[]{1, 3};  // right, left
            case 4:
                return new int[]{index};
            default:
                return new int[0];
        }
    };
}
