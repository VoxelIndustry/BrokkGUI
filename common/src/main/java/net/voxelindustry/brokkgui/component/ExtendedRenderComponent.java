package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;

public interface ExtendedRenderComponent<T extends IRenderCommandReceiver> extends RenderComponent
{
    @Override
    @SuppressWarnings("unchecked")
    default void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {
        renderExtendedContent((T) renderer, mouseX, mouseY);
    }

    void renderExtendedContent(T renderer, float mouseX, float mouseY);
}
