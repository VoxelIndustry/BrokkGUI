package fr.ourten.brokkgui.wrapper.container;

import fr.ourten.brokkgui.control.GuiControl;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.teabeans.value.BaseProperty;
import net.minecraft.item.ItemStack;

/**
 * @author Ourten 29 oct. 2016
 *
 *         A custom node used only for itemstack display. It cannot be used as a
 *         replacement of a slot.
 */
public class ItemStackNode extends GuiControl
{
    private final BaseProperty<ItemStack> stackProperty;

    public ItemStackNode(final ItemStack stack)
    {
        this.stackProperty = new BaseProperty<>(stack, "stackProperty");
    }

    public ItemStackNode()
    {
        this(null);
    }

    public BaseProperty<ItemStack> getStackProperty()
    {
        return this.stackProperty;
    }

    public ItemStack getItemStack()
    {
        return this.stackProperty.getValue();
    }

    public void setItemStack(final ItemStack stack)
    {
        this.stackProperty.setValue(stack);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new ItemStackNodeSkin(this, new ItemStackNodeBehavior(this));
    }
}