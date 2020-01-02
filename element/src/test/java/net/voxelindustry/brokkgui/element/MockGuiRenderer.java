package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.*;

public class MockGuiRenderer
{
    private IGuiRenderer mockRenderer;
    private IGuiHelper   mockHelper;

    private Optional<InOrder> rendererOrderChecker;
    private Optional<InOrder> helperOrderChecker;

    private int mouseX;
    private int mouseY;

    public MockGuiRenderer()
    {
        reset();
    }

    public void reset()
    {
        if (mockRenderer == null)
        {
            mockRenderer = mock(IGuiRenderer.class);
            mockHelper = mock(IGuiHelper.class);
        }
        else
        {
            Mockito.reset(mockRenderer, mockHelper);
        }

        doReturn(mockHelper).when(mockRenderer).getHelper();

        doAnswer(answer -> trimStringToWidth(answer.getArgument(0), answer.getArgument(1)))
                .when(mockHelper)
                .trimStringToPixelWidth(any(), any());
    }

    private String trimStringToWidth(String text, int width)
    {
        StringBuilder output = new StringBuilder();
        int currentSize = 0;

        for (int index = 0; index < text.length(); index++)
        {
            float charSize = mockHelper.getStringWidth(String.valueOf(text.charAt(index)));
            if (currentSize + charSize > width)
                return output.toString();

            currentSize += charSize;
            output.append(text.charAt(index));
        }

        return output.toString();
    }

    public void stringHeight(int height)
    {
        doReturn(height).when(mockHelper).getStringHeight();
    }

    public void stringWidth(float width)
    {
        stringWidth(text -> text.length() * width);
    }

    public void stringWidth(Function<String, Float> textWidthFunction)
    {
        doAnswer(answer -> textWidthFunction.apply(answer.getArgument(0)))
                .when(mockHelper)
                .getStringWidth(any());
    }

    public void mouseX(int mouseX)
    {
        this.mouseX = mouseX;
    }

    public void mouseY(int mouseY)
    {
        this.mouseY = mouseY;
    }

    public MockGuiRenderer ordered()
    {
        rendererOrderChecker = ofNullable(inOrder(mockRenderer));
        helperOrderChecker = ofNullable(inOrder(mockHelper));
        return this;
    }

    public MockGuiRenderer unordered()
    {
        rendererOrderChecker = empty();
        helperOrderChecker = empty();
        return this;
    }

    public MockGuiRenderer assertBindTexture(Texture texture)
    {
        verifyHelper().bindTexture(texture);
        return this;
    }

    public MockGuiRenderer assertBeginScissor()
    {
        verifyHelper().beginScissor();
        return this;
    }

    public MockGuiRenderer assertEndScissor()
    {
        verifyHelper().endScissor();
        return this;
    }

    public MockGuiRenderer assertScissorBox(float f, float g, float h, float i)
    {
        verifyHelper().scissorBox(f, g, h, i);
        return this;
    }

    public MockGuiRenderer assertDrawString(String string, float x, float y, float zLevel, Color textColor, Color shadowColor)
    {
        verifyHelper().drawString(string, x, y, zLevel, textColor, shadowColor);
        return this;
    }

    public MockGuiRenderer assertDrawString(String string, float x, float y, float zLevel, Color textColor)
    {
        verifyHelper().drawString(string, x, y, zLevel, textColor);
        return this;
    }

    public MockGuiRenderer assertDrawTexturedRect(float xStart, float yStart, float uMin, float vMin, float uMax, float vMax, float width, float height, float zLevel)
    {
        verifyHelper().drawTexturedRect(mockRenderer, xStart, yStart, uMin, vMin, uMax, vMax, width, height, zLevel);
        return this;
    }

    public MockGuiRenderer assertDrawTexturedRect(float xStart, float yStart, float uMin, float vMin, float width, float height, float zLevel)
    {
        verifyHelper().drawTexturedRect(mockRenderer, xStart, yStart, uMin, vMin, width, height, zLevel);
        return this;
    }

