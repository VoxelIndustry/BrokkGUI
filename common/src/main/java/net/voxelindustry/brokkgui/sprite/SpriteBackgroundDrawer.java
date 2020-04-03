package net.voxelindustry.brokkgui.sprite;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.shape.GuiShape;

public class SpriteBackgroundDrawer
{
    public static void drawBackground(GuiShape shape, IGuiRenderer renderer)
    {
        Texture texture = shape.getBackgroundTexture();
        if (shape.getBackgroundAnimation() != null)
            texture = shape.getBackgroundAnimation().getCurrentFrame(System.currentTimeMillis());

        draw(shape, renderer, texture, shape.getBackgroundRepeat(), shape.getBackgroundPosition(), shape.getBackgroundRotationArray());
    }

    public static void drawForeground(GuiShape shape, IGuiRenderer renderer)
    {
        Texture texture = shape.getForegroundTexture();
        if (shape.getForegroundAnimation() != null)
            texture = shape.getForegroundAnimation().getCurrentFrame(System.currentTimeMillis());

        draw(shape, renderer, texture, shape.getForegroundRepeat(), shape.getForegroundPosition(), shape.getForegroundRotationArray());
    }

    private static void draw(GuiShape shape, IGuiRenderer renderer, Texture texture, SpriteRepeat repeat, RectBox position, SpriteRotation... rotations)
    {
        renderer.getHelper().bindTexture(texture);

        if (repeat == SpriteRepeat.NONE)
        {
            drawSimple(shape, renderer, texture, position, rotations == null ? SpriteRotation.NONE : rotations[0]);
        }
        else if (repeat == SpriteRepeat.REPEAT_X)
        {
            drawRepeatX(shape, renderer, texture, position, rotations);
        }
        else if (repeat == SpriteRepeat.REPEAT_Y)
        {
            drawRepeatY(shape, renderer, texture, position, rotations);
        }
        else
        {
            drawRepeatBoth(shape, renderer, texture, position, rotations);
        }
    }

    private static void drawSimple(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position, SpriteRotation rotation)
    {
        if (position == RectBox.EMPTY)
        {
            if (rotation != SpriteRotation.NONE)
                renderer.getHelper().drawTexturedRect(renderer, shape.getLeftPos(), shape.getTopPos(), texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(), shape.getWidth(), shape.getHeight(), shape.getzLevel(), rotation);
            else
                renderer.getHelper().drawTexturedRect(renderer, shape.getLeftPos(), shape.getTopPos(), texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(), shape.getWidth(), shape.getHeight(), shape.getzLevel());
        }
        else
        {
            if (rotation != SpriteRotation.NONE)
                renderer.getHelper().drawTexturedRect(renderer,
                        shape.getLeftPos() + position.getLeft(),
                        shape.getTopPos() + position.getTop(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        shape.getWidth() - position.getHorizontal(),
                        shape.getHeight() - position.getVertical(),
                        shape.getzLevel(),
                        rotation);
            else
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

    private static void drawRepeatX(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position, SpriteRotation... rotations)
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
                    shape.getzLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[index]);
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
                    shape.getzLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[repeatCount]);
        }
    }

    private static void drawRepeatY(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position, SpriteRotation... rotations)
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
                    shape.getzLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[index]);
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
                    shape.getzLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[repeatCount]);
        }
    }

    private static void drawRepeatBoth(GuiShape shape, IGuiRenderer renderer, Texture texture, RectBox position, SpriteRotation... rotations)
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
                        shape.getzLevel(),
                        rotations == null ? SpriteRotation.NONE : rotations[yIndex * repeatCountX + xIndex]);
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
                        shape.getzLevel(),
                        rotations == null ? SpriteRotation.NONE : rotations[repeatCountX * repeatCountY + yIndex]);
            }
        }
        if (leftOverY != 0)
        {
            for (int xIndex = 0; xIndex < repeatCountX; xIndex++)
            {
                renderer.getHelper().drawTexturedRect(renderer,
                        shape.getLeftPos() + position.getLeft() + xIndex * texture.getPixelWidth(),
                        shape.getTopPos() + position.getTop() + repeatCountY * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        texture.getPixelWidth(),
                        leftOverY,
                        shape.getzLevel(),
                        rotations == null ? SpriteRotation.NONE : rotations[repeatCountX * repeatCountY + repeatCountY + xIndex]);
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
                    shape.getzLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[repeatCountX * repeatCountY + repeatCountX + repeatCountY]);
        }
    }
}
