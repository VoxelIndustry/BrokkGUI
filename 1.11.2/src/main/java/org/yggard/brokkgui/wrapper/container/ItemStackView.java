package org.yggard.brokkgui.wrapper.container;

import org.yggard.brokkgui.control.GuiControl;
import org.yggard.brokkgui.skin.GuiSkinBase;

import fr.ourten.teabeans.value.BaseProperty;
import net.minecraft.item.ItemStack;

/**
 * @author Ourten 29 oct. 2016
 *
 *         A custom node used only for itemstack display. It cannot be used as a
 *         replacement of a slot.
 */
public class ItemStackView extends GuiControl
{
    private final BaseProperty<String>    alternateStringProperty;
    private final BaseProperty<ItemStack> stackProperty;

    private final BaseProperty<Boolean>   tooltipProperty;

    public ItemStackView(final ItemStack stack)
    {
        this.stackProperty = new BaseProperty<>(stack, "stackProperty");
        this.alternateStringProperty = new BaseProperty<>(null, "alternateStringProperty");
        this.tooltipProperty = new BaseProperty<>(false, "tooltipProperty");
    }

    public ItemStackView()
    {
        this(null);
    }

    public BaseProperty<ItemStack> getStackProperty()
    {
        return this.stackProperty;
    }

    public BaseProperty<String> getAlternateStringProperty()
    {
        return this.alternateStringProperty;
    }

    public BaseProperty<Boolean> getTooltipProperty()
    {
        return this.tooltipProperty;
    }

    public ItemStack getItemStack()
    {
        return this.stackProperty.getValue();
    }

    public void setItemStack(final ItemStack stack)
    {
        this.stackProperty.setValue(stack);
    }

    public String getAlternateString()
    {
        return this.alternateStringProperty.getValue();
    }

    /**
     * @param alternateString
     *            a string to be displayed in place of the usual itemstack
     *            quantity number at the down-right corner.
     */
    public void setAlternateString(final String alternateString)
    {
        this.alternateStringProperty.setValue(alternateString);
    }

    public boolean hasTooltip()
    {
        return this.getTooltipProperty().getValue();
    }

    public void setTooltip(final boolean tooltip)
    {
        this.getTooltipProperty().setValue(tooltip);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new ItemStackViewSkin(this, new ItemStackViewBehavior(this));
    }
}