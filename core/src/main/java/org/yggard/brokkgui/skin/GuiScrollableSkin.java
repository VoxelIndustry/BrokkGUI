package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import org.yggard.brokkgui.behavior.GuiScrollableBehavior;
import org.yggard.brokkgui.control.GuiScrollableBase;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.policy.EScrollbarPolicy;
import org.yggard.brokkgui.shape.Rectangle;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollableSkin<C extends GuiScrollableBase, B extends GuiScrollableBehavior<C>> extends
        GuiBehaviorSkinBase<C, B>
{
    private final Rectangle gripX, gripY;

    public GuiScrollableSkin(final C model, final B behavior)
    {
        super(model, behavior);

        this.gripX = new Rectangle();

        this.gripX.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollableSkin.this.getModel().getxPosProperty(),
                        GuiScrollableSkin.this.getModel().getxTranslateProperty(),
                        GuiScrollableSkin.this.getModel().getScrollXProperty(),
                        GuiScrollableSkin.this.getModel().getTrueWidthProperty(),
                        GuiScrollableSkin.this.getModel().getWidthProperty(),
                        GuiScrollableSkin.this.gripX.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollableSkin.this.getModel().getWidth() >= GuiScrollableSkin.this.getModel().getTrueWidth())
                    return GuiScrollableSkin.this.getModel().getxPos()
                            + GuiScrollableSkin.this.getModel().getxTranslate();
                else
                {
                    final float area = GuiScrollableSkin.this.getModel().getTrueWidth()
                            - GuiScrollableSkin.this.getModel().getWidth();
                    final float ratio = GuiScrollableSkin.this.getModel().getScrollX() / area;
                    final float size = GuiScrollableSkin.this.getModel().getWidth()
                            - GuiScrollableSkin.this.gripX.getWidth();
                    return GuiScrollableSkin.this.getModel().getxPos()
                            + GuiScrollableSkin.this.getModel().getxTranslate() + size * ratio;
                }
            }
        });
        this.gripX.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollableSkin.this.getModel().getyPosProperty(),
                        GuiScrollableSkin.this.getModel().getyTranslateProperty(),
                        GuiScrollableSkin.this.getModel().getHeightProperty(),
                        GuiScrollableSkin.this.gripX.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                return GuiScrollableSkin.this.getModel().getyPos() + GuiScrollableSkin.this.getModel().getyTranslate()
                        + GuiScrollableSkin.this.getModel().getHeight() - GuiScrollableSkin.this.gripX.getHeight();
            }
        });
        this.gripX.setHeight(5);
        this.gripX.getWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollableSkin.this.getModel().getWidthProperty(),
                        GuiScrollableSkin.this.getModel().getTrueWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollableSkin.this.getModel().getWidth() >= GuiScrollableSkin.this.getModel().getTrueWidth())
                    return GuiScrollableSkin.this.getModel().getWidth();
                return Math.min(10,
                        GuiScrollableSkin.this.getModel().getWidth() / GuiScrollableSkin.this.getModel().getTrueWidth()
                                * GuiScrollableSkin.this.getModel().getWidth());
            }
        });
        this.gripX.setzLevel(10);

        this.gripY = new Rectangle();
        this.gripY.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollableSkin.this.getModel().getxPosProperty(),
                        GuiScrollableSkin.this.getModel().getxTranslateProperty(),
                        GuiScrollableSkin.this.getModel().getWidthProperty(),
                        GuiScrollableSkin.this.gripY.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                return GuiScrollableSkin.this.getModel().getxPos() + GuiScrollableSkin.this.getModel().getxTranslate()
                        + GuiScrollableSkin.this.getModel().getWidth() - GuiScrollableSkin.this.gripY.getHeight();
            }
        });
        this.gripY.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollableSkin.this.getModel().getyPosProperty(),
                        GuiScrollableSkin.this.getModel().getyTranslateProperty(),
                        GuiScrollableSkin.this.getModel().getScrollYProperty(),
                        GuiScrollableSkin.this.getModel().getTrueHeightProperty(),
                        GuiScrollableSkin.this.getModel().getHeightProperty(),
                        GuiScrollableSkin.this.gripY.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollableSkin.this.getModel().getHeight() >= GuiScrollableSkin.this.getModel().getTrueHeight())
                    return GuiScrollableSkin.this.getModel().getyPos()
                            + GuiScrollableSkin.this.getModel().getyTranslate();
                else
                {
                    final float area = GuiScrollableSkin.this.getModel().getTrueHeight()
                            - GuiScrollableSkin.this.getModel().getHeight();
                    final float ratio = GuiScrollableSkin.this.getModel().getScrollY() / area;
                    final float size = GuiScrollableSkin.this.getModel().getHeight()
                            - GuiScrollableSkin.this.gripY.getHeight();
                    return GuiScrollableSkin.this.getModel().getyPos()
                            + GuiScrollableSkin.this.getModel().getyTranslate() + size * ratio;
                }
            }
        });
        this.gripY.setWidth(20);
        this.gripY.getHeightProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(GuiScrollableSkin.this.getModel().getHeightProperty(),
                        GuiScrollableSkin.this.getModel().getTrueHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (GuiScrollableSkin.this.getModel().getHeight() >= GuiScrollableSkin.this.getModel().getTrueHeight())
                    return GuiScrollableSkin.this.getModel().getHeight();
                return Math.min(10,
                        GuiScrollableSkin.this.getModel().getHeight()
                                / GuiScrollableSkin.this.getModel().getTrueHeight()
                                * GuiScrollableSkin.this.getModel().getHeight());
            }
        });

        this.getModel().getStyle().registerAlias("grip-x", this.gripX.getStyle());
        this.getModel().getStyle().registerAlias("grip-y", this.gripY.getStyle());
    }

    @Override
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        if (pass == RenderPass.SPECIAL)
        {
            if (this.getModel().getScrollXPolicy() == EScrollbarPolicy.ALWAYS
                    || this.getModel().getScrollXPolicy() == EScrollbarPolicy.NEEDED)
                if (this.getModel().getWidth() >= this.getModel().getTrueWidth())
                {
                    if (this.getModel().getScrollXPolicy() == EScrollbarPolicy.ALWAYS)
                        this.gripX.renderNode(renderer, RenderPass.MAIN, mouseX, mouseY);
                }
                else
                    this.gripX.renderNode(renderer, RenderPass.MAIN, mouseX, mouseY);
            if (this.getModel().getScrollYPolicy() == EScrollbarPolicy.ALWAYS
                    || this.getModel().getScrollYPolicy() == EScrollbarPolicy.NEEDED)
                if (this.getModel().getHeight() >= this.getModel().getTrueHeight())
                {
                    if (this.getModel().getScrollYPolicy() == EScrollbarPolicy.ALWAYS)
                        this.gripY.renderNode(renderer, RenderPass.MAIN, mouseX, mouseY);
                }
                else
                    this.gripY.renderNode(renderer, RenderPass.MAIN, mouseX, mouseY);
        }
    }
}