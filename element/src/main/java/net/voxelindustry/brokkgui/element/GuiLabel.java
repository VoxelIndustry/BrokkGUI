package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;

public class GuiLabel extends GuiLabeled
{
    public GuiLabel(boolean value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(char value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(byte value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(short value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(int value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(long value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(float value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(double value, GuiElement icon)
    {
        this(String.valueOf(value), icon);
    }

    public GuiLabel(boolean value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(char value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(byte value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(short value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(int value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(long value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(float value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(double value)
    {
        this(String.valueOf(value));
    }

    public GuiLabel(String text, GuiElement icon)
    {
        super(text, icon);
    }

    public GuiLabel(String text)
    {
        this(text, null);
    }

    public GuiLabel()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "label";
    }
}