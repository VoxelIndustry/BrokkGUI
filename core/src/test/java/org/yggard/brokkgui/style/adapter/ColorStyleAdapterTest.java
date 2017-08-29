package org.yggard.brokkgui.style.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.paint.Color;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ColorStyleAdapterTest
{
    private ColorStyleAdapter adapter;

    @Before
    public void init()
    {
        this.adapter = new ColorStyleAdapter();
    }

    @Test
    public void testHexDecode()
    {
        Color color = Color.fromHex("#ffaa25");

        assertThat(adapter.decode("#ffaa25")).isEqualTo(color);
    }

    @Test
    public void testRGBDecode()
    {
        Color color = new Color(0.2f, 0.2f, 0.2f);

        assertThat(adapter.decode("rgb( 20%,20% ,20%)")).isEqualTo(color);
        assertThat(adapter.decode("rgb( 51, 51,51)")).isEqualTo(color);
    }

    @Test
    public void testRGBADecode()
    {
        Color color = new Color(0.2f, 0.2f, 0.2f, 0.5f);

        assertThat(adapter.decode("rgba( 20%,20% ,20%, 0.5)")).isEqualTo(color);
        assertThat(adapter.decode("rgba( 51, 51,51,0.5 )")).isEqualTo(color);
    }

    @Test
    public void testColorNameDecode()
    {
        Color color = Color.AQUA;

        assertThat(adapter.decode("aqua")).isEqualTo(color);
    }
}
