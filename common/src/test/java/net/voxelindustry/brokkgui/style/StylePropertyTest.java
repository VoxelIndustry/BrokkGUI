package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StylePropertyTest
{
    @BeforeEach
    public void init()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    public void forcefullSet()
    {
        String defaultValue = "defaultValue";
        String codeValue = "codeValue";
        String authorValue = "authorValue";

        StyleProperty<String> styleProperty = new StyleProperty<>(defaultValue, "name", String.class);

        assertThat(styleProperty.getValue()).isEqualTo(defaultValue);

        styleProperty.setValue(codeValue);
        assertThat(styleProperty.getValue()).isEqualTo(codeValue);

        styleProperty.setToDefault();

        assertThat(styleProperty.getValue()).isEqualTo(defaultValue);

        styleProperty.setStyle(StyleSource.AUTHOR, 10, authorValue);
        assertThat(styleProperty.getValue()).isEqualTo(authorValue);

        styleProperty.setValue(codeValue);

        assertThat(styleProperty.getValue()).isEqualTo(codeValue);

        // AUTHOR has lowest priority than CODE
        // Value should not be changed
        styleProperty.setStyle(StyleSource.AUTHOR, 10, authorValue);
        assertThat(styleProperty.getValue()).isNotEqualTo(authorValue).isEqualTo(codeValue);
    }
}
