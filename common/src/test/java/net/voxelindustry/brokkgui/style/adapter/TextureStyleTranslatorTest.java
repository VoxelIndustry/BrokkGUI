package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.sprite.Texture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TextureStyleTranslatorTest
{
    private TextureStyleTranslator adapter;

    @Before
    public void init()
    {
        this.adapter = new TextureStyleTranslator();
    }

    @Test
    public void decode_givenSimpleUrl_thenShouldParse()
    {
        Texture texture = new Texture("brokkgui:textures/gui/container_background.png");
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\")"))
                .isEqualTo(texture);
    }

    @Test
    public void decode_givenUrlWithUVMin_thenShouldParse()
    {
        Texture textureUV = new Texture("brokkgui:textures/gui/container_background.png", 100, 100);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 100, 100)"))
                .isEqualTo(textureUV);
    }

    @Test
    public void decode_givenUrlWithUVMinAndMax_thenShouldParse()
    {
        Texture textureUVST = new Texture("brokkgui:textures/gui/container_background.png", 16, 16, 32, 32);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 16, 16,32, 32 )"))
                .isEqualTo(textureUVST);
    }

    @Test
    public void decode_givenUrlWithPixelSize_thenShouldParse()
    {
        Texture textureSize = new Texture("brokkgui:textures/gui/container_background.png", 0, 0, 1, 1, 16, 16);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 16px, 16px)"))
                .isEqualTo(textureSize);
    }

    @Test
    public void decode_givenUrlWithUVMinAndPixelSize_thenShouldParse()
    {
        Texture textureUVSize = new Texture("brokkgui:textures/gui/container_background.png", 0.5F, 0.5F, 1, 1, 16, 16);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 0.5, 0.5, 16px, 16px)"))
                .isEqualTo(textureUVSize);
    }

    @Test
    public void decode_givenUrlWithUVMinAndMaxAndPixelSize_thenShouldParse()
    {
        Texture textureUVSTSize = new Texture("brokkgui:textures/gui/container_background.png", 0.5F, 0.5F, 0.8F, 0.8F, 16, 16);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 0.5, 0.5, 0.8, 0.8, 16px, 16px)"))
                .isEqualTo(textureUVSTSize);
    }

    @Test
    public void decode_givenInvalidUrl_thenShouldReturnNull()
    {
        assertThat(adapter.decode("file(brokkgui:textures/gui/container_background.png"))
                .isNull();
    }
}
