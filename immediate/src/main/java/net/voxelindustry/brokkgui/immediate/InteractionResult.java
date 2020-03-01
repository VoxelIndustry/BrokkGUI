package net.voxelindustry.brokkgui.immediate;

public enum InteractionResult
{
    NONE,
    HOVERED,
    CLICKED;

    public boolean isHovered()
    {
        return this == HOVERED || this == CLICKED;
    }

    public boolean isClicked()
    {
        return this == CLICKED;
    }
}
