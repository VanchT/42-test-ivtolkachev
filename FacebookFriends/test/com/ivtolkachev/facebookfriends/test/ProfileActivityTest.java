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
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.activity.ProfileActivity;

@RunWith(RobolectricTestRunner.class)
public class ProfileActivityTest {
	
	private ProfileActivity mActivity;
	private ProfilePictureView mImage;
	private TextView mName;
	private TextView mUsername;
	private TextView mBirthdayLable;
	private TextView mBirthday;
	private TextView mLink;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new ProfileActivity();
		Session session = new Session.Builder(mActivity).setApplicationId(mActivity.getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		mActivity.create();
		mImage = (ProfilePictureView)mActivity.findViewById(R.id.profile_pic);
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
	}
	
	@Test
	public void testViewsAlign() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertLeftAligned(origin, mImage);
	}
	
	@Test
	public void testViewsContent() throws Exception {
		assertThat(mBirthdayLable.getText().toString(), equalTo(mActivity.getString(R.string.me_birth)));
	}

}
