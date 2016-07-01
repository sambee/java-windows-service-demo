package com.hhf.schedule.test;

import com.hhf.schedule.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import static com.hhf.schedule.Log.log;

/**
 * Created by Administrator on 2016/7/1.
 */
public class TestTimeReminder {

    private static Scheduler scheduler = new Scheduler();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     *  stop service with argument
     *
     * @param args
     */
    public static void stopService(String[] args) {
        log("",sdf.format(new Date()) + "Service stopped");
    }

    /**
     * start service with arguments
     *
     * @param args
     */
    public static void startService(String[] args) {
        Log.log("",sdf.format(new Date()) +"service start");
        ScheduleIterator iterator = new DailyIterator();

        scheduler.schedule(new SchedulerTask() {
            @Override
            public void run() {
                log("",sdf.format(new Date()) +"service running");
            }
        }, iterator);
    }

    public static void main(String[] args) {
        new TestTimeReminder().startService(new String[]{""});
    }

}
