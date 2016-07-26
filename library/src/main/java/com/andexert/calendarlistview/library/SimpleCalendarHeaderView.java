package com.andexert.calendarlistview.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created on 7/27/2016.
 */
public class SimpleCalendarHeaderView extends View {

    public static final String VIEW_PARAMS_WEEK_START = "week_start";

    protected static int MONTH_DAY_LABEL_TEXT_SIZE;
    protected static int MONTH_HEADER_SIZE;

    protected int mWeekStart = 1;
    protected int mNumDays = 7;

    private Calendar mCalendar;
    private Calendar mDayLabelCalendar;

    private String mDayOfWeekTypeface;

    protected int mWidth;
    protected int mPadding = 0;

    protected Paint mMonthDayLabelPaint;

    protected int mDayTextColor;

    private TypedArray typedArray;

    private DateFormatSymbols mDateFormatSymbols = new DateFormatSymbols();

    public SimpleCalendarHeaderView(Context context) {
        this(context, null);
    }

    public SimpleCalendarHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleCalendarHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleCalendarHeaderView);
        setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));

        mCalendar = Calendar.getInstance();
        mDayLabelCalendar = Calendar.getInstance();

        Resources resources = context.getResources();

        mWeekStart = mCalendar.getFirstDayOfWeek();

        mDayOfWeekTypeface = resources.getString(R.string.sans_serif);

        mDayTextColor = typedArray.getColor(R.styleable.SimpleCalendarHeaderView_headerColorDayName, resources.getColor(R.color.normal_day));

        MONTH_DAY_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.SimpleCalendarHeaderView_headerTextSizeDayName, resources.getDimensionPixelSize(R.dimen.text_size_day_name));
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.SimpleCalendarHeaderView_headerHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));

        mMonthDayLabelPaint = new Paint();
        mMonthDayLabelPaint.setAntiAlias(true);
        mMonthDayLabelPaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
        mMonthDayLabelPaint.setColor(mDayTextColor);
        mMonthDayLabelPaint.setTypeface(Typeface.create(mDayOfWeekTypeface, Typeface.NORMAL));
        mMonthDayLabelPaint.setStyle(Paint.Style.FILL);
        mMonthDayLabelPaint.setTextAlign(Paint.Align.CENTER);
        mMonthDayLabelPaint.setFakeBoldText(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMonthDayLabels(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MONTH_HEADER_SIZE);
    }

    public void setHeaderParams(HashMap<String, Integer> params) {
        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            mWeekStart = params.get(VIEW_PARAMS_WEEK_START);
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }
    }

    private void drawMonthDayLabels(Canvas canvas) {
        int y = (MONTH_HEADER_SIZE / 2) + (MONTH_DAY_LABEL_TEXT_SIZE / 2);
        int dayWidthHalf = (mWidth - mPadding * 2) / (mNumDays * 2);

        for (int i = 0; i < mNumDays; i++) {
            int calendarDay = (i + mWeekStart) % mNumDays;
            int x = (2 * i + 1) * dayWidthHalf + mPadding;
            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            canvas.drawText(mDateFormatSymbols.getShortWeekdays()[mDayLabelCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(Locale.getDefault()), x, y, mMonthDayLabelPaint);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
    }

}
