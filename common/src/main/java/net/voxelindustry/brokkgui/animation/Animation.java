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
    public static final int INFINITE_CYCLES = -1;

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

            currentCycleProperty().setValue(currentCycle() + 1);
            if (currentCycle() == maxCycles)
            {
                complete();
                return;
            }
        }

        float currentProgress = reverse && currentCycle() % 2 != 0 ? 1 - ((float) elapsedTime / duration) :
                ((float) elapsedTime / duration);
        progressProperty().setValue(currentProgress);
        totalProgressProperty().setValue((float) (currentCycle() / maxCycles()) + (currentProgress / maxCycles()));
    }

    public void setCurrentProgress(float progress)
    {
        currentCycleProperty().setValue((int) Math.floor(progress));

        progressProperty().setValue(progress - currentCycle());
        totalProgressProperty().setValue(progress / maxCycles());
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

        statusProperty().setValue(AnimationStatus.RUNNING);
        currentCycleProperty().setValue(0);
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }

    public void pause()
    {
        statusProperty().setValue(AnimationStatus.PAUSED);
        BrokkGuiPlatform.getInstance().getTickSender().removeTicking(this);
    }

    public void resume()
    {
        if (status() != AnimationStatus.PAUSED)
            return;

        BrokkGuiPlatform.getInstance().getTickSender().addTicking(this);
        startTime = System.currentTimeMillis() - elapsedTime;
        statusProperty().setValue(AnimationStatus.RUNNING);
    }

    public void complete()
    {
        statusProperty().setValue(AnimationStatus.COMPLETED);
        BrokkGuiPlatform.getInstance().getTickSender().removeTicking(this);

        startTime = 0;

        if (onFinishEvent != null)
            onFinishEvent.handle(new AnimationFinishEvent(this));
    }

    public Property<AnimationStatus> statusProperty()
    {
        return statusProperty;
    }

    public Property<Integer> currentCycleProperty()
    {
        return currentCycleProperty;
    }

    public Property<Float> progressProperty()
    {
        return progressProperty;
    }

    public Property<Float> totalProgressProperty()
    {
        return totalProgressProperty;
    }

    /**
     * @return a float between 0-1 representing the current progress of the current Cycle of this Animation
     */
    public float progress()
    {
        return progressProperty().getValue();
    }

    /**
     * @return the total progress of this Animation
     */
    public float totalProgress()
    {
        return totalProgressProperty().getValue();
    }

    public AnimationStatus status()
    {
        return statusProperty().getValue();
    }

    public boolean isRunning()
    {
        return status() == AnimationStatus.RUNNING;
    }

    public boolean isCompleted()
    {
        return status() == AnimationStatus.COMPLETED;
    }

    public long duration()
    {
        return duration;
    }

    public void duration(long duration)
    {
        this.duration = duration;
    }

    public long startTime()
    {
        return startTime;
    }

    public int maxCycles()
    {
        return maxCycles;
    }

    public void maxCycles(int maxCycles)
    {
        this.maxCycles = maxCycles;
    }

    public int currentCycle()
    {
        return currentCycleProperty().getValue();
    }

    public boolean isReverse()
    {
        return reverse;
    }

    public void reverse(boolean reverse)
    {
        this.reverse = reverse;
    }

    public Animation parent()
    {
        return parent;
    }

    public void parent(Animation parent)
    {
        this.parent = parent;
    }

    public void onFinishEvent(EventHandler<AnimationFinishEvent> onFinishEvent)
    {
        if (eventDispatcher == null)
            eventDispatcher = new EventDispatcher(this);
        this.onFinishEvent = onFinishEvent;
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        return eventDispatcher;
    }
}
