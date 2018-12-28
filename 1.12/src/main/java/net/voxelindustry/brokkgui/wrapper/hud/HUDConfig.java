package net.voxelindustry.brokkgui.wrapper.hud;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HUDConfig
{
    private boolean openByDefault;
    private boolean closeOnHide;

    private RenderGameOverlayEvent.ElementType type;

    public HUDConfig(boolean openByDefault, boolean closeOnHide, RenderGameOverlayEvent.ElementType type)
    {
        this.openByDefault = openByDefault;
        this.closeOnHide = closeOnHide;
        this.type = type;
    }

    public boolean isOpenByDefault()
    {
        return openByDefault;
    }

    public boolean isCloseOnHide()
    {
        return closeOnHide;
    }

    public RenderGameOverlayEvent.ElementType getType()
    {
        return type;
    }

    public static Builder build()
    {
        return new Builder();
    }

    public static class Builder
    {
        private boolean openByDefault;
        private boolean closeOnHide;

        private RenderGameOverlayEvent.ElementType type;

        public Builder setOpenByDefault(boolean openByDefault)
        {
            this.openByDefault = openByDefault;
            return this;
        }

        public Builder setCloseOnHide(boolean closeOnHide)
        {
            this.closeOnHide = closeOnHide;
            return this;
        }

        public Builder setType(RenderGameOverlayEvent.ElementType type)
        {
            this.type = type;
            return this;
        }

        public HUDConfig create()
        {
            return new HUDConfig(openByDefault, closeOnHide, type);
        }
    }
}
