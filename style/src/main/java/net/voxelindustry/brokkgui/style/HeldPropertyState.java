package net.voxelindustry.brokkgui.style;

public enum HeldPropertyState
{
    PRESENT,
    CONDITIONAL,
    ABSENT;

    public boolean isHeld()
    {
        return this != ABSENT;
    }
}
