package net.voxelindustry.brokkgui.style.selector.structural;

import net.voxelindustry.brokkgui.style.IStyleParent;
import net.voxelindustry.brokkgui.style.StyleHolder;

import java.util.List;

public class StructuralSelectors
{
    private static StructuralSelector FIRST_CHILD = style -> style.getParent().isPresent()
            && style.getParent().getValue() instanceof IStyleParent
            && ((IStyleParent) style.getParent().getValue()).getChildCount() == 1
            || ((IStyleParent) style.getParent().getValue()).getChildStyles().get(0) == style;

    private static StructuralSelector LAST_CHILD = style ->
    {
        if (!style.getParent().isPresent()
                || !(style.getParent().getValue() instanceof IStyleParent))
            return false;
        if (((IStyleParent) style.getParent().getValue()).getChildCount() == 1)
            return true;

        List<StyleHolder> childStyles = ((IStyleParent) style.getParent().getValue()).getChildStyles();

        return childStyles.get(childStyles.size() - 1) == style;
    };

    private static StructuralSelector ONLY_CHILD = style -> style.getParent().isPresent()
            && style.getParent().getValue() instanceof IStyleParent
            && ((IStyleParent) style.getParent().getValue()).getChildCount() == 1;

    private static StructuralSelector FIRST_OF_TYPE = style ->
    {
        if (!style.getParent().isPresent()
                || !(style.getParent().getValue() instanceof IStyleParent))
            return false;
        if (((IStyleParent) style.getParent().getValue()).getChildCount() == 1)
            return true;

        List<StyleHolder> childStyles = ((IStyleParent) style.getParent().getValue()).getChildStyles();

        for (StyleHolder childStyle : childStyles)
        {
            if (childStyle == style)
                return true;
            if (childStyle.getOwner().getType().equals(style.getOwner().getType()))
                return false;
        }
        return false;
    };

    private static StructuralSelector LAST_OF_TYPE = style ->
    {
        if (!style.getParent().isPresent()
                || !(style.getParent().getValue() instanceof IStyleParent))
            return false;
        if (((IStyleParent) style.getParent().getValue()).getChildCount() == 1)
            return true;

        List<StyleHolder> childStyles = ((IStyleParent) style.getParent().getValue()).getChildStyles();

        for (int i = childStyles.size() - 1; i >= 0; i--)
        {
            StyleHolder childStyle = childStyles.get(i);
            if (childStyle == style)
                return true;
            if (childStyle.getOwner().getType().equals(style.getOwner().getType()))
                return false;
        }
        return false;
    };

    private static StructuralSelector ONLY_OF_TYPE = style ->
    {
        if (!style.getParent().isPresent()
                || !(style.getParent().getValue() instanceof IStyleParent))
            return false;
        if (((IStyleParent) style.getParent().getValue()).getChildCount() == 1)
            return true;

        List<StyleHolder> childStyles = ((IStyleParent) style.getParent().getValue()).getChildStyles();

        for (StyleHolder childStyle : childStyles)
        {
            if (childStyle.getOwner().getType().equals(style.getOwner().getType()) && childStyle != style)
                return false;
        }
        return true;
    };

    // Non functional until rework of the skin system. Cause stackoverflow from endless refreshStyle at the node level.
    private static StructuralSelector EMPTY = style -> style.getOwner() instanceof IStyleParent &&
            ((IStyleParent) style).getChildCount() == 0;

    public static boolean isStructural(String selector)
    {
        switch (selector)
        {
            case "first-child":
            case "last-child":
            case "only-child":
            case "first-of-type":
            case "last-of-type":
            case "only-of-type":
                return true;
        }
        return false;
    }

    public static StructuralSelector fromString(String selector)
    {
        switch (selector)
        {
            case "first-child":
                return FIRST_CHILD;
            case "last-child":
                return LAST_CHILD;
            case "only-child":
                return ONLY_CHILD;
            case "first-of-type":
                return FIRST_OF_TYPE;
            case "last-of-type":
                return LAST_OF_TYPE;
            case "only-of-type":
                return ONLY_OF_TYPE;
        }
        return null;
    }
}
