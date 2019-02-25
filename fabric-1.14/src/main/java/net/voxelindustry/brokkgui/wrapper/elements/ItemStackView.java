package net.voxelindustry.brokkgui.wrapper.elements;

import fr.ourten.teabeans.value.BaseProperty;
import net.minecraft.item.ItemStack;
import net.voxelindustry.brokkgui.control.GuiElement;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 29 oct. 2016
 *
 *         A custom node used only for itemstack display. It cannot be used as a
 *         replacement of a slot.
 */
public class ItemStackView extends GuiElement
{
    private final BaseProperty<String>    alternateStringProperty;
    private final BaseProperty<ItemStack> stackProperty;

    private final BaseProperty<Boolean> itemTooltipProperty;

    private final BaseProperty<Color> colorProperty;

    public ItemStackView(final ItemStack stack)
    {
        super("itemstack");

        this.stackProperty = new BaseProperty<>(stack, "stackProperty");
        this.alternateStringProperty = new BaseProperty<>(null, "alternateStringProperty");
        this.itemTooltipProperty = new BaseProperty<>(false, "itemTooltipProperty");

        this.colorProperty = new BaseProperty<>(Color.WHITE, "colorProperty");
    }

    public ItemStackView()
    {
        this(ItemStack.EMPTY);
    }

    public BaseProperty<ItemStack> getStackProperty()
    {
        return this.stackProperty;
    }

    public BaseProperty<String> getAlternateStringProperty()
    {
        return this.alternateStringProperty;
    }

    public BaseProperty<Boolean> getItemTooltipProperty()
    {
        return this.itemTooltipProperty;
    }

    public BaseProperty<Color> getColorProperty()
    {
        return this.colorProperty;
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
     *            a string to be displayed in place of the usual itemstack quantity
     *            number at the down-right corner.
     */
    public void setAlternateString(final String alternateString)
    {
        this.alternateStringProperty.setValue(alternateString);
    }

    public boolean hasItemTooltip()
    {
        return this.getItemTooltipProperty().getValue();
    }

    public void setItemTooltip(final boolean tooltip)
    {
        this.getItemTooltipProperty().setValue(tooltip);
    }

    public Color getColor()
    {
        return this.getColorProperty().getValue();
    }

    public void setColor(Color color)
    {
        this.getColorProperty().setValue(color);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new ItemStackViewSkin(this, new ItemStackViewBehavior(this));
    }
}