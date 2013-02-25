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

import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.activity.MainActivity;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	private MainActivity mActivity;
	private ImageView mImage;
	private TextView mName;
	private TextView mUsername;
	private TextView mBirthdayLable;
	private TextView mBirthday;
	private TextView mLink;
	private TextView mLocationLable;
	private TextView mLocationCountry;
	private TextView mLocationState;
	private TextView mLocationCity;
	private TextView mLocationStreet;
	private TextView mLocationZip;
	private TextView mLocationLatitude;
	private TextView mLocationLongitude;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new MainActivity();
		mActivity.create();
		mImage = (ImageView)mActivity.findViewById(R.id.image_me);
		assertNotNull(mImage);
		mName = (TextView)mActivity.findViewById(R.id.name_me);
		assertNotNull(mName);
		mUsername = (TextView)mActivity.findViewById(R.id.username_me);
		assertNotNull(mUsername);
		mBirthdayLable = (TextView)mActivity.findViewById(R.id.birthday_me_lable);
		assertNotNull(mBirthdayLable);
		mBirthday = (TextView)mActivity.findViewById(R.id.birthday_me);
		assertNotNull(mBirthday);
		mLink = (TextView)mActivity.findViewById(R.id.link_me);
		assertNotNull(mLink);
		mLocationLable = (TextView)mActivity.findViewById(R.id.location_me_lable);
		assertNotNull(mLocationLable);
		mLocationCountry = (TextView)mActivity.findViewById(R.id.location_country);
		assertNotNull(mLocationCountry);
		mLocationState = (TextView)mActivity.findViewById(R.id.location_state);
		assertNotNull(mLocationState);
		mLocationCity = (TextView)mActivity.findViewById(R.id.location_city);
		assertNotNull(mLocationCity);
		mLocationStreet= (TextView)mActivity.findViewById(R.id.location_street);
		assertNotNull(mLocationStreet);
		mLocationZip = (TextView)mActivity.findViewById(R.id.location_zip);
		assertNotNull(mLocationZip);
		mLocationLatitude = (TextView)mActivity.findViewById(R.id.location_latitude);
		assertNotNull(mLocationLatitude);
		mLocationLongitude = (TextView)mActivity.findViewById(R.id.location_longitede);
		assertNotNull(mLocationLongitude);

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
		ViewAsserts.assertOnScreen(origin, mUsername);
		ViewAsserts.assertOnScreen(origin, mBirthdayLable);
		ViewAsserts.assertOnScreen(origin, mBirthday);
		ViewAsserts.assertOnScreen(origin, mLink);
		ViewAsserts.assertOnScreen(origin, mLocationLable);
		ViewAsserts.assertOnScreen(origin, mLocationCountry);
		ViewAsserts.assertOnScreen(origin, mLocationState);
		ViewAsserts.assertOnScreen(origin, mLocationCity);
		ViewAsserts.assertOnScreen(origin, mLocationStreet);
		ViewAsserts.assertOnScreen(origin, mLocationZip);
		ViewAsserts.assertOnScreen(origin, mLocationLatitude);
		ViewAsserts.assertOnScreen(origin, mLocationLongitude);
	}
	
	@Test
	public void testViewsAlign() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertLeftAligned(origin, mImage);
	}
	
	@Test
	public void testViewsContent() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		assertThat(mBirthdayLable.getText().toString(), equalTo(mActivity.getString(R.string.me_birth)));
		assertThat(mLocationLable.getText().toString(), equalTo(mActivity.getString(R.string.me_location)));
	}

}
