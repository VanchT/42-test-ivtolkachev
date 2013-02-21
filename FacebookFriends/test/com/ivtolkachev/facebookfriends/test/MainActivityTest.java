package com.ivtolkachev.facebookfriends.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.test.ViewAsserts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.activity.MainActivity;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	private MainActivity mActivity;
	private TextView mName;
	private TextView mSurname;
	private TextView mBirthday;
	private TextView mLocation;
	private TextView mUsername;
	private ImageView mImage;
	private TextView mBirthdayLable;
	private TextView mLocationLable;
	private TextView mUsernameLable;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new MainActivity();
		mActivity.create();
		mImage = (ImageView)mActivity.findViewById(R.id.image_me);
		assertNotNull(mImage);
		mName = (TextView)mActivity.findViewById(R.id.name_me);
		assertNotNull(mName);
		mSurname = (TextView)mActivity.findViewById(R.id.surname_me);
		assertNotNull(mSurname);
		mBirthday = (TextView)mActivity.findViewById(R.id.birthday_me);
		assertNotNull(mBirthday);
		mBirthdayLable = (TextView)mActivity.findViewById(R.id.birthday_me_lable);
		assertNotNull(mBirthdayLable);
		mLocationLable = (TextView)mActivity.findViewById(R.id.location_me_lable);
		assertNotNull(mLocationLable);
		mLocation = (TextView)mActivity.findViewById(R.id.location_me);
		assertNotNull(mLocation);
		mUsername = (TextView)mActivity.findViewById(R.id.lusername_me);
		assertNotNull(mUsername);
		mUsernameLable = (TextView)mActivity.findViewById(R.id.lusername_me_lable);
		assertNotNull(mUsernameLable);
	}
	
	@Test
	public void testApplicationShouldHaveAppName() throws Exception {
		String appName = mActivity.getResources().getString(R.string.app_name);
		assertThat(appName, equalTo("FacebookFriends"));
	}
	
	@Test
	public void testViewsOnScreen() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, mImage);
		ViewAsserts.assertOnScreen(origin, mName);
		ViewAsserts.assertOnScreen(origin, mSurname);
		ViewAsserts.assertOnScreen(origin, mBirthday);
		ViewAsserts.assertOnScreen(origin, mLocation);
		ViewAsserts.assertOnScreen(origin, mUsername);
		ViewAsserts.assertOnScreen(origin, mBirthdayLable);
		ViewAsserts.assertOnScreen(origin, mLocationLable);
		ViewAsserts.assertOnScreen(origin, mUsernameLable);
	}
	
	@Test
	public void testViewsAlign() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertLeftAligned(origin, mImage);
		ViewAsserts.assertLeftAligned(origin, mLocationLable);
		ViewAsserts.assertLeftAligned(origin, mLocation);
	}

}
