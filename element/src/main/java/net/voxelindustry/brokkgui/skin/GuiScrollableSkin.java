package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.Binding;
import net.voxelindustry.brokkgui.behavior.GuiScrollableBehavior;
import net.voxelindustry.brokkgui.control.GuiScrollableBase;
import net.voxelindustry.brokkgui.policy.GuiScrollbarPolicy;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollableSkin<C extends GuiScrollableBase, B extends GuiScrollableBehavior<C>> extends
        GuiBehaviorSkinBase<C, B>
{
    private final Rectangle gripX, gripY;

    public GuiScrollableSkin(C model, B behavior)
    {
        super(model, behavior);

        gripX = new Rectangle();
        gripX.transform().xPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        getModel().getScrollXProperty(),
                        getModel().getTrueWidthProperty(),
                        transform().widthProperty(),
                        gripX.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().width() >= getModel().getTrueWidth())
                    return getModel().getLeftPos();
                else
                {
                    float area = getModel().getTrueWidth() - getModel().width();
                    float ratio = getModel().getScrollX() / area;
                    float size = getModel().width() - gripX.width();

                    return getModel().getLeftPos() - size * ratio;
                }
            }
        });
        gripX.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        transform().heightProperty(),
                        gripX.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getBottomPos() - gripX.height();
            }
        });
        gripX.transform().heightProperty().bindProperty(getModel().getGripXHeightProperty());
        gripX.transform().widthProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().widthProperty(),
                        getModel().getTrueWidthProperty(),
                        getModel().getGripXWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getGripXWidth() != 0)
                    return getModel().getGripXWidth();

                if (getModel().width() >= getModel().getTrueWidth())
                    return getModel().width();
                return Math.min(10, getModel().width() / getModel().getTrueWidth() * getModel().width());
            }
        });

        gripY = new Rectangle();
        gripY.transform().xPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        transform().widthProperty(),
                        gripY.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getRightPos() - gripY.width();
            }
        });
        gripY.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        getModel().getScrollYProperty(),
                        getModel().getTrueHeightProperty(),
                        transform().heightProperty(),
                        gripY.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().height() >= getModel().getTrueHeight())
                    return transform().yPos()
                            + transform().yTranslate();
                else
                {
                    float area = getModel().getTrueHeight() - getModel().height();
                    float ratio = getModel().getScrollY() / area;
                    float size = getModel().height() - gripY.height();

                    return getModel().getTopPos() - size * ratio;
                }
            }
        });
        gripY.transform().widthProperty().bindProperty(getModel().getGripYWidthProperty());
        gripY.transform().heightProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().heightProperty(),
                        getModel().getTrueHeightProperty(),
                        getModel().getGripYHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getGripYHeight() != 0)
                    return getModel().getGripYHeight();

                if (getModel().height() >= getModel().getTrueHeight())
                    return getModel().height();
                return Math.min(10, getModel().height() / getModel().getTrueHeight() * getModel().height());
            }
        });

        getModel().addChild(gripX);
        getModel().addChild(gripY);

        gripX.get(StyleComponent.class).styleClass().add("grip-x");
        gripY.get(StyleComponent.class).styleClass().add("grip-y");

        gripX.visibleProperty().bindProperty(new Binding<Boolean>()
        {
            {
                super.bind(getModel().getScrollXPolicyProperty(),
                        transform().widthProperty(),
                        getModel().getTrueWidthProperty());
            }

            @Override
            public Boolean computeValue()
            {
                if (getModel().getScrollXPolicy() == GuiScrollbarPolicy.ALWAYS
                        || getModel().getScrollXPolicy() == GuiScrollbarPolicy.NEEDED)
                {
                    if (getModel().width() >= getModel().getTrueWidth())
                        return getModel().getScrollXPolicy() == GuiScrollbarPolicy.ALWAYS;
                    else
                        return true;
                }
                return false;
            }
        });

        gripY.visibleProperty().bindProperty(new Binding<Boolean>()
        {
            {
                super.bind(getModel().getScrollYPolicyProperty(),
                        transform().heightProperty(),
                        getModel().getTrueHeightProperty());
            }

            @Override
            public Boolean computeValue()
            {
                if (getModel().getScrollYPolicy() == GuiScrollbarPolicy.ALWAYS
                        || getModel().getScrollYPolicy() == GuiScrollbarPolicy.NEEDED)
                {
                    if (getModel().height() >= getModel().getTrueHeight())
                        return getModel().getScrollYPolicy() == GuiScrollbarPolicy.ALWAYS;
                    else
                        return true;
                }
                return false;
            }
        });
    }
}
