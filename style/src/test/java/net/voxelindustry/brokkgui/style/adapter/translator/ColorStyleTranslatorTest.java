package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkcolor.Color;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class ColorStyleTranslatorTest
{
    private final ColorStyleTranslator adapter = new ColorStyleTranslator();

    @Test
    public void testHexDecode()
    {
        var color = Color.fromHex("#ffaa25");
        var consumedLength = new AtomicInteger();
        assertThat(adapter.decode("#ffaa25", consumedLength)).isEqualTo(color);
        assertThat(consumedLength).hasValue(7);

        Color color2 = Color.fromHex("#aaff25", 0.3F);

        assertThat(adapter.decode("#aaff25 30%", consumedLength)).isEqualTo(color2);
        assertThat(consumedLength).hasValue(11);
    }

    @Test
    public void testRGBDecode()
    {
        var color = new Color(0.2f, 0.2f, 0.2f);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("rgb( 20%,20% ,20%)", consumedLength)).isEqualTo(color);
        assertThat(consumedLength).hasValue(18);

        assertThat(adapter.decode("rgb( 51, 51,51)", consumedLength)).isEqualTo(color);
        assertThat(consumedLength).hasValue(15);
    }

    @Test
    public void testRGBADecode()
    {
        var color = new Color(0.2F, 0.2F, 0.2F, 0.5F);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("rgba( 20%,20% ,20%, 50%)", consumedLength)).isEqualTo(color);
        assertThat(consumedLength).hasValue(24);

        assertThat(adapter.decode("rgba( 51, 51,51,50% )", consumedLength)).isEqualTo(color);
        assertThat(consumedLength).hasValue(21);
    }

    @Test
    public void testColorNameDecode()
    {
        var color = Color.AQUA;
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("aqua", consumedLength)).isEqualTo(color);
        assertThat(consumedLength).hasValue(4);

        var translucentAqua = new Color(color);
        translucentAqua.setAlpha(0.5F);
        assertThat(adapter.decode("aqua 50%", consumedLength)).isEqualTo(translucentAqua);
        assertThat(consumedLength).hasValue(8);
    }

    @Test
    public void testError()
    {
        Assertions.assertThatThrownBy(() -> adapter.decode("abcdef")).hasMessageContaining("Cannot retrieve specified Color constant.");
    }
}
