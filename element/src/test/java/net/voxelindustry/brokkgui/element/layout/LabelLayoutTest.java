package net.voxelindustry.brokkgui.element.layout;

import net.voxelindustry.brokkgui.element.Label;
import net.voxelindustry.brokkgui.element.MockGuiRenderer;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@BrokkGuiTest(styleEngine = false)
public class LabelLayoutTest
{
    @Test
    void given(MockGuiRenderer mockRenderer)
    {
        var label = new Label("TEST");

        mockRenderer.stringWidth(5);
        mockRenderer.stringHeight(5);

        mockRenderer.render(label);

        mockRenderer
                .ordered()
                .assertDrawString(
                        eq("TEST"),
                        eq(0),
                        eq(0),
                        any(),
                        any(),
                        any())
                .assertComplete();
    }
}
