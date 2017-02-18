package org.yggard.brokkgui.internal;

import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.Vector2i;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IGuiHelper
{
    public void bindTexture(final Texture texture);

    public void beginScissor();

    public void endScissor();

    public void scissorBox(final float f, final float g, final float h, final float i);

    public void drawString(final String string, final int x, final int y, final float zLevel, final Color color,
            final EAlignment alignment, final boolean shadow);

    public void drawString(final String string, final double x, final double y, final float zLevel, final Color color,
            final boolean shadow);

    public void drawString(final String string, final double x, final double y, final float zLevel, final Color color,
            final EAlignment alignment);

    public void drawString(final String string, final double x, final double y, final float zLevel, final Color color);

    public void drawTexturedRect(final IGuiRenderer renderer, final float xStart, final float yStart, final float uMin,
            final float vMin, final float uMax, final float vMax, final float width, final float height,
            final float zLevel);

    public void drawTexturedRect(final IGuiRenderer renderer, final float xStart, final float yStart, final float uMin,
            final float vMin, final float width, final float height, final float zLevel);

    public void drawColoredEmptyRect(final IGuiRenderer renderer, final float startX, final float startY,
            final float width, final float height, final float zLevel, final Color c, final float thin);

    public void drawColoredRect(final IGuiRenderer renderer, final float startX, final float startY, final float width,
            final float height, final float zLevel, final Color c);

    public void drawTexturedCircle(final IGuiRenderer renderer, final float xStart, final float yStart,
            final float uMin, final float vMin, final float uMax, final float vMax, final float radius,
            final float zLevel);

    public void drawTexturedCircle(final IGuiRenderer renderer, final float xStart, final float yStart,
            final float uMin, final float vMin, final float radius, final float zLevel);

    public void drawColoredEmptyCircle(final IGuiRenderer renderer, final float startX, final float startY,
            final float radius, final float zLevel, final Color c, final float thin);

    public void drawColoredCircle(final IGuiRenderer renderer, final float startX, final float startY,
            final float radius, final float zLevel, final Color c);

    public void drawColoredLine(final IGuiRenderer renderer, final float startX, final float startY, final float endX,
            final float endY, final float lineWeight, final float zLevel, final Color c);

    public void translateVecToScreenSpace(final Vector2i vec);

    public String trimStringToPixelWidth(final String str, final int pixelWidth);

    public float getStringWidth(final String str);

    public float getStringHeight();
}