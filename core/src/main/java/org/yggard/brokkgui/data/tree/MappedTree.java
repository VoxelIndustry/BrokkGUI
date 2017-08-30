package org.yggard.brokkgui.data.tree;

import javax.annotation.Nonnull;
import java.util.*;

public class MappedTree<N> implements MutableTree<N>
{
    private final Map<N, N>        nodeParent = new HashMap<>();
    private final LinkedHashSet<N> nodeList   = new LinkedHashSet<>();

    @Override
    public boolean add(N parent, @Nonnull N node)
    {
        Objects.requireNonNull(node, "child node cannot be null");

        if (parent != null)
        {
            N current = parent;
            do
            {
                if (node.equals(current))
                    throw new IllegalArgumentException("node must not be the same or an ancestor of the parent");
            } while ((current = getParent(current)) != null);
        }

        boolean added = nodeList.add(node);
        if (parent != null)
        {
            nodeList.add(parent);
            nodeParent.put(node, parent);
        }
        return added;
    }

    @Override
    public boolean remove(@Nonnull N node, boolean cascade)
    {
        Objects.requireNonNull(node, "node cannot be null");

        if (!nodeList.contains(node))
            return false;
        if (cascade)
        {
            for (N child : getChildren(node))
                remove(child, true);
        }
        else
        {
            for (N child : getChildren(node))
                nodeParent.remove(child);
        }
        nodeList.remove(node);
        return true;
    }

    @Override
    public List<N> getRoots()
    {
        return getChildren(null);
    }

    @Override
    public N getParent(@Nonnull N node)
    {
        Objects.requireNonNull(node, "node cannot be null");
        return nodeParent.get(node);
    }

    @Override
    public List<N> getChildren(N node)
    {
        List<N> children = new LinkedList<>();
        for (N n : nodeList)
        {
            N parent = nodeParent.get(n);
            if (node == null && parent == null)
                children.add(n);
            else if (node != null && parent != null && parent.equals(node))
                children.add(n);
        }
        return children;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (!this.isEmpty())
            dumpNodeStructure(builder, null, "- ");
        else
            builder.append("empty");
        return builder.toString();
    }

    private void dumpNodeStructure(StringBuilder builder, N node, String prefix)
    {
        if (node != null)
        {
            builder.append(prefix);
            builder.append(node.toString());
            builder.append('\n');
            prefix = "    " + prefix;
        }
        for (N child : getChildren(node))
            dumpNodeStructure(builder, child, prefix);
    }
}
