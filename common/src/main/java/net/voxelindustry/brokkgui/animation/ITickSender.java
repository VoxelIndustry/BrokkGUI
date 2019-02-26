package net.voxelindustry.brokkgui.animation;

public interface ITickSender
{
    void addTicking(ITicking toTick);

    void removeTicking(ITicking toTick);
}
