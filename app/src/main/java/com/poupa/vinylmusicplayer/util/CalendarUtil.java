package com.poupa.vinylmusicplayer.util;

import androidx.annotation.IntRange;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtil {
    private static final long MS_PER_MINUTE = 60 * 1000;
    private static final long MS_PER_DAY = 24 * 60 * MS_PER_MINUTE;

    private final Calendar calendar;

    public CalendarUtil() {
        this.calendar = Calendar.getInstance();
    }

    public long getElapsedToday() {
        // Time elapsed so far today
        return (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * MS_PER_MINUTE
                + calendar.get(Calendar.SECOND) * 1000
                + calendar.get(Calendar.MILLISECOND);
    }

    public long getElapsedDays(@IntRange(from=1) int numDays) {
        long elapsed = getElapsedToday();
        elapsed += (numDays - 1) * MS_PER_DAY;

        return elapsed;
    }

    public long getElapsedWeek() {
        // Today + days passed this week
        long elapsed = getElapsedToday();

        final int passedWeekdays = calendar.get(Calendar.DAY_OF_WEEK) - 1 - calendar.getFirstDayOfWeek();
        if (passedWeekdays > 0) {
            elapsed += passedWeekdays * MS_PER_DAY;
        }

        return elapsed;
    }

    public long getElapsedWeeks(@IntRange(from=1) int numWeeks) {
        // Today + this week
        long elapsed = getElapsedWeek();

        // The other full weeks
        elapsed += (numWeeks - 1) * 7 * MS_PER_DAY;

        return elapsed;
    }

    public long getElapsedMonth() {
        // Today + rest of this month
        return getElapsedToday() +
                ((calendar.get(Calendar.DAY_OF_MONTH) - 1) * MS_PER_DAY);
    }

    public long getElapsedMonths(@IntRange(from=1) int numMonths) {
        // Today + rest of this month
        long elapsed = getElapsedMonth();

        // Previous numMonths months
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        for (int i = 0; i < numMonths; i++) {
            month--;

            if (month < Calendar.JANUARY) {
                month = Calendar.DECEMBER;
                year--;
            }

            elapsed += getDaysInMonth(year, month) * MS_PER_DAY;
        }

        return elapsed;
    }

    public long getElapsedYear() {
        // Today + rest of this month + previous months until January
        long elapsed = getElapsedMonth();

        int month = calendar.get(Calendar.MONTH) - 1;
        int year = calendar.get(Calendar.YEAR);
        while (month > Calendar.JANUARY) {
            elapsed += getDaysInMonth(year, month) * MS_PER_DAY;

            month--;
        }

        return elapsed;
    }

    public long getElapsedYears(@IntRange(from=1) int numYears) {
        // This year
        long elapsed = getElapsedYear();

        // The other full years
        elapsed += (numYears - 1) * 365 * MS_PER_DAY;

        return elapsed;
    }

    private int getDaysInMonth(int year, int month) {
        final Calendar monthCal = new GregorianCalendar(year, month, 1);
        return monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
