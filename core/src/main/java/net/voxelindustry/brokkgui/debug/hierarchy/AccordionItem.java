package net.voxelindustry.brokkgui.debug.hierarchy;

public interface AccordionItem
{
    float getHeaderPos();

    void setHeaderPos(float headerPos);

    float getHeaderSize();

    float getMinimalSize();

    boolean isCollapsed();
}
