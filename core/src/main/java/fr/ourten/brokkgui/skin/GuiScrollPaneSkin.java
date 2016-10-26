package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.behavior.GuiScrollPaneBehavior;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.panel.ScrollPane;
import fr.ourten.brokkgui.policy.EScrollbarPolicy;
import fr.ourten.brokkgui.shape.Rectangle;
import fr.ourten.teabeans.binding.BaseBinding;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollPaneSkin extends GuiBehaviorSkinBase<ScrollPane, GuiScrollPaneBehavior>
{
    private final Rectangle gripX, gripY;

    public GuiScrollPaneSkin(final ScrollPane model, final GuiScrollPaneBehavior behavior)
    {
        super(model, behavior);

        this.gripX = new Rectangle();

        this.gripX.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollPaneSkin.this.getModel().getxPosProperty(),
                        GuiScrollPaneSkin.this.getModel().getxTranslateProperty(),
                        GuiScrollPaneSkin.this.getModel().getScrollXProperty(),
                        GuiScrollPaneSkin.this.getModel().getTrueWidthProperty(),
                        GuiScrollPaneSkin.this.getModel().getWidthProperty(),
                        GuiScrollPaneSkin.this.gripX.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollPaneSkin.this.getModel().getWidth() >= GuiScrollPaneSkin.this.getModel().getTrueWidth())
                    return GuiScrollPaneSkin.this.getModel().getxPos()
                            + GuiScrollPaneSkin.this.getModel().getxTranslate();
                else
                {
                    final float area = GuiScrollPaneSkin.this.getModel().getTrueWidth()
                            - GuiScrollPaneSkin.this.getModel().getWidth();
                    final float ratio = GuiScrollPaneSkin.this.getModel().getScrollX() / area;
                    final float size = GuiScrollPaneSkin.this.getModel().getWidth()
                            - GuiScrollPaneSkin.this.gripX.getWidth();
                    return GuiScrollPaneSkin.this.getModel().getxPos()
                            + GuiScrollPaneSkin.this.getModel().getxTranslate() + size * ratio;
                }
            }
        });
        this.gripX.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollPaneSkin.this.getModel().getyPosProperty(),
                        GuiScrollPaneSkin.this.getModel().getyTranslateProperty(),
                        GuiScrollPaneSkin.this.getModel().getHeightProperty(),
                        GuiScrollPaneSkin.this.gripX.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                return GuiScrollPaneSkin.this.getModel().getyPos() + GuiScrollPaneSkin.this.getModel().getyTranslate()
                        + GuiScrollPaneSkin.this.getModel().getHeight() - GuiScrollPaneSkin.this.gripX.getHeight();
            }
        });
        this.gripX.setHeight(5);
        this.gripX.getWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollPaneSkin.this.getModel().getWidthProperty(),
                        GuiScrollPaneSkin.this.getModel().getTrueWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollPaneSkin.this.getModel().getWidth() >= GuiScrollPaneSkin.this.getModel().getTrueWidth())
                    return GuiScrollPaneSkin.this.getModel().getWidth();
                return Math.min(10,
                        GuiScrollPaneSkin.this.getModel().getWidth() / GuiScrollPaneSkin.this.getModel().getTrueWidth()
                                * GuiScrollPaneSkin.this.getModel().getWidth());
            }
        });
        this.gripX.getColorProperty().bind(this.getModel().getScrollbarColorProperty());
        this.gripX.setzLevel(10);

        this.gripY = new Rectangle();
        this.gripY.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollPaneSkin.this.getModel().getxPosProperty(),
                        GuiScrollPaneSkin.this.getModel().getxTranslateProperty(),
                        GuiScrollPaneSkin.this.getModel().getWidthProperty(),
                        GuiScrollPaneSkin.this.gripY.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                return GuiScrollPaneSkin.this.getModel().getxPos() + GuiScrollPaneSkin.this.getModel().getxTranslate()
                        + GuiScrollPaneSkin.this.getModel().getWidth() - GuiScrollPaneSkin.this.gripY.getHeight();
            }
        });
        this.gripY.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollPaneSkin.this.getModel().getyPosProperty(),
                        GuiScrollPaneSkin.this.getModel().getyTranslateProperty(),
                        GuiScrollPaneSkin.this.getModel().getScrollYProperty(),
                        GuiScrollPaneSkin.this.getModel().getTrueHeightProperty(),
                        GuiScrollPaneSkin.this.getModel().getHeightProperty(),
                        GuiScrollPaneSkin.this.gripY.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollPaneSkin.this.getModel().getHeight() >= GuiScrollPaneSkin.this.getModel().getTrueHeight())
                    return GuiScrollPaneSkin.this.getModel().getyPos()
                            + GuiScrollPaneSkin.this.getModel().getyTranslate();
                else
                {
                    final float area = GuiScrollPaneSkin.this.getModel().getTrueHeight()
                            - GuiScrollPaneSkin.this.getModel().getHeight();
                    final float ratio = GuiScrollPaneSkin.this.getModel().getScrollY() / area;
                    final float size = GuiScrollPaneSkin.this.getModel().getHeight()
                            - GuiScrollPaneSkin.this.gripY.getHeight();
                    return GuiScrollPaneSkin.this.getModel().getyPos()
                            + GuiScrollPaneSkin.this.getModel().getyTranslate() + size * ratio;
                }
            }
        });
        this.gripY.setWidth(20);
        this.gripY.getHeightProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollPaneSkin.this.getModel().getHeightProperty(),
                        GuiScrollPaneSkin.this.getModel().getTrueHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollPaneSkin.this.getModel().getHeight() >= GuiScrollPaneSkin.this.getModel().getTrueHeight())
                    return GuiScrollPaneSkin.this.getModel().getHeight();
                return Math.min(10,
                        GuiScrollPaneSkin.this.getModel().getHeight()
                                / GuiScrollPaneSkin.this.getModel().getTrueHeight()
                                * GuiScrollPaneSkin.this.getModel().getHeight());
            }
        });

        this.gripY.getColorProperty().bind(this.getModel().getScrollbarColorProperty());
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        if (!this.getModel().getChildrens().isEmpty())
            this.getModel().getChildrens().get(0).renderNode(renderer, pass, mouseX, mouseY);
        if (pass == EGuiRenderPass.SPECIAL)
        {
            if (this.getModel().getScrollXPolicy() == EScrollbarPolicy.ALWAYS
                    || this.getModel().getScrollXPolicy() == EScrollbarPolicy.NEEDED)
                if (this.getModel().getWidth() >= this.getModel().getTrueWidth())
                {
                    if (this.getModel().getScrollXPolicy() == EScrollbarPolicy.ALWAYS)
                        this.gripX.renderNode(renderer, EGuiRenderPass.MAIN, mouseX, mouseY);
                }
                else
                    this.gripX.renderNode(renderer, EGuiRenderPass.MAIN, mouseX, mouseY);
            if (this.getModel().getScrollYPolicy() == EScrollbarPolicy.ALWAYS
                    || this.getModel().getScrollYPolicy() == EScrollbarPolicy.NEEDED)
                if (this.getModel().getHeight() >= this.getModel().getTrueHeight())
                {
                    if (this.getModel().getScrollYPolicy() == EScrollbarPolicy.ALWAYS)
                        this.gripY.renderNode(renderer, EGuiRenderPass.MAIN, mouseX, mouseY);
                }
                else
                    this.gripY.renderNode(renderer, EGuiRenderPass.MAIN, mouseX, mouseY);
        }
    }
}