package net.voxelindustry.brokkgui.animation;

import fr.ourten.teabeans.value.BaseProperty;
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

    private int                   maxCycles;
    private boolean               reverse;
    private BaseProperty<Integer> currentCycleProperty;

    private BaseProperty<AnimationStatus> statusProperty;
    private BaseProperty<Float>           progressProperty;
    private BaseProperty<Float>           totalProgressProperty;

    private Animation                          parent;
    private EventDispatcher                    eventDispatcher;
    private EventHandler<AnimationFinishEvent> onFinishEvent;

    public Animation(long duration, TimeUnit unit)
    {
        this.duration = unit.toMillis(duration);
        this.maxCycles = 1;

        this.statusProperty = new BaseProperty<>(AnimationStatus.NOT_STARTED, "statusProperty");
        this.currentCycleProperty = new BaseProperty<>(0, "currentCycleProperty");
        this.progressProperty = new BaseProperty<>(0f, "progressProperty");
        this.totalProgressProperty = new BaseProperty<>(0f, "totalProgressProperty");
    }

    @Override
    public void tick(long currentMillis)
    {
        this.elapsedTime = currentMillis - this.startTime;

        if (elapsedTime >= duration)
        {
            this.startTime = currentMillis;
            this.elapsedTime = 0;

            this.getCurrentCycleProperty().setValue(this.getCurrentCycle() + 1);
            if (this.getCurrentCycle() == this.maxCycles)
            {
                this.complete();
                return;
            }
        }

        float currentProgress = reverse && getCurrentCycle() % 2 != 0 ? 1 - ((float) elapsedTime / duration) :
                ((float) elapsedTime / duration);
        this.getProgressProperty().setValue(currentProgress);
        this.getTotalProgressProperty().setValue((float) (this.getCurrentCycle() / this.getMaxCycles()) + (currentProgress / (float) getMaxCycles()));
    }

    public void setCurrentProgress(float progress)
    {
        getCurrentCycleProperty().setValue((int) Math.floor(progress));

        getProgressProperty().setValue(progress - getCurrentCycle());
        getTotalProgressProperty().setValue(progress / getMaxCycles());
    }

    public void start()
    {
        if (this.isRunning())
            return;
        BrokkGuiPlatform.instance().tickSender().addTicking(this);
        this.restart();
    }

    public void reset()
    {
        this.complete();
    }

    public void restart()
    {
        if (!this.isRunning())
            BrokkGuiPlatform.instance().tickSender().addTicking(this);

        this.getStatusProperty().setValue(AnimationStatus.RUNNING);
        this.getCurrentCycleProperty().setValue(0);
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
    }

    public void pause()
    {
        this.getStatusProperty().setValue(AnimationStatus.PAUSED);
        BrokkGuiPlatform.instance().tickSender().removeTicking(this);
    }

    public void resume()
    {
        if (this.getStatus() != AnimationStatus.PAUSED)
            return;

        BrokkGuiPlatform.instance().tickSender().addTicking(this);
        this.startTime = System.currentTimeMillis() - this.elapsedTime;
        this.getStatusProperty().setValue(AnimationStatus.RUNNING);
    }

    public void complete()
    {
        this.getStatusProperty().setValue(AnimationStatus.COMPLETED);
        BrokkGuiPlatform.instance().tickSender().removeTicking(this);

        this.startTime = 0;

        if (this.onFinishEvent != null)
            this.onFinishEvent.handle(new AnimationFinishEvent(this));
    }

    public BaseProperty<AnimationStatus> getStatusProperty()
    {
        return statusProperty;
    }

    public BaseProperty<Integer> getCurrentCycleProperty()
    {
        return currentCycleProperty;
    }

    public BaseProperty<Float> getProgressProperty()
    {
        return progressProperty;
    }

    public BaseProperty<Float> getTotalProgressProperty()
    {
        return totalProgressProperty;
    }

    /**
     * @return a float between 0-1 representing the current progress of the current Cycle of this Animation
     */
    public float getProgress()
    {
        return this.getProgressProperty().getValue();
    }

    /**
     * @return the total progress of this Animation
     */
    public float getTotalProgress()
    {
        return this.getTotalProgressProperty().getValue();
    }

    public AnimationStatus getStatus()
    {
        return this.getStatusProperty().getValue();
    }

    public boolean isRunning()
    {
        return this.getStatus() == AnimationStatus.RUNNING;
    }

    public boolean isCompleted()
    {
        return this.getStatus() == AnimationStatus.COMPLETED;
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
        return this.getCurrentCycleProperty().getValue();
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
        if (this.eventDispatcher == null)
            this.eventDispatcher = new EventDispatcher();
        this.onFinishEvent = onFinishEvent;
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        return eventDispatcher;
    }
}
