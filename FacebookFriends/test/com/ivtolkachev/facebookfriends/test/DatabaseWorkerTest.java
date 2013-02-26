package com.ivtolkachev.facebookfriends.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.Session;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.activity.ProfileActivity;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.Location;
import com.ivtolkachev.fbfriendslistapp.model.User;

@RunWith(RobolectricTestRunner.class)
public class DatabaseWorkerTest {
		
	private ProfileActivity mActivity;
	private DatabaseWorker mDatabaseWorker;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new ProfileActivity();
		Session session = new Session.Builder(mActivity).setApplicationId(mActivity.getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		mActivity.create();
		mDatabaseWorker = DatabaseWorker.getDatabaseWorker(mActivity.getApplicationContext());
		assertNotNull(mDatabaseWorker);
	}
	
	@Test
	public void testOpenDatabase() throws Exception {
		assertNotNull(mDatabaseWorker);
		mDatabaseWorker.openDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase(); 
		assertTrue(database != null && database.isOpen());
	}
	
	@Test 
 	public void testAddUser() throws Exception {
		String id = "111";
		User user = new User(id, "Иванов Иван Иванович", "Иван", "Иванович", "Иванов", "http://link", "IvanIvanov", "16.12.2012");
		String userId = mDatabaseWorker.addUser(user);
		assertThat(userId, equalTo(id));
	}
	
	@Test 
 	public void testAddLocation() throws Exception {
		String id = "111";
		Location location = new Location("Ukraine", null, "Kherson", "Dimitrova", "73020", 0, 0, id); 
		long rowId = mDatabaseWorker.addLocation(location);
		assertTrue(rowId > -1);
	}
	
	@Test
	public void testGetUserLocation() throws Exception {
		Location location = null;
		String userId = "111";
		location = mDatabaseWorker.getLocation(userId);
		assertNotNull(location);
		assertThat(location.getUserId(), equalTo(userId));
	}
	
	@Test
	public void testGetCurrentUser() throws Exception {
		User user = null;
		String userId = "111";
		assertNotNull(userId);
		assertNotNull(mDatabaseWorker);
		user = mDatabaseWorker.getUser(userId);
		assertNotNull(user);
		assertNotNull(user.getId());
		assertThat(user.getId(), equalTo(userId));
	}
	
	@Test
	public void testCloseDatabase() throws Exception {
		assertNotNull(mDatabaseWorker);
		mDatabaseWorker.closeDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase();
		assertTrue(database == null || !database.isOpen());
	}
	
}
