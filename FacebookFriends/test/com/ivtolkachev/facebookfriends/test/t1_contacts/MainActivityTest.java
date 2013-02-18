package com.ivtolkachev.facebookfriends.test.t1_contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.activity.MainActivity;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	private Activity activity;
	
	@Before
	public void setUp() throws Exception {
		activity = new MainActivity();
	}
	
	@Test
	public void shouldHaveAppName() throws Exception {
		String appName = activity.getResources().getString(R.string.app_name);
		assertThat(appName, equalTo("FacebookFriends"));
	}

}
