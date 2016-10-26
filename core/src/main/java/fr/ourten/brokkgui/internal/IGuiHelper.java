package fr.ourten.brokkgui.internal;

import fr.ourten.brokkgui.data.EAlignment;
import fr.ourten.brokkgui.data.Vector2i;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.paint.Texture;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IGuiHelper
{
    public void bindTexture(final Texture texture);

    public void beginScissor();

    public void endScissor();

    public void scissorBox(final float f, final float g, final float h, final float i);

    public void drawString(final String string, final int x, final int y, final Color color, final EAlignment alignment,
            final boolean shadow);

    public void drawString(final String string, final double x, final double y, final Color color,
            final boolean shadow);

    public void drawString(final String string, final double x, final double y, final Color color,
            final EAlignment alignment);

    public void drawString(final String string, final double x, final double y, final Color color);

    public void drawTexturedModalRect(final IGuiRenderer renderer, final double xStart, final double yStart,
            final double uMin, final double vMin, final double uMax, final double vMax, final double width,
            final double height, final float zLevel);

    public void drawTexturedModalRect(final IGuiRenderer renderer, final double xStart, final double yStart,
            final double uMin, final double vMin, final double width, final double height, final float zLevel);

    public void drawColoredEmptyRect(final IGuiRenderer renderer, final float startX, final float startY,
            final float width, final float height, final float zLevel, final Color c, final float thin);

    public void drawColoredRect(final IGuiRenderer renderer, final float startX, final float startY, final float width,
            final float height, final float zLevel, final Color c);

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