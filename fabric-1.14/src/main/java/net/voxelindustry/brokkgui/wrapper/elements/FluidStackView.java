package net.voxelindustry.brokkgui.wrapper.elements;

import fr.ourten.teabeans.value.BaseProperty;
import net.minecraft.fluid.Fluid;
import net.voxelindustry.brokkgui.control.GuiElement;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class FluidStackView extends GuiElement
{
    private BaseProperty<Fluid> fluidProperty;

    public FluidStackView(Fluid fluid)
    {
        super("fluidstack");

        this.fluidProperty = new BaseProperty<>(fluid, "fluidProperty");

        this.getStyle().registerProperty("flowing", false, Boolean.class);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new FluidStackViewSkin(this, new FluidStackViewBehavior(this));
    }

    public BaseProperty<Fluid> getFluidProperty()
    {
        return fluidProperty;
    }

    public BaseProperty<Boolean> getFlowingProperty()
    {
        return getStyle().getStyleProperty("flowing", Boolean.class);
    }

    public Fluid getFluid()
    {
        return this.getFluidProperty().getValue();
    }

    public void setFluid(Fluid fluid)
    {
        this.getFluidProperty().setValue(fluid);
    }

    public boolean isFlowing()
    {
        return this.getFlowingProperty().getValue();
    }

    public void setFlowing(boolean isFlowing)
    {
        this.getFlowingProperty().setValue(isFlowing);
    }
}
