package com.ivtolkachev.fbfriendslistapp.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ivtolkachev.fbfriendslistapp.R;

public class GridCellAdapter extends BaseAdapter implements OnClickListener {
	private static final String TAG = GridCellAdapter.class.getSimpleName();
	private final Context mContext;

	private final List<String> mList;
	private static final int DAY_OFFSET = 1;
	private static final String[] WEEKDAYS = new String[] { "Sun", "Mon",
			"Tue", "Wed", "Thu", "Fri", "Sat" };
	private static final String[] MONTHS = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private static final int[] DAYS_OF_MONTH = { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };
	private final int mMonth;
	private final int mYear;
	private int mDaysInMonth;
	private int mCurrentDayOfMonth;
	private int mCurrentWeekDay;
	private Button mGridCellButton;
	private Calendar mCalendar;
	private OnCalendarChangeListener mCalendarChangeListener;

	public GridCellAdapter(Context context, int textViewResourceId, Calendar calendar) {
		super();
		this.mContext = context;
		this.mList = new ArrayList<String>();
		this.mCalendar = calendar;
		this.mMonth = calendar.get(Calendar.MONTH) + 1;
		this.mYear = calendar.get(Calendar.YEAR);

		setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
		setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
		Log.d(TAG, "New Calendar:= " + calendar.getTime().toString());
		Log.d(TAG, "CurrentDayOfWeek :" + getCurrentWeekDay());
		Log.d(TAG, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

		// Print Month
		printMonth(mMonth, mYear);
		Log.d(TAG, "GridCellAdapter");
	}

	private String getMonthAsString(int i) {
		return MONTHS[i];
	}

	private String getWeekDayAsString(int i) {
		return WEEKDAYS[i];
	}

	private int getNumberOfDaysOfMonth(int i) {
		return DAYS_OF_MONTH[i];
	}

	public String getItem(int position) {
		Log.d(TAG, "getItem");
		return mList.get(position);
	}

	@Override
	public int getCount() {
		Log.d(TAG, "getCount");
		return mList.size();
	}

	private void printMonth(int mm, int yy) {
		Log.d(TAG, "begin printMonth");
		int trailingSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;

		int currentMonth = mm - 1;
		mDaysInMonth = getNumberOfDaysOfMonth(currentMonth);

		GregorianCalendar calendar = new GregorianCalendar(yy, currentMonth, 1);

		if (currentMonth == 11) {
			prevMonth = currentMonth - 1;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 0;
			prevYear = yy;
			nextYear = yy + 1;
		} else if (currentMonth == 0) {
			prevMonth = 11;
			prevYear = yy - 1;
			nextYear = yy;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 1;
		} else {
			prevMonth = currentMonth - 1;
			nextMonth = currentMonth + 1;
			nextYear = yy;
			prevYear = yy;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
		}

		// getDay() returns 0 for Sunday.
		int currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		trailingSpaces = currentWeekDay;

		if (calendar.isLeapYear(calendar.get(Calendar.YEAR)) && mm == 2) {
			++mDaysInMonth;
		}
		if (calendar.isLeapYear(calendar.get(Calendar.YEAR)) && mm == 3) {
			++daysInPrevMonth;
		}
		for (int i = 0; i < trailingSpaces; i++) {
			mList.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i)
					+ "-GREY-" + getMonthAsString(prevMonth) + "-" + prevYear);
		}

		// Current Month Days
		String color = "";
		for (int i = 1; i <= mDaysInMonth; i++) {
			color = "WHITE";
			if (i == getCurrentDayOfMonth()) {
				color = "BLUE";
			}
			mList.add(String.valueOf(i) + "-" + color + "-"
					+ getMonthAsString(currentMonth) + "-" + yy);
		}

		// Leading Month days
		for (int i = 0; i < mList.size() % 7; i++) {
			mList.add(String.valueOf(i + 1) + "-GREY" + "-"
					+ getMonthAsString(nextMonth) + "-" + nextYear);
		}
		Log.d(TAG, "end printMonth");
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG, "getItemId");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.calendar_day_gridcell, parent,
					false);
		}

		mGridCellButton = (Button) row.findViewById(R.id.calendar_day_gridcell);
		mGridCellButton.setOnClickListener(this);

		String[] day_color = mList.get(position).split("-");
		String theday = day_color[0];
		String themonth = day_color[2];
		String theyear = day_color[3];

		mGridCellButton.setText(theday);
		mGridCellButton.setTag(theday + "/" + themonth + "/" + theyear);

		if (day_color[1].equals("GREY")) {
			mGridCellButton.setTextColor(Color.GRAY);
		}
		if (day_color[1].equals("WHITE")) {
			mGridCellButton.setTextColor(Color.WHITE);
		}
		if (day_color[1].equals("BLUE")) {
			mGridCellButton.setTextColor(mContext.getResources().getColor(R.color.static_text_color));
		}
		if (day_color[1].equals("RED")) {
			mGridCellButton.setTextColor(Color.RED);
		}
		return row;
	}

	@Override
	public void onClick(View view) {
		String date_month_year = (String) view.getTag();
		String[] date = date_month_year.split("/");
		mCurrentDayOfMonth = Integer.parseInt(date[0]);
		mCalendar.set(Calendar.DAY_OF_MONTH, mCurrentDayOfMonth);
		mCalendarChangeListener.onCalendarCanged(mCalendar);
	}

	public int getCurrentDayOfMonth() {
		return mCurrentDayOfMonth;
	}

	public Boolean isSaturday(String dateString) {
		String[] dateAr = dateString.split("-");
		int year = Integer.parseInt(dateAr[2]);
		int month = Integer.parseInt(dateAr[1]);
		int day = Integer.parseInt(dateAr[0]);
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month, day);
		if (cal.get(Calendar.DAY_OF_WEEK) == 7)
			return true;
		return false;
	}

	private void setCurrentDayOfMonth(int currentDayOfMonth) {
		this.mCurrentDayOfMonth = currentDayOfMonth;
	}

	public void setCurrentWeekDay(int currentWeekDay) {
		this.mCurrentWeekDay = currentWeekDay;
	}

	public int getCurrentWeekDay() {
		return mCurrentWeekDay;
	}

	public void setOnCalendarChangeListener(OnCalendarChangeListener listener){
		this.mCalendarChangeListener = listener;
	}
	
	public interface OnCalendarChangeListener{
		public void onCalendarCanged(Calendar calendar);
	}
}