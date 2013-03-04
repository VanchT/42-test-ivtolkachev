package com.ivtolkachev.fbfriendslistapp.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.codeus.android.DateSlider;
import net.codeus.android.MonthYearDateSlider;

import android.app.Dialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.adapter.GridCellAdapter;

public class CalendarView extends FrameLayout implements OnClickListener {

	private static final String TAG = CalendarView.class.getSimpleName();
	private static final String DATE_TEMPLATE_FOR_ADAPTER = "MMMM yyyy";
	private static final String DATE_TEMPLATE_FOR_RETURN = "MM/dd/yyyy";

	private View mRootView;
	private Context mContext;
	private Button mCurrentMonthBtn;
	private ImageView mPrevMonthView;
	private ImageView mNextMonthView;
	private GridView mCalendarView;
	private Button mDoneButton;
	private GridCellAdapter mGridAdapter;
	private Calendar mCalendar;
	private int mMonth;
	private int mYear;
	private CalendarOnCloseListener mOnCloseListener;

	public CalendarView(Context context) {
		super(context);
		this.mContext = context;
		initialize();
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initialize();
	}

	private void initialize() {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootView = inflater.inflate(R.layout.layout_calendar, this);

		mRootView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mCalendar = Calendar.getInstance(Locale.getDefault());
		Log.d(TAG, "Calendar Instance:= " + "Month: " + mMonth + " " + "Year: " + mYear);

		mPrevMonthView = (ImageView) mRootView.findViewById(R.id.prevMonth);
		mPrevMonthView.setOnClickListener(this);

		mCurrentMonthBtn = (Button) mRootView.findViewById(R.id.currentMonth);
		mCurrentMonthBtn.setText(DateFormat.format(DATE_TEMPLATE_FOR_ADAPTER,
				mCalendar.getTime()));
		mCurrentMonthBtn.setOnClickListener(this);

		mNextMonthView = (ImageView) mRootView.findViewById(R.id.nextMonth);
		mNextMonthView.setOnClickListener(this);

		mCalendarView = (GridView) mRootView.findViewById(R.id.calendar);
		
		mDoneButton = (Button) mRootView.findViewById(R.id.calendar_done_btn);
		mDoneButton.setOnClickListener(this);

		retrieveCalendarView();
	}

	private void retrieveCalendarView() {
		mMonth = mCalendar.get(Calendar.MONTH) + 1;
		mYear = mCalendar.get(Calendar.YEAR);
		mGridAdapter = new GridCellAdapter(mContext.getApplicationContext(),
				R.id.calendar_day_gridcell, mCalendar);
		mGridAdapter.setOnCalendarChangeListener(new GridCellAdapter.OnCalendarChangeListener() {
			
			@Override
			public void onCalendarCanged(Calendar calendar) {
				mCalendar = calendar;
				retrieveCalendarView();
			}
		});
		mGridAdapter.notifyDataSetChanged();
		mCalendarView.setAdapter(mGridAdapter);
	}

	private void setGridCellAdapterToDate(int month, int year) {
		Log.d(TAG, "setGridCellAdapterToDate");
		mCalendar.set(year, month - 1, mCalendar.get(Calendar.DAY_OF_MONTH));
		mCurrentMonthBtn.setText(DateFormat.format(DATE_TEMPLATE_FOR_ADAPTER, mCalendar.getTime()));

		retrieveCalendarView();
	}

	@Override
	public void onClick(View v) {
		if (v == mPrevMonthView) {
			if (mMonth <= 1) {
				mMonth = 12;
				mYear--;
			} else {
				mMonth--;
			}
			setGridCellAdapterToDate(mMonth, mYear);
		}
		if (v == mNextMonthView) {
			if (mMonth > 11) {
				mMonth = 1;
				mYear++;
			} else {
				mMonth++;
			}
			setGridCellAdapterToDate(mMonth, mYear);
		}
		if (v == mCurrentMonthBtn) {
			showDialog();
		}
		if (v == mDoneButton){
			mRootView.setVisibility(GONE);
			mOnCloseListener.onClose(DateFormat.format(DATE_TEMPLATE_FOR_RETURN, mCalendar.getTime()).toString());
		}
	}
	
	private void showDialog(){
		Dialog dialog = new MonthYearDateSlider(mContext, mDateSetListener, mCalendar);
		dialog.show();
	}
	
	private DateSlider.OnDateSetListener mDateSetListener = new DateSlider.OnDateSetListener() {
				
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			mMonth = selectedDate.get(Calendar.MONTH)+1;
			mYear = selectedDate.get(Calendar.YEAR);
			setGridCellAdapterToDate(selectedDate.get(Calendar.MONTH)+1, selectedDate.get(Calendar.YEAR));
	    }
	};

	public void setOnCloseListener(CalendarOnCloseListener listener){
		this.mOnCloseListener = listener;
	}
	
	public void setDate(String date){
		try {
			Date newDate = new SimpleDateFormat(DATE_TEMPLATE_FOR_RETURN, Locale.getDefault()).parse(date);
			Log.d(TAG, "date = " + newDate.toString());
			mCalendar.setTime(newDate);
			Log.d(TAG, "Calendar date = " + mCalendar.getTime().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mCurrentMonthBtn.setText(DateFormat.format(DATE_TEMPLATE_FOR_ADAPTER, mCalendar.getTime()));
		retrieveCalendarView();
	}
	
	public interface CalendarOnCloseListener{
		public void onClose(String date);
	}

}
