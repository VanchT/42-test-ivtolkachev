package com.ivtolkachev.facebookfriends.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;

import com.facebook.Session;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.activity.MainActivity;
import com.ivtolkachev.fbfriendslistapp.activity.ProfileActivity;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	private MainActivity mActivity;
	private Button mProfileButton;
	private Button mFriendsButton;
	
	@Before
	public void setUp(){
		mActivity = new MainActivity();
		Session session = new Session.Builder(mActivity).setApplicationId(mActivity.getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		mActivity.create();
		
		mProfileButton = (Button)mActivity.findViewById(R.id.profile_button);
		assertNotNull(mProfileButton);
		mFriendsButton = (Button)mActivity.findViewById(R.id.friends_button);
		assertNotNull(mFriendsButton);
	}
	
	@Test
	public void testApplicationShouldHasApplicationId() throws Exception {
		PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(
				mActivity.getComponentName().toString(), PackageManager.GET_META_DATA);
		Bundle metaData = packageInfo.applicationInfo.metaData;
		String origin = metaData.getString("com.facebook.sdk.ApplicationId");
		assertThat(origin, equalTo(mActivity.getString(R.string.app_id)));
	}
	
	@Test
	public void testApplicationShouldHasPermissions() throws Exception {
		PackageManager packageManager = mActivity.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageInfo(
				mActivity.getComponentName().toString(), PackageManager.GET_PERMISSIONS);
		int origin = packageManager.checkPermission(Manifest.permission.INTERNET, packageInfo.packageName);
		assertTrue(origin == PackageManager.PERMISSION_GRANTED);
	}
	
	@Test
	public void testViewsOnScreen() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, mProfileButton);
		ViewAsserts.assertOnScreen(origin, mFriendsButton);
	}
	
	@Test
	public void testButtonsProperties() throws Exception {
		assertThat(mProfileButton.getText().toString(), equalTo(mActivity.getString(R.string.button_profile)));
		assertThat(mFriendsButton.getText().toString(), equalTo(mActivity.getString(R.string.button_friends)));
		
		assertThat(mProfileButton.getBackground().toString(), equalTo(mActivity.getString(R.color.com_facebook_blue)));
		assertThat(mFriendsButton.getBackground().toString(), equalTo(mActivity.getString(R.color.com_facebook_blue)));
	}
	
	@Test
    public void pressingTheProfileButtonShouldStartTheProfileActivity() throws Exception {
        mProfileButton.performClick();

        ShadowActivity shadowActivity = Robolectric.shadowOf(mActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(ProfileActivity.class.getName()));
    }

}
