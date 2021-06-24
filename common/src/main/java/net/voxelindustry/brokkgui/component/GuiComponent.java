package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import fr.ourten.teabeans.property.specific.DoubleProperty;
import fr.ourten.teabeans.property.specific.FloatProperty;
import fr.ourten.teabeans.property.specific.IntProperty;
import fr.ourten.teabeans.property.specific.LongProperty;
import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.internal.IKeyboardUtil;
import net.voxelindustry.brokkgui.internal.IMouseUtil;
import net.voxelindustry.brokkgui.internal.ITextHelper;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.IEventEmitter;

public abstract class GuiComponent implements IEventEmitter
{
    private GuiElement element;
    private Transform  transform;

    public void attach(GuiElement element)
    {
        this.element = element;
        transform = element.transform();
    }

    public void detach(GuiElement element)
    {

    }

    public GuiElement element()
    {
        return element;
    }

    public boolean hasElement()
    {
        return element == null;
    }

    public Transform transform()
    {
        return transform;
    }

    protected ITextHelper textHelper()
    {
        return BrokkGuiPlatform.getInstance().getTextHelper();
    }

    protected IKeyboardUtil keyboard()
    {
        return BrokkGuiPlatform.getInstance().getKeyboardUtil();
    }

    protected IMouseUtil mouse()
    {
        return BrokkGuiPlatform.getInstance().getMouseUtil();
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        return element().getEventDispatcher();
    }

    protected <T> Property<T> createRenderProperty(T initialValue)
    {
        Property<T> property = new Property<>(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected BooleanProperty createRenderPropertyBoolean(boolean initialValue)
    {
        BooleanProperty property = new BooleanProperty(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected FloatProperty createRenderPropertyFloat(float initialValue)
    {
        FloatProperty property = new FloatProperty(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected DoubleProperty createRenderPropertyDouble(double initialValue)
    {
        DoubleProperty property = new DoubleProperty(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected IntProperty createRenderPropertyInt(int initialValue)
    {
        IntProperty property = new IntProperty(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected LongProperty createRenderPropertyLong(long initialValue)
    {
        LongProperty property = new LongProperty(initialValue);
        property.addListener(this::onRenderPropertyChange);
        return property;
    }

    protected <T> Property<T> createRenderPropertyPropagateChildren(T initialValue)
    {
        Property<T> property = new Property<>(initialValue);
        property.addListener(this::onRenderPropertyPropagatedChange);
        return property;
    }

    protected BooleanProperty createRenderPropertyPropagateChildrenBoolean(boolean initialValue)
    {
        BooleanProperty property = new BooleanProperty(initialValue);
        property.addListener(this::onRenderPropertyPropagatedChange);
        return property;
    }

    private void onRenderPropertyChange(Observable observable)
    {
        if (element() != null)
            element().markRenderDirty();
    }

    private void onRenderPropertyPropagatedChange(Observable observable)
    {
        if (element() != null)
        {
            element().markRenderDirty();
            element().markChildRenderDirty();
        }
    }
}
