package net.voxelindustry.brokkgui.wrapper.elements;

import fr.ourten.teabeans.value.BaseProperty;
import net.minecraftforge.fluids.FluidStack;
import net.voxelindustry.brokkgui.control.GuiElement;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class FluidStackView extends GuiElement
{
    private BaseProperty<FluidStack> fluidStackProperty;

    public FluidStackView(FluidStack stack)
    {
        super("fluidstack");

        this.fluidStackProperty = new BaseProperty<>(stack, "fluidStackProperty");

        this.getStyle().registerProperty("flowing", false, Boolean.class);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new FluidStackViewSkin(this, new FluidStackViewBehavior(this));
    }

    public BaseProperty<FluidStack> getFluidStackProperty()
    {
        return fluidStackProperty;
    }

    public BaseProperty<Boolean> getFlowingProperty()
    {
        return getStyle().getStyleProperty("flowing", Boolean.class);
    }

    public FluidStack getFluidStack()
    {
        return this.getFluidStackProperty().getValue();
    }

    public void setFluidStack(FluidStack stack)
    {
        this.getFluidStackProperty().setValue(stack);
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
