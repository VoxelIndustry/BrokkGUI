package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.control.GuiSkinedElement;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.style.StyleComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiSkinBase<T extends GuiSkinedElement> implements IGuiSkin
{
    private final List<GuiElement> childrens;
    private final T                model;

    public GuiSkinBase(T model)
    {
        if (model == null)
            throw new IllegalArgumentException("Cannot pass a null model");
        this.model = model;

        childrens = new ArrayList<>();
    }

    public T getModel()
    {
        return model;
    }

    public Transform transform()
    {
        return getModel().transform();
    }

    public StyleComponent style()
    {
        return getModel().style();
    }

    @Override
    public void render(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {
    }

    public List<GuiElement> getChildrens()
    {
        return childrens;
    }
}