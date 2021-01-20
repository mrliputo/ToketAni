package com.tecmanic.toketani.modelclass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyCalendarData {

    private int startday;
    private int currentmonth;
    private int currentyear;
    private String stringDayofWeek;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E");
    private Calendar calendar;
    // constructor

    public MyCalendarData(int start) {

        this.calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.add(Calendar.DATE, start);
        setThis();

    }

    private void setThis() {
        this.startday = calendar.get(Calendar.DAY_OF_MONTH);
        this.currentmonth = calendar.get(Calendar.MONTH);
        this.currentyear = calendar.get(Calendar.YEAR);
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        this.stringDayofWeek = dateFormat.format(calendar.getTime());

    }

    public void getNextWeekDay(int nxt) {

        calendar.add(Calendar.DATE, nxt);
        setThis();

    }

    public String getWeekDay() {
        return this.stringDayofWeek;
    }

    public int getYear() {
        return this.currentyear;
    }

    public int getMonth() {
        return this.currentmonth;
    }

    public int getDay() {
        return this.startday;
    }
}
