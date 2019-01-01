package net.voxelindustry.brokkgui.wrapper.hud;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HUDConfig
{
    private boolean      openByDefault;
    private HUDGuiPolicy hudGuiPolicy;

    private RenderGameOverlayEvent.ElementType type;

    public HUDConfig(boolean openByDefault, HUDGuiPolicy hudGuiPolicy,
                     RenderGameOverlayEvent.ElementType type)
    {
        this.openByDefault = openByDefault;
        this.hudGuiPolicy = hudGuiPolicy;
        this.type = type;
    }

    public boolean isOpenByDefault()
    {
        return openByDefault;
    }

    public HUDGuiPolicy getHudGuiPolicy()
    {
        return hudGuiPolicy;
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
        private boolean                            openByDefault;
        private HUDGuiPolicy                       hudGuiPolicy;
        private RenderGameOverlayEvent.ElementType type;

        public Builder setOpenByDefault(boolean openByDefault)
        {
            this.openByDefault = openByDefault;
            return this;
        }

        public Builder setHudGuiPolicy(HUDGuiPolicy hudGuiPolicy)
        {
            this.hudGuiPolicy = hudGuiPolicy;
            return this;
        }

        public Builder setType(RenderGameOverlayEvent.ElementType type)
        {
            this.type = type;
            return this;
        }

        public HUDConfig create()
        {
            return new HUDConfig(openByDefault, hudGuiPolicy, type);
        }
    }
}
