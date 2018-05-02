package org.yggard.brokkgui.component;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import org.apache.commons.lang3.tuple.Pair;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.element.GuiToast;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.internal.PopupHandler;

import java.util.ArrayDeque;
import java.util.Queue;

public class ToastManager
{
    private final BrokkGuiScreen screen;

    private final BaseProperty<Float>      xPosProperty;
    private final BaseProperty<Float>      yPosProperty;
    private final BaseProperty<Float>      relativeXPosProperty;
    private final BaseProperty<Float>      relativeYPosProperty;
    private final BaseProperty<EAlignment> toastAlignmentProperty;

    private final BaseProperty<Float>   toastExitXProperty;
    private final BaseProperty<Float>   toastExitYProperty;
    private final BaseProperty<GuiNode> current;

    private GuiToast                   toastHolder;
    private Queue<Pair<GuiNode, Long>> toastQueue;

    public ToastManager(BrokkGuiScreen screen)
    {
        this.screen = screen;

        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");
        this.relativeXPosProperty = new BaseProperty<>(-1f, "relativeXPosProperty");
        this.relativeYPosProperty = new BaseProperty<>(-1f, "relativeYPosProperty");
        this.toastAlignmentProperty = new BaseProperty<>(EAlignment.MIDDLE_CENTER, "toastAlignmentProperty");

        this.toastExitXProperty = new BaseProperty<>(0f, "toastExitXProperty");
        this.toastExitYProperty = new BaseProperty<>(-100f, "toastExitYProperty");

        this.toastQueue = new ArrayDeque<>();
        this.current = new BaseProperty<>(null, "currentToastProperty");
        this.toastHolder = new GuiToast(null, 0L);

        toastHolder.getCurrentTimeProperty().addListener((obs, oldValue, newValue) ->
        {
            if (!(newValue > toastHolder.getLifeTime()))
                return;

            PopupHandler.getInstance().removePopup(toastHolder);
            current.setValue(null);
            toastHolder.setContent(null);

            if (!toastQueue.isEmpty())
            {
                Pair<GuiNode, Long> next = toastQueue.poll();
                current.setValue(next.getKey());
                toastHolder.setContent(current.getValue());
                toastHolder.setCurrentTime(0);
                toastHolder.setLifeTime(next.getValue());
                PopupHandler.getInstance().addPopup(toastHolder);
            }
        });

        toastHolder.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(xPosProperty, getToastAlignmentProperty(), toastHolder.getWidthProperty(),
                        getToastExitXProperty(), current);
            }

            @Override
            public Float computeValue()
            {
                float offset = (current.getValue() == toastHolder.getContent() ? 0 : getToastExitXProperty().getValue());

                if (getToastAlignmentProperty().getValue().isLeft())
                    return xPosProperty.getValue() - toastHolder.getWidth() + offset;
                else if (getToastAlignmentProperty().getValue().isRight())
                    return xPosProperty.getValue() + offset;
                else
                    return xPosProperty.getValue() - toastHolder.getWidth() / 2 + offset;
            }
        });
        toastHolder.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(yPosProperty, getToastAlignmentProperty(), toastHolder.getHeightProperty(),
                        getToastExitYProperty(), current);
            }

            @Override
            public Float computeValue()
            {
                float offset = (current.getValue() == toastHolder.getContent()? 0 : getToastExitYProperty().getValue());

                if (getToastAlignmentProperty().getValue().isUp())
                    return yPosProperty.getValue() - toastHolder.getHeight() + offset;
                else if (getToastAlignmentProperty().getValue().isDown())
                    return yPosProperty.getValue() + offset;
                else
                    return yPosProperty.getValue() - toastHolder.getHeight() / 2 + offset;
            }
        });
    }

    public void addToast(GuiNode toastContent, Long lifeTime)
    {
        if (current.isPresent())
            toastQueue.add(Pair.of(toastContent, lifeTime));
        else
        {
            current.setValue(toastContent);
            toastHolder.setContent(current.getValue());
            toastHolder.setCurrentTime(0);
            toastHolder.setLifeTime(lifeTime);
            PopupHandler.getInstance().addPopup(toastHolder);
        }
    }

    public BaseProperty<EAlignment> getToastAlignmentProperty()
    {
        return toastAlignmentProperty;
    }

    public BaseProperty<Float> getToastExitXProperty()
    {
        return toastExitXProperty;
    }

    public BaseProperty<Float> getToastExitYProperty()
    {
        return toastExitYProperty;
    }

    public BaseProperty<Float> getxPosProperty()
    {
        return xPosProperty;
    }

    public BaseProperty<Float> getyPosProperty()
    {
        return yPosProperty;
    }

    public BaseProperty<Float> getRelativeXPosProperty()
    {
        return relativeXPosProperty;
    }

    public BaseProperty<Float> getRelativeYPosProperty()
    {
        return relativeYPosProperty;
    }

    public EAlignment getToastAlignment()
    {
        return this.getToastAlignmentProperty().getValue();
    }

    public void setToastAlignment(EAlignment toastAlignment)
    {
        this.getToastAlignmentProperty().setValue(toastAlignment);
    }

    public float getToastExitX()
    {
        return this.getToastExitXProperty().getValue();
    }

    public void setToastExitX(float toastExitX)
    {
        this.getToastExitXProperty().setValue(toastExitX);
    }

    public float getToastExitY()
    {
        return this.getToastExitYProperty().getValue();
    }

    public void setToastExitY(float toastExitY)
    {
        this.getToastExitYProperty().setValue(toastExitY);
    }

    public float getPosX()
    {
        return this.getxPosProperty().getValue();
    }

    public void setPosX(float posX)
    {
        if (this.getxPosProperty().isBound())
            this.getxPosProperty().unbind();
        this.getxPosProperty().setValue(posX);
    }

    public float getPosY()
    {
        return this.getyPosProperty().getValue();
    }

    public void setPosY(float posY)
    {
        if (this.getyPosProperty().isBound())
            this.getyPosProperty().unbind();
        this.getyPosProperty().setValue(posY);
    }

    public float getRelativeXPos()
    {
        return this.getRelativeXPosProperty().getValue();
    }

    public void setRelativeXPos(float relativeXPos)
    {
        if (relativeXPos == -1)
            this.getxPosProperty().unbind();
        else if (!this.getxPosProperty().isBound() && this.screen != null)
        {
            this.getxPosProperty().bind(BaseExpression.biCombine(this.getRelativeXPosProperty(),
                    screen.getScreenWidthProperty(),
                    (relativeX, screenWidth) -> screenWidth * relativeX));
        }
        this.getRelativeXPosProperty().setValue(relativeXPos);
    }

    public float getRelativeYPos()
    {
        return this.getRelativeYPosProperty().getValue();
    }

    public void setRelativeYPos(float relativeYPos)
    {
        if (relativeYPos == -1)
            this.getyPosProperty().unbind();
        else if (!this.getyPosProperty().isBound() && this.screen != null)
        {
            this.getyPosProperty().bind(BaseExpression.biCombine(this.getRelativeYPosProperty(),
                    screen.getScreenHeightProperty(),
                    (relativeY, screenHeight) -> screenHeight * relativeY));
        }
        this.getRelativeYPosProperty().setValue(relativeYPos);
    }
}
