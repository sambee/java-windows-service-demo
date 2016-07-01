package com.hhf.schedule;

/**
 * Created by Administrator on 2016/7/1.
 */

import java.util.TimerTask;

public abstract class SchedulerTask
        implements Runnable
{
    static final int VIRGIN = 0;
    static final int SCHEDULED = 1;
    static final int CANCELLED = 2;
    private Object lock = new Object();

    private int state = 0;

    private TimerTask timerTask;

    public abstract void run();

    public final boolean cancel()
    {
        synchronized (this.lock) {
            if (this.timerTask != null) {
                this.timerTask.cancel();
            }
            boolean result = this.state == 1;
            this.state = 2;
            return result;
        }
    }

    public final long scheduledExecutionTime()
    {
        synchronized (this.lock) {
            if (this.timerTask == null) {
                return 0L;
            }
            return this.timerTask.scheduledExecutionTime();
        }
    }

    public final Object getLock()
    {
        return this.lock;
    }

    public final void setLock(Object lock)
    {
        this.lock = lock;
    }

    public final int getState()
    {
        return this.state;
    }

    public final void setState(int state)
    {
        this.state = state;
    }

    public final TimerTask getTimerTask()
    {
        return this.timerTask;
    }

    public final void setTimerTask(TimerTask timerTask)
    {
        this.timerTask = timerTask;
    }
}