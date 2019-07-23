package net.voxelindustry.brokkgui.element.shape;

import net.voxelindustry.brokkgui.component.PaintStyle;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static net.voxelindustry.brokkgui.style.adapter.StyleEngine.ElementStyleStatus.DEFAULT_DISABLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RectangleTest
{
    @BeforeAll
    static void setup()
    {
        StyleEngine.getInstance().start();
    }

    @Test
    void useStyle_givenDefaultInitAtFalse_thenShouldUsePaintStyleAndRemoveBasicPaint()
    {
        StyleEngine.getInstance().elementsStyleStatus(DEFAULT_DISABLED);
        Rectangle rectangle = spy(new Rectangle(128, 128));

        // Execution
        rectangle.useStyle(true);

        // Asserts
        verify(rectangle).remove(Paint.class);
        verify(rectangle).add(any(PaintStyle.class));

        assertThat(rectangle.useStyle()).isTrue();
        assertThat(rectangle.get(PaintStyle.class).shape()).isEqualTo(Rectangle.SHAPE);
    }
}
