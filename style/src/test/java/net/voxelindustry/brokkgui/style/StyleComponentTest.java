package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.style.optional.BorderProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StyleComponentTest
{
    @BeforeEach
    public void before()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void parseSimpleCSS()
    {
        StyleComponent styleHolder = new StyleComponent();

        styleHolder.registerProperty("border-width", 0, Integer.class);
        styleHolder.registerProperty("border-color", Color.BLACK, Color.class);
        styleHolder.registerProperty("color", Color.WHITE, Color.class);

        styleHolder.parseInlineCSS("color: aqua; border-color: red; border-width: 2;");

        assertThat(styleHolder.getProperty("border-width", Integer.class).getValue()).isEqualTo(2);
        assertThat(styleHolder.getProperty("border-color", Color.class).getValue()).isEqualTo(Color.RED);
        assertThat(styleHolder.getProperty("color", Color.class).getValue()).isEqualTo(Color.AQUA);
    }

    @Test
    public void parseConditionalProperties()
    {
        StyleComponent styleHolder = new StyleComponent();

        assertThat(styleHolder.doesHoldProperty("border-color")).isEqualTo(HeldPropertyState.ABSENT);

        styleHolder.registerConditionalProperties("border*", BorderProperties.getInstance());

        assertThat(styleHolder.doesHoldProperty("border-color")).isEqualTo(HeldPropertyState.CONDITIONAL);
        styleHolder.parseInlineCSS("border-color: red;");

        assertThat(styleHolder.doesHoldProperty("border-color")).isEqualTo(HeldPropertyState.PRESENT);
    }
}
