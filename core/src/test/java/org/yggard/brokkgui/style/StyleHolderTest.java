package org.yggard.brokkgui.style;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.paint.Color;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StyleHolderTest
{
    @Test
    public void parseSimpleCSS()
    {
        StyleHolder styleHolder = new StyleHolder(null);

        styleHolder.registerProperty("-border-thin", 0, Integer.class);
        styleHolder.registerProperty("-border-color", Color.BLACK, Color.class);
        styleHolder.registerProperty("-color", Color.WHITE, Color.class);

        styleHolder.parseInlineCSS("-color: aqua; -border-color: red; -border-thin: 2;");

        assertThat(styleHolder.getStyleProperty("-border-thin", Integer.class).getValue()).isEqualTo(2);
        assertThat(styleHolder.getStyleProperty("-border-color", Color.class).getValue()).isEqualTo(Color.RED);
        assertThat(styleHolder.getStyleProperty("-color", Color.class).getValue()).isEqualTo(Color.AQUA);
    }
}
