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

import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;
import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.activity.MainActivity;
import com.ivtolkachev.facebookfriends.data.DatabaseWorker;
import com.ivtolkachev.facebookfriends.model.Location;
import com.ivtolkachev.facebookfriends.model.User;

@RunWith(RobolectricTestRunner.class)
public class DatabaseWorkerTest {
		
	private MainActivity mActivity;
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mAppPref;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new MainActivity();
		mActivity.create();
		mDatabaseWorker = DatabaseWorker.getDatabaseWorker(mActivity.getApplicationContext());
		assertNotNull(mDatabaseWorker);
		mAppPref = mActivity.getSharedPreferences(mActivity.getString(R.string.app_preferences), 0);
		assertNotNull(mAppPref);
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
		String userId = "111";
		User user = new User(userId, "Иванов Иван Иванович", "Иван", "Иванович", "Иванов", "http://link", "IvanIvanov", "16.12.2012");
		long rowId = mDatabaseWorker.addUser(user);
		assertTrue(rowId > -1);
		if (rowId > -1) {
			SharedPreferences.Editor editor = mAppPref.edit();
			editor.putString(mActivity.getString(R.string.preference_user_id), userId);
			editor.commit();
		}
	}
	
	@Test 
 	public void testAddLocation() throws Exception {
		Location location = new Location("Ukraine", null, "Kherson", "Dimitrova", "73020", 0, 0); 
		long rowId = mDatabaseWorker.addLocation(location, "111");
		assertTrue(rowId > -1);
	}
	
	@Test
	public void testGetUserLocation() throws Exception {
		GraphLocation location = null;
		String userId = mAppPref.getString(mActivity.getString(R.string.preference_user_id), null);
		assertNotNull(userId);
		location = mDatabaseWorker.getUserLocation(userId);
		assertNotNull(location);
	}
	
	@Test
	public void testGetCurrentUser() throws Exception {
		User user = null;
		String userId = mAppPref.getString(mActivity.getString(R.string.preference_user_id), null);
		assertNotNull(userId);
		assertNotNull(mDatabaseWorker);
		user = mDatabaseWorker.getUser(userId);
		assertNotNull(user);
		assertNotNull(user.getId());
		assertTrue(userId == user.getId());
	}
	
	@Test
	public void testCloseDatabase() throws Exception {
		assertNotNull(mDatabaseWorker);
		mDatabaseWorker.closeDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase();
		assertTrue(database == null || !database.isOpen());
	}
	
}
