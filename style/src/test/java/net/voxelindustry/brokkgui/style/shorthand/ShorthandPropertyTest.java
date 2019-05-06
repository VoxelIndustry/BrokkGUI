package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShorthandPropertyTest
{
    @BeforeAll
    public void init()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void simpleMappedSet()
    {
        ShorthandArgMapper simpleMapper = (index, count) -> new int[]{index};
        ShorthandProperty<Integer> shorthand = new ShorthandProperty<>(0, "shorthand-simple",
                Integer.class, simpleMapper);

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<Integer> child2 = new StyleProperty<>(0, "child2", Integer.class);
        StyleProperty<Integer> child3 = new StyleProperty<>(0, "child3", Integer.class);
        StyleProperty<Integer> child4 = new StyleProperty<>(0, "child4", Integer.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);
        shorthand.addChild(child4);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "10 20 30 40");

        assertThat(child1.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo(20);
        assertThat(child3.getValue()).isEqualTo(30);
        assertThat(child4.getValue()).isEqualTo(40);
    }

    @Test
    public void mapToAllSet()
    {
        ShorthandArgMapper allMapper = (index, count) -> IntStream.range(0, 4).toArray();
        ShorthandProperty<Integer> shorthand = new ShorthandProperty<>(0, "shorthand-all",
                Integer.class, allMapper);

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<Integer> child2 = new StyleProperty<>(0, "child2", Integer.class);
        StyleProperty<Integer> child3 = new StyleProperty<>(0, "child3", Integer.class);
        StyleProperty<Integer> child4 = new StyleProperty<>(0, "child4", Integer.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);
        shorthand.addChild(child4);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "10");

        assertThat(child1.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo(10);
        assertThat(child3.getValue()).isEqualTo(10);
        assertThat(child4.getValue()).isEqualTo(10);
    }
}
