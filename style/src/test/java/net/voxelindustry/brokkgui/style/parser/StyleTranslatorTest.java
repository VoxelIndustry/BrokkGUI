package net.voxelindustry.brokkgui.style.parser;

import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StyleTranslatorTest
{
    @Test
    public void testPrimitives()
    {
        assertThat((Integer) StyleTranslator.getInstance().decode("6", Integer.class)).isEqualTo(6);
        assertThat((Long) StyleTranslator.getInstance().decode("25645875250000", Long.class)).isEqualTo(25645875250000L);
        assertThat((Float) StyleTranslator.getInstance().decode("6.5", Float.class)).isEqualTo(6.5f);
        assertThat((Double) StyleTranslator.getInstance().decode("6.00000000012", Double.class)).isEqualTo(6.00000000012);
        assertThat((Boolean) StyleTranslator.getInstance().decode("true", Boolean.class)).isEqualTo(true);
        assertThat((String) StyleTranslator.getInstance().decode("one", String.class)).isEqualTo("one");
    }
}
