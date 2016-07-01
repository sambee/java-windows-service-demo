package com.hhf.schedule;

/**
 * Created by HHF on 2016/7/1.
 */
import java.util.Calendar;
import java.util.Date;

public class DailyIterator
        implements ScheduleIterator
{
//    private final int hourOfDay;
//    private final int minute;
//    private final int second;
    private final Calendar calendar = Calendar.getInstance();

    public DailyIterator(int hourOfDay, int minute, int second)
    {
        this(hourOfDay, minute, second, new Date());
    }

    public DailyIterator(int hourOfDay, int minute, int second, Date date)
    {
//        this.hourOfDay = hourOfDay;
//        this.minute = minute;
//        this.second = second;
        this.calendar.setTime(date);
        this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.calendar.set(Calendar.MINUTE, minute);
        this.calendar.set(Calendar.SECOND, second);
        this.calendar.set(Calendar.MILLISECOND, 0);
        if (!this.calendar.getTime().before(date)) {
            this.calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
    }

    public DailyIterator(){
        this.calendar.setTime(new Date());
    }

    public final Date next()
    {
        this.calendar.add(Calendar.MINUTE, 1);
        return this.calendar.getTime();
    }

}
