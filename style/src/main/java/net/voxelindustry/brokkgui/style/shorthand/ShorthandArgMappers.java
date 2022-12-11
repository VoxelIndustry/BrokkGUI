package net.voxelindustry.brokkgui.style.shorthand;

public class ShorthandArgMappers
{
    private static final int[] topRightBottomLeft = new int[] { 0, 1, 2, 3 };

    private static final int[] topBottom = new int[] { 0, 2 };
    private static final int[] rightLeft = new int[] { 1, 3 };

    private static final int[] top    = new int[] { 0 };
    private static final int[] bottom = new int[] { 2 };
    private static final int[] empty  = new int[0];

    public static ShorthandArgMapper BOX_MAPPER = (index, count) ->
            switch (count)
                    {
                        case 1 -> topRightBottomLeft;
                        case 2 -> index == 0 ? topBottom : rightLeft;
                        case 3 -> index == 0 ? top : index == 2 ? bottom : rightLeft;
                        case 4 -> new int[] { index };
                        default -> empty;
                    };
}
