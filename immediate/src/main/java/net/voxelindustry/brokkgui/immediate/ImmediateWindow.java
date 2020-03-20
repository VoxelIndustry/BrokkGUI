package net.voxelindustry.brokkgui.immediate;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.voxelindustry.brokkgui.immediate.element.BoxElement;
import net.voxelindustry.brokkgui.immediate.element.ButtonElement;
import net.voxelindustry.brokkgui.immediate.element.ImmediateWindowBridge;
import net.voxelindustry.brokkgui.immediate.element.TextBoxElement;
import net.voxelindustry.brokkgui.immediate.element.TextElement;
import net.voxelindustry.brokkgui.immediate.style.StyleType;

public abstract class ImmediateWindow extends BaseImmediateWindow implements ImmediateWindowBridge,
        BoxElement, TextElement, TextBoxElement, ButtonElement
{
    private Table<Class<?>, StyleType, Object> elementStyles = HashBasedTable.create();

    @Override
    public <T> void setStyleObject(T object, StyleType type)
    {
        this.elementStyles.put(object.getClass(), type, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getStyleObject(StyleType type, Class<T> objectClass)
    {
        return (T) this.elementStyles.get(objectClass, type);
    }
}
