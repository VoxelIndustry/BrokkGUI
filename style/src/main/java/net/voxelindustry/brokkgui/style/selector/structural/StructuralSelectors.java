package net.voxelindustry.brokkgui.style.selector.structural;

import fr.ourten.teabeans.property.ListProperty;
import net.voxelindustry.brokkgui.component.impl.Transform;

import java.util.List;

public class StructuralSelectors
{
    private static final StructuralSelector FIRST_CHILD = style ->
            style.transform().parentProperty().isPresent() &&
                    (style.transform().parent().childrenProperty().size() == 1
                            || style.transform().parent().childrenProperty().get(0) == style.transform());

    private static final StructuralSelector LAST_CHILD = style ->
    {
        if (style.transform().parentProperty().isPresent())
            return false;

        ListProperty<Transform> children = style.transform().parent().childrenProperty();
        if (children.size() == 1)
            return true;

        return children.get(children.size() - 1) == style.transform();
    };

    private static StructuralSelector ONLY_CHILD = style ->
            style.transform().parentProperty().isPresent()
                    && style.transform().parent().childrenProperty().size() == 1;

    private static final StructuralSelector FIRST_OF_TYPE = style ->
    {
        if (style.transform().parentProperty().isPresent())
            return false;

        List<Transform> children = style.transform().parent().childrenProperty().getModifiableValue();
        if (children.size() == 1)
            return true;

        for (Transform child : children)
        {
            if (child == style.transform())
                return true;
            if (child.element().type().equals(style.element().type()))
                return false;
        }
        return false;
    };

    private static final StructuralSelector LAST_OF_TYPE = style ->
    {
        if (style.transform().parentProperty().isPresent())
            return false;

        ListProperty<Transform> children = style.transform().parent().childrenProperty();
        if (children.size() == 1)
            return true;

        for (int i = children.size() - 1; i >= 0; i--)
        {
            Transform child = children.get(i);
            if (child == style.transform())
                return true;
            if (child.element().type().equals(style.element().type()))
                return false;
        }
        return false;
    };

    private static final StructuralSelector ONLY_OF_TYPE = style ->
    {
        if (style.transform().parentProperty().isPresent())
            return false;

        List<Transform> children = style.transform().parent().childrenProperty().getModifiableValue();
        if (children.size() == 1)
            return true;

        for (Transform childStyle : children)
        {
            if (childStyle.element().type().equals(style.element().type()) && childStyle != style.transform())
                return false;
        }
        return true;
    };

    // Non functional until rework of the skin system. Cause stackoverflow from endless refreshStyle at the node level.
    private static final StructuralSelector EMPTY = style -> style.transform().childrenProperty().isEmpty();

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
