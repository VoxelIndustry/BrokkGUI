package org.yggard.brokkgui.element;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiTogglableButtonBehavior;
import org.yggard.brokkgui.control.GuiToggleButton;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.skin.GuiRadioButtonSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 18 nov. 2016
 */
public class GuiRadioButton extends GuiToggleButton
{
    private final BaseProperty<Float>       labelPaddingProperty;
    private final BaseProperty<EHAlignment> labelAlignmentProperty;

    public GuiRadioButton(final String label)
    {
        super("radio-button", label);

        this.labelAlignmentProperty = new BaseProperty<>(EHAlignment.RIGHT, "labelAlignmentProperty");
        this.labelPaddingProperty = new BaseProperty<>(0f, "labelPaddingProperty");
        this.setStyle("-text-color: #000000 78%");
        this.setLabelPadding(2);

        this.bindSizeToText();
    }

    public GuiRadioButton()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiRadioButtonSkin(this, new GuiTogglableButtonBehavior<>(this));
    }

    public BaseProperty<Float> getLabelPaddingProperty()
    {
        return this.labelPaddingProperty;
    }

    public BaseProperty<EHAlignment> getLabelAlignmentProperty()
    {
        return this.labelAlignmentProperty;
    }

    public float getLabelPadding()
    {
        return this.getLabelPaddingProperty().getValue();
    }

    public void setLabelPadding(final float labelPadding)
    {
        this.getLabelPaddingProperty().setValue(labelPadding);
    }

    public EHAlignment getLabelAlignment()
    {
        return this.getLabelAlignmentProperty().getValue();
    }

    public void setLabelAlignment(final EHAlignment alignment)
    {
        this.getLabelAlignmentProperty().setValue(alignment);
    }

    @Override
    protected void bindSizeToText()
    {
        if (this.getLabelPaddingProperty() != null)
        {
            this.getWidthProperty().bind(BaseExpression.triCombine(this.getTextProperty(),
                    this.getLabelPaddingProperty(), this.getHeightProperty(),
                    (text, padding,
                            height) -> (BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(this.getText())
                                    + this.getHeight() + this.getLabelPadding())));
            this.getHeightProperty().bind(BaseExpression.transform(this.getTextProperty(),
                    text -> BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()));
        }
    }
}