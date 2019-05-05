package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.data.RectBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RectBoxTranslatorTest
{
    private RectBoxTranslator adapter;

    @BeforeAll
    public void init()
    {
        this.adapter = new RectBoxTranslator();
    }

    @Test
    public void oneValue()
    {
        RectBox box = adapter.decode("1.5");

        assertThat(box.getLeft())
                .isEqualTo(box.getTop())
                .isEqualTo(box.getRight())
                .isEqualTo(box.getBottom())
                .isEqualTo(1.5f);
    }

    @Test
    public void twoValue()
    {
        RectBox box = adapter.decode("1.5 0.2");

        assertThat(box.getLeft()).isEqualTo(box.getRight()).isEqualTo(0.2f);
        assertThat(box.getTop()).isEqualTo(box.getBottom()).isEqualTo(1.5f);
    }

    @Test
    public void threeValue()
    {
        RectBox box = adapter.decode("1.5 0.2 3.5");

        assertThat(box.getTop()).isEqualTo(1.5f);
        assertThat(box.getLeft()).isEqualTo(box.getRight()).isEqualTo(0.2f);
        assertThat(box.getBottom()).isEqualTo(3.5f);
    }

    @Test
    public void fourValue()
    {
        RectBox box = adapter.decode("1.5 0.2 3.5 .1");

        assertThat(box.getTop()).isEqualTo(1.5f);
        assertThat(box.getRight()).isEqualTo(0.2f);
        assertThat(box.getBottom()).isEqualTo(3.5f);
        assertThat(box.getLeft()).isEqualTo(0.1f);
    }
}