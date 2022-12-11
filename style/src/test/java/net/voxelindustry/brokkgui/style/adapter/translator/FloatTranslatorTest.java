package net.voxelindustry.brokkgui.style.adapter.translator;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class FloatTranslatorTest
{
    @Test
    void decode_givenTypicalFloats_thenShouldParseAndConsume()
    {
        var noLeadingDecimal = ".1255";
        var typical = "0.543";
        var noFloatingPart = "146";
        var emptyFloatingPart = "146.";

        var translator = new FloatTranslator();
        var consumedLength = new AtomicInteger();

        assertThat(translator.decode(noLeadingDecimal, consumedLength)).isEqualTo(.1255F);
        assertThat(consumedLength).hasValue(5);

        assertThat(translator.decode(typical, consumedLength)).isEqualTo(0.543F);
        assertThat(consumedLength).hasValue(5);

        assertThat(translator.decode(noFloatingPart, consumedLength)).isEqualTo(146);
        assertThat(consumedLength).hasValue(3);

        assertThat(translator.decode(emptyFloatingPart, consumedLength)).isEqualTo(146);
        assertThat(consumedLength).hasValue(4);
    }

    @Test
    void decode_givenPercentFloats_thenShouldParseAndConsume()
    {
        var typical = "12%";
        var floatingPercent = "0.5%";
        var hundredPercent = "100%";

        var translator = new FloatTranslator();
        var consumedLength = new AtomicInteger();

        assertThat(translator.decode(typical, consumedLength)).isEqualTo(.12F);
        assertThat(consumedLength).hasValue(3);

        assertThat(translator.decode(floatingPercent, consumedLength)).isEqualTo(0.005F);
        assertThat(consumedLength).hasValue(4);

        assertThat(translator.decode(hundredPercent, consumedLength)).isEqualTo(1);
        assertThat(consumedLength).hasValue(4);
    }
}