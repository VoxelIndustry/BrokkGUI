package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.sprite.Texture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class TextureStyleTranslatorTest
{
    private final TextureStyleTranslator adapter = new TextureStyleTranslator();

    @BeforeAll
    public static void init()
    {
        BrokkGuiPlatform.getInstance().setLogger(Logger.getLogger("TextureStyleTranslator"));
    }

    @Test
    public void decode_givenSimpleUrl_thenShouldParse()
    {
        var texture = new Texture("brokkgui:textures/gui/container_background.png");
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("assets(\"brokkgui:textures/gui/container_background.png\")", consumedLength))
                .isEqualTo(texture);
        assertThat(consumedLength).hasValue(56);
    }

    @Test
    public void decode_givenUrlWithUVMin_thenShouldParse()
    {
        var textureUV = new Texture("brokkgui:textures/gui/container_background.png", 100, 100);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("assets(\"brokkgui:textures/gui/container_background.png\", 100, 100)", consumedLength))
                .isEqualTo(textureUV);
        assertThat(consumedLength).hasValue(66);
    }

    @Test
    public void decode_givenUrlWithUVMinAndMax_thenShouldParse()
    {
        var textureUVST = new Texture("brokkgui:textures/gui/container_background.png", 16, 16, 32, 32);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("assets(\"brokkgui:textures/gui/container_background.png\", 16, 16,32, 32 )", consumedLength))
                .isEqualTo(textureUVST);
        assertThat(consumedLength).hasValue(72);
    }

    @Test
    public void decode_givenUrlWithPixelSize_thenShouldParse()
    {
        var textureSize = new Texture("brokkgui:textures/gui/container_background.png", 0, 0, 1, 1, 16, 16);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("assets(\"brokkgui:textures/gui/container_background.png\", 16px, 16px)", consumedLength))
                .isEqualTo(textureSize);
        assertThat(consumedLength).hasValue(68);
    }

    @Test
    public void decode_givenUrlWithUVMinAndPixelSize_thenShouldParse()
    {
        var textureUVSize = new Texture("brokkgui:textures/gui/container_background.png", 0.5F, 0.5F, 1, 1, 16, 16);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("assets(\"brokkgui:textures/gui/container_background.png\", 0.5, 0.5, 16px, 16px)", consumedLength))
                .isEqualTo(textureUVSize);
        assertThat(consumedLength).hasValue(78);
    }

    @Test
    public void decode_givenUrlWithUVMinAndMaxAndPixelSize_thenShouldParse()
    {
        var textureUVSTSize = new Texture("brokkgui:textures/gui/container_background.png", 0.5F, 0.5F, 0.8F, 0.8F, 16, 16);
        var consumedLength = new AtomicInteger();

        assertThat(adapter.decode("assets(\"brokkgui:textures/gui/container_background.png\", 0.5, 0.5, 0.8, 0.8, 16px, 16px)", consumedLength))
                .isEqualTo(textureUVSTSize);
        assertThat(consumedLength).hasValue(88);
    }

    @Test
    public void decode_givenInvalidUrl_thenShouldReturnNullResource()
    {
        assertThat(adapter.decode("file(brokkgui:textures/gui/container_background.png").getResource())
                .isNull();
    }
}
