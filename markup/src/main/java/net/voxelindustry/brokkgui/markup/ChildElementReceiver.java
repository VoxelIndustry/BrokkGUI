package net.voxelindustry.brokkgui.markup;

public interface ChildElementReceiver
{
    boolean canReceive(String elementName);

    void receive(String elementName, MarkupElementReader reader);
}
