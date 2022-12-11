package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.data.RectBox;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RectBoxTranslatorTest
{
    private RectBoxTranslator adapter;

    @BeforeEach
    public void init()
    {
        adapter = new RectBoxTranslator();
    }

    @Test
    public void oneValue()
    {
        RectBox box = adapter.decode("1.5");

        Assertions.assertThat(box.getLeft())
                .isEqualTo(box.getTop())
                .isEqualTo(box.getRight())
                .isEqualTo(box.getBottom())
                .isEqualTo(1.5f);
    }

    @Test
    public void twoValue()
    {
        RectBox box = adapter.decode("1.5 0.2");

        Assertions.assertThat(box.getLeft()).isEqualTo(box.getRight()).isEqualTo(0.2f);
        Assertions.assertThat(box.getTop()).isEqualTo(box.getBottom()).isEqualTo(1.5f);
    }

    @Test
    public void threeValue()
    {
        RectBox box = adapter.decode("1.5 0.2 3.5");

        Assertions.assertThat(box.getTop()).isEqualTo(1.5f);
        Assertions.assertThat(box.getLeft()).isEqualTo(box.getRight()).isEqualTo(0.2f);
        Assertions.assertThat(box.getBottom()).isEqualTo(3.5f);
    }

    @Test
    public void fourValue()
    {
        RectBox box = adapter.decode("1.5 0.2 3.5 .1");

        Assertions.assertThat(box.getTop()).isEqualTo(1.5f);
        Assertions.assertThat(box.getRight()).isEqualTo(0.2f);
        Assertions.assertThat(box.getBottom()).isEqualTo(3.5f);
        Assertions.assertThat(box.getLeft()).isEqualTo(0.1f);
    }
}