    public MockGuiRenderer assertDrawColoredRect(float xStart, float yStart, float width, float height, float zLevel, Color color)
    {
        verifyHelper().drawColoredRect(mockRenderer, xStart, yStart, width, height, zLevel, color);
        return this;
    }

    public MockGuiRenderer assertDrawColoredEmptyRect(float xStart, float yStart, float width, float height, float zLevel, Color color, float thin)
    {
        verifyHelper().drawColoredEmptyRect(mockRenderer, xStart, yStart, width, height, zLevel, color, thin);
        return this;
    }

    public MockGuiRenderer assertDrawTexturedCircle(float xStart, float yStart, float uMin, float vMin, float uMax, float vMax, float radius, float zLevel)
    {
        verifyHelper().drawTexturedCircle(mockRenderer, xStart, yStart, uMin, vMin, uMax, vMax, radius, zLevel);
        return this;
    }

    public MockGuiRenderer assertDrawTexturedCircle(float xStart, float yStart, float uMin, float vMin, float radius, float zLevel)
    {
        verifyHelper().drawTexturedCircle(mockRenderer, xStart, yStart, uMin, vMin, radius, zLevel);
        return this;
    }

    public MockGuiRenderer assertDrawColoredCircle(float xStart, float yStart, float radius, float zLevel, Color color)
    {
        verifyHelper().drawColoredCircle(mockRenderer, xStart, yStart, radius, zLevel, color);
        return this;
    }

    public MockGuiRenderer assertDrawColoredEmptyCircle(float xStart, float yStart, float radius, float zLevel, Color color, float thin)
    {
        verifyHelper().drawColoredEmptyCircle(mockRenderer, xStart, yStart, radius, zLevel, color, thin);
        return this;
    }

    public MockGuiRenderer assertDrawColoredLine(float xStart, float yStart, float endX, float endY, float lineWeight, float zLevel, Color color)
    {
        verifyHelper().drawColoredLine(mockRenderer, xStart, yStart, endX, endY, lineWeight, zLevel, color);
        return this;
    }

    public MockGuiRenderer assertDrawColoredArc(float centerX, float centerY, float radius, float zLevel, Color color, RectCorner corner)
    {
        verifyHelper().drawColoredArc(mockRenderer, centerX, centerY, radius, zLevel, color, corner);
        return this;
    }

    public MockGuiRenderer assertDrawColoredCross(float centerX, float centerY, float radius, float width, float zLevel, Color color)
    {
        verifyHelper().drawColoredCross(mockRenderer, centerX, centerY, radius, width, zLevel, color);
        return this;
    }

    public MockGuiRenderer assertStartAlphaMask(double opacity)
    {
        verifyHelper().startAlphaMask(opacity);
        return this;
    }

    public MockGuiRenderer assertCloseAlphaMask()
    {
        verifyHelper().closeAlphaMask();
        return this;
    }

    public MockGuiRenderer assertComplete()
    {
        if (helperOrderChecker.isPresent())
            helperOrderChecker.get().verifyNoMoreInteractions();
        else
            verifyNoMoreInteractions(mockHelper);
        return this;
    }

    private IGuiHelper verifyHelper()
    {
        if (helperOrderChecker.isPresent())
            return helperOrderChecker.get().verify(mockHelper);
        return mockHelper;
    }

    ////////////
    // RENDER //
    ////////////

    public void render(GuiElement element)
    {
        render(element, 1);
    }

    public void render(GuiElement element, int times)
    {
        render(element, new RenderPass[]{RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER});
    }

    public void render(GuiElement element, RenderPass[] passes)
    {
        render(element, passes, 1);
    }

    public void render(GuiElement element, RenderPass[] passes, int times)
    {
        for (int time = 0; time < times; time++)
        {
            for (RenderPass pass : passes)
            {
                element.renderNode(mockRenderer, pass, mouseX, mouseY);
            }
        }
    }
}
