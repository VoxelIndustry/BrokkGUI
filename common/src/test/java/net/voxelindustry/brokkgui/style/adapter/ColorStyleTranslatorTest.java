package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class ColorStyleTranslatorTest
{
    private ColorStyleTranslator adapter;

    @Before
    public void init()
    {
        this.adapter = new ColorStyleTranslator();
    }

    @Test
    public void testHexDecode()
    {
        Color color = Color.fromHex("#ffaa25");

        assertThat(adapter.decode("#ffaa25")).isEqualTo(color);

        Color color2 = Color.fromHex("#aaff25", 0.3f);

        assertThat(adapter.decode("#aaff25 30%")).isEqualTo(color2);
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

    @Test
    public void testError()
    {
        assertThatExceptionThrownBy(() -> adapter.decode("abcdef")).hasMessageContaining("Cannot retrieve specified Color constant.");
    }
}
