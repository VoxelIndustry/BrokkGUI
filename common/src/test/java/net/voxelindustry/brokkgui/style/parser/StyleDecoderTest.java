package net.voxelindustry.brokkgui.style.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleDecoderTest
{
    @Test
    public void testPrimitives()
    {
        assertThat((Integer) StyleDecoder.getInstance().decode("6", Integer.class)).isEqualTo(6);
        assertThat((Long) StyleDecoder.getInstance().decode("25645875250000", Long.class)).isEqualTo(25645875250000L);
        assertThat((Float) StyleDecoder.getInstance().decode("6.5", Float.class)).isEqualTo(6.5f);
        assertThat((Double) StyleDecoder.getInstance().decode("6.00000000012", Double.class)).isEqualTo(6.00000000012);
        assertThat((Boolean) StyleDecoder.getInstance().decode("true", Boolean.class)).isEqualTo(true);
        assertThat((String) StyleDecoder.getInstance().decode("one", String.class)).isEqualTo("one");
    }
}
