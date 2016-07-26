package com.andexert.calendarlistview.library;

import java.util.Calendar;

/**
 * Created on 7/27/2016.
 */
public class EventData {
    public int mYear;
    public int mMonth;
    public int mDay;
    public boolean hasEvent;

    public EventData(boolean hasEvent, int mDay, int mMonth, int mYear) {
        this.hasEvent = hasEvent;
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mYear = mYear;
    }

    public EventData(boolean hasEvent, Calendar calendar) {
        this.hasEvent = hasEvent;
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
        this.mMonth = calendar.get(Calendar.MONTH);
        this.mYear = calendar.get(Calendar.YEAR);
    }

    public boolean isHasEvent(int day, int month, int year) {
        if (this.mYear == year && this.mMonth == month && this.mDay == day) {
            return hasEvent;
        } else {
            return false;
        }
    }
}
