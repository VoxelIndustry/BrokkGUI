package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.StyleableProperty;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StylePropertyTest
{
    @Test
    public void forcefullSet()
    {
        String defaultValue = "defaultValue";
        String codeValue = "codeValue";
        String authorValue = "authorValue";

        StyleableProperty<String> styleProperty = new StyleableProperty<>(defaultValue, "name", String.class);

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
