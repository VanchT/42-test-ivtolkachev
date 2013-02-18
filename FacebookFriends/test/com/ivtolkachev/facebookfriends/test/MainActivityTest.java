package com.ivtolkachev.facebookfriends.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.activity.MainActivity;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	private Activity mActivity;
	private TextView mName;
	private TextView mSurname;
	private TextView mBirthday;
	private TextView mBio;
	private TextView mContacts;
	private ImageView mImage;
	private TextView mBirthdayLable;
	private TextView mBioLable;
	private TextView mContactsLable;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new MainActivity();
		mName = (TextView)mActivity.findViewById(R.id.name_me);
		assertNotNull(mName);
		mSurname = (TextView)mActivity.findViewById(R.id.surname_me);
		assertNotNull(mSurname);
		mBirthday = (TextView)mActivity.findViewById(R.id.birthday_me);
		assertNotNull(mBirthday);
		mBio = (TextView)mActivity.findViewById(R.id.bio_me);
		assertNotNull(mBio);
		mContacts = (TextView)mActivity.findViewById(R.id.contacts_me);
		assertNotNull(mContacts);
		mImage = (ImageView)mActivity.findViewById(R.id.image_me);
		assertNotNull(mImage);
		mBirthdayLable = (TextView)mActivity.findViewById(R.id.birthday_me_lable);
		assertNotNull(mBirthdayLable);
		mBioLable = (TextView)mActivity.findViewById(R.id.bio_me_lable);
		assertNotNull(mBioLable);
		mContactsLable = (TextView)mActivity.findViewById(R.id.contacts_me_lable);
		assertNotNull(mContactsLable);
	}
	
	@Test
	public void testApplicationShouldHaveAppName() throws Exception {
		String appName = mActivity.getResources().getString(R.string.app_name);
		assertThat(appName, equalTo("FacebookFriends"));
	}
	
	@Test
	public void testFildsOnScreen() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, mName);
		ViewAsserts.assertOnScreen(origin, mSurname);
		ViewAsserts.assertOnScreen(origin, mBirthday);
		ViewAsserts.assertOnScreen(origin, mBio);
		ViewAsserts.assertOnScreen(origin, mContacts);
		ViewAsserts.assertOnScreen(origin, mImage);
		ViewAsserts.assertOnScreen(origin, mBirthdayLable);
		ViewAsserts.assertOnScreen(origin, mBioLable);
		ViewAsserts.assertOnScreen(origin, mContactsLable);
	}
	
	@Test
	public void testImageSize() throws Exception {
		int expected = 128;
		assertEquals(expected, mImage.getWidth());
		assertEquals(expected, mImage.getHeight());
	}

}
