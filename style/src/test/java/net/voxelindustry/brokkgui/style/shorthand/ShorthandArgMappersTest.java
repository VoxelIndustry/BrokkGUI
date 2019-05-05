package net.voxelindustry.brokkgui.style.shorthand;

import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShorthandArgMappersTest
{
    @BeforeAll
    public void init()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void mapOneToAll()
    {
        ShorthandProperty<Integer> shorthand = new ShorthandProperty<>(0, "shorthand-all",
                Integer.class, ShorthandArgMappers.BOX_MAPPER);

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

    @Test
    public void mapTwoToSides()
    {
        ShorthandProperty<Integer> shorthand = new ShorthandProperty<>(0, "shorthand-all",
                Integer.class, ShorthandArgMappers.BOX_MAPPER);

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<Integer> child2 = new StyleProperty<>(0, "child2", Integer.class);
        StyleProperty<Integer> child3 = new StyleProperty<>(0, "child3", Integer.class);
        StyleProperty<Integer> child4 = new StyleProperty<>(0, "child4", Integer.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);
        shorthand.addChild(child4);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "10 20");

        assertThat(child1.getValue()).isEqualTo(child3.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo(child4.getValue()).isEqualTo(20);
    }

    @Test
    public void mapThreeToTopSidesBottom()
    {
        ShorthandProperty<Integer> shorthand = new ShorthandProperty<>(0, "shorthand-all",
                Integer.class, ShorthandArgMappers.BOX_MAPPER);

        StyleProperty<Integer> child1 = new StyleProperty<>(0, "child1", Integer.class);
        StyleProperty<Integer> child2 = new StyleProperty<>(0, "child2", Integer.class);
        StyleProperty<Integer> child3 = new StyleProperty<>(0, "child3", Integer.class);
        StyleProperty<Integer> child4 = new StyleProperty<>(0, "child4", Integer.class);

        shorthand.addChild(child1);
        shorthand.addChild(child2);
        shorthand.addChild(child3);
        shorthand.addChild(child4);

        shorthand.setStyleRaw(StyleSource.AUTHOR, 1000, "10 20 30");

        assertThat(child1.getValue()).isEqualTo(10);
        assertThat(child2.getValue()).isEqualTo(child4.getValue()).isEqualTo(20);
        assertThat(child3.getValue()).isEqualTo(30);
    }

    @Test
    public void mapFourToEach()
    {
        ShorthandProperty<Integer> shorthand = new ShorthandProperty<>(0, "shorthand-all",
                Integer.class, ShorthandArgMappers.BOX_MAPPER);

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
}
