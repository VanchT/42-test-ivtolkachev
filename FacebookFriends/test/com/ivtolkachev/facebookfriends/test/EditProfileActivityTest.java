package com.ivtolkachev.facebookfriends.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.activity.ProfileActivity;

@RunWith(RobolectricTestRunner.class)
public class EditProfileActivityTest {
	
	private EditProfileActivit mActivity;
	private ImageView mProfileImage;
	private TextView mFirstNameLable;
	private TextView mMiddleNameLable;
	private TextView mLastNameLable;
	private TextView mUsernameLable;
	private TextView mBirthdayLable;
	private TextView mBirthdayDate;
	private EditText mFirstNameEdit;
	private EditText mMiddleNameEdit;
	private EditText mLastNameEdit;
	private EditText mUsernameEdit;
	private Button mSaveButton;
	private Button mEditBirthdayButton;
	private Button mEditImageButton;
	private CalendarView mCalendar;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new ProfileActivity();
		Session session = new Session.Builder(mActivity).setApplicationId(mActivity.getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		mActivity.create();
		mProfileImage = (ImageView)mActivity.findViewById(R.id.profile_image_edit);
		assertNotNull(mProfileImage);
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
		mUsernameEdit = (EditText)mActivity.findViewById(R.id.username_edit);
		assertNotNull(mUsernameEdit);
		mSaveButton = (Button)mActivity.findViewById(R.id.profile_save_btn);
		assertNotNull(mSaveButton);
		mEditBirthdayButton = (Button)mActivity.findViewById(R.id.birthday_edit_btn);
		assertNotNull(mEditBirthdayButton);
		mEditImageButton = (Button)mActivity.findViewById(R.id.image_edit_btn);
		assertNotNull(mEditImageButton);
	}
	
	@Test
	public void testViewsOnScreen() throws Exception {
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, mProfileImage);
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
		ViewAsserts.assertOnScreen(origin, mEditBirthdayButton);
		ViewAsserts.assertOnScreen(origin, mEditImageButton);
	}
	
	@Test
	public void testViewsContent() throws Exception {
		assertThat(mBirthdayLable.getText().toString(), equalTo(mActivity.getString(R.string.me_birth)));
		assertThat(mFirstNameLable.getText().toString(), equalTo(mActivity.getString(R.string.first_name_lable)));
		assertThat(mMiddleNameLable.getText().toString(), equalTo(mActivity.getString(R.string.middle_name_lable)));
		assertThat(mLastNameLable.getText().toString(), equalTo(mActivity.getString(R.string.last_name_lable)));
		assertThat(mUsernameLable.getText().toString(), equalTo(mActivity.getString(R.string.username_lable)));
		assertThat(mSaveButton.getText().toString(), equalTo(mActivity.getString(R.string.button_save)));
		assertThat(mEditBirthdayButton.getText().toString(), equalTo(mActivity.getString(R.string.button_edit)));
		assertThat(mEditImageButton.getText().toString(), equalTo(mActivity.getString(R.string.button_edit)));
	}
	
	@Test
	public void testViewsVisibility() throws Exception {   
		assertTrue(mProfileImage.getVisibility() == View.VISIBLE);       
		assertTrue(mFirstNameLable.getVisibility() == View.VISIBLE);       
		assertTrue(mMiddleNameLable.getVisibility() == View.VISIBLE);      
		assertTrue(mLastNameLable.getVisibility() == View.VISIBLE);        
		assertTrue(mUsernameLable.getVisibility() == View.VISIBLE);        
		assertTrue(mBirthdayLable.getVisibility() == View.VISIBLE);        
		assertTrue(mBirthdayDate.getVisibility() == View.VISIBLE);         
		assertTrue(mFirstNameEdit.getVisibility() == View.VISIBLE);        
		assertTrue(mMiddleNameEdit.getVisibility() == View.VISIBLE);       
		assertTrue(mLastNameEdit.getVisibility() == View.VISIBLE);         
		assertTrue(mUsernameEdit.getVisibility() == View.VISIBLE);         
		assertTrue(mSaveButton.getVisibility() == View.VISIBLE);             
		assertTrue(mEditBirthdayButton.getVisibility() == View.VISIBLE);    
		assertTrue(mEditImageButton.getVisibility() == View.VISIBLE);
		assertTrue(mCalendar.getVisibility() == View.GONE);
	}

	@Test
    public void testTheButtonShouldOpenGallery() throws Exception {
        mEditImageButton.performClick();

       //TODO: Not implemented yet
    }
	
	@Test
    public void testTheButtonShouldDoCalendarVisible() throws Exception {
        mEditImageButton.performClick();

       //TODO: Not implemented yet
    }
	
	@Test
    public void testTheButtonShouldSaveData() throws Exception {
        mEditImageButton.performClick();

        assertTrue(mCalendar.getVisibility() == View.VISIBLE);
       //TODO: Not implemented yet
    }

}

