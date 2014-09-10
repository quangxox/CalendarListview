package com.andexert.calendarlistview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SimpleMonthAdapter extends BaseAdapter implements SimpleMonthView.OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final AttributeSet attrs;
	private final Context mContext;
	private final DatePickerController mController;
    private final Calendar calendar;
    private final SelectedDays<CalendarDay> selectedDays;

	public SimpleMonthAdapter(Context context, DatePickerController datePickerController, AttributeSet attrs) {
        this.attrs = attrs;
        selectedDays = new SelectedDays<CalendarDay>();
		mContext = context;
		mController = datePickerController;
        calendar = Calendar.getInstance();
		init();
	}

	public int getCount() {
        return ((mController.getMaxYear() - mController.getMinYear()) + 1) * MONTHS_IN_YEAR;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		SimpleMonthView v;
        HashMap<String, Integer> drawingParams = null;
		if (convertView != null) {
			v = (SimpleMonthView) convertView;
            drawingParams = (HashMap<String, Integer>) v.getTag();
        } else {
			v = new SimpleMonthView(mContext, attrs, 0);
			v.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			v.setClickable(true);
			v.setOnDayClickListener(this);
		}
        if (drawingParams == null) {
            drawingParams = new HashMap<String, Integer>();
        }
        drawingParams.clear();
        final int month = (calendar.get(Calendar.MONTH) + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        final int year = position / MONTHS_IN_YEAR + mController.getMinYear() + ((calendar.get(Calendar.MONTH) + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);

        int selectedFirstDay = -1;
        int selectedLastDay = -1;
        int selectedFirstMonth = -1;
        int selectedLastMonth = -1;
        int selectedFirstYear = -1;
        int selectedLastYear = -1;

        if (selectedDays.getFirst() != null)
        {
            selectedFirstDay = selectedDays.getFirst().day;
            selectedFirstMonth = selectedDays.getFirst().month;
            selectedFirstYear = selectedDays.getFirst().year;
        }

        if (selectedDays.getLast() != null)
        {
            selectedLastDay = selectedDays.getLast().day;
            selectedLastMonth = selectedDays.getLast().month;
            selectedLastYear = selectedDays.getLast().year;
        }

		v.reuse();

        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, selectedFirstYear);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_YEAR, selectedLastYear);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, selectedFirstMonth);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_MONTH, selectedLastMonth);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, selectedFirstDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DAY, selectedLastDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
		v.setMonthParams(drawingParams);
		v.invalidate();

		return v;
	}

	protected void init() {
        setSelectedDay(new CalendarDay(System.currentTimeMillis()));
	}

	public void onDayClick(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
		if (calendarDay != null) {
			onDayTapped(calendarDay);
        }
	}

	protected void onDayTapped(CalendarDay calendarDay) {
		mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
		setSelectedDay(calendarDay);
	}

	public void setSelectedDay(CalendarDay calendarDay) {
        if (selectedDays.getFirst() != null && selectedDays.getLast() == null)
        {
            selectedDays.setLast(calendarDay);

            if (selectedDays.getFirst().month < calendarDay.month)
            {
                for (int i = 0; i < selectedDays.getFirst().month - calendarDay.month - 1; ++i)
                    mController.onDayOfMonthSelected(selectedDays.getFirst().year, selectedDays.getFirst().month + i, selectedDays.getFirst().day);
            }
        }
        else if (selectedDays.getLast() != null)
        {
            selectedDays.setFirst(calendarDay);
            selectedDays.setLast(null);
        }
        else
            selectedDays.setFirst(calendarDay);

		notifyDataSetChanged();
	}

	public static class CalendarDay implements Serializable
    {
        private static final long serialVersionUID = -5456695978688356202L;
        private Calendar calendar;

		int day;
		int month;
		int year;

		public CalendarDay() {
			setTime(System.currentTimeMillis());
		}

		public CalendarDay(int year, int month, int day) {
			setDay(year, month, day);
		}

		public CalendarDay(long timeInMillis) {
			setTime(timeInMillis);
		}

		public CalendarDay(Calendar calendar) {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		private void setTime(long timeInMillis) {
			if (calendar == null) {
				calendar = Calendar.getInstance();
            }
			calendar.setTimeInMillis(timeInMillis);
			month = this.calendar.get(Calendar.MONTH);
			year = this.calendar.get(Calendar.YEAR);
			day = this.calendar.get(Calendar.DAY_OF_MONTH);
		}

		public void set(CalendarDay calendarDay) {
		    year = calendarDay.year;
			month = calendarDay.month;
			day = calendarDay.day;
		}

		public void setDay(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
		}

        public Date getDate()
        {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.set(year, month, day);
            return calendar.getTime();
        }
	}

    public SelectedDays<CalendarDay> getSelectedDays()
    {
        return selectedDays;
    }

    public static class SelectedDays<K> implements Serializable
    {
        private static final long serialVersionUID = 3942549765282708376L;
        private K first;
        private K last;

        public K getFirst()
        {
            return first;
        }

        public void setFirst(K first)
        {
            this.first = first;
        }

        public K getLast()
        {
            return last;
        }

        public void setLast(K last)
        {
            this.last = last;
        }
    }
}