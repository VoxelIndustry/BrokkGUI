package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
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

    private final Property<Float>         xPosProperty;
    private final Property<Float>         yPosProperty;
    private final Property<Float>         relativeXPosProperty;
    private final Property<Float>         relativeYPosProperty;
    private final Property<RectAlignment> toastAlignmentProperty;

    private final Property<Float>      toastExitXProperty;
    private final Property<Float>      toastExitYProperty;
    private final Property<GuiElement> current;

    private GuiToast                      toastHolder;
    private Queue<Pair<GuiElement, Long>> toastQueue;

    public ToastManager(IGuiWindow screen)
    {
        this.screen = screen;

        xPosProperty = new Property<>(0F);
        yPosProperty = new Property<>(0F);
        relativeXPosProperty = new Property<>(-1F);
        relativeYPosProperty = new Property<>(-1F);
        toastAlignmentProperty = new Property<>(RectAlignment.MIDDLE_CENTER);

        toastExitXProperty = new Property<>(0F);
        toastExitYProperty = new Property<>(-100F);

        toastQueue = new ArrayDeque<>();
        current = new Property<>(null);
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

        toastHolder.transform().xPosProperty().bindProperty(new Binding<Float>()
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
        toastHolder.transform().yPosProperty().bindProperty(new Binding<Float>()
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

    public Property<RectAlignment> getToastAlignmentProperty()
    {
        return toastAlignmentProperty;
    }

    public Property<Float> getToastExitXProperty()
    {
        return toastExitXProperty;
    }

    public Property<Float> getToastExitYProperty()
    {
        return toastExitYProperty;
    }

    public Property<Float> getxPosProperty()
    {
        return xPosProperty;
    }

    public Property<Float> getyPosProperty()
    {
        return yPosProperty;
    }

    public Property<Float> getRelativeXPosProperty()
    {
        return relativeXPosProperty;
    }

    public Property<Float> getRelativeYPosProperty()
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
            getxPosProperty().bindProperty(getRelativeXPosProperty().combine(
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
            getyPosProperty().bindProperty(getRelativeYPosProperty().combine(
                    screen.getScreenHeightProperty(),
                    (relativeY, screenHeight) -> screenHeight * relativeY));
        }
        getRelativeYPosProperty().setValue(relativeYPos);
    }
}
