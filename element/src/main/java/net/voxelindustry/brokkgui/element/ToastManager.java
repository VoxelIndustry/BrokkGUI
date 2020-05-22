package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.window.IGuiWindow;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.Queue;

public class ToastManager
{
    private final IGuiWindow screen;

    private final BaseProperty<Float>         xPosProperty;
    private final BaseProperty<Float>         yPosProperty;
    private final BaseProperty<Float>         relativeXPosProperty;
    private final BaseProperty<Float>         relativeYPosProperty;
    private final BaseProperty<RectAlignment> toastAlignmentProperty;

    private final BaseProperty<Float>      toastExitXProperty;
    private final BaseProperty<Float>      toastExitYProperty;
    private final BaseProperty<GuiElement> current;

    private GuiToast                      toastHolder;
    private Queue<Pair<GuiElement, Long>> toastQueue;

    public ToastManager(IGuiWindow screen)
    {
        this.screen = screen;

        xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        yPosProperty = new BaseProperty<>(0f, "yPosProperty");
        relativeXPosProperty = new BaseProperty<>(-1f, "relativeXPosProperty");
        relativeYPosProperty = new BaseProperty<>(-1f, "relativeYPosProperty");
        toastAlignmentProperty = new BaseProperty<>(RectAlignment.MIDDLE_CENTER, "toastAlignmentProperty");

        toastExitXProperty = new BaseProperty<>(0f, "toastExitXProperty");
        toastExitYProperty = new BaseProperty<>(-100f, "toastExitYProperty");

        toastQueue = new ArrayDeque<>();
        current = new BaseProperty<>(null, "currentToastProperty");
        toastHolder = new GuiToast(null, 0L);

        toastHolder.getCurrentTimeProperty().addListener((obs, oldValue, newValue) ->
        {
            if (!(newValue > toastHolder.getLifeTime()))
                return;

            PopupHandler.getInstance(screen).removePopup(toastHolder);
            current.setValue(null);
            toastHolder.setContent(null);

            if (!toastQueue.isEmpty())
            {
                Pair<GuiElement, Long> next = toastQueue.poll();
                current.setValue(next.getKey());
                toastHolder.setContent(current.getValue());
                toastHolder.resetCurrentTime();
                toastHolder.setLifeTime(next.getValue());
                PopupHandler.getInstance(screen).addPopup(toastHolder);
            }
        });

        toastHolder.transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(xPosProperty, getToastAlignmentProperty(), toastHolder.transform().widthProperty(),
                        getToastExitXProperty(), current);
            }

            @Override
            public Float computeValue()
            {
                float offset = (current.getValue() == toastHolder.getContent() ? 0 :
                        getToastExitXProperty().getValue());

                if (getToastAlignmentProperty().getValue().isLeft())
                    return xPosProperty.getValue() - toastHolder.transform().width() + offset;
                else if (getToastAlignmentProperty().getValue().isRight())
                    return xPosProperty.getValue() + offset;
                else
                    return xPosProperty.getValue() - toastHolder.transform().width() / 2 + offset;
            }
        });
        toastHolder.transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(yPosProperty, getToastAlignmentProperty(), toastHolder.transform().heightProperty(),
                        getToastExitYProperty(), current);
            }

            @Override
            public Float computeValue()
            {
                float offset = (current.getValue() == toastHolder.getContent() ? 0 :
                        getToastExitYProperty().getValue());

                if (getToastAlignmentProperty().getValue().isUp())
                    return yPosProperty.getValue() - toastHolder.transform().height() + offset;
                else if (getToastAlignmentProperty().getValue().isDown())
                    return yPosProperty.getValue() + offset;
                else
                    return yPosProperty.getValue() - toastHolder.transform().height() / 2 + offset;
            }
        });
    }

    public void addToast(GuiElement toastContent, Long lifeTime)
    {
        if (current.isPresent())
            toastQueue.add(Pair.of(toastContent, lifeTime));
        else
        {
            current.setValue(toastContent);
            toastHolder.resetCurrentTime();
            toastHolder.setLifeTime(lifeTime);
            toastHolder.setContent(current.getValue());
            PopupHandler.getInstance(screen).addPopup(toastHolder);
        }
    }

    public BaseProperty<RectAlignment> getToastAlignmentProperty()
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

    public RectAlignment getToastAlignment()
    {
        return getToastAlignmentProperty().getValue();
    }

    public void setToastAlignment(RectAlignment toastAlignment)
    {
        getToastAlignmentProperty().setValue(toastAlignment);
    }

    public float getToastExitX()
    {
        return getToastExitXProperty().getValue();
    }

    public void setToastExitX(float toastExitX)
    {
        getToastExitXProperty().setValue(toastExitX);
    }

    public float getToastExitY()
    {
        return getToastExitYProperty().getValue();
    }

    public void setToastExitY(float toastExitY)
    {
        getToastExitYProperty().setValue(toastExitY);
    }

    public float getPosX()
    {
        return getxPosProperty().getValue();
    }

    public void setPosX(float posX)
    {
        if (getxPosProperty().isBound())
            getxPosProperty().unbind();
        getxPosProperty().setValue(posX);
    }

    public float getPosY()
    {
        return getyPosProperty().getValue();
    }

    public void setPosY(float posY)
    {
        if (getyPosProperty().isBound())
            getyPosProperty().unbind();
        getyPosProperty().setValue(posY);
    }

    public float getRelativeXPos()
    {
        return getRelativeXPosProperty().getValue();
    }

    public void setRelativeXPos(float relativeXPos)
    {
        if (relativeXPos == -1)
            getxPosProperty().unbind();
        else if (!getxPosProperty().isBound() && screen != null)
        {
            getxPosProperty().bind(BaseExpression.biCombine(getRelativeXPosProperty(),
                    screen.getScreenWidthProperty(),
                    (relativeX, screenWidth) -> screenWidth * relativeX));
        }
        getRelativeXPosProperty().setValue(relativeXPos);
    }

    public float getRelativeYPos()
    {
        return getRelativeYPosProperty().getValue();
    }

    public void setRelativeYPos(float relativeYPos)
    {
        if (relativeYPos == -1)
            getyPosProperty().unbind();
        else if (!getyPosProperty().isBound() && screen != null)
        {
            getyPosProperty().bind(BaseExpression.biCombine(getRelativeYPosProperty(),
                    screen.getScreenHeightProperty(),
                    (relativeY, screenHeight) -> screenHeight * relativeY));
        }
        getRelativeYPosProperty().setValue(relativeYPos);
    }
}
