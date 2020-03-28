package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericShorthandPropertyTest
{
    @BeforeEach
    public void init()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void orderedIdenticalSet()
    {
        GenericShorthandProperty shorthand = new GenericShorthandProperty("", "shorthand-test");

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<Integer> child2 = new StyleProperty<>(0, "child2", Integer.class);
        StyleProperty<Integer> child3 = new StyleProperty<>(0, "child3", Integer.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "10 20 30");

        assertThat(child1.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo(20);
        assertThat(child3.getValue()).isEqualTo(30);
    }

    @Test
    public void orderedVaryingSet()
    {
        GenericShorthandProperty shorthand = new GenericShorthandProperty("", "shorthand-test");

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<String> child2 = new StyleProperty<>("", "child2", String.class);
        StyleProperty<Float> child3 = new StyleProperty<>(0f, "child3", Float.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "10 somestring 30.2");

        assertThat(child1.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo("somestring");
        assertThat(child3.getValue()).isEqualTo(30.2f);
    }

    @Test
    public void unorderedSet()
    {
        GenericShorthandProperty shorthand = new GenericShorthandProperty("", "shorthand-test");

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<String> child2 = new StyleProperty<>("", "child2", String.class);
        StyleProperty<Float> child3 = new StyleProperty<>(0f, "child3", Float.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "somestring 30.2 10");

        assertThat(child1.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo("somestring");
        assertThat(child3.getValue()).isEqualTo(30.2f);
    }
}
