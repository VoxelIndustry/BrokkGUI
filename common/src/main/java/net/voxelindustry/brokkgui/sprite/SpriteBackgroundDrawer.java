package net.voxelindustry.brokkgui.sprite;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class SpriteBackgroundDrawer
{
    public static void drawBackground(Paint paint, IRenderCommandReceiver renderer)
    {
        Texture texture = paint.backgroundTexture();
        if (paint.backgroundAnimationResource() != null)
            texture = paint.backgroundAnimation().getCurrentFrame(System.currentTimeMillis());

        draw(paint, renderer, RenderPass.BACKGROUND, texture, paint.backgroundRepeat(), paint.backgroundColor(), paint.backgroundPosition(), paint.backgroundRotationArray());
    }

    public static void drawForeground(Paint paint, IRenderCommandReceiver renderer)
    {
        Texture texture = paint.foregroundTexture();
        if (paint.foregroundAnimationResource() != null)
            texture = paint.foregroundAnimation().getCurrentFrame(System.currentTimeMillis());

        draw(paint, renderer, RenderPass.FOREGROUND, texture, paint.foregroundRepeat(), paint.foregroundColor(), paint.foregroundPosition(), paint.foregroundRotationArray());
    }

    private static void draw(Paint paint, IRenderCommandReceiver renderer, RenderPass renderPass, Texture texture, SpriteRepeat repeat, Color color, RectBox position, SpriteRotation... rotations)
    {
        renderer.bindTexture(texture);

        switch (repeat)
        {
            case NONE:
                drawSimple(paint, renderer, renderPass, texture, color, position, rotations == null ? SpriteRotation.NONE : rotations[0]);
                break;
            case REPEAT_X:
                drawRepeatX(paint, renderer, renderPass, texture, color, position, rotations);
                break;
            case REPEAT_Y:
                drawRepeatY(paint, renderer, renderPass, texture, color, position, rotations);
                break;
            default:
                drawRepeatBoth(paint, renderer, renderPass, texture, color, position, rotations);
                break;
        }
    }

    private static void drawSimple(Paint paint, IRenderCommandReceiver renderer, RenderPass renderPass, Texture texture, Color color, RectBox position, SpriteRotation rotation)
    {
        if (position == RectBox.EMPTY)
        {
            if (rotation != SpriteRotation.NONE)
                renderer.drawTexturedRectWithColor(paint.transform().leftPos(),
                        paint.transform().topPos(),
                        texture.getUMin(),
                        texture.getVMin(),
                        texture.getUMax(),
                        texture.getVMax(),
                        paint.transform().width(),
                        paint.transform().height(),
                        paint.transform().zLevel(),
                        rotation,
                        renderPass,
                        color);
            else
                renderer.drawTexturedRectWithColor(paint.transform().leftPos(),
                        paint.transform().topPos(),
                        texture.getUMin(),
                        texture.getVMin(),
                        texture.getUMax(),
                        texture.getVMax(),
                        paint.transform().width(),
                        paint.transform().height(),
                        paint.transform().zLevel(),
                        renderPass,
                        color);
        }
        else
        {
            if (rotation != SpriteRotation.NONE)
                renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft(),
                        paint.transform().topPos() + position.getTop(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        paint.transform().width() - position.getHorizontal(),
                        paint.transform().height() - position.getVertical(),
                        paint.transform().zLevel(),
                        rotation,
                        renderPass,
                        color);
            else
                renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft(),
                        paint.transform().topPos() + position.getTop(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        paint.transform().width() - position.getHorizontal(),
                        paint.transform().height() - position.getVertical(),
                        paint.transform().zLevel(),
                        renderPass,
                        color);
        }
    }

    private static void drawRepeatX(Paint paint, IRenderCommandReceiver renderer, RenderPass renderPass, Texture texture, Color color, RectBox position, SpriteRotation... rotations)
    {
        int repeatCount = (int) ((paint.transform().width() - position.getHorizontal()) / texture.getPixelWidth());
        float leftOver = (paint.transform().width() - position.getHorizontal()) % texture.getPixelWidth();

        for (int index = 0; index < repeatCount; index++)
        {
            renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft() + index * texture.getPixelWidth(),
                    paint.transform().topPos() + position.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    texture.getPixelWidth(),
                    paint.transform().height() - position.getVertical(),
                    paint.transform().zLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[index],
                    renderPass,
                    color);
        }

        if (leftOver != 0)
        {
            renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft() + repeatCount * texture.getPixelWidth(),
                    paint.transform().topPos() + position.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    leftOver,
                    paint.transform().height() - position.getVertical(),
                    paint.transform().zLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[repeatCount],
                    renderPass,
                    color);
        }
    }

    private static void drawRepeatY(Paint paint, IRenderCommandReceiver renderer, RenderPass renderPass, Texture texture, Color color, RectBox position, SpriteRotation... rotations)
    {
        int repeatCount = (int) ((paint.transform().height() - position.getVertical()) / texture.getPixelHeight());
        float leftOver = (paint.transform().height() - position.getVertical()) % texture.getPixelHeight();

        for (int index = 0; index < repeatCount; index++)
        {
            renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft(),
                    paint.transform().topPos() + position.getTop() + index * texture.getPixelHeight(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    paint.transform().width() - position.getHorizontal(),
                    texture.getPixelHeight(),
                    paint.transform().zLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[index],
                    renderPass,
                    color);
        }

        if (leftOver != 0)
        {
            renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft(),
                    paint.transform().topPos() + position.getTop() + repeatCount * texture.getPixelHeight(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    paint.transform().width() - position.getHorizontal(),
                    leftOver,
                    paint.transform().zLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[repeatCount],
                    renderPass,
                    color);
        }
    }

    private static void drawRepeatBoth(Paint paint, IRenderCommandReceiver renderer, RenderPass renderPass, Texture texture, Color color, RectBox position, SpriteRotation... rotations)
    {
        int repeatCountX = (int) ((paint.transform().width() - position.getHorizontal()) / texture.getPixelWidth());
        float leftOverX = (paint.transform().width() - position.getHorizontal()) % texture.getPixelWidth();
        int repeatCountY = (int) ((paint.transform().height() - position.getVertical()) / texture.getPixelHeight());
        float leftOverY = (paint.transform().height() - position.getVertical()) % texture.getPixelHeight();

        for (int xIndex = 0; xIndex < repeatCountX; xIndex++)
        {
            for (int yIndex = 0; yIndex < repeatCountY; yIndex++)
            {
                renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft() + xIndex * texture.getPixelWidth(),
                        paint.transform().topPos() + position.getTop() + yIndex * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        texture.getPixelWidth(),
                        texture.getPixelHeight(),
                        paint.transform().zLevel(),
                        rotations == null ? SpriteRotation.NONE : rotations[yIndex * repeatCountX + xIndex],
                        renderPass,
                        color);
            }
        }

        if (leftOverX != 0)
        {
            for (int yIndex = 0; yIndex < repeatCountY; yIndex++)
            {
                renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft() + repeatCountX * texture.getPixelWidth(),
                        paint.transform().topPos() + position.getTop() + yIndex * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        leftOverX,
                        texture.getPixelHeight(),
                        paint.transform().zLevel(),
                        rotations == null ? SpriteRotation.NONE : rotations[repeatCountX * repeatCountY + yIndex],
                        renderPass,
                        color);
            }
        }
        if (leftOverY != 0)
        {
            for (int xIndex = 0; xIndex < repeatCountX; xIndex++)
            {
                renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft() + xIndex * texture.getPixelWidth(),
                        paint.transform().topPos() + position.getTop() + repeatCountY * texture.getPixelHeight(),
                        texture.getUMin(), texture.getVMin(),
                        texture.getUMax(), texture.getVMax(),
                        texture.getPixelWidth(),
                        leftOverY,
                        paint.transform().zLevel(),
                        rotations == null ? SpriteRotation.NONE : rotations[repeatCountX * repeatCountY + repeatCountY + xIndex],
                        renderPass,
                        color);
            }
        }
        if (leftOverX != 0 && leftOverY != 0)
        {
            renderer.drawTexturedRectWithColor(paint.transform().leftPos() + position.getLeft() + repeatCountX * texture.getPixelWidth(),
                    paint.transform().topPos() + position.getTop() + repeatCountY * texture.getPixelHeight(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    leftOverX,
                    leftOverY,
                    paint.transform().zLevel(),
                    rotations == null ? SpriteRotation.NONE : rotations[repeatCountX * repeatCountY + repeatCountX + repeatCountY],
                    renderPass,
                    color);
        }
    }
}
