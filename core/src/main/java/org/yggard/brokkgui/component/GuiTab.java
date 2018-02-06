package org.yggard.brokkgui.component;

import org.yggard.brokkgui.data.ESide;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.panel.GuiTabPane;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTab
{
    private final BaseProperty<String>     textProperty;
    private final BaseProperty<GuiNode>    contentProperty;
    private final BaseProperty<GuiTabPane> tabPaneProperty;
    private final BaseProperty<Boolean>    selectedProperty;

    public GuiTab(final String text, final GuiNode content)
    {
        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.contentProperty = new BaseProperty<>(content, "contentProperty");
        this.tabPaneProperty = new BaseProperty<>(null, "tabPaneProperty");
        this.selectedProperty = new BaseProperty<>(false, "selectedProperty");
    }

    public GuiTab(final String text)
    {
        this(text, null);
    }

    public GuiTab()
    {
        this("");
    }

    public BaseProperty<String> getTextProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<GuiNode> getContentProperty()
    {
        return this.contentProperty;
    }

    public BaseProperty<GuiTabPane> getTabPaneProperty()
    {
        return this.tabPaneProperty;
    }

    public BaseProperty<Boolean> getSelectedProperty()
    {
        return this.selectedProperty;
    }

    public String getText()
    {
        return this.getTextProperty().getValue();
    }

    public void setText(final String text)
    {
        this.getTextProperty().setValue(text);
    }

    public GuiNode getContent()
    {
        return this.getContentProperty().getValue();
    }

    public void setContent(final GuiNode content)
    {
        if (content != null && this.getTabPane() != null)
            this.setupContent(this.getTabPane(), content);
        if (this.getContent() != null)
            this.disposeContent();
        this.getContentProperty().setValue(content);
    }

    public GuiTabPane getTabPane()
    {
        return this.getTabPaneProperty().getValue();
    }

    public void setTabPane(final GuiTabPane tabPane)
    {
        if (tabPane != null && this.getContent() != null)
            this.setupContent(tabPane, this.getContent());
        else if (this.getContent() != null && tabPane == null)
            this.disposeContent();
        this.getTabPaneProperty().setValue(tabPane);
    }

    private void setupContent(final GuiTabPane pane, final GuiNode content)
    {
        final BaseBinding<Float> xPadding = new BaseExpression<>(() ->
        {
            if (pane.getTabSide() == ESide.LEFT)
                return pane.getWidth() / 10 + 1;
            return 1f;
        }, pane.getSideProperty(), pane.getWidthProperty());

        final BaseBinding<Float> yPadding = new BaseExpression<>(() ->
        {
            if (pane.getTabSide() == ESide.UP)
                return pane.getHeight() / 10 + 1;
            return 1f;
        }, pane.getSideProperty(), pane.getHeightProperty());

        this.getContent().setFather(pane);
        RelativeBindingHelper.bindToPos(content, pane, xPadding, yPadding);

        content.getWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(pane.getWidthProperty(), pane.getSideProperty(), pane.getTabHeightRatioProperty());
            }

            @Override
            public Float computeValue()
            {
                if (pane.getTabSide().equals(ESide.LEFT) || pane.getTabSide().equals(ESide.RIGHT))
                    return pane.getWidth() - pane.getWidth() * pane.getTabHeightRatio();
                return pane.getWidth();
            }
        });
        content.getHeightProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(pane.getHeightProperty(), pane.getSideProperty(), pane.getTabHeightRatioProperty());
            }

            @Override
            public Float computeValue()
            {
                if (pane.getTabSide().equals(ESide.UP) || pane.getTabSide().equals(ESide.DOWN))
                    return pane.getHeight() - pane.getHeight() * pane.getTabHeightRatio();
                return pane.getHeight();
            }
        });
    }

    private void disposeContent()
    {
        this.getContent().getxPosProperty().unbind();
        this.getContent().getyPosProperty().unbind();
        this.getContent().getWidthProperty().unbind();
        this.getContent().getHeightProperty().unbind();
        this.getContent().setFather(null);
    }

    public boolean isSelected()
    {
        return this.getSelectedProperty().getValue();
    }

    public void setSelected(final boolean selected)
    {
        this.getSelectedProperty().setValue(selected);
    }

    public void renderChild(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (this.getContent() != null)
            this.getContent().renderNode(renderer, pass, mouseX, mouseY);
    }
}