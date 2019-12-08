package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.data.TextLayoutHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.layout.LayoutGroup;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class TextRenderer extends GuiComponent implements RenderComponent
{
    private Text      text;
    private Transform transform;

    protected BaseProperty<Color>   colorProperty;
    protected BaseProperty<Color>   shadowColorProperty;
    protected BaseProperty<Boolean> shadowProperty;

    private Binding<Float> xPosBinding;
    private Binding<Float> yPosBinding;

    private Binding<Float> widthBinding;
    private float          height;

    private Binding<String> ellipsedTextBinding;

    private LayoutGroup layoutGroup;

    public TextRenderer()
    {
    }

    @Override
    public void attach(GuiElement element)
    {
        if (this.element() != null)
        {
            this.ellipsedTextBinding.unbindAll();
            this.xPosBinding.unbindAll();
            this.yPosBinding.unbindAll();
            this.widthBinding.unbindAll();
        }

        super.attach(element);

        if (!element.has(Text.class))
            throw new GuiComponentException(
                    "TextRenderer must be added to a GuiElement already containing the component Text!");

        this.text = element.get(Text.class);
        this.transform = element.transform();

        Icon icon = element.get(Icon.class);

        layoutGroup = new LayoutGroup(text, icon, icon.iconSide().opposite().toLayout());

        // Bind alignment
        icon.iconSideProperty().addListener(obs -> layoutGroup.firstAlignment(icon.iconSide().opposite().toLayout()));

        if (icon != null)
        {
            this.ellipsedTextBinding = TextLayoutHelper.createEllipsedTextBinding(element, text, icon);
            this.xPosBinding = TextLayoutHelper.createXPosBinding(element, text, icon, ellipsedTextBinding);
            this.yPosBinding = TextLayoutHelper.createYPosBinding(element, text, icon);
        }
        else
        {
            this.ellipsedTextBinding = TextLayoutHelper.createEllipsedTextBinding(element, text);
            this.xPosBinding = TextLayoutHelper.createXPosBinding(element, text, ellipsedTextBinding);
            this.yPosBinding = TextLayoutHelper.createYPosBinding(element, text);
        }

        widthBinding = BaseExpression.transform(ellipsedTextBinding,
                BrokkGuiPlatform.instance().guiHelper()::getStringWidth);
        height = BrokkGuiPlatform.instance().guiHelper().getStringHeight();
    }

    private void refreshLayout()
    {
        if (layoutGroup.isLayoutDirty())
        {
            layoutGroup.refreshLayout();
        }
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.BACKGROUND)
        {
            refreshLayout();
        }

        if (pass == RenderPass.MAIN)
        {
            renderer.getHelper().drawString(text.textTrimmedForRender(), transform.leftPos() + layoutGroup.firstPosX(),
                    transform.topPos() + layoutGroup.firstPosY(), transform.zLevel(),
                    color(), this.shadow() ? this.shadowColor() : Color.ALPHA);
        }
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public BaseProperty<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = new BaseProperty<>(Color.WHITE, "colorProperty");
        return colorProperty;
    }

    public BaseProperty<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = new BaseProperty<>(Color.BLACK, "shadowColorProperty");
        return shadowColorProperty;
    }

    public BaseProperty<Boolean> shadowProperty()
    {
        if (shadowProperty == null)
            shadowProperty = new BaseProperty<>(Boolean.FALSE, "shadowProperty");
        return shadowProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public Color color()
    {
        return colorProperty().getValue();
    }

    public void color(Color color)
    {
        colorProperty().setValue(color);
    }

    public Color shadowColor()
    {
        return shadowColorProperty().getValue();
    }

    public void shadowColor(Color color)
    {
        shadowColorProperty().setValue(color);
    }

    public boolean shadow()
    {
        return shadowProperty().getValue();
    }

    public void shadow(boolean shadow)
    {
        shadowProperty().setValue(shadow);
    }
}
