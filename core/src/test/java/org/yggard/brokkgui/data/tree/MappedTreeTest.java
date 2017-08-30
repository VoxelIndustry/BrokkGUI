package org.yggard.brokkgui.data.tree;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class MappedTreeTest
{
    @Test
    public void addNodes()
    {
        MappedTree<String> tree = new MappedTree<>();

        tree.add(null, "ROOT");
        tree.add("ROOT", "CHILDREN1");

        assertThat(tree.getRoots()).contains("ROOT");
        assertThat(tree.getRoots()).doesNotContain("CHILDREN1");
        assertThat(tree.getChildren("ROOT")).contains("CHILDREN1");
        assertThat(tree.getParent("CHILDREN1")).isEqualTo("ROOT");
    }

    @Test
    public void removeNodes()
    {
        MappedTree<String> tree = new MappedTree<>();

        tree.add(null, "ROOT");
        tree.add("ROOT", "CHILDREN1");
        tree.remove("ROOT", true);

        assertThat(tree.getRoots()).doesNotContain("ROOT");

        tree.add(null, "ROOT");
        tree.add("ROOT", "CHILDREN1");
        tree.remove("ROOT", false);

        assertThat(tree.getRoots()).contains("CHILDREN1");

        tree.add(null, "ROOT");

        assertThat(tree.isEmpty()).isFalse();

        tree.clear();
        assertThat(tree.isEmpty()).isTrue();

        assertThat(tree.remove("CHILDREN1", true)).isFalse();
    }

    @Test
    public void argumentCheck()
    {
        MappedTree<String> tree = new MappedTree<>();

        assertThatExceptionThrownBy(() -> tree.add(null, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatExceptionThrownBy(() -> tree.getParent(null)).isInstanceOf(IllegalArgumentException.class);

        assertThatExceptionThrownBy(() -> tree.add("ROOT", "ROOT")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void toStringMethod()
    {
        MappedTree<String> tree = new MappedTree<>();

        tree.add(null, "ROOT");
        tree.add("ROOT", "CHILDREN1");
        tree.remove("ROOT", true);

        assertThat(tree.toString()).isEqualTo("empty");

        tree.add(null, "ROOT");
        tree.add("ROOT", "CHILDREN1");

        assertThat(tree.toString()).isEqualTo("- ROOT\n    - CHILDREN1\n");
    }
}
