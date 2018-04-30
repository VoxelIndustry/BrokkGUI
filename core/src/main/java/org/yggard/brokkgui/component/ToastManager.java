package org.yggard.brokkgui.component;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
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
    private final BaseProperty<EAlignment> toastAlignmentProperty;

    private final BaseProperty<Float>    toastExitXProperty;
    private final BaseProperty<Float>    toastExitYProperty;
    private final BaseProperty<GuiToast> current;

    private Queue<GuiToast> toastQueue;

    public ToastManager(BrokkGuiScreen screen)
    {
        this.screen = screen;

        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");
        this.toastAlignmentProperty = new BaseProperty<>(EAlignment.MIDDLE_CENTER, "toastAlignmentProperty");

        this.toastExitXProperty = new BaseProperty<>(0f, "toastExitXProperty");
        this.toastExitYProperty = new BaseProperty<>(-100f, "toastExitYProperty");

        this.toastQueue = new ArrayDeque<>();
        this.current = new BaseProperty<>(null, "currentToastProperty");
    }

    public void addToast(GuiToast toast)
    {
        toast.getCurrentTimeProperty().addListener((obs, oldValue, newValue) ->
        {
            if (!(newValue >= toast.getLifeTime()) || current.getValue() != toast)
                return;
            toast.getxPosProperty().unbind();
            toast.getyPosProperty().unbind();
            toast.getCurrentTimeProperty().unbind();

            PopupHandler.getInstance().removePopup(toast);
            current.setValue(null);

            if (!toastQueue.isEmpty())
            {
                current.setValue(toastQueue.poll());
                PopupHandler.getInstance().addPopup(current.getValue());
            }
        });

        toast.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(xPosProperty, getToastAlignmentProperty(), toast.getWidthProperty(),
                        getToastExitXProperty(), current);
            }

            @Override
            public Float computeValue()
            {
                float offset = (current.getValue() == toast ? 0 : getToastExitXProperty().getValue());

                if (getToastAlignmentProperty().getValue().isLeft())
                    return xPosProperty.getValue() - toast.getWidth() + offset;
                else if (getToastAlignmentProperty().getValue().isRight())
                    return xPosProperty.getValue() + offset;
                else
                    return xPosProperty.getValue() - toast.getWidth() / 2 + offset;
            }
        });
        toast.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(yPosProperty, getToastAlignmentProperty(), toast.getHeightProperty(),
                        getToastExitYProperty(), current);
            }

            @Override
            public Float computeValue()
            {
                float offset = (current.getValue() == toast ? 0 : getToastExitYProperty().getValue());

                if (getToastAlignmentProperty().getValue().isUp())
                    return yPosProperty.getValue() - toast.getHeight() + offset;
                else if (getToastAlignmentProperty().getValue().isDown())
                    return yPosProperty.getValue() + offset;
                else
                    return yPosProperty.getValue() - toast.getHeight() / 2 + offset;
            }
        });

        if (current != null)
            this.toastQueue.add(toast);
        else
        {
            this.current.setValue(toast);
            PopupHandler.getInstance().addPopup(current.getValue());
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
        this.getxPosProperty().setValue(posX);
    }

    public float getPosY()
    {
        return this.getyPosProperty().getValue();
    }

    public void setPosY(float posY)
    {
        this.getyPosProperty().setValue(posY);
    }
}
