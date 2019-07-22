package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import net.voxelindustry.brokkgui.style.optional.BorderProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StyleHolderTest
{
    @BeforeAll
    public void before()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void parseSimpleCSS()
    {
        StyleHolder styleHolder = new StyleHolder();

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
        StyleHolder styleHolder = new StyleHolder();

        assertThat(styleHolder.doesHoldProperty("border-color")).isEqualTo(HeldPropertyState.ABSENT);

        styleHolder.registerConditionalProperties("border*", BorderProperties.getInstance());

        assertThat(styleHolder.doesHoldProperty("border-color")).isEqualTo(HeldPropertyState.CONDITIONAL);
        styleHolder.parseInlineCSS("border-color: red;");

        assertThat(styleHolder.doesHoldProperty("border-color")).isEqualTo(HeldPropertyState.PRESENT);
    }

    @Test
    public void refresh_givenIdChangeOnElement_thenShouldCallRefresh()
    {
        DummyGuiElement element = new DummyGuiElement("dummytype");
        StyleHolder styleHolder = element.add(spy(StyleHolder.class));

        reset(styleHolder);

        element.setId("id");

        verify(styleHolder).refresh();
        verifyNoMoreInteractions(styleHolder);
    }
}
