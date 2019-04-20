package net.voxelindustry.brokkgui.paint;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Color1DGradientTest
{
    @Test
    public void twoValuesGradient()
    {
        Color1DGradient gradient = Color1DGradient.build().color(Color.RED, 0).color(Color.YELLOW, 1).create();

        assertThat(gradient.getValue(0)).isEqualTo(Color.RED);
        assertThat(gradient.getValue(1)).isEqualTo(Color.YELLOW);
    }

    @Test
    public void twoValuesCachedGradient()
    {
        Color1DGradient gradient = Color1DGradient.build().color(Color.RED, 0).color(Color.YELLOW, 1).precompute(0.5f).create();

        assertThat(gradient.getValue(0)).isEqualTo(Color.RED);
        assertThat(gradient.getValue(1)).isEqualTo(Color.YELLOW);
    }

    @Test
    public void threeValuesWrappingGradient()
    {
        Color1DGradient gradient = Color1DGradient.build().color(Color.RED, 0).color(Color.YELLOW, 0.5f).color(Color.RED, 1).create();

        assertThat(gradient.getValue(0)).isEqualTo(Color.RED);
        assertThat(gradient.getValue(0.5f)).isEqualTo(Color.YELLOW);
        assertThat(gradient.getValue(1)).isEqualTo(Color.RED);

        assertThat(gradient.getValue(0.25f)).isEqualTo(Color.RED.interpolate(Color.YELLOW, 0.5f));
        assertThat(gradient.getValue(0.75f)).isEqualTo(Color.RED.interpolate(Color.YELLOW, 0.5f));
    }

    @Test
    public void threeValuesWrappingCachedGradient()
    {
        Color1DGradient gradient = Color1DGradient.build().color(Color.RED, 0).color(Color.YELLOW, 0.5f).color(Color.RED, 1).precompute(0.05f).create();

        assertThat(gradient.getValue(0)).isEqualTo(Color.RED);
        assertThat(gradient.getValue(0.5f)).isEqualTo(Color.YELLOW);
        assertThat(gradient.getValue(1)).isEqualTo(Color.RED);

        assertThat(gradient.getValue(0.25f)).isEqualTo(Color.RED.interpolate(Color.YELLOW, 0.5f));
        assertThat(gradient.getValue(0.75f)).isEqualTo(Color.RED.interpolate(Color.YELLOW, 0.5f));
    }
}
