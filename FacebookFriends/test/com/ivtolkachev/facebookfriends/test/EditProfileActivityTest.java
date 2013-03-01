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
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.activity.ProfileActivity;

@RunWith(RobolectricTestRunner.class)
public class EditProfileActivityTest {
	
	private EditProfileActivit mActivity;
	private TextView mFirstNameLable;
	private TextView mMiddleNameLable;
	private TextView mLastNameLable;
	private TextView mUsernameLable;
	private TextView mBirthdayLable;
	private EditText mFirstNameEdit;
	private EditText mMiddleNameEdit;
	private EditText mLastNameEdit;
	private EditText mUsernameEdit;
	private Button mSaveButton;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new ProfileActivity();
		Session session = new Session.Builder(mActivity).setApplicationId(mActivity.getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		mActivity.create();
		mFirstNameLable = (TextView)mActivity.findViewById(R.id.first_name_lable);
		assertNotNull(mFirstNameLable);
		mMiddleNameLable = (TextView)mActivity.findViewById(R.id.middle_name_lable);
		assertNotNull(mMiddleNameLable);
		mLastNameLable = (TextView)mActivity.findViewById(R.id.last_name_lable);
		assertNotNull(mLastNameLable);
		mBirthdayLable = (TextView)mActivity.findViewById(R.id.birthday_lable);
		assertNotNull(mBirthdayLable);
		mUsernameLable = (TextView)mActivity.findViewById(R.id.username_lable);
		assertNotNull(mUsernameLable);
		mFirstNameEdit = (EditText)mActivity.findViewById(R.id.first_name_edit);
		assertNotNull(mFirstNameEdit);
		mMiddleNameEdit = (EditText)mActivity.findViewById(R.id.middle_name_edit);
		assertNotNull(mMiddleNameEdit);
		mLastNameEdit = (EditText)mActivity.findViewById(R.id.last_name_edit);
		assertNotNull(mLastNameEdit);
		mUsernameEdit = (EditText)mActivity.findViewById(R.id.usedname_edit);
		assertNotNull(mUsernameEdit);
		mSaveButton = (EditText)mActivity.findViewById(R.id.profile_save_btn);
		assertNotNull(mSaveButton);
	}
	
	@Test
	public void testViewsOnScreen() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, mFirstNameLable);
		ViewAsserts.assertOnScreen(origin, mMiddleNameLable);
		ViewAsserts.assertOnScreen(origin, mLastNameLable);
		ViewAsserts.assertOnScreen(origin, mBirthdayLable);
		ViewAsserts.assertOnScreen(origin, mUsernameLable);
		ViewAsserts.assertOnScreen(origin, mFirstNameEdit);
		ViewAsserts.assertOnScreen(origin, mMiddleNameEdit);
		ViewAsserts.assertOnScreen(origin, mLastNameEdit);
		ViewAsserts.assertOnScreen(origin, mUsernameEdit);
		ViewAsserts.assertOnScreen(origin, mSaveButton);
	}
	
	@Test
	public void testViewsContent() throws Exception {
		assertThat(mBirthdayLable.getText().toString(), equalTo(mActivity.getString(R.string.me_birth)));
		assertThat(mFirstNameLable.getText().toString(), equalTo(mActivity.getString(R.string.first_name_lable)));
		assertThat(mMiddleNameLable.getText().toString(), equalTo(mActivity.getString(R.string.middle_name_lable)));
		assertThat(mLastNameLable.getText().toString(), equalTo(mActivity.getString(R.string.last_name_lable)));
		assertThat(mUsernameLable.getText().toString(), equalTo(mActivity.getString(R.string.username_lable)));
	}
	


	

}

