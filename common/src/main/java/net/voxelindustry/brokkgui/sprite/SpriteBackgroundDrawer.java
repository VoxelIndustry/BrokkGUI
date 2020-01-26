package net.voxelindustry.brokkgui.sprite;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.shape.GuiShape;

public class SpriteBackgroundDrawer
{
    public static void drawBackground(GuiShape shape, IGuiRenderer renderer)
    {
        draw(shape, renderer, shape.getBackgroundTexture(), shape.getBackgroundRepeat(), shape.getBackgroundAnimation(), shape.getBackgroundPosition());
    }

    public static void drawForeground(GuiShape shape, IGuiRenderer renderer)
    {
        draw(shape, renderer, shape.getForegroundTexture(), shape.getForegroundRepeat(), shape.getForegroundAnimation(), shape.getForegroundPosition());
    }

    private static void draw(GuiShape shape, IGuiRenderer renderer, Texture texture, SpriteRepeat repeat, SpriteAnimation animation, RectBox position)
    {
        renderer.getHelper().bindTexture(texture);

        if (repeat == SpriteRepeat.NONE)
        {
            drawSimple(shape, renderer, texture, position);
        }
        else if (repeat == SpriteRepeat.REPEAT_X)
        {
            drawRepeatX(shape, renderer, texture, position);
        }
        else if (repeat == SpriteRepeat.REPEAT_Y)
        {
            drawRepeatY(shape, renderer, texture, position);
        }
        else
        {
            drawRepeatBoth(shape, renderer, texture, position);
        }
    }

    private static void drawSimple(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position)
    {
        if (position == RectBox.EMPTY)
        {
            renderer.getHelper().drawTexturedRect(renderer, shape.getLeftPos(), shape.getTopPos(), texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(), shape.getWidth(), shape.getHeight(), shape.getzLevel());
        }
        else
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    shape.getLeftPos() + position.getLeft(),
                    shape.getTopPos() + position.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    shape.getWidth() - position.getHorizontal(),
                    shape.getHeight() - position.getVertical(),
                    shape.getzLevel());
        }
    }

    private static void drawRepeatX(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position)
    {
        int repeatCount = (int) ((shape.getWidth() - position.getHorizontal()) / texture.getPixelWidth());
        float leftOver = (shape.getWidth() - position.getHorizontal()) % texture.getPixelWidth();

        for (int index = 0; index < repeatCount; index++)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    shape.getLeftPos() + position.getLeft() + index * texture.getPixelWidth(),
                    shape.getTopPos() + position.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    texture.getPixelWidth(),
                    shape.getHeight() - position.getVertical(),
                    shape.getzLevel());
        }

        if (leftOver != 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    shape.getLeftPos() + position.getLeft() + repeatCount * texture.getPixelWidth(),
                    shape.getTopPos() + position.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    leftOver,
                    shape.getHeight() - position.getVertical(),
                    shape.getzLevel());
        }
    }

    private static void drawRepeatY(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position)
    {
        int repeatCount = (int) ((shape.getHeight() - position.getVertical()) / texture.getPixelHeight());
        float leftOver = (shape.getHeight() - position.getVertical()) % texture.getPixelHeight();

        for (int index = 0; index < repeatCount; index++)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    shape.getLeftPos() + position.getLeft(),
                    shape.getTopPos() + position.getTop() + index * texture.getPixelHeight(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    shape.getWidth() - position.getHorizontal(),
                    texture.getPixelHeight(),
                    shape.getzLevel());
        }

        if (leftOver != 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    shape.getLeftPos() + position.getLeft(),
                    shape.getTopPos() + position.getTop() + repeatCount * texture.getPixelHeight(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    shape.getWidth() - position.getHorizontal(),
                    leftOver,
                    shape.getzLevel());
        }
    }

    private static void drawRepeatBoth(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position)
    {
        int repeatCountX = (int) ((shape.getWidth() - position.getHorizontal()) / texture.getPixelWidth());
        float leftOverX = (shape.getWidth() - position.getHorizontal()) % texture.getPixelWidth();
        int repeatCountY = (int) ((shape.getHeight() - position.getVertical()) / texture.getPixelHeight());
        float leftOverY = (shape.getHeight() - position.getVertical()) % texture.getPixelHeight();

        for (int xIndex = 0; xIndex < repeatCountX; xIndex++)
        {
            for (int yIndex = 0; yIndex < repeatCountY; yIndex++)
            {
                renderer.getHelper().drawTexturedRect(renderer,
                        shape.getLeftPos() + position.getLeft() + xIndex * texture.getPixelWidth(),
                        shape.getTopPos() + position.getTop() + yIndex * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        texture.getPixelWidth(),
                        texture.getPixelHeight(),
                        shape.getzLevel());
            }
        }

        if (leftOverX != 0)
        {
            for (int yIndex = 0; yIndex < repeatCountY; yIndex++)
            {
                renderer.getHelper().drawTexturedRect(renderer,
                        shape.getLeftPos() + position.getLeft() + repeatCountX * texture.getPixelWidth(),
                        shape.getTopPos() + position.getTop() + yIndex * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        leftOverX,
                        texture.getPixelHeight(),
                        shape.getzLevel());
            }
        }
        if (leftOverY != 0)
        {
            for (int xIndex = 0; xIndex < repeatCountX; xIndex++)
            {
                renderer.getHelper().drawTexturedRect(renderer,
                        shape.getLeftPos() + position.getLeft(),
                        shape.getTopPos() + position.getTop() + repeatCountY * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        texture.getPixelWidth(),
                        leftOverY,
                        shape.getzLevel());
            }
        }
        if (leftOverX != 0 && leftOverY != 0)
        {
            renderer.getHelper().drawTexturedRect(renderer,
                    shape.getLeftPos() + position.getLeft() + repeatCountX * texture.getPixelWidth(),
                    shape.getTopPos() + position.getTop() + repeatCountY * texture.getPixelHeight(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    leftOverX,
                    leftOverY,
                    shape.getzLevel());
        }
    }
}
