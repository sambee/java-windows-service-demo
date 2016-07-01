package com.hhf.schedule;

/**
 * Created by Administrator on 2016/7/1.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.hhf.schedule.Log.log;


public class Scheduler
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    class SchedulerTimerTask
            extends TimerTask
    {
        private SchedulerTask schedulerTask;
        private ScheduleIterator iterator;

        public SchedulerTimerTask(SchedulerTask schedulerTask, ScheduleIterator iterator)
        {
            this.schedulerTask = schedulerTask;
            this.iterator = iterator;
        }

        public void run() { this.schedulerTask.run();
            Scheduler.this.reschedule(this.schedulerTask, this.iterator);
        }
    }

    private final Timer timer = new Timer();

    public final void cancel()
    {
        this.timer.cancel();
    }

    public final void schedule(SchedulerTask schedulerTask, ScheduleIterator iterator)
    {
        Date time = iterator.next();
        if (time == null) {
            schedulerTask.cancel();
        } else {
            synchronized (schedulerTask.getLock()) {
                if (schedulerTask.getState() != 0) {
                    throw new IllegalStateException("Task already scheduled or cancelled");
                }
                schedulerTask.setState(1);
                schedulerTask.setTimerTask(new SchedulerTimerTask(schedulerTask, iterator));
                log("","the first time execute at :" + sdf.format(time));
                this.timer.schedule(schedulerTask.getTimerTask(), time);
            }
        }
    }

    private void reschedule(SchedulerTask schedulerTask, ScheduleIterator iterator)
    {
        Date time = iterator.next();
        if (time == null) {
            schedulerTask.cancel();
        } else {
            synchronized (schedulerTask.getLock()) {
                if (schedulerTask.getState() != 2) {
                    schedulerTask.setTimerTask(new SchedulerTimerTask(schedulerTask, iterator));
                    log("","next time execute at :" + sdf.format(time));
                    this.timer.schedule(schedulerTask.getTimerTask(), time);
                }
            }
        }
    }
}