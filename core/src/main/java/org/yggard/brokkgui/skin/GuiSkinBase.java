package org.yggard.brokkgui.skin;

import java.util.List;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiControl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;

import com.google.common.collect.Lists;

import fr.ourten.teabeans.value.BaseProperty;

public class GuiSkinBase<T extends GuiControl> implements IGuiSkin
{
    private final List<GuiNode>         childrens;
    private final T                     model;

    private final BaseProperty<Integer> borderThinProperty;
    private final BaseProperty<Color>   borderColorProperty;

    public GuiSkinBase(final T model)
    {
        if (model == null)
            throw new IllegalArgumentException("Cannot pass a null model");
        this.model = model;

        this.borderThinProperty = new BaseProperty<>(0, "borderThinProperty");
        this.borderColorProperty = new BaseProperty<>(Color.BLACK, "borderColorProperty");

        this.childrens = Lists.newArrayList();
    }

    public T getModel()
    {
        return this.model;
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.SPECIAL && this.getBorderThin() > 0 && this.getBorderColor() != Color.ALPHA)
            renderer.getHelper().drawColoredEmptyRect(renderer, this.model.getxPos() + this.model.getxTranslate(),
                    this.model.getyPos() + this.model.getyTranslate(), this.model.getWidth(), this.model.getHeight(),
                    this.model.getzLevel(), this.getBorderColor(), this.getBorderThin());
    }

    public BaseProperty<Integer> getBorderThinProperty()
    {
        return this.borderThinProperty;
    }

    public BaseProperty<Color> getBorderColorProperty()
    {
        return this.borderColorProperty;
    }

    public int getBorderThin()
    {
        return this.getBorderThinProperty().getValue();
    }

    public void setBorderThin(final int thinness)
    {
        this.getBorderThinProperty().setValue(thinness);
    }

    public Color getBorderColor()
    {
        return this.getBorderColorProperty().getValue();
    }

    public void setBorderColor(final Color color)
    {
        this.getBorderColorProperty().setValue(color);
    }

    public List<GuiNode> getChildrens()
    {
        return this.childrens;
    }
}