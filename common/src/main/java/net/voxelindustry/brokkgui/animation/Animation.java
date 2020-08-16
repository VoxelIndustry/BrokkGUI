package net.voxelindustry.brokkgui.animation;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.event.AnimationFinishEvent;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.concurrent.TimeUnit;

public abstract class Animation implements ITicking, IEventEmitter
{
    private long duration;
    private long startTime;
    private long elapsedTime;

    private int               maxCycles;
    private boolean           reverse;
    private Property<Integer> currentCycleProperty;

    private Property<AnimationStatus> statusProperty;
    private Property<Float>           progressProperty;
    private Property<Float>           totalProgressProperty;

    private Animation                          parent;
    private EventDispatcher                    eventDispatcher;
    private EventHandler<AnimationFinishEvent> onFinishEvent;

    public Animation(long duration, TimeUnit unit)
    {
        this.duration = unit.toMillis(duration);
        maxCycles = 1;

        statusProperty = new Property<>(AnimationStatus.NOT_STARTED);
        currentCycleProperty = new Property<>(0);
        progressProperty = new Property<>(0F);
        totalProgressProperty = new Property<>(0F);
    }

    @Override
    public void tick(long currentMillis)
    {
        elapsedTime = currentMillis - startTime;

        if (elapsedTime >= duration)
        {
            startTime = currentMillis;
            elapsedTime = 0;

            getCurrentCycleProperty().setValue(getCurrentCycle() + 1);
            if (getCurrentCycle() == maxCycles)
            {
                complete();
                return;
            }
        }

        float currentProgress = reverse && getCurrentCycle() % 2 != 0 ? 1 - ((float) elapsedTime / duration) :
                ((float) elapsedTime / duration);
        getProgressProperty().setValue(currentProgress);
        getTotalProgressProperty().setValue((float) (getCurrentCycle() / getMaxCycles()) + (currentProgress / getMaxCycles()));
    }

    public void setCurrentProgress(float progress)
    {
        getCurrentCycleProperty().setValue((int) Math.floor(progress));

        getProgressProperty().setValue(progress - getCurrentCycle());
        getTotalProgressProperty().setValue(progress / getMaxCycles());
    }

    public void start()
    {
        if (isRunning())
            return;
        BrokkGuiPlatform.getInstance().getTickSender().addTicking(this);
        restart();
    }

    public void reset()
    {
        complete();
    }

    public void restart()
    {
        if (!isRunning())
            BrokkGuiPlatform.getInstance().getTickSender().addTicking(this);

        getStatusProperty().setValue(AnimationStatus.RUNNING);
        getCurrentCycleProperty().setValue(0);
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }

    public void pause()
    {
        getStatusProperty().setValue(AnimationStatus.PAUSED);
        BrokkGuiPlatform.getInstance().getTickSender().removeTicking(this);
    }

    public void resume()
    {
        if (getStatus() != AnimationStatus.PAUSED)
            return;

        BrokkGuiPlatform.getInstance().getTickSender().addTicking(this);
        startTime = System.currentTimeMillis() - elapsedTime;
        getStatusProperty().setValue(AnimationStatus.RUNNING);
    }

    public void complete()
    {
        getStatusProperty().setValue(AnimationStatus.COMPLETED);
        BrokkGuiPlatform.getInstance().getTickSender().removeTicking(this);

        startTime = 0;

        if (onFinishEvent != null)
            onFinishEvent.handle(new AnimationFinishEvent(this));
    }

    public Property<AnimationStatus> getStatusProperty()
    {
        return statusProperty;
    }

    public Property<Integer> getCurrentCycleProperty()
    {
        return currentCycleProperty;
    }

    public Property<Float> getProgressProperty()
    {
        return progressProperty;
    }

    public Property<Float> getTotalProgressProperty()
    {
        return totalProgressProperty;
    }

    /**
     * @return a float between 0-1 representing the current progress of the current Cycle of this Animation
     */
    public float getProgress()
    {
        return getProgressProperty().getValue();
    }

    /**
     * @return the total progress of this Animation
     */
    public float getTotalProgress()
    {
        return getTotalProgressProperty().getValue();
    }

    public AnimationStatus getStatus()
    {
        return getStatusProperty().getValue();
    }

    public boolean isRunning()
    {
        return getStatus() == AnimationStatus.RUNNING;
    }

    public boolean isCompleted()
    {
        return getStatus() == AnimationStatus.COMPLETED;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public int getMaxCycles()
    {
        return maxCycles;
    }

    public void setMaxCycles(int maxCycles)
    {
        this.maxCycles = maxCycles;
    }

    public int getCurrentCycle()
    {
        return getCurrentCycleProperty().getValue();
    }

    public boolean isReverse()
    {
        return reverse;
    }

    public void setReverse(boolean reverse)
    {
        this.reverse = reverse;
    }

    public Animation getParent()
    {
        return parent;
    }

    public void setParent(Animation parent)
    {
        this.parent = parent;
    }

    public void setOnFinishEvent(EventHandler<AnimationFinishEvent> onFinishEvent)
    {
        if (eventDispatcher == null)
            eventDispatcher = new EventDispatcher();
        this.onFinishEvent = onFinishEvent;
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        return eventDispatcher;
    }
}
